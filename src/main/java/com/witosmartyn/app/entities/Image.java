package com.witosmartyn.app.entities;

import com.witosmartyn.app.entities.model.NamedEntity;
import lombok.*;

import javax.persistence.*;

/**
 * vitali
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"name"},callSuper = true)
@EqualsAndHashCode(of = {"name"},callSuper = true)
@Data
@Table(name="images")
public class Image extends NamedEntity {
    private long size;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] pic;

    @ManyToOne(optional = false,fetch = FetchType.LAZY )
    private Item item;



}
