package com.witosmartyn.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.witosmartyn.app.config.constants.ItemConfig;
import com.witosmartyn.app.config.validators.PhoneNumberConstraint;
import com.witosmartyn.app.entities.model.BaseEntity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

/**
 * vitali
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"description", "name"},callSuper = true)
@Entity
@Table(name = "items")
@ToString(of = {"name"},callSuper = true)
public class Item extends BaseEntity {

    @Version
    private Long version;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", updatable = false)
    private Date created;


    @UpdateTimestamp
    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @Column(length=ItemConfig.MAX_NAME_LENGTH)
    private String name;
    @Column(length=ItemConfig.MAX_DESCRIPTION_LENGTH)
    private String description;
    @Column(name = "avatar_id")
    private String avatarId;
    @ManyToOne(optional = false,
            fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "item",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Column(nullable = true)
    private Set<Image> images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",nullable = true)
    private Category category;

    private String phone;

    @Column(name = "price",nullable = false)
    private Double price = 0.0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id",nullable = true)
    private City city;
}
