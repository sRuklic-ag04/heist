package com.notch.heist.domain;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "skill")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @OneToMany(
            mappedBy = "skill",
            cascade = CascadeType.MERGE
    )
    private Set<MemberSkill> members;

    @OneToMany(
            mappedBy = "skill",
            cascade = CascadeType.MERGE
    )
    private Set<HeistSkill> heists;

    public Skill(String name) {
        this.name = name;
    }

    public Skill() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MemberSkill> getMembers() {
        return members;
    }

    public void setMembers(Set<MemberSkill> members) {
        this.members = members;
    }

    public Set<HeistSkill> getHeists() {
        return heists;
    }

    public void setHeists(Set<HeistSkill> heists) {
        this.heists = heists;
    }
}
