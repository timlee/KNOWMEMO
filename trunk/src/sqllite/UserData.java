package sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.EditText;

public class UserData {

	private static final String TABLE_NAME_USERS = "account";
	
	Context context;
	SqlHelper sqlhelper;
	UserData userdata;
	ContentValues values;
	
	public UserData(Context mContext){
		context = mContext;
		sqlhelper = new SqlHelper(context);
	}
	
	public void insert(EditText name, EditText password, EditText user_name, 
			EditText email, EditText birthday){
		
		values = new ContentValues();
		
		values.put("name", name.getText().toString());//帳號
		values.put("password",password.getText().toString());//密碼
		values.put("user_name", user_name.getText().toString());//名字
		values.put("email", email.getText().toString());//信箱
		values.put("birthday",birthday.getText().toString());//生日
		values.put("level"," ");
		values.put("experience"," ");
		values.put("learning_state"," ");
		
		sqlhelper.Insert(context, TABLE_NAME_USERS, values);
	}
	
	public void delete(){
		
	}
	
	public void update(){
		
	}
	
	public Cursor queryPsw(EditText name){		
		String[] columns = {"password"};
		String[] SelectorArgs = {name.getText().toString()};
		Cursor cs = sqlhelper.Query(context, TABLE_NAME_USERS, 
			columns, "name=?", SelectorArgs, null, null, null);
		return cs;
	}
	
	public Cursor getAll(){
		String[] columns = {"name","password","user_name","email","birthday"};
		Cursor cs = sqlhelper.Query(context, TABLE_NAME_USERS, 
				columns, null, null, null, null, null);
		return cs;
	}
	
	public boolean isAccount(EditText name){//name為帳號
		String[] columns = {"name","password","user_name","email","birthday"};
		String[] SelectorArgs = {name.getText().toString()};
		Cursor cs = sqlhelper.Query(context, TABLE_NAME_USERS, 
				columns, "name=?", SelectorArgs, null, null, null);
		if(cs.getCount()==0){
			return false;
		}
		else{
			return true;
		}
	}
	
	public boolean canLogin(EditText name,EditText password){//name為帳號
		String[] columns = {"name","password","user_name","email","birthday"};
		String[] SelectorArgs = {name.getText().toString(), password.getText().toString()};
		Cursor cs = sqlhelper.Query(context, TABLE_NAME_USERS, 
				columns, "name=? and password=?", SelectorArgs, null, null, null);
		if(cs.getCount()==0){
			return false;
		}
		else{
			return true;
		}
	}
}
