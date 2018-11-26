package com.tinecommerce.core.catalog.model;


public class Product extends AbstractNameableEntity {

    private Set<Price> prices;

    private Set<Category> categories = new HashSet<>();

}
