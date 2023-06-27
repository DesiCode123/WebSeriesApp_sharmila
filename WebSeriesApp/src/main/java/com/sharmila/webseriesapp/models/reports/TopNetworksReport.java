package com.sharmila.webseriesapp.models.reports;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopNetworksReport {

    private double avgRating;

    private String network;

    private String toRatedShow;

    private double topRating;

    private long showCount;

}
