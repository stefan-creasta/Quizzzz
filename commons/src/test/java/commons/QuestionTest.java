package commons;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

class QuestionTest {

    @Test
    void testEquals() {
        Question q1 = new Question("a","a","a","a");
        assertEquals(q1, q1);
    }
    @Test
    void testNotEquals() {
        Question q1 = new Question("a","a","a","a");
        Question q2 = new Question("a","a","a","b");
        assertNotEquals(q1, q2);
    }

    @Test
    void testHashCode() {
        Question q1 = new Question("a","a","a","a");
        assertEquals(1360777365, HashCodeBuilder.reflectionHashCode(q1));
    }

    @Test
    void testToString() {
        Question q1 = new Question("a","a","a","a");
        System.out.println(ToStringBuilder.reflectionToString(q1, MULTI_LINE_STYLE));
        assertEquals(q1.toString(), "commons.Question@[0-9a-f]*\\\\[\r\n" +
                "  answer=a\r\n" +
                "  id=0\r\n" +
                "  question=a\r\n" +
                "  wrongAnswer1=a\r\n" +
                "  wrongAnswer2=a\r\n" +
                "]");
    }
}