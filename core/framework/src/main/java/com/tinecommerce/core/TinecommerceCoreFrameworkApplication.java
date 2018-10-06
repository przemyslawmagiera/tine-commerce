package com.tinecommerce.core;

import com.tinecommerce.core.solr.SolrIndexServiceImpl;
import com.tinecommerce.core.solr.SolrSearchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TinecommerceCoreFrameworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(TinecommerceCoreFrameworkApplication.class, args);
	}
}
