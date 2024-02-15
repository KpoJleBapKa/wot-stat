package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class App {

    private static final String APPLICATION_ID = "d889298af2382fa0cfeb010e26874b63";
    private static final String WOT_API_URL = "https://api.worldoftanks.eu/wot";

    public static void main(String[] args) {
        displayPlayerStats(getPlayerData(getPlayerName()));
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

    private static void displayPlayerStats(JSONObject playerObject) {
        if (playerObject != null && playerObject.has("data")) {
            JSONArray playerArray = playerObject.getJSONArray("data");

            if (playerArray.length() > 0) {
                JSONObject playerData = playerArray.getJSONObject(0);
                long accountId = playerData.getLong("account_id");

                JSONObject statistics = getStatistics(accountId);

                if (statistics != null && statistics.has("data")) {
                    JSONObject playerStats = statistics.getJSONObject("data").getJSONObject(String.valueOf(accountId));
                    JSONObject allStats = playerStats.optJSONObject("statistics").optJSONObject("all");

                    if (allStats != null) {
                        int battles = allStats.optInt("battles", 0);
                        int wins = allStats.optInt("wins", 0);
                        int hits = allStats.optInt("hits_percents", 0);
                        int damageDealt = allStats.optInt("damage_dealt", 0);
                        int xp = allStats.optInt("xp", 0);
                        int maxFrags = allStats.optInt("max_frags", 0);
                        int maxXP = allStats.optInt("max_xp", 0);

                        double winRate = battles > 0 ? ((double) wins / battles) * 100 : 0;
                        double hitRate = hits > 0 ? Math.min((double) hits, 100) : 0;


                        long clanId = playerData.optLong("clan_id", 0);
                        String clanName = getClanName(clanId);

                        System.out.println("Player ID: " + accountId);
                        System.out.println("Battles: " + battles);
                        System.out.println("Win Rate: " + String.format("%.2f", winRate) + "%");
                        System.out.println("Hit Rate: " + String.format("%.2f", hitRate) + "%");
                        System.out.println("Average Damage: " + Math.round(battles > 0 ? (double) damageDealt / battles : 0));
                        System.out.println("Average XP: " + Math.round(battles > 0 ? (double) xp / battles : 0));
                        System.out.println("Max Frags: " + maxFrags);
                        System.out.println("Max XP: " + maxXP);

                        if (!clanName.isEmpty()) {
                            System.out.println("Clan Name: " + clanName);
                        }
                    } else {
                        System.out.println("Player statistics not available.");
                    }
                } else {
                    System.out.println("Failed to retrieve player statistics.");
                }
            } else {
                System.out.println("Player not found.");
            }
        } else {
            System.out.println("Failed to retrieve player data.");
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

    private static String getClanName(long clanId) {
        if (clanId > 0) {
            JSONObject clanInfo = getClanInfo(clanId);

            if (clanInfo != null && clanInfo.has("data")) {
                JSONObject clanData = clanInfo.getJSONObject("data");

                if (clanData.has("name")) {
                    return clanData.getString("name");
                }
            }
        }
        return "";
    }

    private static JSONObject getClanInfo(long clanId) {
        try {
            String apiUrl = WOT_API_URL + "/clans/info/?application_id=" + APPLICATION_ID + "&clan_id=" + clanId;

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
