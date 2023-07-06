package com.alexandresocha.gigaquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE = "extraScore";
    private static long COUNTDOWN_IN_MILLIS = 30000;

    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";

    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;

    private TextView textViewCategory;
    private TextView textViewDifficulty;
    private Button buttonConfirmNext;

    private Button buttonAnswer1;
    private Button buttonAnswer2;
    private Button buttonAnswer3;
    private Button buttonAnswer4;


    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;

    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    ProgressBar mProgressBar;

    private ArrayList<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private int score;
    private boolean answered;

    private long backPressedTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestion = findViewById(R.id.txv_question);
        textViewScore = findViewById(R.id.txv_score);
        textViewQuestionCount = findViewById(R.id.txv_question_count);
        textViewCategory = findViewById(R.id.txv_categorie);
        textViewDifficulty = findViewById(R.id.text_view_difficulty);
        buttonConfirmNext = findViewById(R.id.btn_confirm_next);
        mProgressBar=(ProgressBar)findViewById(R.id.progressbar);
        buttonAnswer1 = findViewById(R.id.btn_answer_1);
        buttonAnswer2 = findViewById(R.id.btn_answer_2);
        buttonAnswer3 = findViewById(R.id.btn_answer_3);
        buttonAnswer4 = findViewById(R.id.btn_answer_4);
        //textColorDefaultCd = textViewCountDown.getTextColors();

        Intent intent = getIntent();
        int categoryID = intent.getIntExtra(QuizHomeActivity.EXTRA_CATEGORIE_ID, 0);
        String categoryName = intent.getStringExtra(QuizHomeActivity.EXTRA_CATEGORIE_NAME);
        String difficulty = intent.getStringExtra(QuizHomeActivity.EXTRA_DIFFICULTE);

        textViewCategory.setText("Category: " + categoryName);
        textViewDifficulty.setText("Difficulty: " + difficulty);

        if(savedInstanceState == null){
            DbHelper dbHelper = DbHelper.getInstance(this);
            questionList = dbHelper.getQuestions(categoryID, difficulty);
            ArrayList<Question> allQuestions = dbHelper.getAllQuestions();
            questionCountTotal = questionList.size();
            Collections.shuffle(questionList);

            showNextQuestion();
        } else {
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCountTotal = questionList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = questionList.get(questionCounter - 1);
            score = savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);

            if(!answered){
                startCountdown();
            } else {
                updateCountDownText();
                showSolution();
            }
        }

        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNextQuestion();
            }
        });
        buttonAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!answered) {
                    checkAnswerNew(1);

                } else {
                    showNextQuestion();
                }
            }
        });
        buttonAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!answered) {
                    checkAnswerNew(2);
                } else {
                    showNextQuestion();
                }
            }
        });
        buttonAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!answered) {
                    checkAnswerNew(3);
                } else {
                    showNextQuestion();
                }
            }
        });
        buttonAnswer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!answered) {
                    checkAnswerNew(4);
                } else {
                    showNextQuestion();
                }
            }
        });
    }

    private void showNextQuestion(){
        buttonConfirmNext.setVisibility(View.INVISIBLE);

        buttonAnswer1.setBackgroundColor(Color.BLUE);
        buttonAnswer2.setBackgroundColor(Color.BLUE);
        buttonAnswer3.setBackgroundColor(Color.BLUE);
        buttonAnswer4.setBackgroundColor(Color.BLUE);

        if(questionCounter < questionCountTotal){
            currentQuestion = questionList.get(questionCounter);

            textViewQuestion.setText(currentQuestion.getQuestion());
            buttonAnswer1.setText(currentQuestion.getOption1());
            buttonAnswer2.setText(currentQuestion.getOption2());
            buttonAnswer3.setText(currentQuestion.getOption3());
            buttonAnswer4.setText(currentQuestion.getOption4());

            questionCounter++;
            textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;

            startCountdown();
        } else{
            finishQuiz();
        }
    }

    private void manageExpertMode(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.contains("mode_expert") && preferences.getBoolean("mode_expert",false) == true)
            COUNTDOWN_IN_MILLIS = 10000;
        else
            COUNTDOWN_IN_MILLIS = 30000;
        timeLeftInMillis = COUNTDOWN_IN_MILLIS;
    }

    private void startCountdown(){
        manageExpertMode();
        mProgressBar.setProgress(100);
        countDownTimer = new CountDownTimer(timeLeftInMillis, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
                int progress = (int) (timeLeftInMillis/(COUNTDOWN_IN_MILLIS/100));
                mProgressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                showSolution();
                buttonConfirmNext.setVisibility(View.VISIBLE);
                mProgressBar.setProgress((int)timeLeftInMillis);
            }
        }.start();
    }
    private void updateCountDownText(){

        if(timeLeftInMillis < (COUNTDOWN_IN_MILLIS/3)){
            mProgressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        } else {
            mProgressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
        }
    }

    private void checkAnswerNew(int answerNr) {
        answered = true;
        countDownTimer.cancel();
        buttonConfirmNext.setVisibility(View.VISIBLE);

        if(answerNr == currentQuestion.getAnswerNr()){
            score++;
            textViewScore.setText("Score: " + score);
        }
        showSolution();
    }

    private void showSolution(){
        buttonAnswer1.setBackgroundColor(Color.RED);
        buttonAnswer2.setBackgroundColor(Color.RED);
        buttonAnswer3.setBackgroundColor(Color.RED);
        buttonAnswer4.setBackgroundColor(Color.RED);

        switch(currentQuestion.getAnswerNr()){
            case 1:
                buttonAnswer1.setBackgroundColor(Color.GREEN);
                textViewQuestion.setText("Answer 1 is correct");
                break;
            case 2:
                buttonAnswer2.setBackgroundColor(Color.GREEN);
                textViewQuestion.setText("Answer 2 is correct");
                break;
            case 3:
                buttonAnswer3.setBackgroundColor(Color.GREEN);
                textViewQuestion.setText("Answer 3 is correct");
                break;
            case 4:
                buttonAnswer4.setBackgroundColor(Color.GREEN);
                textViewQuestion.setText("Answer 4 is correct");
                break;
        }



        if(questionCounter < questionCountTotal){
            buttonConfirmNext.setText("Continuer");
        } else {
            buttonConfirmNext.setText("Terminer");
        }
    }

    private void finishQuiz(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {

        if(backPressedTime + 2000> System.currentTimeMillis()){
            finishQuiz();
        } else {
            Toast.makeText(this, "Appuyer de nouveau pour quitter", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
    }
}