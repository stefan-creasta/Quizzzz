package commons;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

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
        assertEquals(-1190977743, HashCodeBuilder.reflectionHashCode(q1));
    }

    @Test
    void testToString() {
        Question q1 = new Question("a","a","a","a");
        assertLinesMatch(List.of(
                "commons.Question@[0-9a-f]*\\[",
                "  answer=a",
                "  id=0",
                "  question=a",
                "  questionImage=<null>",
                "  wrongAnswer1=a",
                "  wrongAnswer2=a",
        "]"
), Arrays.asList(q1.toString().split(System.lineSeparator())));

    }
}