package com.alexandresocha.gigaquiz.backoffice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alexandresocha.gigaquiz.DbHelper;
import com.alexandresocha.gigaquiz.QuestionCategorie;
import com.alexandresocha.gigaquiz.R;

import java.util.ArrayList;

public class BackOfficeCategoriesListeActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_office_categories_liste);

        listView = (ListView) findViewById(R.id.lvw_categories);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(BackOfficeCategoriesListeActivity.this, BackOfficeCategoriesEditActivity.class);
                intent.putExtra("selected_category", String.valueOf(myAdapter.getItemId(i)));
                startActivity(intent);
            }
        });

        Button btn = findViewById(R.id.btn_liste_categories_gerer_categories);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BackOfficeCategoriesListeActivity.this, BackOfficeCategoriesEditActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateListView();
    }

    public void populateListView() {

        DbHelper db = DbHelper.getInstance(this);
        ArrayList<QuestionCategorie> categories = db.getAllCategories();
        myAdapter = new ArrayAdapter<QuestionCategorie>(this,
                android.R.layout.simple_list_item_1, categories);
        listView.setAdapter(myAdapter);
    }
}