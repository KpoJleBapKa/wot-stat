package com.example.wotstat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AvgDamagePage extends AppCompatActivity {
        private EditText battlesEditText;
        private EditText currentDamageEditText;
        private EditText desiredDamageEditText;
        private TextView resultTextView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_avg_damage_page);

            battlesEditText = findViewById(R.id.editTextText4);
            currentDamageEditText = findViewById(R.id.editTextText5);
            desiredDamageEditText = findViewById(R.id.editTextText6);
            resultTextView = findViewById(R.id.textView9);
        }

        public void calculateAvgDamage(View view) {
            String battlesString = battlesEditText.getText().toString();
            String currentDamageString = currentDamageEditText.getText().toString();
            String desiredDamageString = desiredDamageEditText.getText().toString();

            if (battlesString.isEmpty() || currentDamageString.isEmpty() || desiredDamageString.isEmpty()) {
                resultTextView.setText("Заповніть всі поля.");
                return;
            }

            int battles = Integer.parseInt(battlesString);
            int currentDamage = Integer.parseInt(currentDamageString);
            int desiredDamage = Integer.parseInt(desiredDamageString);

            StringBuilder result = new StringBuilder("Кількість боїв задля отримання бажаної середньої шкоди: \n");

            int[] avgDamages = {5000, 4500, 4000, 3500};
            for (int avgDamage : avgDamages) {
                if (avgDamage <= desiredDamage) {
                    result.append("Шкоди у розмірі ").append(avgDamage).append(" DMG не вистачить задля покращення статистики. \n");
                } else {
                    int additionalBattles = 0;
                    int currentAvgDamage = currentDamage;
                    int battles_res = battles;
                    while (currentAvgDamage < desiredDamage) {
                        currentAvgDamage += (avgDamage - currentAvgDamage) / ++battles_res;
                        additionalBattles++;
                    }
                    result.append("Якщо грати на ").append(avgDamage).append(" DMG - ").append(additionalBattles).append(" боїв \n");
                }
            }
            resultTextView.setText(result.toString());
        }

    public void userBack(View v) {
        finish();
    }
}
