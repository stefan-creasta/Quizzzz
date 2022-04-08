package server.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.service.GameService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;



class PowerUpControllerTest {
    GameService mockedGameService;
    PowerUpController pController;
    @BeforeEach
            void setup() {
        mockedGameService = mock(GameService.class);
        pController = new PowerUpController(mockedGameService);
    }
    @Test
    void postDoublePointsTest(){
        when(mockedGameService.doublePointsPowerUp(-1, -1)).thenReturn("success");
        assertEquals("success", pController.postPowerUp("doublePointsPowerUp___-1___-1"));
    }
    @Test
    void postHalfTimeTest(){
        when(mockedGameService.halfTimePowerUp(-1,-1)).thenReturn("success");
        assertEquals("success",pController.postPowerUp("halfTimePowerUp___-1___-1"));
    }
    @Test
    void postEliminateWrongAnswerTest(){
        when(mockedGameService.eliminateWrongAnswerPowerUp(-1,-1)).thenReturn("success");
        assertEquals("success",pController.postPowerUp("eliminateWrongAnswerPowerUp___-1___-1"));
    }

}