package com.rentnest.models;

import java.math.BigDecimal;

public class Listing {
    private final int id;
    private final String name;
    private final String location;
    private final BigDecimal price;

    public Listing(int id, String name, String location, BigDecimal price) {
        this.id = id; this.name = name; this.location = location; this.price = price;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public BigDecimal getPrice() { return price; }
}
