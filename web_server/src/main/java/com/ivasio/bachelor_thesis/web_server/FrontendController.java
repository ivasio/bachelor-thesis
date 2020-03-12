package com.ivasio.bachelor_thesis.web_server;

import java.util.LinkedList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ivasio.bachelor_thesis.shared.Junction;


@RestController
public class FrontendController {

    private static final String responseTemplate = "Hello, %s!";

    @GetMapping("/junctions")
    public List<Junction> listJunctions() {
        Junction sampleJunction = new Junction(1, "leningrarka", 4.6f, 6.8f);
        List<Junction> response = new LinkedList<Junction>();
        response.add(sampleJunction);
        return response;
    }

}