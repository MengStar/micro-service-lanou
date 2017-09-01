package meng.xing.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;


@Entity
@Table(name = "subject")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    @Column(unique = true)
    private String type;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Set<Paper> papers;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Set<TestItem> testItems;

    protected Subject() {
    }

    public Subject(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Paper> getPapers() {
        return papers;
    }

    public void setPapers(Set<Paper> papers) {
        this.papers = papers;
    }

    public Set<TestItem> getTestItems() {
        return testItems;
    }

    public void setTestItems(Set<TestItem> testItems) {
        this.testItems = testItems;
    }
}
