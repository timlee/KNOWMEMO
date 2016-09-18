package com.knowmemo.usermanagement;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import knowmemoAPI.knowmemoCallBackListener;

public class ForgetPwdActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);


        final EditText forget_account = (EditText) findViewById(R.id.editAccount_in_forget);
        final EditText forget_email = (EditText) findViewById(R.id.editEmail_in_forget);

        Button forget_ensurepwd = (Button) findViewById(R.id.button_ensure_in_forget);
        Button forget_back = (Button) findViewById(R.id.button_back_in_forget);

        final TextView ans = (TextView) findViewById(R.id.forget);

        forget_ensurepwd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    knowmemoAPI.knowmemoAPI.userForgetPassword(forget_account.getText().toString(), forget_email.getText().toString(),
                            new knowmemoCallBackListener() {
                                @Override
                                public void onSuccess(JSONObject result) {
                                    try
                                    {
                                       ans.setText("密碼是:" + result.get("message").toString());
                                    } catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFail(JSONObject result)
                                {
                                    ans.setText("輸入錯誤!");
                                }
                            });
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        forget_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgetPwdActivity.this.finish();
            }
        });

    }

}
