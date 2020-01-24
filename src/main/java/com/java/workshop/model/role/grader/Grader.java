package com.java.workshop.model.role.grader;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.java.workshop.model.Score;
import com.java.workshop.model.User;
import com.java.workshop.model.workshop.Group;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "graders")
public class Grader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private GraderStatus status;

    @ManyToOne
    @JsonIgnore
    private Group group;

    @OneToOne(mappedBy = "grader")
    private GraderResponsibility responsibility;

    @OneToOne(mappedBy = "insGrader")
    private GraderScore instructorScore;

    @OneToMany(mappedBy = "grader")
    private List<GraderScore> studentsScore;

    @ManyToOne
    @JsonIgnore
    private User user;

    private String requestMessage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GraderStatus getStatus() {
        return status;
    }

    public void setStatus(GraderStatus status) {
        this.status = status;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public GraderResponsibility getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(GraderResponsibility responsibility) {
        this.responsibility = responsibility;
    }

    public GraderScore getInstructorScore() {
        return instructorScore;
    }

    public void setInstructorScore(GraderScore instructorScore) {
        this.instructorScore = instructorScore;
    }

    public List<GraderScore> getStudentsScore() {
        return studentsScore;
    }

    public void setStudentsScore(List<GraderScore> studentsScore) {
        this.studentsScore = studentsScore;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public Long getSessionId() {
        return this.getGroup().getWorkshopSession().getId();
    }

    public Long getGroupId() {
        return this.getGroup().getId();
    }

    public float getInstructorAverageScore() {
        if (this.instructorScore != null) {
            return this.instructorScore.getAverage();
        }
        return 0;
    }

    public float getStudentAverageScore() {
        int i = 0;
        float flt = 0;
        if (this.studentsScore == null) {
            return 0;
        }
        for (GraderScore sc : this.studentsScore) {
            flt = flt + sc.getAverage();
            i++;
        }
        float j = (float)(i/10);
        return Math.round(flt/j)/(float)10;
    }
}
