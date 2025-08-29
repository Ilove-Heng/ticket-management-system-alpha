package com.nangseakheng.user.entity;

import com.nangseakheng.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tbl_users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", nullable = false, unique = true)
    private String username;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "user_img")
    private String userImage;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "user_type", nullable = false)
    private String userType;
    @Column(name="gender")
    private String gender;
    @Column(name="date_of_birth")
    private LocalDateTime dateOfBirth;
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    @Column(name = "login_attempt")
    private Integer loginAttempt;
    @Column(name = "max_attempt")
    private Integer maxAttempt;
    @Column(name = "enable_allocate")
    private boolean enableAllocate;
    @Column(name = "status")
    private String status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tbl_user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tbl_user_group",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id")
    )
    private Set<Group> groups = new HashSet<>();

    // Helper method to add role to the user
    public void addRole(Role role) {
        this.roles.add(role);
    }
    // Helper method to add group to the user
    public void addGroup(Group group) {
        this.groups.add(group);
    }

}
