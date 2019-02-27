package com.example.jpa.entity;

import javax.persistence.*;

@Entity
@Table(name = "t_test")
public class Test {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column()
    private Integer id;

    @Column
    private String name;

    public Integer getId() {
        return id;
    }

    public Test setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Test setName(String name) {
        this.name = name;
        return this;
    }
}
