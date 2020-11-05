package com.jp.covid19.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;


import javax.persistence.*;

@ToString
@Data
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "isactive")
    private boolean isActive;

}
