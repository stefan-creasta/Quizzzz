package server.api;

import org.junit.jupiter.api.Test;
import server.service.GameService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



class PowerUpControllerTest {

    GameService mockedGameService = mock(GameService.class);
    PowerUpController pController = new PowerUpController(mockedGameService);

    @Test
    void postPowerUpTest(){
        when(mockedGameService.doublePointsPowerUp(-1, -1)).thenReturn("success");
        assertEquals("success", pController.postPowerUp("doublePointsPowerUp___-1___-1"));
    }

}