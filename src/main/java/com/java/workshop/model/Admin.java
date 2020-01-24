package com.java.workshop.model;

import com.java.workshop.model.role.Student.Payment;
import com.java.workshop.model.workshop.Workshop;
import com.java.workshop.model.workshop.WorkshopSession;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "admins")
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "admin")
    private List<Workshop> workshops;

    @OneToMany(mappedBy = "admin")
    private List<WorkshopSession> workshopSessions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Workshop> getWorkshops() {
        return workshops;
    }

    public void setWorkshops(List<Workshop> workshops) {
        this.workshops = workshops;
    }

    public List<WorkshopSession> getWorkshopSessions() {
        return workshopSessions;
    }

    public void setWorkshopSessions(List<WorkshopSession> workshopSessions) {
        this.workshopSessions = workshopSessions;
    }


}
