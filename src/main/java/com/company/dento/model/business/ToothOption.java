package com.company.dento.model.business;

import com.company.dento.model.type.ToothOptionColumn;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "tooth_options")
public class ToothOption extends Base implements SoftDelete {

    @Basic
    private String name;

    @Basic
    private String abbreviation;

    @Enumerated(EnumType.STRING)
    private ToothOptionColumn displayColumn;

    @ManyToOne(fetch = FetchType.EAGER)
    private JobTemplate specificJob;

    @Basic
    private boolean deleted;

    @Basic
    private boolean active = true;
}
