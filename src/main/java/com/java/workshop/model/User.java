package com.java.workshop.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.java.workshop.model.role.Instructor;
import com.java.workshop.model.role.Student.Student;
import com.java.workshop.model.role.Student.StudentStatus;
import com.java.workshop.model.role.grader.Grader;
import com.java.workshop.model.workshop.Workshop;
import com.java.workshop.model.workshop.WorkshopSession;
import org.hibernate.jdbc.Work;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;

    @NotNull
    @Size(min = 2, max = 200)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 200)
    private String lastName;

    @NotNull
    @Column(unique = true)
    @Pattern(regexp = "^\\S+@\\S+$")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @DateTimeFormat
    private Date birthDate;

    @OneToMany(mappedBy = "user")
    private List<Instructor> instructor;

    @OneToMany(mappedBy = "user")
    private List<Grader> grader;

    @OneToMany(mappedBy = "user")
    private List<Student> student;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public List<Instructor> getInstructor() {
        return instructor;
    }

    public List<Grader> getGrader() {
        return grader;
    }

    public List<Student> getStudent() {
        return student;
    }

    public void setInstructor(List<Instructor> instructor) {
        this.instructor = instructor;
    }

    public void setGrader(List<Grader> grader) {
        this.grader = grader;
    }

    public void setStudent(List<Student> student) {
        this.student = student;
    }

    public boolean hasBeenStudentFor(WorkshopSession session) {
        if (this.student == null) {
            return false;
        }
        for (Student std : this.student) {
            if (std.getGroup().getWorkshopSession().equals(session) && std.getStatus().equals(StudentStatus.PASSED)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPassedWorkshop(Workshop workshop) {
        if (workshop.getSessions() == null) {
            return false;
        }
        for (WorkshopSession wks : workshop.getSessions()) {
            if (this.hasBeenStudentFor(wks)) {
                return true;
            }
         }
        return false;
    }

    public boolean hasPassedWorkshops(ArrayList<Workshop> workshops) {
        if (workshops == null) {
            return true;
        }
        for (Workshop workshop : workshops) {
            if (!this.hasPassedWorkshop(workshop)) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<WorkshopSession> allSessions() {
        ArrayList<WorkshopSession> sessions = new ArrayList<>();
        if (this.student != null) {
            for (Student std : this.student) {
                sessions.add(std.getGroup().getWorkshopSession());
            }
        }
        if (this.grader != null) {
            for (Grader grd : this.getGrader()) {
                sessions.add(grd.getGroup().getWorkshopSession());
            }
        }
        if (this.instructor != null) {
            for (Instructor ins : this.getInstructor()) {
                sessions.add(ins.getSession());
            }
        }
        return sessions;
    }


    public boolean hasTimeConflict(WorkshopSession wrks) {
        if (this.allSessions() == null) {
            return false;
        }
        for (WorkshopSession ses : this.allSessions()) {
            if ((ses.getEndDate().before(wrks.getStartDate())) || (ses.getStartDate().after(wrks.getEndDate()))) {
                continue;
            }
            return true;
        }
        return false;
    }


}
