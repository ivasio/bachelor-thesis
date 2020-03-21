package com.ivasio.bachelor_thesis.web_server.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.ivasio.bachelor_thesis.shared.models.Junction;
import com.ivasio.bachelor_thesis.web_server.services.JunctionService;


@RestController
@RequestMapping("/junctions")
public class JunctionController {

    @Autowired
    private JunctionService service;

    /*
    public JunctionController() {
        allJunctions = new ConcurrentHashMap<Integer, Junction>();
        allJunctions.put(1, new Junction(1, "МКАД - ш. Энтузиастов", 37.84270f, 55.77692f));
        allJunctions.put(2, new Junction(2, "МКАД - Каширское ш.", 37.72944f, 55.59180f));
        allJunctions.put(3, new Junction(3, "МКАД - Рязанский пр.", 37.83499f, 55.70789f));
    }
    */

    @GetMapping("/")
    @ApiOperation(value = "Получить список всех известных дорожных развязок", response = Junction.class)
    public List<Junction> listAllJunctions() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Получить информацию о дорожной развязке по ее id", response = Junction.class)
    public Junction getJunctionById(
            @ApiParam(value = "id дорожной развязки", required = true, example = "1") @PathVariable("id") Long id
    ) {
        return service.get(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Junction with id %d not found", id))
        );
    }

}