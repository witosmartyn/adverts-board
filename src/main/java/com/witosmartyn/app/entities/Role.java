package com.witosmartyn.app.entities;


import com.witosmartyn.app.entities.model.NamedEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
@Data
@NoArgsConstructor
@AllArgsConstructor

@EqualsAndHashCode(of = {"name"},callSuper = true)
@ToString(of = {"name"},callSuper = true)
@Entity
public class Role  extends NamedEntity implements GrantedAuthority{

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(nullable = false, updatable = false)
//    private Long id;


    @ManyToMany(mappedBy = "authorities")
    private Collection<User> users;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "roles_permissions", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "pemission_id", referencedColumnName = "id"))
    private Collection<Permission> permissions;


//    private String name;


    public Role(final String name) {
        super();
      setName(name);
    }


    @Override
    public String getAuthority() {
        return getName();
    }
}
