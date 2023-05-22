package com.alexandresocha.gigaquiz.backoffice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.alexandresocha.gigaquiz.DbHelper;
import com.alexandresocha.gigaquiz.QuestionCategorie;
import com.alexandresocha.gigaquiz.R;

import java.util.ArrayList;

public class BackOfficeCategoriesListeActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_office_categories_liste);

        listView = (ListView) findViewById(R.id.lvw_categories);
        populateListView();

        Button btn = findViewById(R.id.btn_liste_categories_gerer_categories);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BackOfficeCategoriesListeActivity.this, BackOfficeCategoriesEditActivity.class);
                startActivity(intent);
            }
        });
    }


    public void populateListView() {

        DbHelper db = DbHelper.getInstance(this);
        ArrayList<QuestionCategorie> categories = db.getAllCategories();
        ListAdapter a = new ArrayAdapter<QuestionCategorie>(this,
                android.R.layout.simple_list_item_1, categories);
        listView.setAdapter(a);
    }
}