package com.alexandresocha.gigaquiz.backoffice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.alexandresocha.gigaquiz.DbHelper;
import com.alexandresocha.gigaquiz.Question;
import com.alexandresocha.gigaquiz.QuestionCategorie;
import com.alexandresocha.gigaquiz.R;

import java.util.List;

public class BackOfficeQuestionsEditActivity extends AppCompatActivity {

    private Spinner spinnerCategory;
    private Spinner spinnerDifficulty;
    private Spinner spinnerAnswers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_office_questions_edit);
        spinnerCategory = findViewById(R.id.spinner_category2);
        spinnerDifficulty = findViewById(R.id.spinner_difficulty2);
        spinnerAnswers = findViewById(R.id.spinner_answer);

        loadCategories();
        loadDifficulties();
        loadAnswerSpinner();

        Button btn = findViewById(R.id.btn_add_new_question);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewQuestion();
                finish();
            }
        });
    }

    private void addNewQuestion(){
        DbHelper db = DbHelper.getInstance(this);
        EditText editTextQuestion = findViewById(R.id.edit_text_question);
        String questionText = editTextQuestion.getText().toString();
        EditText editTextAns1 = findViewById(R.id.edit_text_answer_1);
        String answer1Text = editTextAns1.getText().toString();
        EditText editTextAns2 = findViewById(R.id.edit_text_answer_2);
        String answer2Text = editTextAns2.getText().toString();
        EditText editTextAns3 = findViewById(R.id.edit_text_answer_3);
        String answer3Text = editTextAns3.getText().toString();
        EditText editTextAns4 = findViewById(R.id.edit_text_answer_4);
        String answer4Text = editTextAns4.getText().toString();

        QuestionCategorie selectedCategory = (QuestionCategorie) spinnerCategory.getSelectedItem();
        int categoryID = selectedCategory.getId();
        //String categoryName = selectedCategory.getName();

        String difficulty = spinnerDifficulty.getSelectedItem().toString();
        int answer = Integer.parseInt(spinnerAnswers.getSelectedItem().toString());

        Question question = new Question(questionText, answer1Text, answer2Text,
                answer3Text, answer4Text, answer, difficulty, categoryID);
        db.addQuestion(question);
    }

    private void loadCategories(){
        DbHelper dbHelper = DbHelper.getInstance(this);
        List<QuestionCategorie> categories = dbHelper.getAllCategories();

        ArrayAdapter<QuestionCategorie> adapterCategories = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategories);
    }
    private void loadDifficulties(){
        String[] difficultyLevels = Question.getAllDifficultyLevels();

        ArrayAdapter<String> adapterDifficulty = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, difficultyLevels);
        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapterDifficulty);
    }

    private void loadAnswerSpinner() {
        String[] answers = new String[]{ "1", "2", "3", "4" };;

        ArrayAdapter<String> adapterAnswers = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, answers);
        adapterAnswers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnswers.setAdapter(adapterAnswers);
    }
}