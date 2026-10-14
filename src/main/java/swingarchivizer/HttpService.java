package swingarchivizer;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import swingarchivizer.dto.Item;

import java.io.IOException;
import java.time.LocalDate;

public final class HttpService {

    private static final MediaType JSON = MediaType.get("application/json");

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public HttpService() {
    }

    public Item[] postItemsIds(LocalDate from, LocalDate to) throws IOException {
        Request request = new Request.Builder()
                .url("http://127.0.0.1:8069/items")
                .post(RequestBody.create(mapper.writeValueAsString(new ItemIdsRequest(from, to)), JSON))
                .build();
        try (Response response = client.newCall(request).execute()) {
            String body = response.body().string();
            if (!response.isSuccessful()) {
                throw new IOException("HTTP " + response.code() + ": " + body);
            }

            return mapper.readValue(body, Item[].class);
        }
    }
}
