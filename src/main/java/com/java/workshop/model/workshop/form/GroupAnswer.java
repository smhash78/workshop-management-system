package com.java.workshop.model.workshop.form;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "group_answers")
public class GroupAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;

    @ManyToOne
    @JsonIgnore
    private GroupFilledForm groupFilledForm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public GroupFilledForm getGroupFilledForm() {
        return groupFilledForm;
    }

    public void setGroupFilledForm(GroupFilledForm groupFilledForm) {
        this.groupFilledForm = groupFilledForm;
    }
}
