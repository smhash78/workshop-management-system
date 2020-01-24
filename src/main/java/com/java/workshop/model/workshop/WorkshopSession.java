package com.java.workshop.model.workshop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.java.workshop.model.Admin;
import com.java.workshop.model.role.Instructor;
import com.java.workshop.model.role.Student.Student;
import com.java.workshop.model.role.Student.StudentPaymentStatus;
import com.java.workshop.model.role.grader.Grader;
import com.java.workshop.model.role.grader.GraderStatus;
import com.java.workshop.model.workshop.form.Form;
import com.java.workshop.model.place.Place;
import org.omg.PortableInterceptor.INACTIVE;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "workshop_sessions")
public class WorkshopSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;
    private String description;
    private String paymentDescription;
    private Date startDate;
    private Date endDate;

    @ManyToOne
    @JsonIgnore
    private Place place;

    @OneToMany(mappedBy = "workshopSession")
    private List<Group> groups;

    @OneToMany(mappedBy = "workshopSession")
    private List<Form> forms;

    @ManyToOne
    @JsonIgnore
    private Workshop workshop;


    @OneToOne(mappedBy = "session")
    @JsonIgnore
    private Instructor instructor;

    private Integer cachePrice;

    private Integer installmentPrice;

    private Integer numberOfInstallments;

    @OneToMany(mappedBy = "session")
    private List<SessionWorkshopRelation> relations;

    // Each of those above properties shows that the session price can be payed by cache or by installments.
    // For a free session the properties should be:
    // cachedPrice = 0;
    // installmentPrice = null;
    // numberOfInstallments = null;

    @ManyToOne
    @JsonIgnore
    private Admin admin;

    private Integer capacity;

    @NotNull
    private SessionRegisterStatus studentStatus;

    @NotNull
    private SessionRegisterStatus graderStatus;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Form> getForms() {
        return forms;
    }

    public void setForms(List<Form> forms) {
        this.forms = forms;
    }

    public Workshop getWorkshop() {
        return workshop;
    }

    public void setWorkshop(Workshop workshop) {
        this.workshop = workshop;
    }

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public SessionRegisterStatus getStudentStatus() {
        return studentStatus;
    }

    public void setStudentStatus(SessionRegisterStatus studentStatus) {
        this.studentStatus = studentStatus;
    }

    public SessionRegisterStatus getGraderStatus() {
        return graderStatus;
    }

    public void setGraderStatus(SessionRegisterStatus graderStatus) {
        this.graderStatus = graderStatus;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCachePrice() {
        return cachePrice;
    }

    public void setCachePrice(Integer cachePrice) {
        this.cachePrice = cachePrice;
    }

    public Integer getInstallmentPrice() {
        return installmentPrice;
    }

    public void setInstallmentPrice(Integer installmentPrice) {
        this.installmentPrice = installmentPrice;
    }

    public Integer getNumberOfInstallments() {
        return numberOfInstallments;
    }

    public void setNumberOfInstallments(Integer numberOfInstallments) {
        this.numberOfInstallments = numberOfInstallments;
    }

    public String getPaymentDescription() {
        return paymentDescription;
    }

    public void setPaymentDescription(String paymentDescription) {
        this.paymentDescription = paymentDescription;
    }

    public boolean isRequestedForGraderById(Long id) {
        for (Group group : this.groups) {
            for (Grader grader : group.getGraders()) {
                if ((grader.getUser().getId() == id)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isGraderById(Long id) {
        for (Group group : this.groups) {
            if (group.getGraders() == null) {
                continue;
            }
            for (Grader grader : group.getGraders()) {
                if (((grader.getStatus() == GraderStatus.HEAD) || (grader.getStatus() == GraderStatus.REGULAR)) && (grader.getUser().getId() == id)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isStudentById(Long id) {
        for (Group group : this.groups) {
            if (group.getStudents() == null) {
                continue;
            }
            for (Student student : group.getStudents()) {
                if (student.getUser().getId() == id) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasType(SessionType type) {
        if (type.equals(SessionType.ALL)) {
            return true;
        }
        else if (type.equals(SessionType.GRADER)) {
            if (this.getGraderStatus().equals(SessionRegisterStatus.NEED)) {
                return true;
            }
            return false;
        }
        else if (type.equals((SessionType.STUDENT))) {
            if (this.getStudentStatus().equals(SessionRegisterStatus.NEED)) {
                return true;
            }
            return false;
        }
        else if (type.equals(SessionType.PAST)) {
            if (this.getStartDate().before(new Date())) {
                return true;
            }
            return false;
        }
        return false; // for invalid situations
    }

    @JsonIgnore
    public ArrayList<Grader> getAllGraders() {
        ArrayList<Grader> graders = new ArrayList<>();
        for (Group group : this.groups) {
            for (Grader grader : group.getGraders()) {
                graders.add(grader);
            }
        }
        return graders;
    }
    @JsonIgnore
    public ArrayList<Grader> getAllRequestedGraders() {
        ArrayList<Grader> graders = new ArrayList<>();
        for (Group group : this.groups) {
            for (Grader grader : group.getGraders()) {
                if (grader.getStatus().equals(GraderStatus.REQUESTED)) {
                    graders.add(grader);
                }
            }
        }
        return graders;
    }

    @JsonIgnore
    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> students = new ArrayList<>();
        for (Group group : this.groups) {
            for (Student student : group.getStudents()) {
                students.add(student);
            }
        }
        return students;
    }

    public ArrayList<Student> returnAllStatusStudents(StudentPaymentStatus status) {
        ArrayList<Student> stds = new ArrayList<>();
        for (Group gp : this.groups) {
            for (Student std : gp.getStudents()) {
                if (std.getPaymentStatus().equals(status)) {
                    stds.add(std);
                }
            }
        }
        return stds;
    }

    public String getInstructorName() {
        return this.getInstructor().getUser().getFirstName() + " " + this.getInstructor().getUser().getLastName();
    }


    public Long getInstructorId() {
        return this.getInstructor().getUser().getId();
    }

    public ArrayList<Long> getPrerequisitesIds() {
        ArrayList<Long> ids = new ArrayList<Long>();
        if (this.relations != null) {
            for (SessionWorkshopRelation relation : this.relations) {
                if (relation.getRelationType().equals(WWRelation.prerequisites)) {
                    ids.add(relation.getWorkshop().getId());
                }
            }
        }
        return ids;
    }



}
