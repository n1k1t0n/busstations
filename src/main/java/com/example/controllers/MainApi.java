package com.example.controllers;

import com.example.services.RouteSearcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.transport.Response;

@RestController
public class MainApi {

    private final RouteSearcher searcher;

    @Autowired
    public MainApi(RouteSearcher searcher) {
        this.searcher = searcher;
    }

    @GetMapping(value = "/api/direct", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response hasDirection(@RequestParam(name = "from") String from, @RequestParam(name = "to") String to) {
        Integer result = searcher.search(from, to);
        return new Response(from, to, result != null);
    }

}
