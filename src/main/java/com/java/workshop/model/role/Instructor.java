package com.java.workshop.model.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.java.workshop.model.User;
import com.java.workshop.model.workshop.WorkshopSession;

import javax.persistence.*;

@Entity
@Table(name = "instructors")
public class Instructor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    //@JsonIgnore
    private WorkshopSession session;

    @ManyToOne
    @JsonIgnore
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkshopSession getSession() {
        return session;
    }

    public void setSession(WorkshopSession session) {
        this.session = session;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
