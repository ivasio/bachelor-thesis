package com.ivasio.bachelor_thesis.shared.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.GenerationType;


@Entity
@ApiModel(description = "Траектория")
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Уникальный id траектории")
    private long id;

    @ManyToOne
    @JoinColumn(name="junction_id", nullable=false)
    private Junction junction;

}
