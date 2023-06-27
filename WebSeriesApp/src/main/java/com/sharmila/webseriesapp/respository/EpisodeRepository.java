package com.sharmila.webseriesapp.respository;

import com.sharmila.webseriesapp.entities.Episode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {

    List<Episode> findByAirDateBetween(Instant airDateStart, Instant airDateEnd);

}
