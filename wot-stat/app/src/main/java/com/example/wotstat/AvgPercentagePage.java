
package com.example.wotstat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AvgPercentagePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avg_percentage_page);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void calculateAvgPercentage(View view) {
        EditText battlesEditText = findViewById(R.id.editTextText4);
        EditText currentWinRateEditText = findViewById(R.id.editTextText5);
        EditText desiredWinRateEditText = findViewById(R.id.editTextText6);
        TextView resultTextView = findViewById(R.id.textView9);

        String battlesString = battlesEditText.getText().toString();
        String currentWinRateString = currentWinRateEditText.getText().toString();
        String desiredWinRateString = desiredWinRateEditText.getText().toString();

        if (battlesString.isEmpty() || currentWinRateString.isEmpty() || desiredWinRateString.isEmpty()) {
            resultTextView.setText("Заповніть всі поля.");
            return;
        }

        int battles = Integer.parseInt(battlesString);
        double currentWinRate = Double.parseDouble(currentWinRateString) / 100; // Переведення у відсотковий формат
        double desiredWinRate = Double.parseDouble(desiredWinRateString) / 100; // Переведення у відсотковий формат

        StringBuilder result = new StringBuilder("Кількість боїв задля отримання бажаної середньої шкоди:\n");

        double[] avgWinRates = {0.70, 0.65, 0.60}; // Відсоткові значення перемог
        for (double avgWinRate : avgWinRates) {
            if(avgWinRate <= desiredWinRate) {
                result.append("% перемог у розмірі ").append(avgWinRate * 100).append("% не вистачить задля покращення статистики. \n");
            } else {
                int additionalBattles = 0;
                double currentAvgWinRate = currentWinRate;
                int battlesRes = battles;
                while (currentAvgWinRate < desiredWinRate) {
                    currentAvgWinRate += (avgWinRate - currentAvgWinRate) / ++battlesRes;
                    additionalBattles++;
                }
                result.append("Якщо грати на ").append(avgWinRate * 100).append("% перемог - ").append(additionalBattles).append(" боїв \n");
            }
        }

        resultTextView.setText(result.toString());
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void userBack(View v) {
        finish();
    }
}
