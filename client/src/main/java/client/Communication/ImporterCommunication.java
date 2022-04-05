package client.Communication;

import com.google.inject.Inject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ImporterCommunication {
    private HttpClient client;

    @Inject
    public ImporterCommunication(HttpClient client) {
        this.client = client;
    }

    /**
     * Imports the questions from the given path relative to server/resources/images on the server.
     * @param path the path relative to server/resources/images on the server to import the questions.
     * @return a message about the outcome of the import. Intended to be shown to the admin.
     */
    public String importQuestions(String path, String serverString) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverString + "/api/import?activitiesSource=" + path))
                .GET()
                .build();

        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException e) {
            e.printStackTrace();
            return "An error occurred while requesting the import. See the debug console for details.";
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "An error occurred while requesting the import. See the debug console for details.";
        }
    }
}
