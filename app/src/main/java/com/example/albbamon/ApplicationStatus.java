package com.example.albbamon;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ApplicationStatus extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_status);

        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText("지원현황");

    }

}
