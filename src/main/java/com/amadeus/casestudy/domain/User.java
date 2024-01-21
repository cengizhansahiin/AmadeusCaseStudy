package com.amadeus.casestudy.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@RequiredArgsConstructor
public class User extends BaseEntity{

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

}
