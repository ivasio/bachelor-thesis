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