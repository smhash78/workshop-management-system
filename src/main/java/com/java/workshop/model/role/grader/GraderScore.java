package com.java.workshop.model.role.grader;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.java.workshop.model.Score;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "grdaer_scores")
public class GraderScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private Grader grader;

    @OneToOne
    @JsonIgnore
    private Grader insGrader;

    @NotNull
    private Score fluency;

    @NotNull
    private Score behaviour;

    @NotNull
    private Score punctuality;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Score getFluency() {
        return fluency;
    }

    public void setFluency(Score fluency) {
        this.fluency = fluency;
    }

    public Score getBehaviour() {
        return behaviour;
    }

    public void setBehaviour(Score behaviour) {
        this.behaviour = behaviour;
    }

    public Score getPunctuality() {
        return punctuality;
    }

    public void setPunctuality(Score punctuality) {
        this.punctuality = punctuality;
    }

    public Grader getGrader() {
        return grader;
    }

    public void setGrader(Grader grader) {
        this.grader = grader;
    }

    public Grader getInsGrader() {
        return insGrader;
    }

    public void setInsGrader(Grader insGrader) {
        this.insGrader = insGrader;
    }

    public float getAverage() {
        return (Math.round(
                ((float)(this.fluency.getValue() + this.behaviour.getValue() + this.punctuality.getValue()))/0.3))
                /(float)10;
    }


}
