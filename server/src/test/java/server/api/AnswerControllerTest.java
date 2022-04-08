package server.api;

import commons.Emote;
import commons.PlayerAnswer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.service.GameService;
import server.service.QuestionService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnswerControllerTest {

    private GameService gameService;
    private QuestionService questionService;
    private AnswerController answerController;

    PlayerAnswer playerAnswer;
    Emote emote;

    @BeforeEach
    void setUp() {
        gameService = mock(GameService.class);
        questionService = mock(QuestionService.class);
        playerAnswer = new PlayerAnswer("Answer", 1, 0);

        answerController = new AnswerController(questionService, gameService);
        emote = new Emote(Emote.Type.LOL, "User", 1);
    }

    @Test
    void postAnswer() {
        answerController.postAnswer("{\"playerId\":0,\"answer\":\"Answer\",\"gameId\":1}");
        verify(gameService).submitByPlayer(playerAnswer.playerId, playerAnswer.answer, playerAnswer.gameId);
    }

    @Test
    void postEmote() {
        try {
            answerController.postEmote("{\"type\":\"LOL\",\"username\":\"User\",\"gameId\":1}");
            verify(gameService).addEmote(emote);
        } catch (Exception e) {
            fail();
        }
    }
}