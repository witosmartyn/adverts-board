package com.witosmartyn.app.entities;

import com.witosmartyn.app.entities.model.NamedEntity;
import lombok.*;

import javax.persistence.*;

/**
 * vitali
 */
@Entity
@NoArgsConstructor
@ToString(of = "name",callSuper = true)
@EqualsAndHashCode(of = {"name"},callSuper = true)
@Data
public class Category extends NamedEntity {

    public Category(Long id,String name) {
        super.setId(id);
        super.setName(name);
    }
    }
