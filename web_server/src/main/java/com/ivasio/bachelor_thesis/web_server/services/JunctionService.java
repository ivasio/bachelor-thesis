package com.ivasio.bachelor_thesis.web_server.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ivasio.bachelor_thesis.shared.models.Junction;
import com.ivasio.bachelor_thesis.web_server.repositories.JunctionRepository;


@Service
@Transactional
public class JunctionService {

    @Autowired
    private JunctionRepository repo;

    public List<Junction> listAll() {
        return repo.findAll();
    }

    public void save(Junction product) {
        repo.save(product);
    }

    public Optional<Junction> get(Long id) {
        return repo.findById(id);
    }

}