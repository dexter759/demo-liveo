package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "skill_id")
    private long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Level level;

    @ManyToOne
    @JoinColumn (name="person_id")
    @JsonBackReference
    private Person person;

    public Skill() {
    }

    public Skill(String name, Level level) {
        this.name = name;
        this.level = level;
    }

    public long getId() {
        return id;
    }

    public Skill(String name, Level level, Person person) {
        this.name = name;
        this.level = level;
        this.person = person;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }


        public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

}
