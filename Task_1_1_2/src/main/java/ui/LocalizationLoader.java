package ui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

public class LocalizationLoader {
    private final JsonObject messages;

    public LocalizationLoader(String language) {
        String fileName = "messages_" + language + ".json";
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("ui/" + fileName)) {
            if (is == null) {
                throw new RuntimeException("Localization file not found: " + fileName);
            }
            Gson gson = new Gson();
            this.messages = gson.fromJson(new InputStreamReader(is), JsonObject.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load localization: " + e.getMessage(), e);
        }
    }

    public String get(String key) {
        return messages.has(key) ? messages.get(key).getAsString() : "Missing key: " + key;
    }

    public String get(String key, Object... args) {
        String template = get(key);
        return String.format(Locale.getDefault(), template, args);
    }
}
