package com.example.wotstat;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class playerStat extends AppCompatActivity {

    private EditText playerNameEditText;
    private TextView playerStatTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_stat);

        playerNameEditText = findViewById(R.id.editTextText);
        playerStatTextView = findViewById(R.id.textView4);
    }

    public void calculatePlayerStat(View view) {
        String playerName = playerNameEditText.getText().toString();
        String wotApiUrl = "https://api.worldoftanks.eu/wot/account/list/?application_id=d889298af2382fa0cfeb010e26874b63&search=" + playerName;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(wotApiUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> playerStatTextView.setText("Ви не ввели нік-нейм гравця."));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseData = response.body().string();
                    JSONObject jsonData = new JSONObject(responseData);
                    JSONArray dataArray = jsonData.getJSONArray("data");

                    if (dataArray.length() > 0) {
                        JSONObject playerObject = dataArray.getJSONObject(0);
                        displayPlayerStats(playerObject);
                    } else {
                        runOnUiThread(() -> playerStatTextView.setText("Такого гравця не існує."));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> playerStatTextView.setText("Ви не ввели нік-нейм гравця."));
                }
            }
        });
    }

    private void displayPlayerStats(JSONObject playerObject) {
        try {
            long accountId = playerObject.getLong("account_id");
            String statsUrl = "https://api.worldoftanks.eu/wot/account/info/?application_id=d889298af2382fa0cfeb010e26874b63&account_id=" + accountId;

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(statsUrl)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> playerStatTextView.setText("Ви не ввели нік-нейм гравця."));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String responseData = response.body().string();
                        JSONObject jsonData = new JSONObject(responseData);
                        JSONObject playerStats = jsonData.getJSONObject("data").getJSONObject(String.valueOf(accountId)).getJSONObject("statistics").getJSONObject("all");

                        int battles = playerStats.getInt("battles");
                        int wins = playerStats.getInt("wins");
                        int hits = playerStats.getInt("hits");
                        int shots = playerStats.getInt("shots");
                        int damageDealt = playerStats.getInt("damage_dealt");
                        int xp = playerStats.getInt("xp");
                        int maxFrags = playerStats.getInt("max_frags");
                        int maxXP = playerStats.getInt("max_xp");

                        double winRate = (double) wins / battles * 100;
                        double accuracy = (double) hits / shots * 100;
                        double avgDamage = (double) damageDealt / battles;
                        double avgXP = (double) xp / battles;

                        // Get clan information
                        String clanUrl = "https://api.worldoftanks.eu/wot/clans/accountinfo/?application_id=d889298af2382fa0cfeb010e26874b63&account_id=" + accountId;
                        Request clanRequest = new Request.Builder()
                                .url(clanUrl)
                                .build();

                        client.newCall(clanRequest).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                                runOnUiThread(() -> playerStatTextView.setText("Ви не ввели нік-нейм гравця."));
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    String responseData = response.body().string();
                                    JSONObject jsonData = new JSONObject(responseData);
                                    JSONObject clanData = jsonData.getJSONObject("data").getJSONObject(String.valueOf(accountId));

                                    String clanName = clanData.getJSONObject("clan").getString("tag");

                                    runOnUiThread(() -> {
                                        playerStatTextView.setText(
                                                "Клан: " + clanName + "\n" +
                                                        "Кількість боїв: " + battles + "\n" +
                                                        "% перемог: " + String.format("%.2f", winRate) + "%\n" +
                                                        "% влучень: " + String.format("%.2f", accuracy) + "%\n" +
                                                        "Середня шкода: " + String.format("%.2f", avgDamage) + "\n" +
                                                        "Середній досвід: " + String.format("%.2f", avgXP) + "\n" +
                                                        "Максимум знищено за бій: " + maxFrags + "\n" +
                                                        "Максимальний досвід за бій: " + maxXP
                                        );
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    runOnUiThread(() -> playerStatTextView.setText("Ви не ввели нік-нейм гравця."));
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> playerStatTextView.setText("Ви не ввели нік-нейм гравця."));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(() -> playerStatTextView.setText("Ви не ввели нік-нейм гравця."));
        }
    }
}
