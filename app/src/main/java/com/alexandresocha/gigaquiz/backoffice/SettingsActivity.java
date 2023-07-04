package com.alexandresocha.gigaquiz.backoffice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.alexandresocha.gigaquiz.R;

public class SettingsActivity extends AppCompatActivity {

    CheckBox checkBox;
    EditText pseudo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        checkBox = (CheckBox)findViewById(R.id.chk_mode_expert);
        pseudo = (EditText)findViewById(R.id.edt_pseudo);
        Button btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSettings();
            }
        });

        displaySettings();
    }

    private void displaySettings(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if(preferences.contains("mode_expert") && preferences.getBoolean("mode_expert",false) == true)
            checkBox.setChecked(true);
        else
            checkBox.setChecked(false);

        pseudo.setText(preferences.getString("pseudo_user", null));
    }

    private void saveSettings(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();

        if(checkBox.isChecked())
            editor.putBoolean("mode_expert", true);
        else
            editor.putBoolean("mode_expert", false);

        editor.putString("pseudo_user", pseudo.getText().toString());
        editor.apply();
        finish();

    }
}