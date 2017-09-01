package meng.xing.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "paper")
public class Paper {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    private String description;
    @NotNull
    private String username;
    @ManyToOne(fetch = FetchType.EAGER)
    private Subject subject;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "paper_and_test_item", joinColumns = @JoinColumn(name = "test_item_id"), inverseJoinColumns = @JoinColumn(name = "paper_id"))
    private Set<TestItem> testItems;

    protected Paper() {
    }

    public Paper(String username,String description, Subject subject, Set<TestItem> testItems) {
        this.username = username;
        this.description = description;
        this.subject = subject;
        this.testItems = testItems;
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
        this.subject = this.subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Set<TestItem> getTestItems() {
        return testItems;
    }

    public void setTestItems(Set<TestItem> testItems) {
        this.testItems = testItems;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
