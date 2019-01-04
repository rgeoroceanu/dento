package com.company.dento.model.business;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class CadFile {

    @Basic
    private String name;
    @Lob
    private byte[] content;
}
