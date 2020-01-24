package com.java.workshop.model.role.Student;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.java.workshop.model.User;
import com.java.workshop.model.workshop.form.GroupFilledForm;
import com.java.workshop.model.role.Role;
import com.java.workshop.model.workshop.Group;
import com.java.workshop.model.workshop.form.StudentFilledForm;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "students")
public class Student implements Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private StudentStatus status;

    @NotNull
    private StudentPaymentStatus paymentStatus;

    @ManyToOne
    @JsonIgnore
    private Group group;
    private StudentAttendancy attendancy;


    @OneToMany(mappedBy = "student")
    private List<Payment> payments;

    @OneToMany(mappedBy = "student")
    private List<StudentFilledForm> filledForms;

    @ManyToOne
    @JsonIgnore
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StudentStatus getStatus() {
        return status;
    }

    public void setStatus(StudentStatus status) {
        this.status = status;
    }

    public StudentPaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(StudentPaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public StudentAttendancy getAttendancy() {
        return attendancy;
    }

    public void setAttendancy(StudentAttendancy attendancy) {
        this.attendancy = attendancy;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<StudentFilledForm> getFilledForms() {
        return filledForms;
    }

    public void setFilledForms(List<StudentFilledForm> filledForms) {
        this.filledForms = filledForms;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonIgnore
    public boolean isAllPayed() {
        if (this.payments == null)
            return true;
        for (Payment p : this.payments) {
            if (!p.isPayed()) {
                return false;
            }
        }
        return true;
    }
}
