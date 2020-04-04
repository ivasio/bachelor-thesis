package com.ivasio.bachelor_thesis.shared.models;

import java.time.OffsetDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@ApiModel(description = "Точка траектории")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Уникальный id точки")
    private long id;

    @ApiModelProperty(notes = "Координаты точки : долгота")
    private float longitude;
    private float latitude;
    private OffsetDateTime timestamp;


    public Point() {
    }
}
