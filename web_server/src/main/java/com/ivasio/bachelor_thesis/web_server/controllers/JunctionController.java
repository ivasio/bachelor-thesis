package com.ivasio.bachelor_thesis.web_server.controllers;

import com.ivasio.bachelor_thesis.shared.models.Junction;
import com.ivasio.bachelor_thesis.web_server.services.JunctionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


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

    @GetMapping("/{id}/routes_count")
    @ApiOperation(value = "Получить количество траекторий, соответствующих дорожной развязке, " +
            "по ее id", response = Long.class)
    public Long getRoutesCountForId(
            @ApiParam(value = "id дорожной развязки", required = true, example = "1") @PathVariable("id") Long id
    ) {
        Junction junction = service.get(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Junction with id %d not found", id))
        );
        return (long)junction.getRoutes().size();
    }

    @PostMapping("/")
    @ApiOperation(value = "Создать запись о дорожной развязке")
    public void createJunction(@RequestBody Junction junction) {
        service.add(junction);
    }

}