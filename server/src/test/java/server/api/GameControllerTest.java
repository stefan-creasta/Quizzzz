package server.api;

import commons.Game;
import commons.GameState;
import commons.LeaderboardEntry;
import org.junit.jupiter.api.Test;
import server.service.GameService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class GameControllerTest {

    GameService service = mock(GameService.class);
    GameController controller = new GameController(service);
    Game game = mock(Game.class);
    @Test
    void getByIdTest1() {
        when(service.existsById(any(long.class))).thenReturn(false);
        when(service.getId(any(long.class))).thenReturn(game);
        assertEquals(null, controller.getById(1));
    }

    @Test
    void getByIdTest2() {
        when(service.existsById(any(long.class))).thenReturn(true);
        when(service.getId(any(long.class))).thenReturn(game);
        assertEquals(game.getCurrentQuestion(), controller.getById(1));
    }

    @Test
    void createGameTest() {
        assertEquals(0, service.createGame());
    }

    @Test
    void createSingleplayerGameTest() {
        assertEquals(service.createGame(), service.createSingleplayerGame());
    }

    @Test
    void joinGameTest() {
        GameState state = mock(GameState.class);
        when(service.joinGame(any(long.class), any(String.class))).thenReturn(state);
        assertEquals(state, controller.joinGame(0, "Player_1"));
    }

    @Test
    void joinSingleplayerGameTest() {
        GameState state = mock(GameState.class);
        when(service.joinSingleplayerGame(any(long.class), any(String.class))).thenReturn(state);
        assertEquals(state, controller.joinSingleplayerGame(0, "Player_1"));
    }

    @Test
    void initiateGameTest() {
        controller.initiateGame(0);
    }

    @Test
    void initiateSingpleplayerGameTest() {
        controller.createSingleplayerGame();
        controller.initiateGame(1);
    }

    @Test
    void getPlayersTest() {
        String player1 = "abc";
        String player2 = "bcd";
        String player3 = "afg";
        List<String> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        when(service.getPlayers(any(long.class))).thenReturn(players);
        assertEquals(players, controller.getPlayers(0));
    }

    @Test
    void getLeaderboardTest() {
        LeaderboardEntry leaderboard1 = mock(LeaderboardEntry.class);
        LeaderboardEntry leaderboard2 = mock(LeaderboardEntry.class);
        LeaderboardEntry leaderboard3 = mock(LeaderboardEntry.class);
        List<LeaderboardEntry> leaderboardEntries = new ArrayList<>();
        leaderboardEntries.add(leaderboard1);
        leaderboardEntries.add(leaderboard2);
        leaderboardEntries.add(leaderboard3);
        when(service.getLeaderboard(any(long.class))).thenReturn(leaderboardEntries);
        assertEquals(leaderboardEntries, controller.getLeaderboard(0));
    }

    @Test
    void checkUsernameTest() {
        when(service.checkUsername(any(long.class), any(String.class))).thenReturn(false);
        assertFalse(controller.checkUsername(0, "abc"));
    }
}