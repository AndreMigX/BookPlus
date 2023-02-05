package com.am.bookplus.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "student", schema = "student")
public class EntityStudent
{
    @Id
    private String id;

    @Column(name="name")
    private String name;

    @Column(name="surname")
    private String surname;

    @Column(name="email")
    private String email;
    
    @OneToMany
    @JoinColumn(name = "student_id")
    @JsonIgnore
    private Set<EntityRegister> registerOccurrences;

    public EntityStudent(){}
    public EntityStudent(String id, String name, String surname, String email)
    {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }
    
    public Set<EntityRegister> getRegisterOccurrences() {
        return registerOccurrences;
    }
}