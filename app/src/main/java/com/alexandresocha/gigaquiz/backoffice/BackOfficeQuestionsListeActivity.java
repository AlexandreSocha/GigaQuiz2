package com.alexandresocha.gigaquiz.backoffice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alexandresocha.gigaquiz.R;

public class BackOfficeQuestionsListeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_office_questions_liste);

        Button btn = findViewById(R.id.btn_liste_questions_gerer_questions);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BackOfficeQuestionsListeActivity.this, BackOfficeQuestionsEditActivity.class);
                startActivity(intent);
            }
        });
    }
}