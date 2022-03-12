package client.Communication;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javafx.scene.image.Image;

import java.io.InputStream;

import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;

public class ImageCommunication {
    public static HttpClient client = HttpClient.newBuilder().build();

    public static Image getImage(String path) throws IllegalArgumentException {
        if (path == null) throw new IllegalArgumentException();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(path))
                        .GET()
                        .build();

        Image image = null;

        try {
        HttpResponse<InputStream> response = null;
        boolean requestMade = false;
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
                requestMade = true;
            }
            catch (ConnectException e) {
                System.out.println("A connection error has occurred while fetching an image!");
            }

        InputStream is;
        if (requestMade && response.statusCode() == 200) {
            is = response.body();
        }
        else {
            is = new FileInputStream("src/main/resources/images/image-fallback.png");
        }
        BufferedImage bufferedImage = ImageIO.read(is);
        is.close();
        //Image image = SwingFXUtils.toFXImage(bufferedImage, null);
        //Image image = new Image("https://br-tomassen.com/wp-content/uploads/2018/05/DUCK-7.png%22");
        image = convertToFxImage(bufferedImage);
        }
        catch (IOException e) {
            System.out.println("An IO Exception has occurred while getting an image!");
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            System.out.println("Interrupted!");
            e.printStackTrace();
        }

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
