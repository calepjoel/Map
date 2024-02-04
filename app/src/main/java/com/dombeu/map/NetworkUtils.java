package com.dombeu.map;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class NetworkUtils {

    public interface NetworkCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    public static void sendPostRequest(final String requestUrl, final String postData, final NetworkCallback callback) {
        new Thread(() -> {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(requestUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoOutput(true);

                OutputStream os = urlConnection.getOutputStream();
                os.write(postData.getBytes(StandardCharsets.UTF_8));
                os.close();

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    if (callback != null) {
                        callback.onSuccess(response.toString());
                    }
                } else {
                    if (callback != null) {
                        callback.onError("Error response code: " + responseCode);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (callback != null) {
                    callback.onError("Exception: " + e.getMessage());
                }
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }).start();
    }

    // Add a method to send image data
    public static void sendImage(final String requestUrl, final byte[] imageData, final NetworkCallback callback) {
        new Thread(() -> {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(requestUrl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "image/jpeg"); // Set the appropriate content type for images
                urlConnection.setDoOutput(true);

                OutputStream os = urlConnection.getOutputStream();
                os.write(imageData);
                os.close();

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    if (callback != null) {
                        callback.onSuccess("Image uploaded successfully");
                    }
                } else {
                    if (callback != null) {
                        callback.onError("Error response code: " + responseCode);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (callback != null) {
                    callback.onError("Exception: " + e.getMessage());
                }
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }).start();
    }
}