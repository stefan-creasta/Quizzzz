package server.database;

import commons.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("SELECT q FROM Question q WHERE q.id = :id")
    public Question getId(long id);

    @Query("SELECT q FROM Question q")
    public List<Question> getAll();

    @Query("SELECT q FROM Question q WHERE (q.type = '1' OR q.type = '3') and LOWER(q.question) LIKE LOWER(:query)")
    List<Question> searchWithWattsAnswer(String query);
}
