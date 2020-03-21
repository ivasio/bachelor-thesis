package com.ivasio.bachelor_thesis.web_server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ivasio.bachelor_thesis.shared.Junction;



@RestController
@RequestMapping("/junctions")
public class JunctionController {

    private final ConcurrentHashMap<Integer, Junction> allJunctions;

    public JunctionController() {
        allJunctions = new ConcurrentHashMap<Integer, Junction>();
        allJunctions.put(1, new Junction(1, "МКАД - ш. Энтузиастов", 37.84270f, 55.77692f));
        allJunctions.put(2, new Junction(2, "МКАД - Каширское ш.", 37.72944f, 55.59180f));
        allJunctions.put(3, new Junction(3, "МКАД - Рязанский пр.", 37.83499f, 55.70789f));
    }

    @GetMapping("/")
    public List<Junction> listAllJunctions() {
        return new ArrayList<Junction>(allJunctions.values());
    }

    @GetMapping("/{id}")
    public Junction getJunctionById(@PathVariable("id") int id) {
        Junction result =  allJunctions.get(id);
        if (result != null) {
            return result;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Junction with id %d not found", id));
        }
    }

}