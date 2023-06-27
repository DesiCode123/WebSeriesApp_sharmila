package com.sharmila.webseriesapp.models.reports;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BestSeriesSummary {

    private String name;
    private String network;
    private String genre;
    private int seasonNumber;
    private int episodeNumber;
    private String episodeName;
    private double episodeRating;
}
