package com.java.workshop.model.workshop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.java.workshop.model.Admin;

import javax.management.relation.Relation;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workshops")
public class Workshop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "workshop")
    private List<WorkshopSession> sessions;

    @OneToMany(mappedBy = "firstWorkshop")
    private List<WorkshopWorkshopRelation> relations;

    @OneToMany(mappedBy = "secondWorkshop")
    private List<WorkshopWorkshopRelation> workshopsWhichHaveRelationWithThis;

    @OneToMany(mappedBy = "workshop")
    private List<SessionWorkshopRelation> sessionsWhichHaveRelationWithThis;

    @ManyToOne
    @JsonIgnore
    private Admin admin;

    @ManyToOne
    @JsonIgnore
    @NotNull
    private Category category;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<WorkshopSession> getSessions() {
        return sessions;
    }

    public void setSessions(List<WorkshopSession> sessions) {
        this.sessions = sessions;
    }

    public List<WorkshopWorkshopRelation> getRelations() {
        return relations;
    }

    public void setRelations(List<WorkshopWorkshopRelation> relations) {
        this.relations = relations;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public List<WorkshopWorkshopRelation> getWorkshopsWhichHaveRelationWithThis() {
        return workshopsWhichHaveRelationWithThis;
    }

    public void setWorkshopsWhichHaveRelationWithThis(List<WorkshopWorkshopRelation> workshopsWhichHaveRelationWithThis) {
        this.workshopsWhichHaveRelationWithThis = workshopsWhichHaveRelationWithThis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    //@JsonIgnore
    public ArrayList<Workshop> getAllPrerequisites() {
        ArrayList<Workshop> workshops = new ArrayList<>();
        if (this.relations == null) {
            return workshops;
        }
        for (WorkshopWorkshopRelation rel : this.relations) {
            if (rel.getRelationType().equals(WWRelation.prerequisites)) {
                workshops.add(rel.getSecondWorkshop());
            }
        }
        return workshops;
    }
}
