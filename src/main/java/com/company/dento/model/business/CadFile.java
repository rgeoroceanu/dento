package com.company.dento.model.business;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "cad_files")
public class CadFile extends Base {

    @Basic
    private String name;
    @Lob
    private byte[] content;
}
