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
            JSONArray playerArray = playerData.getJSONArray("data");

            if (playerArray.length() > 0) {
                JSONObject playerObject = playerArray.getJSONObject(0);
                displayPlayerStats(playerObject);
            } else {
                System.out.println("Player not found.");
            }
        } else {
            System.out.println("Failed to retrieve player data.");
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

                return new JSONObject(response.toString());
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

        JSONObject statistics = getStatistics(accountId);

        if (statistics != null && statistics.has("data")) {
            JSONObject playerStats = statistics.getJSONObject("data").getJSONObject(String.valueOf(accountId));
            JSONObject allStats = playerStats.getJSONObject("statistics").getJSONObject("all");

            int battles = allStats.getInt("battles");
            int wins = allStats.getInt("wins");
            int damageDealt = allStats.getInt("damage_dealt");

            System.out.println("Player ID: " + accountId);
            System.out.println("Battles: " + battles);
            double winRate = ((double) wins / battles) * 100;
            System.out.println("Win Rate: " + String.format("%.2f", winRate) + "%");
            System.out.println("Average Damage: " + Math.round((double) damageDealt / battles));
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

                return new JSONObject(response.toString());
            } else {
                System.out.println("HTTP request failed with error code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
