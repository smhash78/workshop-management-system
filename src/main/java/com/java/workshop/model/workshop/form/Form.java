package com.java.workshop.model.workshop.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.java.workshop.model.workshop.WorkshopSession;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "forms")
public class Form {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "form")
    private List<Question> questions;

    @ManyToOne
    @JsonIgnore
    private WorkshopSession workshopSession;

    private FormType formType;

    @OneToMany(mappedBy = "form")
    private List<GroupFilledForm> groupFilledForm;

    @OneToMany(mappedBy = "form")
    private List<StudentFilledForm> studentFilledForm;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public WorkshopSession getWorkshopSession() {
        return workshopSession;
    }

    public void setWorkshopSession(WorkshopSession workshopSession) {
        this.workshopSession = workshopSession;
    }

    public List<GroupFilledForm> getGroupFilledForm() {
        return groupFilledForm;
    }

    public void setGroupFilledForm(List<GroupFilledForm> groupFilledForm) {
        this.groupFilledForm = groupFilledForm;
    }

    public List<StudentFilledForm> getStudentFilledForm() {
        return studentFilledForm;
    }

    public void setStudentFilledForm(List<StudentFilledForm> studentFilledForm) {
        this.studentFilledForm = studentFilledForm;
    }

    public FormType getFormType() {
        return formType;
    }

    public void setFormType(FormType formType) {
        this.formType = formType;
    }
}

