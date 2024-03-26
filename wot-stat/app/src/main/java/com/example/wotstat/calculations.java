package com.example.wotstat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class calculations extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calculations);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void userBack (View v) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void dmgPage (View v) {
        Intent intent = new Intent(this, AvgDamagePage.class);
        startActivity(intent);
    }

    public void expPage (View v) {
        Intent intent = new Intent(this,AvgExpPage.class);
        startActivity(intent);
    }

    public void percentagePage (View v) {
        Intent intent = new Intent(this, AvgPercentagePage.class);
        startActivity(intent);
    }

    public void alivePage (View v) {
        Intent intent = new Intent(this, StatShow.class);
        startActivity(intent);
    }
}