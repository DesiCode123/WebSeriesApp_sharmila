package com.sharmila.webseriesapp.models.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TvShow {

    private long id;
    private String url;
    private String name;
    private String type;
    private String language;
    private List<String> genres;
    private String status;
    private int runtime;
    private int averageRuntime;
    private String premiered;
    private String ended;
    private String officialSite;
    private Schedule schedule;
    private Rating rating;
    private int weight;
    private Network network;
    private String dvdCountry;
    private Externals externals;
    private String summary;
    private long updated;
    private Embedded _embedded;
}

