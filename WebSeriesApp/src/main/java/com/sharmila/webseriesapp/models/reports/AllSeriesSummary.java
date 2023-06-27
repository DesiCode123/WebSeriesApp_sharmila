package com.sharmila.webseriesapp.models.reports;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AllSeriesSummary {

    private String name;
    private String network;
    private String genre;
    private long episodeCount;
    private long releasedEpisodeCount;
}
