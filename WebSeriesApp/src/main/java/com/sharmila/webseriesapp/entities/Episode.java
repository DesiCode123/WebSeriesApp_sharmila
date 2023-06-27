package com.sharmila.webseriesapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@Entity
public class Episode {

    @Id
    private Long id;

    private String name;

    private int season;

    private int number;

    private Date airDate;

    private double rating;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "tv_series_id")
    @JsonIgnore
    private TvSeries tvSeries;

}
