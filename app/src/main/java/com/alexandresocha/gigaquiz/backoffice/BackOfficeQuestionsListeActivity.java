package com.alexandresocha.gigaquiz.backoffice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.alexandresocha.gigaquiz.DbHelper;
import com.alexandresocha.gigaquiz.Question;
import com.alexandresocha.gigaquiz.QuestionCategorie;
import com.alexandresocha.gigaquiz.QuizHomeActivity;
import com.alexandresocha.gigaquiz.R;

import java.util.ArrayList;
import java.util.List;

public class BackOfficeQuestionsListeActivity extends AppCompatActivity {


    private Spinner spinnerCategorie;
    private Spinner spinnerDifficulte;

    private ListView listView;
    private ArrayAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_office_questions_liste);

        listView = (ListView) findViewById(R.id.lvw_categ);

        spinnerCategorie = findViewById(R.id.spn_categ);
        spinnerDifficulte = findViewById(R.id.spn_diffi);
        loadCategories();
        loadDifficulties();

        Button btn = findViewById(R.id.btn_liste_questions_gerer_questions);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BackOfficeQuestionsListeActivity.this, BackOfficeQuestionsEditActivity.class);
                startActivity(intent);
            }
        });
        spinnerCategorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Search();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerDifficulte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Search();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Search();
    }

    private void Search(){
        QuestionCategorie selectedCategory = (QuestionCategorie) spinnerCategorie.getSelectedItem();
        int categoryID = selectedCategory.getId();
        String difficulty = spinnerDifficulte.getSelectedItem().toString();
        populateListView(categoryID, difficulty);
    }

    public void populateListView(int categoryID, String difficulty) {

        DbHelper db = DbHelper.getInstance(this);
        ArrayList<Question> questions = db.getQuestions(categoryID, difficulty);

        myAdapter = new ArrayAdapter<Question>(this,
                android.R.layout.simple_list_item_1, questions);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Question q = (Question) myAdapter.getItem(i);
                Intent intent = new Intent(BackOfficeQuestionsListeActivity.this, BackOfficeQuestionsEditActivity.class);
                intent.putExtra("selectedQuestion", q);
                startActivity(intent);
            }
        });


        listView.setVisibility(View.VISIBLE);
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
}