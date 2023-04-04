package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonDownloader {

    private static String json = null;

    public static String getJson() {
        if (json != null) return json;
        try {
            // URL, откуда необходимо скачать JSON
            String urlString = "https://ton.org/global-config.json";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                response.append(inputLine);
            }
            bufferedReader.close();
            // Полученный JSON
            json = response.toString();
            return json;
        } catch (Exception e) {

            System.out.println("An error occurred: " + e.getMessage());
            return null;
        }

    }
}
