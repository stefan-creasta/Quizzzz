package client.Communication;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

//import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.io.InputStream;

import commons.Question;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;

public class ImageCommunication {
    private static HttpClient client = HttpClient.newBuilder().build();

    public static Image getImage(Question question) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://br-tomassen.com/wp-content/uploads/2018/05/DUCK-7.png%22"))
                        .GET()
                        .build();
        InputStream is = client.send(request, HttpResponse.BodyHandlers.ofInputStream()).body();

        BufferedImage bufferedImage = ImageIO.read(is);
        //Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        //Image image = new Image("https://br-tomassen.com/wp-content/uploads/2018/05/DUCK-7.png%22");
        Image image = convertToFxImage(bufferedImage);
        return image;
    }
    private static Image convertToFxImage(BufferedImage image) {
        WritableImage wr = null;
        if (image != null) {
            wr = new WritableImage(image.getWidth(), image.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pw.setArgb(x, y, image.getRGB(x, y));
                }
            }
        }

        return new ImageView(wr).getImage();
    }
}
