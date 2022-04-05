package client.Communication;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import commons.Question;
import jakarta.ws.rs.core.UriBuilder;

import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class QuestionCommunication {
    private static HttpClient client;

    private ObjectMapper mapper;
    private TypeReference<List<Question>> typeRefList;
    private TypeReference<Question> typeRefQuestion;

    @Inject
    public QuestionCommunication(HttpClient client) {
        this.client = client;
        this.mapper = new ObjectMapper();
        this.typeRefList = new TypeReference<>() {};
        this.typeRefQuestion = new TypeReference<>() {};
    }

    /**
     * Sends a patch request with the question. The question id is significant.
     * @param q
     * @param serverString the server URL for the API endpoint
     * @return the patched question
     * @throws ConnectException
     */
    public Question patchQuestion(Question q, String serverString) throws ConnectException {
        String body;
        try {
            body = mapper.writeValueAsString(q);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        HttpRequest req = HttpRequest.newBuilder()
                .uri(UriBuilder
                        .fromUri(serverString + "/api/questions/")
                        .build())
                .setHeader("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(body))
                .build();
        try {
            body = client.send(req, HttpResponse.BodyHandlers.ofString()).body();
            return mapper.readValue(body, typeRefQuestion);
        } catch (ConnectException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sends a post request with the question. The question id should be 0.
     * @param q
     * @param serverString the server URL for the API endpoint
     * @throws ConnectException
     */
    public void postQuestion(Question q, String serverString) throws ConnectException {
        String body;
        try {
            body = mapper.writeValueAsString(q);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return;
        }
        HttpRequest req = HttpRequest.newBuilder()
                .uri(UriBuilder
                        .fromUri(serverString + "/api/questions/")
                        .build())
                .setHeader("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        try {
            System.out.println(client.send(req, HttpResponse.BodyHandlers.ofString()).statusCode());
        } catch (ConnectException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a post request with the question. The question id should be 0.
     * @param id the question ID to delete
     * @param serverString the server URL for the API endpoint
     * @throws ConnectException
     */
    public void deleteQuestion(long id, String serverString) throws ConnectException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(UriBuilder
                        .fromUri(serverString + "/api/questions/" + id)
                        .build())
                .DELETE()
                .build();
        try {
            client.send(req, HttpResponse.BodyHandlers.discarding());
        } catch (ConnectException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Searches for open-ended and watts answer multiple choice questions.
     * whose question title match the given search keywords.
     * @param text the search keywords
     * @param serverString the server URL for the API endpoint
     * @return the list of results
     * @throws ConnectException
     */
    public List<Question> searchBy(String text, String serverString) throws ConnectException {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(UriBuilder
                        .fromUri(serverString + "/api/questions/search")
                        .queryParam("q", text)
                        .build())
                .GET()
                .build();
        try {
            System.out.println(req.uri().toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            String response = client.send(req, HttpResponse.BodyHandlers.ofString()).body();
            return mapper.readValue(response, typeRefList);
        } catch (ConnectException e) {
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return List.of();
    }
}
