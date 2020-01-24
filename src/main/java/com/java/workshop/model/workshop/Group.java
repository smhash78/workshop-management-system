package com.java.workshop.model.workshop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.java.workshop.model.workshop.form.GroupFilledForm;
import com.java.workshop.model.role.Student.Student;
import com.java.workshop.model.role.grader.Grader;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer internalId;

    @OneToMany(mappedBy = "group")
    private List<Student> students;

    @OneToMany(mappedBy = "group")
    private List<Grader> graders;

    @OneToMany(mappedBy = "group")
    private List<GroupFilledForm> filledForms;

    @ManyToOne
    @JsonIgnore
    private WorkshopSession workshopSession;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Grader> getGraders() {
        return graders;
    }

    public void setGraders(List<Grader> graders) {
        this.graders = graders;
    }

    public List<GroupFilledForm> getFilledForms() {
        return filledForms;
    }

    public void setFilledForms(List<GroupFilledForm> filledForms) {
        this.filledForms = filledForms;
    }

    public WorkshopSession getWorkshopSession() {
        return workshopSession;
    }

    public void setWorkshopSession(WorkshopSession workshopSession) {
        this.workshopSession = workshopSession;
    }

    public Integer getInternalId() {
        return internalId;
    }

    public void setInternalId(Integer internalId) {
        this.internalId = internalId;
    }
}
