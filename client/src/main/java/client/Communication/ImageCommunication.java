package client.Communication;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.io.File;

import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;

public class ImageCommunication {
    public static HttpClient client = HttpClient.newBuilder().build();
    public static String fallbackImageURI = new File("src/main/resources/images/image-fallback.png").toURI().toString();

    /**
     * Gets the image from the given path. Gets called when setting up a question, in order to get the image for that question.
     * @param path The path to the image
     * @return The image or the fallback image if there was an error in getting the question image
     * @throws IOException gets thrown by imageIO
     * @throws IllegalArgumentException if path is null, since the called is likely trying to fetch the image for a question that doesn't have one.
     */
    public static Image getImage(String path) throws IOException, IllegalArgumentException {
        //null path means that the caller is likely trying to fetch the image for a question that doesn't have one.
        if (path == null) throw new IllegalArgumentException();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(path))
                        .GET()
                        .build();

        Image image = null;

        try {
            //client.send(...) may throw a ConnectException if connection fails
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            //the status may be 404 which in case we would still need the fallback image
            if (response.statusCode() == 200) {
            InputStream is = response.body();

            //ImageIO may throw an IOException
            BufferedImage bufferedImage = ImageIO.read(is);
            //ImageIO doesn't close the stream by itself
            is.close();

            //Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            //Image image = new Image("https://br-tomassen.com/wp-content/uploads/2018/05/DUCK-7.png%22");
            image = convertToFxImage(bufferedImage);
            }
        }
        catch (ConnectException e) {
            System.out.println("A connection error has occurred while getting an image!");
            e.printStackTrace();
        }
        catch (IOException e) {
            System.out.println("An IO Exception has occurred while getting an image!");
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            System.out.println("Interrupted!");
            e.printStackTrace();
        }

        if (image == null) {
            try {
                image = new Image(fallbackImageURI);
            }
            catch (IllegalArgumentException e) {
                throw new IOException("Fallback image is likely missing!");
            }
        }
        //if there was an error getting the image, return the fallback image instead
        return image;
    }

    /**
     * Converts a bufferedImage to a normal image.
     * @param image The bufferedImage to convert
     * @return The converted image
     */
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
