package com.knowmemo.usermanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import knowmemoAPI.knowmemoCallBackListener;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText account = (EditText)findViewById(R.id.editAccount);
        final EditText pwd = (EditText)findViewById(R.id.editCode);


        Button login = (Button) findViewById(R.id.button_login);
        Button register = (Button) findViewById(R.id.button_register);
        Button forget= (Button) findViewById(R.id.button_forget);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try
                {
                    knowmemoAPI.knowmemoAPI.userLogin(account.getText().toString(), pwd.getText().toString(), new knowmemoCallBackListener() {
                        @Override
                        public void onSuccess(JSONObject result)
                        {
                            System.out.println("登入成功");
                            Intent intent = new Intent(MainActivity.this, MainChoiceActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFail(JSONObject result)
                        {
                            Toast.makeText(MainActivity.this , "登入失敗" , Toast.LENGTH_LONG ).show();
                            account.setText("");
                            pwd.setText("");
                        }
                    });
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForgetPwdActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
