package com.sharmila.webseriesapp.respository;

import com.sharmila.webseriesapp.entities.TvSeries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Date;
import java.util.List;

public interface TvSeriesRepository extends JpaRepository<TvSeries, Long> {

    List<TvSeries> findByEpisodes_AirDateBetween(Instant airDateStart, Instant airDateEnd);

    List<TvSeries> findTop10ByOrderByRatingDesc();

    List<TvSeries> findAllByRatingGreaterThan(double rating);
}
