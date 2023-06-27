package com.sharmila.webseriesapp.models.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Network {
    private Long id;
    private String name;
    private Country country;
    private String officialSite;
}
