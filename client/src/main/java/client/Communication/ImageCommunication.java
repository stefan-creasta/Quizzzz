package client.Communication;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javafx.scene.image.Image;

import java.io.InputStream;

import commons.Question;

public class ImageCommunication {
    private static HttpClient client = HttpClient.newBuilder().build();

    public static Image getImage(Question question) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://br-tomassen.com/wp-content/uploads/2018/05/DUCK-7.png%22"))
                        .GET()
                        .build();
        InputStream is = client.send(request, HttpResponse.BodyHandlers.ofInputStream()).body();

            //Image image = SwingFXUtils.toFXImage(ImageIO.read(is), null);
        return null;
    }
}
