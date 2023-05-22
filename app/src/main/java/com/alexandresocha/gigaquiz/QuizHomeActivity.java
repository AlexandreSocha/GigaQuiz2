package com.alexandresocha.gigaquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.List;

public class QuizHomeActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORIE_ID = "extraCategorieID";
    public static final String EXTRA_CATEGORIE_NAME = "extraCategorieName";
    public static final String EXTRA_DIFFICULTE = "extraDifficulte";

    private Spinner spinnerCategorie;
    private Spinner spinnerDifficulte;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_home);

        spinnerCategorie = findViewById(R.id.spn_categorie);
        spinnerDifficulte = findViewById(R.id.spn_difficulte);

        loadCategories();
        loadDifficulties();

        Button buttonStartQuiz = findViewById(R.id.btn_start_quiz);
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuiz();
            }
        });
    }

    private void loadCategories(){
        DbHelper dbHelper = DbHelper.getInstance(this);
        List<QuestionCategorie> categories = dbHelper.getAllCategories();

        ArrayAdapter<QuestionCategorie> a = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);

        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorie.setAdapter(a);
    }
    private void loadDifficulties(){
        String[] difficultyLevels = Question.getAllDifficultyLevels();

        ArrayAdapter<String> a = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, difficultyLevels);

        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulte.setAdapter(a);
    }

    private void startQuiz(){
        QuestionCategorie selectedCategory = (QuestionCategorie) spinnerCategorie.getSelectedItem();
        int categoryID = selectedCategory.getId();
        String categoryName = selectedCategory.getName();
        String difficulty = spinnerDifficulte.getSelectedItem().toString();

        Intent intent = new Intent(QuizHomeActivity.this, QuizActivity.class);
        intent.putExtra(EXTRA_CATEGORIE_ID, categoryID);
        intent.putExtra(EXTRA_CATEGORIE_NAME, categoryName);
        intent.putExtra(EXTRA_DIFFICULTE, difficulty);

        startActivity(intent);
        //startActivityForResult(intent, REQUEST_CODE_QUIZ);
    }
}