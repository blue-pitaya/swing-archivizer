package swingarchivizer;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import swingarchivizer.dto.Item;
import swingarchivizer.dto.ItemIdsRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    public void fetchItem(int id, Path downloadDir) throws IOException {
        Request request = new Request.Builder()
                .url("http://127.0.0.1:8069/items/" + id)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                String body = response.body().string();
                throw new IOException("HTTP " + response.code() + ": " + body);
            }

            Path itemDir = Files.createDirectories(downloadDir.resolve(String.valueOf(id)));
            Files.copy(response.body().byteStream(), itemDir.resolve(getFileName(response)), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static Path getFileName(Response response) throws IOException {
        String disposition = response.header("Content-Disposition");
        int index = disposition == null ? -1 : disposition.indexOf("filename=");
        if (index == -1) {
            throw new IOException("Missing file name in Content-Disposition");
        }

        return Paths.get(disposition.substring(index + "filename=".length()).replace("\"", "")).getFileName();
    }
}
