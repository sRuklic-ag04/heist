package com.notch.heist.domain;

import com.notch.heist.domain.enums.Sex;
import com.notch.heist.domain.enums.MemberStatus;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(unique = true)
    private String email;

    @OneToMany(
            mappedBy = "member",
            cascade = CascadeType.MERGE,
            orphanRemoval = true
    )
    private Set<MemberSkill> skills;

    @ManyToMany(mappedBy = "members")
    private Set<Heist> heists;

//    @ManyToOne(cascade = CascadeType.ALL)
//    private Skill mainSkill;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

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

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<MemberSkill> getSkills() {
        return skills;
    }

    public void setSkills(Set<MemberSkill> skills) {
        this.skills = skills;
    }

//    public Skill getMainSkill() {
//        return mainSkill;
//    }
//
//    public void setMainSkill(Skill mainSkill) {
//        this.mainSkill = mainSkill;
//    }


    public Set<Heist> getHeists() {
        return heists;
    }

    public void setHeists(Set<Heist> heists) {
        this.heists = heists;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }
}
