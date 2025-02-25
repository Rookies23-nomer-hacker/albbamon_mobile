package com.example.albbamon.Experience;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.albbamon.R;

public class ExperienceSearch extends AppCompatActivity {
    ImageButton back_img_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_experience_search);

        back_img_btn = findViewById(R.id.back_img_btn);


        back_img_btn.setOnClickListener(view -> finish());
    }
}