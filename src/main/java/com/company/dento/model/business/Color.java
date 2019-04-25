package com.company.dento.model.business;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "colors")
public class Color extends Base {

    @Basic
    private String name;
}
