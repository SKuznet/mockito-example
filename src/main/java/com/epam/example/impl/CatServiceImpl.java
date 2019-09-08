package com.epam.example.impl;

import com.epam.example.CatService;

public class CatServiceImpl implements CatService {
    private CatService catService;

    public CatServiceImpl(CatService catService) {
        this.catService = catService;
    }

    public double add(double d1, double d2) {
        return catService.add(d1, d2);
    }

    public double subtract(double d1, double d2) {
        return catService.subtract(d1, d2);
    }

    public double multiply(double d1, double d2) {
        return catService.multiply(d1, d2);
    }

    public double divide(double d1, double d2) {
        return catService.multiply(d1, d2);
    }

    public double double15() {
        return 15.0;
    }
}
