package com.collectpop.dto;


public enum ApiCode {
    KAKAOLOGINCODE("b5c744c7e75dfd76ba65c844a489ff44");

    private final String abbreviation;
    private ApiCode(String abbreviation){
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation(){
        return abbreviation;
    }
}
