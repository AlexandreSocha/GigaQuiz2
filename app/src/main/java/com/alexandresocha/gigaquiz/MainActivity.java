package com.alexandresocha.gigaquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alexandresocha.gigaquiz.backoffice.BackOfficeActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            DbHelper dbHelper = DbHelper.getInstance(this);
            ArrayList<Question> questions = dbHelper.getAllQuestions();
        }

        TextView txvInvite = (TextView) findViewById(R.id.txv_invite);
        txvInvite.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(MainActivity.this, QuizHomeActivity.class);
                 startActivity(intent);
             }
         });

        TextView txvGoBackoffice = (TextView) findViewById(R.id.txv_go_backoffice);
        txvGoBackoffice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BackOfficeActivity.class);
                startActivity(intent);
                //Toast.makeText(MainActivity.this, "Back-office", Toast.LENGTH_SHORT).show();
            }
        });
    }
}