package com.example.wotstat;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import java.io.IOException;

public class clanStat extends AppCompatActivity {

    private EditText clanNameEditText;
    private TextView clanStatTextView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clan_stat);

        clanNameEditText = findViewById(R.id.editTextText);
        clanStatTextView = findViewById(R.id.textView4);
        progressBar = findViewById(R.id.progressBar);
    }

    public void userBack(View v) {
        finish();
    }

    public void searchClan(View view) {
        hideKeyboard(view); // Приховуємо клавіатуру
        progressBar.setVisibility(View.VISIBLE); // Показуємо індикатор перед початком запиту

        String clanName = clanNameEditText.getText().toString();
        String wotApiUrl = "https://api.worldoftanks.eu/wot/clans/list/?application_id=d889298af2382fa0cfeb010e26874b63&search=" + clanName;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(wotApiUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    clanStatTextView.setText("Failed to retrieve clan data.");
                    progressBar.setVisibility(View.INVISIBLE); // Приховуємо індикатор у випадку помилки
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseData = response.body().string();
                    JSONObject jsonData = new JSONObject(responseData);

                    if (jsonData.has("data")) {
                        JSONObject clanObject = jsonData.getJSONArray("data").getJSONObject(0); // Assuming the first clan is the desired one
                        displayClanStats(clanObject);
                    } else {
                        runOnUiThread(() -> {
                            clanStatTextView.setText("Clan not found.");
                            progressBar.setVisibility(View.INVISIBLE); // Приховуємо індикатор у випадку помилки
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        clanStatTextView.setText("Failed to retrieve clan data.");
                        progressBar.setVisibility(View.INVISIBLE); // Приховуємо індикатор у випадку помилки
                    });
                }
            }
        });
    }

    private void displayClanStats(JSONObject clanObject) {
        try {
            String clanTag = clanObject.getString("tag");
            int membersCount = clanObject.getInt("members_count");

            runOnUiThread(() -> {
                clanStatTextView.setText(
                        "Клан: " + clanTag + "\n" +
                                "Кількість учасників: " + membersCount
                );
                progressBar.setVisibility(View.INVISIBLE); // Приховуємо індикатор після відображення статистики
            });
        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(() -> {
                clanStatTextView.setText("Failed to display clan statistics.");
                progressBar.setVisibility(View.INVISIBLE); // Приховуємо індикатор у випадку помилки
            });
        }

    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
