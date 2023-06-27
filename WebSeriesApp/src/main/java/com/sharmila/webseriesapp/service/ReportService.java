package com.sharmila.webseriesapp.service;

import com.sharmila.webseriesapp.entities.Episode;
import com.sharmila.webseriesapp.entities.TvSeries;
import com.sharmila.webseriesapp.models.reports.AllSeriesSummary;
import com.sharmila.webseriesapp.models.reports.BestSeriesSummary;
import com.sharmila.webseriesapp.respository.TvSeriesRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class ReportService {

    @Autowired
    private TvSeriesRepository tvSeriesRepository;

    public List<TvSeries> getTop10ByRating() {
        List<TvSeries> top10 = tvSeriesRepository.findTop10ByOrderByRatingDesc();
        return top10;
    }

    public void saveSeries(TvSeries tvSeries) {
        Random random = new Random();
        if (tvSeries.getId() == null) {
            tvSeries.setId(random.nextLong(100000));
        }
        if (tvSeries.getNetwork() != null && tvSeries.getNetwork().getId() == null) {
            tvSeries.getNetwork().setId(random.nextLong(100000));
        }

        // This is to avoid errors while generating the report
        if(tvSeries.getEnded() == null) {
            tvSeries.setEnded(new Date());
        }
        if(tvSeries.getPremiered() == null) {
            tvSeries.setPremiered(new Date());
        }

        tvSeriesRepository.save(tvSeries);
    }


    public List<AllSeriesSummary> getTvSeriesSummary() {
        List<TvSeries> series = tvSeriesRepository.findAll();
        List<AllSeriesSummary> summaries = new ArrayList<>();

        for (TvSeries s : series) {
            String genres = "";
            for (String genre : s.getGenres()) {
                genres += genre + ",";
            }
            if (genres.endsWith(",")) {
                genres = genres.substring(0, genres.length() - 1);
            }

            long releasedEpisodeCount = 0;
            for (Episode episode : s.getEpisodes()) {
                if (episode.getAirDate().toInstant().isBefore(Instant.now())) {
                    releasedEpisodeCount++;
                }
            }

            String network = "";
            if (s.getNetwork() != null) {
                network = s.getNetwork().getName();
            }

            AllSeriesSummary summary = new AllSeriesSummary(s.getName(), network, genres, s.getEpisodes().size(), releasedEpisodeCount);
            summaries.add(summary);
        }

        return summaries;
    }


    public List<BestSeriesSummary> getBestSeriesEpisodesSummary() {
        List<TvSeries> series = tvSeriesRepository.findAll();
        List<BestSeriesSummary> summaries = new ArrayList<>();

        for (TvSeries s : series) {
            StringBuilder genresBuilder = new StringBuilder();
            for (String genre : s.getGenres()) {
                genresBuilder.append(genre);
            }
            String genres = genresBuilder.toString();

            Episode bestEpisode = null;
            for (Episode episode : s.getEpisodes()) {
                if (bestEpisode == null || episode.getRating() > bestEpisode.getRating()) {
                    bestEpisode = episode;
                }
            }

            String network = "";
            if (s.getNetwork() != null) {
                network = s.getNetwork().getName();
            }

            if (bestEpisode != null) {
                BestSeriesSummary summary = new BestSeriesSummary(
                        s.getName(),
                        network,
                        genres,
                        bestEpisode.getSeason(),
                        bestEpisode.getNumber(),
                        bestEpisode.getName(),
                        bestEpisode.getRating()
                );
                summaries.add(summary);
            }
        }

        return summaries;
    }


    public List<TvSeries> getAllSeries() {
        List<TvSeries> allTvseries = tvSeriesRepository.findAll();
        return allTvseries;
    }

    public void deleteSeries(Long id) {
        tvSeriesRepository.deleteById(id);
    }

    public TvSeries getById(Long id) {
        TvSeries tvSeries = tvSeriesRepository.findById(id).get();
        return tvSeries;
    }

    public void createBestSeriesSummaryTextFile(HttpServletResponse response) throws IOException {

        List<BestSeriesSummary> bestSeriesSummaryList = this.getBestSeriesEpisodesSummary();

        // Write the text data to the response
        PrintWriter writer = response.getWriter();
        writer.write("SHOW_NAME;NETWORK;GENRES;SEASON_NUMBER;EPISODE_NUMBER;EPISODE_NAME;RATING;\n");
        for (BestSeriesSummary bestSeriesSummary : bestSeriesSummaryList) {
            writer.write(bestSeriesSummary.getName() + ";");
            writer.write(bestSeriesSummary.getNetwork() + ";");
            writer.write(bestSeriesSummary.getGenre() + ";");
            writer.write(bestSeriesSummary.getSeasonNumber() + ";");
            writer.write(bestSeriesSummary.getEpisodeNumber() + ";");
            writer.write(bestSeriesSummary.getEpisodeName() + ";");
            writer.write(bestSeriesSummary.getEpisodeRating() + ";\n");
        }
    }


    public void createAllSummeryTextFile(HttpServletResponse response) throws IOException {

        List<AllSeriesSummary> allSeriesSummary = this.getTvSeriesSummary();

        PrintWriter writer = response.getWriter();
        // Write the text data to the response
        writer.write("SHOW_NAME;NETWORK;GENRES;EPISODE_COUNT;RELEASED_EPISODE_COUNT\n");
        for (AllSeriesSummary tvSeriesSummary : allSeriesSummary) {
            writer.write(tvSeriesSummary.getName() + ";");
            writer.write(tvSeriesSummary.getNetwork() + ";");
            writer.write(tvSeriesSummary.getGenre() + ";");
            writer.write(tvSeriesSummary.getEpisodeCount() + ";");
            writer.write(tvSeriesSummary.getReleasedEpisodeCount() + "\n");
        }
    }

    public void createTop10TextFile(HttpServletResponse response) throws IOException {

        List<TvSeries> top10 = this.getTop10ByRating();
        PrintWriter writer = response.getWriter();
        writer.write("Name;Rating;Genres;Network;Premiered;Ended\n");
        for (TvSeries tvSeries : top10) {
            writer.write(tvSeries.getName() + ";");
            writer.write(tvSeries.getRating() + ";");
            writer.write(String.join(", ", tvSeries.getGenres()) + ";");
            writer.write((tvSeries.getNetwork() != null ? tvSeries.getNetwork().getName() : "") + ";");
            writer.write(tvSeries.getPremiered().toString() + ";");
            writer.write((tvSeries.getEnded() != null ? tvSeries.getEnded().toString() : "Present") + ";");
            writer.write("\n");
        }
    }
}
