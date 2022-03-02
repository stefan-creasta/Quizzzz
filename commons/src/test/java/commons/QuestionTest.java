package commons;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.jupiter.api.Test;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;
import static org.junit.jupiter.api.Assertions.*;

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
        //System.out.println(ToStringBuilder.reflectionToString(q1, MULTI_LINE_STYLE));
        assertEquals(0, q1.id);
        assertEquals("a", q1.question);
        assertEquals("a",q1.answer);
        assertEquals("a",q1.wrongAnswer1);
        assertEquals("a",q1.wrongAnswer2);
    }
}