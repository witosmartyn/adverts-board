package com.witosmartyn.app.entities;

import com.witosmartyn.app.entities.model.NamedEntity;
import lombok.*;

import javax.persistence.Entity;

/**
 * vitali
 */
@Entity
@NoArgsConstructor
@ToString(of = "name",callSuper = true)
@EqualsAndHashCode(of = {"name"},callSuper = true)
@Data
public class City  extends NamedEntity{

    public City(Long id,String name) {
        super.setId(id);
        super.setName(name);
    }
}
