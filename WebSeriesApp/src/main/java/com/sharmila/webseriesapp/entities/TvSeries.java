package com.sharmila.webseriesapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
public class TvSeries {

    @Id
    private Long id;

    private String name;

    private double rating;

    @ElementCollection
    private List<String> genres = new java.util.ArrayList<>();

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn
    private Network network;

    private Date premiered;

    private Date ended;

    @OneToMany(mappedBy = "tvSeries", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}, orphanRemoval = true)
    @JsonIgnore
    private List<Episode> episodes = new java.util.ArrayList<>();

}
