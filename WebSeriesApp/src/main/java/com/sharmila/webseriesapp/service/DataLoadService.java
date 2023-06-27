package com.sharmila.webseriesapp.service;

import com.sharmila.webseriesapp.entities.Episode;
import com.sharmila.webseriesapp.entities.Network;
import com.sharmila.webseriesapp.entities.TvSeries;
import com.sharmila.webseriesapp.models.api.TvShow;
import com.sharmila.webseriesapp.respository.TvSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class DataLoadService implements ApplicationRunner {

    @Value("${movies.file.path}")
    public String path;

    @Autowired
    private WebClient webClient;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private TvSeriesRepository tvSeriesRepository;

    @Transactional
    @Override
    public void run(ApplicationArguments args) {

        System.out.println("The run method called");

        if(!tvSeriesRepository.findAll().isEmpty()){
            System.out.println("Data already inserted");
            return;
        }
        List<String> series = readSeriesFromFile(path);

        List<TvSeries> seriesAll = new ArrayList<>();

        for (String seriesName:series) {
            System.out.println("Starting "+seriesName);
            TvShow tvShow = getDetailsFromApi(seriesName);

            TvSeries tvSeries = toTvSeriesEntity(tvShow);

            seriesAll.add(tvSeries);
            System.out.println("done "+seriesName);
        }
        tvSeriesRepository.saveAll(seriesAll);
        System.out.println("Completed");
    }

    private TvShow getDetailsFromApi(String seriesName) {
        try {
            Thread.sleep(1000);
            String uri ="/singlesearch/shows?q=" + seriesName + "&embed=episodes";
            TvShow response = webClient.get().uri(uri).retrieve().bodyToMono(TvShow.class).block();
            return response;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<String> readSeriesFromFile(String path) {
        try{
            List<String> series = new ArrayList<>();
            InputStream inputStream = resourceLoader.getResource(path).getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            String fileContent = FileCopyUtils.copyToString(reader);
            String[] lines = fileContent.split(System.lineSeparator());

            for (String line : lines) {
                series.add(line.strip());
            }
            return series;

        }catch (Exception e) {
            // throw new WebSeriesException("Error while reading the file");
            throw  new RuntimeException("Unable to fetch the data from file");
        }
    }

    private TvSeries toTvSeriesEntity(TvShow tvShow) {
        TvSeries series = new TvSeries();
        series.setName(tvShow.getName());
        series.setId(tvShow.getId());

        if(tvShow.getRating() != null) {
            series.setRating(tvShow.getRating().getAverage());
        }

        series.setGenres(tvShow.getGenres());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if(tvShow.getPremiered() != null) {
            LocalDate localDate = LocalDate.parse(tvShow.getPremiered(), formatter);
            Instant instant = localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
            Date date = Date.from(instant);
            series.setPremiered(date);
        }

        if(tvShow.getEnded() != null) {
            LocalDate localDate = LocalDate.parse(tvShow.getEnded(), formatter);
            Instant instant = localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
            Date date = Date.from(instant);
            series.setEnded(date);
        }

        if(tvShow.getNetwork() != null) {
            Network network = new Network();
            network.setName(tvShow.getNetwork().getName());
            network.setId(tvShow.getNetwork().getId());
            network.setCountry(tvShow.getNetwork().getCountry().getName());
            series.setNetwork(network);
        }


        if(tvShow.get_embedded() != null){
            List<Episode> episodes = new ArrayList<>();
            tvShow.get_embedded().getEpisodes().forEach(e -> {
                Episode episode = new Episode();
                episode.setId(e.getId());
                episode.setSeason(e.getSeason());
                episode.setNumber(e.getNumber());
                episode.setName(e.getName());
                if(e.getRating() != null) {
                    episode.setRating(e.getRating().getAverage());
                }
                if(e.getAirdate() != null) {
                    LocalDate localDate = LocalDate.parse(e.getAirdate(), formatter);
                    Instant instant = localDate.atStartOfDay().toInstant(ZoneOffset.UTC);
                    Date date = Date.from(instant);
                    episode.setAirDate(date);
                }
                episode.setTvSeries(series);
                episodes.add(episode);
            });
            series.setEpisodes(episodes);
        }

        return series;
    }



}
