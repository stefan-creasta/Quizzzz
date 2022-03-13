package client.Communication;


import java.net.http.HttpClient;
import com.google.gson.Gson;


public class QuestionCommunication {

    private static HttpClient client = HttpClient.newBuilder().build();

    private static Gson gson = new Gson();



}
