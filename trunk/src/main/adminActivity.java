package main;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.knowmemo.R;
import sqllite.*;

public class adminActivity extends Activity {
	
	Button button_listAllAccount;
	Button button_deleteAccount;
	TextView usersView;
	UserData userdata;
	SqlHelper sqlhelper;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.admin);
		
		button_listAllAccount = (Button)findViewById(R.id.button_listAllAccount);
		button_listAllAccount.setOnClickListener(button_listener);
		button_deleteAccount = (Button)findViewById(R.id.button_deleteAccount);
		button_deleteAccount.setOnClickListener(button_listener);
		
		userdata = new UserData(this);
		sqlhelper = new SqlHelper(this);
		sqlhelper.open();
		
		
		usersView = (TextView)findViewById(R.id.usersView);
		
	}
	
	private OnClickListener button_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v == button_listAllAccount){//列出所有使用者
				
					Cursor cs = userdata.getAll();
					try{
						System.out.println(cs.getCount());
					}catch(Exception e){
						System.out.println(e.toString());
					}
				
					
				
				
//				if(cs.getCount() == 0){
//					System.out.println("no data");
//					usersView.setText("No Account was found");
//				}
//				else{
//					String[] sNote = new String[cs.getCount()];
//					  
//					 int rows_num = cs.getCount();//取得資料表列數
//					 if(rows_num != 0) {
//					  cs.moveToFirst();   //將指標移至第一筆資料
//					  usersView.append("Account \t Password \t Name \t Email \t Birthday");
//					  
//					  for(int i=0; i<rows_num; i++) {
//					   String strCr = cs.getString(0);
//					   sNote[i]=strCr;
//					   usersView.setText(sNote[i]);
//					   cs.moveToNext();//將指標移至下一筆資料
//					  }
//					}
//				}
//				cs.close();
			}
			
			else if(v == button_deleteAccount){//刪除使用者
			}
		}	
	};
	
}
