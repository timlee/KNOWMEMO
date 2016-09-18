package com.knowmemo.usermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by user on 2016/9/7.
 */
public class MainChoiceActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_choice);

        Button main_choice_add = (Button) findViewById(R.id.button_add);
        Button main_choice_test = (Button) findViewById(R.id.button_test);
        Button main_choice_setTime = (Button) findViewById(R.id.button_setTime);

        Button main_choice_learn = (Button) findViewById(R.id.button_learn);
        main_choice_learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToLearn();
            }
        });
    }
    public void jumpToLearn(){
        setContentView(R.layout.activity_level_choice);
        //Button l= (Button)findViewById(R.id.Button02);
        Intent intent = new Intent(MainChoiceActivity.this, LevelChoiceActivity.class);
        startActivity(intent);

    }
}
