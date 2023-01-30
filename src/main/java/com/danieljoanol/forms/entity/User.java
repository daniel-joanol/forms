package com.danieljoanol.forms.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "_user")
public class User implements GenericEntity<User> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_seq", initialValue = 2, allocationSize = 1)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String username;
    private String password;

    private LocalDate lastPayment;
    private String comments;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    private boolean isEnabled;
    private Date disabledDate;

    private Integer passwordCode;
    private Integer usernameCode;
    private LocalDateTime passwordTimeLimit;
    private LocalDateTime usernameTimeLimit;
    private String newPassword;
    private String newUsername;

}
