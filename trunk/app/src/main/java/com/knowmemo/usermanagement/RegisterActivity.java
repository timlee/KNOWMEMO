package com.knowmemo.usermanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import knowmemoAPI.knowmemoCallBackListener;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button register_ensure = (Button) findViewById(R.id.button_ensure);
        Button register_back = (Button) findViewById(R.id.button_back);

        final EditText account = (EditText) findViewById(R.id.editAccount);
        final EditText oldPwd = (EditText) findViewById(R.id.editOldPassword);
        final EditText newPwd = (EditText) findViewById(R.id.editNewPwd);
        final EditText email = (EditText) findViewById(R.id.editEmail);


        register_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try
                {
                    knowmemoAPI.knowmemoAPI.userRegister(account.getText().toString() , oldPwd.getText().toString(),
                                                         newPwd.getText().toString() , email.getText().toString() ,
                            new knowmemoCallBackListener()
                            {

                                @Override
                                public void onSuccess(JSONObject result) {
                                    Log.i("knowmemoAPI" , result.toString());
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }


                                @Override
                                public void onFail(JSONObject result) {
                                    Log.i("knowmemoAPI" , result.toString());
                                }
                            });
                } catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });

        register_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.this.finish();
            }
        });


    }
}
