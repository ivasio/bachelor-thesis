package com.ivasio.bachelor_thesis.shared;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "Дорожная развязка")
public class Junction {

    @ApiModelProperty(notes = "Уникальный id дорожной развязки")
    private final long id;

    @ApiModelProperty(notes = "Название дорожной развязки")
    private final String name;

    @ApiModelProperty(notes = "Координаты дорожной развязки : долгота")
    private final float longitude;

    @ApiModelProperty(notes = "Координаты дорожной развязки : широта")
    private final float latitude;

    public Junction(long id, String name, float longitude, float latitude) {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
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
}
