package com.ivasio.bachelor_thesis.web_server.services;

import com.ivasio.bachelor_thesis.shared.models.Junction;
import com.ivasio.bachelor_thesis.web_server.repositories.JunctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class JunctionService {

    private final JunctionUpdatesSender updatesSender = new JunctionUpdatesSender();

    @Autowired
    private JunctionRepository repo;

    public List<Junction> listAll() {
        return repo.findAll();
    }

    public Optional<Junction> get(Long id) {
        return repo.findById(id);
    }

    public void add(Junction junction) {
        repo.saveAndFlush(junction);
        updatesSender.send(junction);
    }


}