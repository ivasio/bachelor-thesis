package com.ivasio.bachelor_thesis.shared.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.time.OffsetDateTime;


@Entity
@ApiModel(description = "Точка траектории")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Уникальный id точки")
    private long id;

    @ApiModelProperty(notes = "Координаты точки : долгота")
    private float longitude;

    @ApiModelProperty(notes = "Координаты точки : широта")
    private float latitude;

    @ApiModelProperty(notes = "Метка времени точки")
    private OffsetDateTime timestamp;

    @ManyToOne
    @JoinColumn(name="route_id", nullable=false)
    private Route route;

    protected Point() {}

    public Point(float longitude, float latitude, OffsetDateTime timestamp){
        this.longitude = longitude;
        this.latitude = latitude;
        this.timestamp = timestamp;
    }

    public Point(long id, float longitude, float latitude, OffsetDateTime timestamp) {
        this(longitude, latitude, timestamp);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public float getLongitude() {
        return longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
