package com.sharmila.webseriesapp.controller;

import com.sharmila.webseriesapp.entities.TvSeries;
import com.sharmila.webseriesapp.models.reports.AllSeriesSummary;
import com.sharmila.webseriesapp.models.reports.BestSeriesSummary;
import com.sharmila.webseriesapp.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/")
    public String goToHome() {
        return "index";
    }

    @GetMapping("/all-series")
    public String showAllSeries(Model model) {
        List<TvSeries> allSeries = reportService.getAllSeries();
        model.addAttribute("allSeries", allSeries);

        return "allSeries";
    }

    @GetMapping(value = "/report/top10")
    public String showTop10(Model model) {
        List<TvSeries> top10 = reportService.getTop10ByRating();
        model.addAttribute("top10", top10);

        return "top10";
    }

    @GetMapping(value = "/add-series")
    public String addSeries(Model model) {
        model.addAttribute("series", new TvSeries());

        return "addSeries";
    }

    @PostMapping(value = "/save-series")
    public String saveTvSeries(@ModelAttribute("series") TvSeries tvSeries) {
        reportService.saveSeries(tvSeries);

        return "redirect:all-series";
    }

    @GetMapping(value = "/delete-series")
    public String deleteSeries(@RequestParam Long seriesId) {
        reportService.deleteSeries(seriesId);

        return "redirect:all-series";
    }

    @GetMapping(value = "/edit-series")
    public String editSeries(@RequestParam Long seriesId, Model model) {
        TvSeries tvSeries = reportService.getById(seriesId);
        model.addAttribute("series", tvSeries);

        return "addSeries";
    }

    @GetMapping("/summary/all")
    public String getAllSeriesSummary(Model model) {
        List<AllSeriesSummary> allSeriesSummary = reportService.getTvSeriesSummary();
        model.addAttribute("allSeriesSummary", allSeriesSummary);

        return "allSeriesSummary";
    }

    @GetMapping("/summary/best")
    public String getBestSeriesEpisodesSummary(Model model) {
        List<BestSeriesSummary> bestSeriesSummary = reportService.getBestSeriesEpisodesSummary();
        model.addAttribute("bestSeriesSummary", bestSeriesSummary);

        return "bestSeriesSummary";
    }

    @GetMapping(value = "/summary/best/download", produces = "text/plain")
    public void downloadBestSeriesSummaryText(HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=best_series_summary.txt");

        reportService.createBestSeriesSummaryTextFile(response);
    }

    @GetMapping(value = "/summary/all/download", produces = "text/plain")
    public void downloadAllSummeryText(HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tv_series_summary.txt");

        reportService.createAllSummeryTextFile(response);
    }

    @GetMapping(value = "/report/top10/download", produces = "text/plain")
    public void downloadTxt(HttpServletResponse response) throws IOException {
        // Set the content type and headers for TXT response
        response.setContentType("text/plain");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tv_series_report.txt");

        reportService.createTop10TextFile(response);
    }
}
