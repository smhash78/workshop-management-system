package com.java.workshop.model.workshop.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.java.workshop.model.User;
import com.java.workshop.model.role.Role;
import com.java.workshop.model.workshop.Group;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Entity
@Table(name = "group_filled_forms")
public class GroupFilledForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateCreated;
    @ManyToOne
    @JsonIgnore
    private Form form;

    private Author author;

    @OneToMany(mappedBy = "groupFilledForm")
    private List<GroupAnswer> answers;

    @ManyToOne
    @JsonIgnore
    private Group group;


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

    public List<GroupAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<GroupAnswer> answers) {
        this.answers = answers;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
