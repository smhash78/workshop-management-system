package com.java.workshop.model.workshop.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.java.workshop.model.role.Student.Student;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "student_filled_forms")
public class StudentFilledForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateCreated;
    @ManyToOne
    @JsonIgnore
    private Form form;

//    private Role author;

    @OneToMany(mappedBy = "studentFilledForm")
    private List<StudentAnswer> answers;

    @ManyToOne
    @JsonIgnore
    private Student student;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }

    public List<StudentAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<StudentAnswer> answers) {
        this.answers = answers;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
