package br.com.i9algo.taxiadv.domain.models;


import java.util.ArrayList;

public class GeoCoordinate extends ArrayList {

    private final static long serialVersionUID = 2285655903136022382L;

    public GeoCoordinate(Double latitude, Double longitude) {
        super();
        this.add(latitude);
        this.add(longitude);
    }

}
