package commons;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
@Table(name = "QUESTION")
public class Question {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    @Column(name = "QUESTION")
    public String question;
    @Column(name = "ANSWER")
    public String answer;
    @Column(name = "WRONG_ANSWER1")
    public String wrongAnswer1;
    @Column(name = "WRONG_ANSWER2")
    public String wrongAnswer2;
    @Column(name = "QUESTION_IMAGE")
    public String questionImage;
    @Column(name = "TYPE")
    public String type;

    private Question(){}

//    public Question(String q, String a, String w1, String w2) {
//        this.question = q;
//        this.answer = a;
//        this.wrongAnswer1 = w1;
//        this.wrongAnswer2 = w2;
//        this.questionImage = null;
//    }

//    public Question(String q, String a, String w1, String w2, String qi) {
//        this(q, a, w1, w2);
//        this.questionImage = qi;
//    }

//    public Question(long id, String q, String a, String w1, String w2){
//        this.id = id;
//        this.question = q;
//        this.answer = a;
//        this.wrongAnswer1 = w1;
//        this.wrongAnswer2 = w2;
//    }

    public Question(long id, String q, String a, String w1, String w2, String type){
        this.id = id;
        this.question = q;
        this.answer = a;
        this.wrongAnswer1 = w1;
        this.wrongAnswer2 = w2;
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
