package meng.xing.entity;

import javax.persistence.*;

@Entity //jpa的标签 根据字段，自动创表
@Table(name = "user_done_exam") //生成的表名
public class UserDoneExam {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;//jpa 主键和自增

    private Long examId;

    private String username;

    protected UserDoneExam() {
    }

    public UserDoneExam(Long examId, String username) {
        this.examId = examId;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
