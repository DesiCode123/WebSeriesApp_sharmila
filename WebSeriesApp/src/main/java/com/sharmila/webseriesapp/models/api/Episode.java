package com.sharmila.webseriesapp.models.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Episode {
    private Long id;
    private String url;
    private String name;
    private int season;
    private int number;
    private String type;
    private String airdate;
    private String airtime;
    private String airstamp;
    private int runtime;
    private Rating rating;
    private String summary;
}
