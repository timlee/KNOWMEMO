package com.knowmemo.usermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by user on 2016/9/7.
 */
public class LevelChoiceActivity extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_choice);

        Button level_choice = (Button) findViewById(R.id.level_low);
       level_choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToLearnWord();
            }
        });
    }
    public void jumpToLearnWord(){
        setContentView(R.layout.activity_show_word);
        //Button l= (Button)findViewById(R.id.Button02);
        Intent intent = new Intent(LevelChoiceActivity.this, ShowWordActivity.class);
        startActivity(intent);

    }
}
