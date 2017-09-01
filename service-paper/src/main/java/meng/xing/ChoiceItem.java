package meng.xing;

import java.util.Map;

public class ChoiceItem {
    private String question;
    private Map<String, String> answer;

    public ChoiceItem(String question, Map<String, String> answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Map<String, String> getAnswer() {
        return answer;
    }

    public void setAnswer(Map<String, String> answer) {
        this.answer = answer;
    }
}
