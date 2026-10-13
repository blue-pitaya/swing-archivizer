package swingarchivizer;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.time.LocalDate;

public final class HttpService {

    private static final MediaType JSON = MediaType.get("application/json");

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public HttpService() {
    }

    public void run(LocalDate from, LocalDate to) throws IOException {
        Request request = new Request.Builder()
                .url("http://127.0.0.1:8069/items/ids")
                .post(RequestBody.create(mapper.writeValueAsString(new ItemIdsRequest(from, to)), JSON))
                .build();
        try (Response response = client.newCall(request).execute()) {
            String body = response.body().string();
            if (!response.isSuccessful()) {
                throw new IOException("HTTP " + response.code() + ": " + body);
            }
            int[] ids = mapper.readValue(body, int[].class);
            for (int i : ids) {
                System.out.println(i);
            }
        }
    }
}
