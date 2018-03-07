package meng.xing.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "answer")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private String answer;
    @NotNull
    private String username;

    @NotNull
    private Long paperId;
    @NotNull
    private Long testItemId;

    protected Answer() {
    }

    public Answer(String answer, String username, Long paperId, Long testItemId) {
        this.answer = answer;
        this.username = username;
        this.paperId = paperId;
        this.testItemId = testItemId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getPaperId() {
        return paperId;
    }

    public void setPaperId(Long paperId) {
        this.paperId = paperId;
    }

    public Long getTestItemId() {
        return testItemId;
    }

    public void setTestItemId(Long testItemId) {
        this.testItemId = testItemId;
    }
}
