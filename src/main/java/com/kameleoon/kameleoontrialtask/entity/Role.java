package com.kameleoon.kameleoontrialtask.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role_table")
public class Role implements GrantedAuthority {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "role_name")
    private String roleName;

    @ManyToMany(cascade = {CascadeType.PERSIST,
        CascadeType.DETACH,
        CascadeType.REFRESH,
        CascadeType.MERGE})
    @JoinTable(name = "user_role_table",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;

    @Override
    public String getAuthority() {
        return getRoleName();
    }
}
