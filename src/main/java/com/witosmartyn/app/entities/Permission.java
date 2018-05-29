package com.witosmartyn.app.entities;

import com.witosmartyn.app.entities.model.NamedEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Collection;

/**
 * vitali
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"name"},callSuper = true)
@ToString(of = {"name"},callSuper = true)
@Entity
public class Permission extends NamedEntity {


    @ManyToMany(mappedBy = "permissions")
    private Collection<Role> roles;


    public Permission(final String name) {
        super();
        setName(name);
    }




}
