package io.github.lordviktor.javaPersistencePoc.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;
    
    private String name;
    
    @ManyToOne
    @JoinColumn
    private Pastoral admin;

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
    
}
