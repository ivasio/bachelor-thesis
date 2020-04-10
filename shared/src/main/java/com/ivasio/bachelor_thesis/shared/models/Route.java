package com.ivasio.bachelor_thesis.shared.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.List;


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

    @OneToMany(mappedBy="route", fetch=FetchType.LAZY)
    private List<Point> points;

    protected Route() {}

    public Route(long id, Junction junction) {
        this.id = id;
        this.junction = junction;
    }

    public long getId() {
        return id;
    }

    public Junction getJunction() {
        return junction;
    }

    public List<Point> getPoints() {
        return points;
    }
}
