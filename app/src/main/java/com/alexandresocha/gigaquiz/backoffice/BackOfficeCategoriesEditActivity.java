package com.alexandresocha.gigaquiz.backoffice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alexandresocha.gigaquiz.DbHelper;
import com.alexandresocha.gigaquiz.Question;
import com.alexandresocha.gigaquiz.QuestionCategorie;
import com.alexandresocha.gigaquiz.R;

public class BackOfficeCategoriesEditActivity extends AppCompatActivity {

    private int categoryId;
    private EditText categoryText;
    private LinearLayout btnAddQstContainer;
    private LinearLayout btnUpdQstContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_office_categories_edit);

        categoryText = findViewById(R.id.edt_libelle_categorie_label);
        btnAddQstContainer = (LinearLayout) findViewById(R.id.container_btn_add);
        btnUpdQstContainer = (LinearLayout) findViewById(R.id.container_update_category);

        Intent intent = getIntent();
        QuestionCategorie c = intent.getParcelableExtra("selectedCategory");

        Button btnAddCat = findViewById(R.id.btn_add_new_categorie);
        btnAddCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewCategorie();
                finish();
            }
        });

        Button btnUpd = findViewById(R.id.btn_update_category);
        btnUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCategory();
                finish();
            }
        });

        Button btnDelQuestion = findViewById(R.id.btn_delete_category);
        btnDelQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BackOfficeCategoriesEditActivity.this);
                builder.setMessage("Êtes-vous bien sûr de vouloir supprimer cette catégorie ?");
                builder.setTitle("Avertissement");
                // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
                builder.setCancelable(false);
                builder.setPositiveButton("Oui", (DialogInterface.OnClickListener) (dialog, which) -> {
                    deleteCategory();
                    finish();
                });
                builder.setNegativeButton("Non", (DialogInterface.OnClickListener) (dialog, which) -> {
                    dialog.cancel();
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        if(c != null){
            categoryId = c.getId();
            populateFields(c);
            btnAddQstContainer.setVisibility(View.GONE);
            btnUpdQstContainer.setVisibility(View.VISIBLE);
        }
        else {
            btnAddQstContainer.setVisibility(View.VISIBLE);
            btnUpdQstContainer.setVisibility(View.GONE);
        }
    }

    private void populateFields(QuestionCategorie c){
        categoryText.setText(c.getName());
    }

    private void addNewCategorie(){
        DbHelper db = DbHelper.getInstance(this);
        EditText editTextCategorie = findViewById(R.id.edt_libelle_categorie_label);
        String categorieLibelle = editTextCategorie.getText().toString();

        QuestionCategorie categorie = new QuestionCategorie(categorieLibelle);
        db.addCategory(categorie);
    }

    private void updateCategory(){
        DbHelper db = DbHelper.getInstance(this);
        EditText editTextCategorie = findViewById(R.id.edt_libelle_categorie_label);
        String categorieLibelle = editTextCategorie.getText().toString();

        QuestionCategorie categorie = new QuestionCategorie(categorieLibelle);
        db.updateCategory(categoryId, categorie);
    }

    private void deleteCategory(){
        DbHelper db = DbHelper.getInstance(this);
        db.deleteCategory(categoryId);
    }
}