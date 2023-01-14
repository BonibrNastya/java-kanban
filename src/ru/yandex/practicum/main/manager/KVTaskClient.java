package manager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class KVTaskClient {

    private String apiToken ="";
    private final String url;
    private final String TYPE = "Content-Type";
    private final String APP_JSON = "application/json";
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;


    public KVTaskClient(String url) {
        this.url = url;
        URI uri = URI.create(url + "/register");


        HttpRequest req = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header(TYPE, APP_JSON)
                .build();

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
            apiToken = res.body();
        } catch (IOException | InterruptedException exp) {
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + uri + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public void put(String key, String json) {
        URI uri = URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken);

        HttpRequest req = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .header(TYPE, APP_JSON)
                .build();

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString(DEFAULT_CHARSET));
            if (response.statusCode() != 200) {
                System.out.println("Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode());
            }
        } catch (NullPointerException | IOException | InterruptedException exp) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }

    }

    public String load(String key) {
        URI uri = URI.create(url + "/load/" + key + "?API_TOKEN=" + apiToken);

        HttpRequest req = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .header(TYPE, APP_JSON)
                .build();

        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString(DEFAULT_CHARSET));
            return response.body();
        } catch (IOException | InterruptedException exp) {
            return "Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.";
        }

    }
}
