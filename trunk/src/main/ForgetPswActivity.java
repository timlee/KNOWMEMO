package main;

import com.knowmemo.R;

import sqllite.UserData;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ForgetPswActivity extends Activity{

	UserData userdata;
	EditText editAccount_in_forget;
	EditText showPsw;
	Button button_ensure_in_forget;
	Button button_back_in_forget;
	TextView showpsw;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_psw);
		
		userdata = new UserData(this);
		
		button_ensure_in_forget = (Button)findViewById(R.id.button_ensure_in_forget);
		button_ensure_in_forget.setOnClickListener(button_listener);
		button_back_in_forget = (Button)findViewById(R.id.button_back_in_forget);
		button_back_in_forget.setOnClickListener(button_listener);
		
		editAccount_in_forget = (EditText)findViewById(R.id.editAccount_in_forget);
		showpsw = (TextView)findViewById(R.id.showpsw);
	}
	
	private OnClickListener button_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v == button_back_in_forget){//返回至登入頁面
				Intent intent = new Intent();
				intent.setClass(ForgetPswActivity.this, MainActivity.class);
				startActivity(intent);
				ForgetPswActivity.this.finish();
			}
			
			else if(v == button_ensure_in_forget){//忘記密碼查詢
				boolean isA = userdata.isAccount(editAccount_in_forget);
				Cursor cs = userdata.queryPsw(editAccount_in_forget);
				
				if(isA == false){
					showpsw.setText("查無此帳號");
				}
				else{
					cs.moveToFirst();
					showpsw.setText("Your password is " + cs.getString(0));
				}
			}		

	    }
	
	};
}
