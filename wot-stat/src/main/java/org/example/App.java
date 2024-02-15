package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;


public class App {

    public static final String APPLICATION_ID = "d889298af2382fa0cfeb010e26874b63";
    public static final String WOT_API_URL = "https://api.worldoftanks.eu/wot";

    public static void main(String[] args) {
        System.out.println("Choose an option:");
        System.out.println("1. Player Statistics");
        System.out.println("2. Clan Statistics");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            int choice = Integer.parseInt(reader.readLine());

            if (choice == 1) {
                PlayerStatistics playerStatistics = new PlayerStatistics();
                playerStatistics.displayPlayerStats(getPlayerData(getPlayerName()));
            } else if (choice == 2) {
                ClanStatistics clanStatistics = new ClanStatistics();
                String clanTag = getClanTag();
                clanStatistics.displayClanStats(getClanData(clanTag));
            } else {
                System.out.println("Invalid option. Please choose 1 or 2.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getPlayerName() {
        System.out.print("Enter player name: ");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            return reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getClanTag() {
        System.out.print("Enter clan tag: ");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            return reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
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
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    return new JSONObject(response.toString());
                }
            } else {
                System.out.println("HTTP request failed with error code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static JSONObject getClanData(String clanName) {
        try {
            String apiUrl = WOT_API_URL + "/clans/list/?application_id=" + APPLICATION_ID + "&search=" + clanName;

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    return new JSONObject(response.toString());
                }
            } else {
                System.out.println("HTTP request failed with error code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
