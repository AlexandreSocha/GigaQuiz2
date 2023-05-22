package com.alexandresocha.gigaquiz.backoffice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alexandresocha.gigaquiz.DbHelper;
import com.alexandresocha.gigaquiz.Question;
import com.alexandresocha.gigaquiz.QuestionCategorie;
import com.alexandresocha.gigaquiz.R;

public class BackOfficeCategoriesEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_office_categories_edit);

        Button btn = findViewById(R.id.btn_add_new_categorie);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewCategorie();
                finish();
            }
        });
    }

    private void addNewCategorie(){
        DbHelper db = DbHelper.getInstance(this);
        EditText editTextCategorie = findViewById(R.id.edt_libelle_categorie_label);
        String categorieLibelle = editTextCategorie.getText().toString();

        QuestionCategorie categorie = new QuestionCategorie(categorieLibelle);
        db.addCategory(categorie);
    }
}