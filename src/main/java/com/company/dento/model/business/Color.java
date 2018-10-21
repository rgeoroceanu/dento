package com.company.dento.model.business;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "colors")
public class Color extends Base {
    private String name;
}
