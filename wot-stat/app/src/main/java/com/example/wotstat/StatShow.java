package com.example.wotstat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class StatShow extends AppCompatActivity {

    private EditText wn8EditText;
    private EditText winRateEditText;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statshow);

        wn8EditText = findViewById(R.id.editTextText4);
        winRateEditText = findViewById(R.id.editTextText6);
        resultTextView = findViewById(R.id.textView9);
    }

    public void calculateStats(View view) {
        String wn8String = wn8EditText.getText().toString();
        String winRateString = winRateEditText.getText().toString();

        if (wn8String.isEmpty() || winRateString.isEmpty()) {
            resultTextView.setText("Заповніть всі поля.");
            return;
        }

        int wn8 = Integer.parseInt(wn8String);
        String wn8Color = getWN8Color(wn8);

        double winRate = Double.parseDouble(winRateString);
        String winRateColor = getWinRateColor(winRate);

        String result = String.format("Ваша кольорова статистика:\nWN8 - %d (%s)\nВідсоток перемог - %.2f%% (%s)",
                wn8, wn8Color, winRate, winRateColor);

        resultTextView.setText(result);
    }

    private String getWN8Color(int wn8) {
        String color;
        if (wn8 >= 0 && wn8 <= 590) {
            color = "червоний";
        } else if (wn8 >= 591 && wn8 <= 1096) {
            color = "помаранчевий";
        } else if (wn8 >= 1097 && wn8 <= 1692) {
            color = "жовтий";
        } else if (wn8 >= 1693 && wn8 <= 2590) {
            color = "зелений";
        } else if (wn8 >= 2591 && wn8 <= 3604) {
            color = "голубий";
        } else {
            color = "фіолетовий";
        }
        return color;
    }

    private String getWinRateColor(double winRate) {
        String color;
        if (winRate >= 0 && winRate <= 46.26) {
            color = "червоний";
        } else if (winRate >= 46.27 && winRate <= 49.21) {
            color = "помаранчевий";
        } else if (winRate >= 49.22 && winRate <= 52.61) {
            color = "жовтий";
        } else if (winRate >= 52.62 && winRate <= 57.87) {
            color = "зелений";
        } else if (winRate >= 57.88 && winRate <= 63.57) {
            color = "голубий";
        } else {
            color = "фіолетовий";
        }
        return color;
    }

    public void userBack(View v) {
        finish();
    }
}

