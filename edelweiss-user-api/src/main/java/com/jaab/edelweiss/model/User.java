package com.jaab.edelweiss.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @Column(name = "user_email", nullable = false, length = 30)
    private String email;

    @Column(name = "user_password", nullable = false, length = 100)
    private String password;

    @Column(name = "user_role", nullable = false, length = 11)
    private Role role;

    public Long getId() {
        return id;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
