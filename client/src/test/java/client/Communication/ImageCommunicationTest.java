package client.Communication;

import static client.Communication.ImageCommunication.getImage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class ImageCommunicationTest {
    @Test
    public void getImageTest() throws IOException, InterruptedException {
        Assertions.assertNull(getImage(null));
    }

}
