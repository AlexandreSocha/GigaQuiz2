package com.alexandresocha.gigaquiz.backoffice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alexandresocha.gigaquiz.R;

public class BackOfficeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_office);

        Button buttonGererQuestion = findViewById(R.id.btn_gerer_questions);
        buttonGererQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BackOfficeActivity.this, BackOfficeQuestionsListeActivity.class);
                startActivity(intent);
            }
        });


        Button buttonGererCategories = findViewById(R.id.btn_gerer_categories);
        buttonGererCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BackOfficeActivity.this, BackOfficeCategoriesListeActivity.class);
                startActivity(intent);
            }
        });

        Button buttonGererSettings = findViewById(R.id.btn_gerer_parametres);
        buttonGererSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BackOfficeActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}