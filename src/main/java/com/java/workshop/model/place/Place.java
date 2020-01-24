package com.java.workshop.model.place;

import com.java.workshop.model.workshop.WorkshopSession;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "places")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private State state;
    private City city;
    private String Address;

    @OneToMany(mappedBy = "place")
    private List<WorkshopSession> workshopSessions;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public List<WorkshopSession> getWorkshopSessions() {
        return workshopSessions;
    }

    public void setWorkshopSessions(List<WorkshopSession> workshopSessions) {
        this.workshopSessions = workshopSessions;
    }


}
