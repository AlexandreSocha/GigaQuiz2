package com.alexandresocha.gigaquiz.backoffice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.alexandresocha.gigaquiz.DbHelper;
import com.alexandresocha.gigaquiz.Question;
import com.alexandresocha.gigaquiz.QuestionCategorie;
import com.alexandresocha.gigaquiz.R;

import java.util.List;

public class BackOfficeQuestionsEditActivity extends AppCompatActivity {

    private int questionId;
    private Spinner spinnerCategory;
    private Spinner spinnerDifficulty;
    private Spinner spinnerAnswers;

    private EditText questionText;
    private EditText answer1;
    private EditText answer2;
    private EditText answer3;
    private EditText answer4;

    private ArrayAdapter<String> adapterDifficulty;
    private ArrayAdapter<QuestionCategorie> adapterCategories;
    private ArrayAdapter<String> adapterAnswers;

    private LinearLayout btnAddQstContainer;
    private LinearLayout btnUpdQstContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_office_questions_edit);
        spinnerCategory = findViewById(R.id.spinner_category2);
        spinnerDifficulty = findViewById(R.id.spinner_difficulty2);
        spinnerAnswers = findViewById(R.id.spinner_answer);
        questionText = findViewById(R.id.edit_text_question);
        answer1 = findViewById(R.id.edit_text_answer_1);
        answer2 = findViewById(R.id.edit_text_answer_2);
        answer3 = findViewById(R.id.edit_text_answer_3);
        answer4 = findViewById(R.id.edit_text_answer_4);
        btnAddQstContainer = (LinearLayout) findViewById(R.id.container_btn_add);
        btnUpdQstContainer = (LinearLayout) findViewById(R.id.container_update_question);

        loadCategories();
        loadDifficulties();
        loadAnswerSpinner();

        Intent intent = getIntent();
        Question q = intent.getParcelableExtra("selectedQuestion");

        Button btnAddQst = findViewById(R.id.btn_add_new_question);
        btnAddQst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewQuestion();
                finish();
            }
        });

        Button btnUpdQst = findViewById(R.id.btn_update_question);
        btnUpdQst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateQuestion();
                finish();
            }
        });

        Button btnDelQuestion = findViewById(R.id.btn_delete_question);
        btnDelQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BackOfficeQuestionsEditActivity.this);
                builder.setMessage("Êtes-vous bien sûr de vouloir supprimer cette question ?");
                builder.setTitle("Avertissement");
                // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
                builder.setCancelable(false);
                builder.setPositiveButton("Oui", (DialogInterface.OnClickListener) (dialog, which) -> {
                    deleteQuestion();
                    finish();
                });
                builder.setNegativeButton("Non", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        if(q != null){
            questionId = q.getId();
            populateFields(q);
            btnAddQstContainer.setVisibility(View.GONE);
            btnUpdQstContainer.setVisibility(View.VISIBLE);
        }
        else {
            btnAddQstContainer.setVisibility(View.VISIBLE);
            btnUpdQstContainer.setVisibility(View.GONE);
        }
    }

    private void populateFields(Question q){
        questionText.setText(q.getQuestion());
        answer1.setText(q.getOption1());
        answer2.setText(q.getOption2());
        answer3.setText(q.getOption3());
        answer4.setText(q.getOption4());

        int positionAnswer = adapterAnswers.getPosition(String.valueOf(q.getAnswerNr()));
        spinnerAnswers.setSelection(positionAnswer);

        int positionDifficulty = adapterDifficulty.getPosition(q.getDifficulty());
        spinnerDifficulty.setSelection(positionDifficulty);

        DbHelper dbHelper = DbHelper.getInstance(this);
        QuestionCategorie categ = dbHelper.getCategory(q.getCategoryID());
        int positionCategory = adapterCategories.getPosition(categ);
        spinnerCategory.setSelection(positionCategory);
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
        Toast.makeText(BackOfficeQuestionsEditActivity.this, "Nouvelle question créée", Toast.LENGTH_SHORT).show();
    }

    private void updateQuestion(){
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
        db.updateQuestion(questionId, question);
        Toast.makeText(BackOfficeQuestionsEditActivity.this, "Question modifiée", Toast.LENGTH_SHORT).show();
    }

    private void deleteQuestion(){
        DbHelper db = DbHelper.getInstance(this);
        db.deleteQuestion(questionId);
        Toast.makeText(BackOfficeQuestionsEditActivity.this, "Question supprimée", Toast.LENGTH_SHORT).show();
    }

    private void loadCategories(){
        DbHelper dbHelper = DbHelper.getInstance(this);
        List<QuestionCategorie> categories = dbHelper.getAllCategories();

        adapterCategories = new ArrayAdapter<QuestionCategorie>(this, android.R.layout.simple_spinner_item, categories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategories);
    }
    private void loadDifficulties(){
        String[] difficultyLevels = Question.getAllDifficultyLevels();

        adapterDifficulty = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, difficultyLevels);
        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapterDifficulty);
    }

    private void loadAnswerSpinner() {
        String[] answers = new String[]{ "1", "2", "3", "4" };;

        adapterAnswers = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, answers);
        adapterAnswers.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnswers.setAdapter(adapterAnswers);
    }
}