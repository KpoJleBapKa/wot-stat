package org.example;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClanStatistics {

    public void displayClanStats(JSONObject response) {
        if (response != null) {
            System.out.println("Server response: " + response.toString());

            if (response.has("data")) {
                try {
                    JSONArray clanArray = response.getJSONArray("data");

                    if (clanArray.length() > 0) {
                        JSONObject firstClan = clanArray.getJSONObject(0);
                        long clanId = firstClan.getLong("clan_id");

                        JSONObject detailedClanInfo = getDetailedClanInfo(clanId);

                        if (detailedClanInfo != null) {
                            JSONArray members = detailedClanInfo.optJSONArray("members");

                            if (members != null && members.length() > 0) {
                                displayAverageStats(members);
                            } else {
                                System.out.println("No members found for this clan.");
                            }
                        } else {
                            System.out.println("Failed to retrieve detailed clan information.");
                        }
                    } else {
                        System.out.println("Clan not found.");
                    }
                } catch (JSONException e) {
                    System.out.println("Error parsing JSON response.");
                    e.printStackTrace();
                }
            } else {
                System.out.println("Response does not contain 'data' field.");
            }
        } else {
            System.out.println("Server response is null.");
        }
    }

    private void displayAverageStats(JSONArray members) {
        System.out.println("Members:");

        int totalBattles = 0;
        int totalWins = 0;
        int totalDamage = 0;
        int totalXP = 0;
        int validMembers = 0;

        for (int i = 0; i < members.length(); i++) {
            JSONObject member = members.getJSONObject(i);

            int battles = member.optInt("battles", 0);
            int wins = member.optInt("wins", 0);
            int damageDealt = member.optInt("damage_dealt", 0);
            int xp = member.optInt("xp", 0);

            if (battles > 0) {
                totalBattles += battles;
                totalWins += wins;
                totalDamage += damageDealt;
                totalXP += xp;
                validMembers++;
            }
        }

        if (validMembers > 0) {
            double averageWinRate = (double) totalWins / totalBattles * 100;
            double averageBattles = (double) totalBattles / validMembers;
            double averageDamage = (double) totalDamage / validMembers;
            double averageXP = (double) totalXP / validMembers;

            System.out.println("Average Win Rate: " + String.format("%.2f", averageWinRate) + "%");
            System.out.println("Average Battles: " + String.format("%.2f", averageBattles));
            System.out.println("Average Damage: " + String.format("%.2f", averageDamage));
            System.out.println("Average XP: " + String.format("%.2f", averageXP));

            System.out.println("-------------------------");
        } else {
            System.out.println("No valid members found for calculating averages.");
        }
    }

    private JSONObject getDetailedClanInfo(long clanId) {
        try {
            String apiUrl = App.WOT_API_URL + "/clans/info/?application_id=" + App.APPLICATION_ID + "&clan_id=" + clanId + "&fields=tag,name,description,members";

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

                    JSONObject jsonResponse = new JSONObject(response.toString());

                    System.out.println("Server response: " + jsonResponse.toString());

                    if (jsonResponse.has("data") && jsonResponse.getJSONObject("data").has(String.valueOf(clanId))) {
                        return jsonResponse.getJSONObject("data").getJSONObject(String.valueOf(clanId));
                    } else {
                        System.out.println("Failed to retrieve detailed clan information. Check if the requested fields are available.");
                    }
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
