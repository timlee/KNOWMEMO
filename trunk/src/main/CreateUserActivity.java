package main;

import sqllite.UserData;

import com.knowmemo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateUserActivity extends Activity{
	
	private Button button_back;
	private Button button_ensure;
	
	private EditText editNewAccount;
	private EditText editNewPassword;
	private EditText editNewName;
	private EditText editNewBirthday;
	private EditText editNewEmail;
	
	UserData userdata;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_user);
		
		userdata = new UserData(this);
		
		button_back = (Button)findViewById(R.id.button_back);
		button_back.setOnClickListener(button_listener);
		button_ensure = (Button)findViewById(R.id.button_ensure);
		button_ensure.setOnClickListener(button_listener);
		
		editNewAccount = (EditText)findViewById(R.id.editNewAccount);
		editNewPassword = (EditText)findViewById(R.id.editNewPassword);
		editNewName = (EditText)findViewById(R.id.editNewName);
		editNewBirthday = (EditText)findViewById(R.id.editNewBirthday);
		editNewEmail = (EditText)findViewById(R.id.editNewEmail);
		
	}
	private OnClickListener button_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v == button_back){//從建立新帳戶頁面返回至登入畫面
				Intent intent = new Intent();
				intent.setClass(CreateUserActivity.this, MainActivity.class);
				startActivity(intent);
				CreateUserActivity.this.finish();
			}
			else if(v == button_ensure){//確定建立新帳戶
				if(editNewAccount.getText().toString().equals("") ||
				   editNewPassword.getText().toString().equals("") ||
				   editNewName.getText().toString().equals("") ||
				   editNewBirthday.getText().toString().equals("") ||
				   editNewEmail.getText().toString().equals(""))
					Toast.makeText(CreateUserActivity.this, "欄位不可空白", Toast.LENGTH_LONG).show();
				else {
					try{
						userdata.insert(editNewAccount, editNewPassword, 
								editNewName, editNewEmail, editNewBirthday);
					}catch(Exception e){
						System.out.println(e.toString());
					}
					Intent intent = new Intent();
					intent.setClass(CreateUserActivity.this, MainActivity.class);
					startActivity(intent);
					CreateUserActivity.this.finish();
				}
					
			}
		}	
	};
}
