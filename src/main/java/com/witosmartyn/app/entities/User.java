package com.witosmartyn.app.entities;

import com.witosmartyn.app.entities.model.NamedEntity;
import lombok.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * User: vitali
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(of = {"username"},callSuper = true)
@Table(name = "users")
@ToString(of = {"username"}, callSuper = true)
public class User extends NamedEntity implements UserDetails {

    @Email
    @Column(unique = true)
    @NotNull
    @NotEmpty
    @Size(min = 4, max = 64)
    private String username;

    @NotNull
    @NotEmpty
    @Size(min = 4, max = 64)
    private String password;

    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;


    @NotNull
    @NotEmpty
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Set<Role> authorities;

    public Set<Item> getItems() {
        if (itemsInternal == null) {
            itemsInternal = new HashSet<>();
        }
        return itemsInternal;
    }

    public void setItems(Set<Item> newData) {
        newData.stream().forEach(el -> el.setUser(this));
        this.itemsInternal.addAll(newData);
    }


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Item> itemsInternal;

    public User(@NotNull String username, @NotNull String password, @NotNull Set<Role> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public void addItem(Item item) {
        if (item != null) {
            item.setUser(this);
            getItems().add(item);// LazyInitializationException
        }
    }
}

