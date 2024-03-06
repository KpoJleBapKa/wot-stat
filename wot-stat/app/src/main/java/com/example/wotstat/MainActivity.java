package com.example.wotstat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void btnClick(View v) {
        showInfo(((Button) v).getText().toString());
    }

    private void showInfo(String text) {
        Toast.makeText(this, "niggers", Toast.LENGTH_SHORT).show();
    }
}