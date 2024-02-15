package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;


public class App {

    private static final String APPLICATION_ID = "d889298af2382fa0cfeb010e26874b63";
    private static final String WOT_API_URL = "https://api.worldoftanks.eu/wot";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter player name: ");
        String playerName = scanner.nextLine();

        scanner.close();

        JSONObject playerData = getPlayerData(playerName);

        if (playerData != null && playerData.has("data")) {
            JSONObject playerObject = playerData.getJSONArray("data").getJSONObject(0);
            displayPlayerStats(playerObject);
        } else {
            System.out.println("Player not found.");
        }
    }

    private static JSONObject getPlayerData(String playerName) {
        try {
            String apiUrl = WOT_API_URL + "/account/list/?application_id=" + APPLICATION_ID + "&search=" + playerName;

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject playerData = new JSONObject(response.toString());
                System.out.println("API Response: " + playerData);

                JSONArray playerArray = playerData.optJSONArray("data");

                if (playerArray != null && playerArray.length() > 0) {
                    long accountId = playerArray.getJSONObject(0).optLong("account_id");
                    return getStatistics(accountId);
                } else {
                    System.out.println("Player not found.");
                }
            } else {
                System.out.println("HTTP request failed with error code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static void displayPlayerStats(JSONObject playerObject) {
        long accountId = playerObject.getLong("account_id");

        // Отримання статистики гравця
        JSONObject statistics = getStatistics(accountId);

        if (statistics != null) {
            JSONObject allStats = statistics.optJSONObject("all");

            if (allStats != null) {
                System.out.println("Player ID: " + accountId);
                System.out.println("Battles: " + allStats.optInt("battles"));
                System.out.println("Win Rate: " + allStats.optDouble("wins") + "%");
                System.out.println("Average Damage: " + allStats.optInt("damage_dealt") / allStats.optInt("battles"));
            } else {
                System.out.println("Statistics not available for the player.");
            }
        } else {
            System.out.println("Failed to retrieve player statistics.");
        }
    }

    private static JSONObject getStatistics(long accountId) {
        try {
            String apiUrl = WOT_API_URL + "/account/info/?application_id=" + APPLICATION_ID + "&account_id=" + accountId;

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return new JSONObject(response.toString()).getJSONObject("data").getJSONObject(String.valueOf(accountId));
            } else {
                System.out.println("HTTP request failed with error code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}