package com.example.wotstat;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AvgExpPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avg_exp_page);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void calculateAvgExp(View view) {
        EditText battlesEditText = findViewById(R.id.editTextText4);
        EditText currentExpEditText = findViewById(R.id.editTextText5);
        EditText desiredExpEditText = findViewById(R.id.editTextText6);
        TextView resultTextView = findViewById(R.id.textView9);

        String battlesString = battlesEditText.getText().toString();
        String currentExpString = currentExpEditText.getText().toString();
        String desiredExpString = desiredExpEditText.getText().toString();

        if (battlesString.isEmpty() || currentExpString.isEmpty() || desiredExpString.isEmpty()) {
            resultTextView.setText("Заповніть всі поля.");
            return;
        }

        int battles = Integer.parseInt(battlesString);
        int currentExp = Integer.parseInt(currentExpString);
        int desiredExp = Integer.parseInt(desiredExpString);

        StringBuilder result = new StringBuilder("Кількість боїв задля отримання бажаного середнього досвіду: \n");

        int[] avgExps = {1200, 1100, 1050, 1000};

        for (int avgExp : avgExps) {
            if (avgExp <= desiredExp) {
                result.append("Досвіду у розмірі ").append(avgExp).append(" EXP не вистачить задля покращення статистики. \n");
            } else {
                int additionalBattles = 0;
                int currentAvgExp = currentExp;
                int battlesRes = battles;
                while (currentAvgExp < desiredExp) {
                    currentAvgExp += (avgExp - currentAvgExp) / ++battlesRes;
                    additionalBattles++;
                }
                result.append("Якщо грати на ").append(avgExp).append(" EXP - ").append(additionalBattles).append(" боїв \n");
            }
        }

        resultTextView.setText(result.toString());
    }

    public void userBack(View v) {
        finish();
    }
}
