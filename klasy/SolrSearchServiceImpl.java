package com.tinecommerce.core.solr;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.tinecommerce.core.catalog.model.CategoryFeature;
import com.tinecommerce.core.catalog.repository.CategoryFeatureRepository;
import com.tinecommerce.core.catalog.repository.PriceRepository;
import com.tinecommerce.core.solr.dto.*;
import com.tinecommerce.core.solr.model.Facetable;
import com.tinecommerce.core.solr.model.SearchField;
import com.tinecommerce.core.solr.repository.SearchFieldRepository;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.naming.directory.SearchResult;

@Service
public class SolrSearchServiceImpl {

    @Autowired
    protected SearchFieldRepository searchFieldRepository;

    @Autowired
    protected CategoryFeatureRepository categoryFeatureRepository;

    @Autowired
    protected PriceRepository priceRepository;

    public SearchResultDTO doSearch(Map<String,String> filterParams) throws IOException, SolrServerException {
        SolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr/tinecommerce").build();
        String textQuery = filterParams.remove("query");
        String sortExpression = filterParams.remove("sort");
        String priceFilter = filterParams.remove("priceFilter");
        Set<SearchField> searchFields = searchFieldRepository.findAllBySearchable(true);
        Set<String> searchableCodes = searchFields.stream().map(SearchField::getName).collect(Collectors.toSet());
        searchableCodes.addAll(categoryFeatureRepository.findAll().stream().map(CategoryFeature::getCode).collect(Collectors.toSet()));
        String queryString =
                searchableCodes.stream()
                .collect(Collectors.joining(":" + textQuery + " OR "));
        queryString = queryString + ":" + textQuery;
        SolrQuery query = new SolrQuery();
        query.setQuery(queryString);
        searchableCodes.add("price_d");
        searchableCodes.add("description");
        query.setFields(searchableCodes.toArray(new String[0]));
        query.setStart(0);
        query.set("defType", "edismax");

        query.setParam("sort", sortExpression);

        query.add("facet.range", "price_d");
        query.add("facet.range.start", "50");
        query.add("facet.range.end", "1000");
        query.add("facet.range.gap", "50");

        List<String> filterList = new ArrayList<>();
        filterParams.forEach((key, value) -> {
            filterList.add(key + ":" + value);
        });
        if(priceFilter != null) {
            filterList.add("price_d:{" + priceFilter + "}");
        }
        query.addFilterQuery(String.join(" AND ", filterList));

        query.setFacet(true);
        Set<String> facetFields = searchFieldRepository.findAllByIsFacet(true).stream().map(SearchField::getName).collect(Collectors.toSet());
        facetFields.addAll(categoryFeatureRepository.findAllByIsFacet(true).stream().map(CategoryFeature::getCode).collect(Collectors.toSet()));
        query.addFacetField(facetFields.toArray(new String[0]));

        QueryResponse response = client.query(query);

        return getSearchResults(response);
    }

    public SearchResultDTO getSearchResults(final QueryResponse queryResponse) {
        SearchResultDTO searchResult = new SearchResultDTO();
        queryResponse.getFacetFields().stream().filter(facetField -> !CollectionUtils.isEmpty(facetField.getValues()))
                .forEach(facetField -> {
            FacetContainer facetContainer = new FacetContainer();
            facetContainer.setCode(facetField.getName());
            facetField.getValues().stream().filter(value -> value.getCount() > 0)
                    .forEach(value -> facetContainer.getFacetDTOS().add(new FacetDTO(value.getName(), value.getCount())));
            searchResult.getFacetContainers().add(facetContainer);
        });
        queryResponse.getResults().forEach(document -> {
            ProductDTO productDTO = new ProductDTO();
            document.getFieldNames().forEach(fieldName -> productDTO.getAttributes().put(fieldName, document.getFieldValue(fieldName)));
            searchResult.getProductDTOS().add(productDTO);
        });
        queryResponse.getFacetRanges().forEach(range ->{
            range.getCounts().stream().filter(count -> ((RangeFacet.Count) count).getCount() > 0).forEach(count->{
                RangeFacet.Count rangeFacetCount = (RangeFacet.Count) count;
                final Double to = Double.parseDouble(rangeFacetCount.getValue()) + 50;
                searchResult.getPriceRanges().add(new PriceRange(Double.toString(to), rangeFacetCount.getValue(), rangeFacetCount.getCount()));
            });
        });

        return searchResult;
    }
}