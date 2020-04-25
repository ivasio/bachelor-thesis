package com.ivasio.bachelor_thesis.shared.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Set;


@Entity
@JsonIgnoreProperties("routes")
@ApiModel(description = "Дорожная развязка")
public class Junction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Уникальный id дорожной развязки")
    private long id;

    @ApiModelProperty(notes = "Название дорожной развязки")
    private String name;

    @ApiModelProperty(notes = "Координаты дорожной развязки : долгота")
    private float longitude;

    @ApiModelProperty(notes = "Координаты дорожной развязки : широта")
    private float latitude;

    @ApiModelProperty(notes = "Условный радиус дорожной развязки (м)")
    private float radius;


    @OneToMany(mappedBy="junction", fetch=FetchType.LAZY)
    private Set<Route> routes;

    protected Junction() {}

    public Junction(long id, String name, float longitude, float latitude, float radius) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.radius = radius;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getRadius() {
        return radius;
    }

    public Set<Route> getRoutes() {
        return routes;
    }

}
