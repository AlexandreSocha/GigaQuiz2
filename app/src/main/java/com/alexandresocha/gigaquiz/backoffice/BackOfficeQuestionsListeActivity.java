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

import com.alexandresocha.gigaquiz.DbHelper;
import com.alexandresocha.gigaquiz.Question;
import com.alexandresocha.gigaquiz.QuestionCategorie;
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

        Button btnFiltrer = findViewById(R.id.btn_filtrer_questions);
        btnFiltrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuestionCategorie selectedCategory = (QuestionCategorie) spinnerCategorie.getSelectedItem();
                int categoryID = selectedCategory.getId();
                String categoryName = selectedCategory.getName();
                String difficulty = spinnerDifficulte.getSelectedItem().toString();
                populateListView(categoryID, difficulty);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        //populateListView();
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