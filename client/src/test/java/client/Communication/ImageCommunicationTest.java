package client.Communication;

import static client.Communication.ImageCommunication.getImage;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

class ImageCommunicationTest {
    @Test
    public void getImageTest() throws IOException, InterruptedException {
        System.out.println(new java.io.File(".").getCanonicalPath());
        InputStream is = new FileInputStream("src/test/resources/1x1.png");

        HttpResponse<InputStream> response = mock(HttpResponse.class);
        ImageCommunication.client = mock(HttpClient.class);

        when(response.body()).thenReturn(is);
        when(ImageCommunication.client.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(response);

        Assertions.assertNotNull(ImageCommunication.getImage("https://localhost:8080/images/dummy.png"));
    }

    @Test
    public void getImageTestNull() {
        Assertions.assertThrows(IllegalArgumentException.class, ()->getImage(null));
    }
}
