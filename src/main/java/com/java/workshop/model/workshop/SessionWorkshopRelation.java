package com.java.workshop.model.workshop;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "s_w_relations")
public class SessionWorkshopRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private WWRelation relationType;

    @ManyToOne
    @JsonIgnore
    private WorkshopSession session; // second pishniaze/hamniaze first

    @ManyToOne
    @JsonIgnore
    private Workshop workshop;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WWRelation getRelationType() {
        return relationType;
    }

    public void setRelationType(WWRelation relationType) {
        this.relationType = relationType;
    }

    public WorkshopSession getSession() {
        return session;
    }

    public void setSession(WorkshopSession session) {
        this.session = session;
    }

    public Workshop getWorkshop() {
        return workshop;
    }

    public void setWorkshop(Workshop workshop) {
        this.workshop = workshop;
    }
}
