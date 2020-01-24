package com.java.workshop.model.workshop.form;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "student_answers")
public class StudentAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;

    @ManyToOne
    @JsonIgnore
    private StudentFilledForm studentFilledForm;

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

    public StudentFilledForm getStudentFilledForm() {
        return studentFilledForm;
    }

    public void setStudentFilledForm(StudentFilledForm studentFilledForm) {
        this.studentFilledForm = studentFilledForm;
    }
}
