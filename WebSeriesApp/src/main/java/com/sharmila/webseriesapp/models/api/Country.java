package com.sharmila.webseriesapp.models.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Country {
    private String name;
    private String code;
    private String timezone;
}
