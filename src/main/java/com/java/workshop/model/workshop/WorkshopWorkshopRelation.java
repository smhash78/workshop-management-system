package com.java.workshop.model.workshop;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "w_w_relations")
public class WorkshopWorkshopRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private WWRelation relationType;

    @ManyToOne
    @JsonIgnore
    private Workshop firstWorkshop; // second pishniaze/hamniaze first

    @ManyToOne
    @JsonIgnore
    private Workshop secondWorkshop;


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

    public Workshop getFirstWorkshop() {
        return firstWorkshop;
    }

    public void setFirstWorkshop(Workshop firstWorkshop) {
        this.firstWorkshop = firstWorkshop;
    }

    public Workshop getSecondWorkshop() {
        return secondWorkshop;
    }

    public void setSecondWorkshop(Workshop secondWorkshop) {
        this.secondWorkshop = secondWorkshop;
    }
}
