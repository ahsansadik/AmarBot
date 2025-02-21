package me.ahsansadik.Moderation.Response;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class OpenAIChat {
    private static final String API_URL = "https://api.pawan.krd/cosmosrp/v1/chat/completions";
    private static final String API_KEY = "pk-DSdrmOQKittUeCnVTUuXfONrZwzalAWRhbmNITgkFHFnyaEf"; // Replace with your key

    public static String getChatGPTResponse(String prompt) {
        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(60, TimeUnit.SECONDS)  // Increase timeout
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();


        JSONObject json = new JSONObject();
        json.put("inputs", prompt);

        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(API_URL)
                .header("Authorization", "Bearer " + API_KEY)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBodyStr = response.body().string();
            System.out.println("API Response: " + responseBodyStr);  // Debugging: Log response

            if (!response.isSuccessful()) {
                return "Error: API request failed (" + response.code() + " - " + response.message() + ")";
            }

            JSONArray responseArray = new JSONArray(responseBodyStr);
            if (responseArray.length() > 0) {
                JSONObject responseObj = responseArray.getJSONObject(0);
                return responseObj.getString("generated_text");
            } else {
                return "Error: No response from AI";
            }
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }
}

