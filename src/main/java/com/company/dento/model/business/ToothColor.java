package com.company.dento.model.business;

import com.company.dento.model.type.ColorCategory;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "tooth_colors")
public class ToothColor extends Base {

    @Basic
    private String name;

    @Enumerated(EnumType.STRING)
    private ColorCategory category;
}