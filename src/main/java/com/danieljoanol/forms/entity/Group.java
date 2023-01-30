package com.danieljoanol.forms.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
@Table(name = "group")
public class Group {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY , generator = "group_id_seq")
    @SequenceGenerator(name = "group_id_seq", sequenceName = "group_seq", initialValue = 2, allocationSize = 1)
    private Long id;

    private String name;
    private Integer maxUsers;
    private Integer totalUsers;

    @OneToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "group_users", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users = new ArrayList<>();

    @OneToMany(fetch=FetchType.LAZY)
    @JoinTable(name = "group_shops", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "shop_id"))
    private List<Shop> shops = new ArrayList<>();

    @OneToMany(fetch=FetchType.LAZY)
    @JoinTable(name = "group_clients", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "client_id"))
    private List<Client> clients = new ArrayList<>();

}
