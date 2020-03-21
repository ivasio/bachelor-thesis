package com.ivasio.bachelor_thesis.web_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ivasio.bachelor_thesis.shared.models.Junction;


public interface JunctionRepository extends JpaRepository<Junction, Long> {

}