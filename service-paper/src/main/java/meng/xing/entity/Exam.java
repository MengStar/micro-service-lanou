package meng.xing.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "exam")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    private String description;
    @NotNull
    private String username;

    @ManyToOne(fetch = FetchType.EAGER)
    private Subject subject;

    @ManyToOne(fetch = FetchType.EAGER)
    private Paper paper;


    protected Exam() {

    }

    public Exam(String description, Subject subject, Paper paper, String  username) {
        this.description = description;
        this.subject = subject;
        this.paper = paper;
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
