package main;

import sqllite.UserData;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.knowmemo.R;
import com.knowmemo.ShowCards;
import com.knowmemo.R.id;
import com.knowmemo.R.layout;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;

public class MainActivity extends FragmentActivity {
	
	private Button button_login;
	private Button button_register;
	private Button button_forget;
	private Button button_forgetAccount;
	
	private EditText editAccount;
	private EditText editCode;
	UserData userdata;
	
	//Fragment
	private static final int SPLASH = 0;
	private static final int FRAGMENT_COUNT = SPLASH +1;
	private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];
			
	private boolean isResumed = false;
	private UiLifecycleHelper uiHelper;
	private MenuItem action_settings;
	public boolean logout=true;

	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};

	@Override
	//onCreate()用來做程式的初使化動作
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		uiHelper = new UiLifecycleHelper(this, callback);
	    uiHelper.onCreate(savedInstanceState);
	    
		setContentView(R.layout.main);
		userdata = new UserData(this);
		
		//initialize buttons
		button_login = (Button)findViewById(R.id.button_login);
		button_login.setOnClickListener(button_listener);
		button_register = (Button)findViewById(R.id.button_register);
		button_register.setOnClickListener(button_listener);
		button_forget = (Button)findViewById(R.id.button_forget);
		button_forget.setOnClickListener(button_listener);
		button_forgetAccount = (Button)findViewById(R.id.button_forgetAccount);
		button_forgetAccount.setOnClickListener(button_listener);
		
		editAccount = (EditText)findViewById(R.id.editAccount);
		editCode = (EditText)findViewById(R.id.editCode);
			
		//為了管理Activity中的fragments，需要使用FragmentManager。為了得到它，需要调用Activity中的getFragmentManager()方法。
		//得到Activity中存在的fragment：使用findFragmentById()或findFragmentByTag()方法。
		FragmentManager fm = getSupportFragmentManager();
	    fragments[SPLASH] = fm.findFragmentById(R.id.splashFragment);
	    //使用Fragment時，可以通過用户交互来執行一些動作，比如增加、移除、替换等。
	    //所有這些改變構成一个集合，這個集合被叫做一個transaction。
	    FragmentTransaction transaction = fm.beginTransaction();
	    for(int i = 0; i < fragments.length; i++) {
	        transaction.hide(fragments[i]);
	    }
		//调用commit()方法並不能立即执行transaction中包含的改變動作，commit()方法把transaction加入activity的UI線程隊列中。
		transaction.commit();
		   
	}
	
	private OnClickListener button_listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v == button_login){//登入
				//登入為管理員
				if(editAccount.getText().toString().equals("admin") &&
						editCode.getText().toString().equals("admin")){
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, adminActivity.class);
					startActivity(intent);
					MainActivity.this.finish();
				}				
				else if(userdata.isAccount(editAccount)==true){//確認帳號是否存在
					if(userdata.canLogin(editAccount, editCode)){//確認帳密是否符合
						Intent intent = new Intent();
						intent.setClass(MainActivity.this, ShowCards.class);
						startActivity(intent);
						MainActivity.this.finish();
					}
					else{//若密碼錯誤，帳號正確
						Toast.makeText(MainActivity.this, "密碼錯誤", Toast.LENGTH_SHORT).show();
					}
				}
				else{//若資料庫中無此帳號
					Toast.makeText(MainActivity.this, "查無此帳號", Toast.LENGTH_SHORT).show();
				}
			}
			else if(v == button_register){//註冊
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, CreateUserActivity.class);
				startActivity(intent);
				MainActivity.this.finish();
			}
			else if(v == button_forget){//忘記密碼
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ForgetPswActivity.class);
				startActivity(intent);
				MainActivity.this.finish();
			}
			else if(v == button_forgetAccount){
				
			}
	    }
	};
		 
	@Override
	// 取得螢幕的控制權
	//onResume()把保存的資料拿回來使用
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
		isResumed = true;
	}

	@Override
	//onPause()時把需要保存的資料保存
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
		isResumed = false;
	}

	@Override
	//onDestory()通常都拿來把onCreate()時的資料做釋放的動作
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}
		
	//新增onActivityResult() onSaveInstanceState()
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

				
	@Override
	//Android的程式在onCreate時會載入一個Bundle參數，利用這個參數就可以"暫存"使用者的UI資訊。
	//Android程式提供了一個方法讓我們儲存狀態：onSaveInstanceState()
	/*onSaveInstanceState()會在activity被銷毀之前執行，
 	      但是由於這個方法不屬於生命週期之一，所以官方網站有特別提到它不一定會被執行，
	      只適合用來暫存UI畫面的資料。如果需要儲存重要的資料，
	      最好是在onPause()進行儲存。*/
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
		
		
		@Override
		//在 Android 的 life cycle 中，一個 Activity 在重新被開啟時會呼叫 onResume()，
		//FragmentActivity 也有相對應的 onResumeFragments
		protected void onResumeFragments() {
		    super.onResumeFragments();
		    // 這個 Session 物件是用來授權人們來使用你的 app , 並且管理 Facebook 登入流程和 app 的 session。
		    //任何需要授權的 Facebook API calls 都需要使用到一個 Session 的 instance。
		    Session session = Session.getActiveSession();

		    //Toast.makeText(this, "onResumeFragments "+logout, Toast.LENGTH_SHORT).show();
            if (session != null && session.isOpened() && logout==false) {
            	//跳到ShowCards.class
		    	Intent intent = new Intent();
				intent.setClass(this, ShowCards.class);
				this.startActivityForResult(intent, 0);	
		    }
		    else {
		        showFragment(SPLASH, false);
		    }
		}
		
		
		private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		    // Only make changes if the activity is visible
		    if (isResumed) {
		        FragmentManager manager = getSupportFragmentManager();
		        // Get the number of entries in the back stack
		        int backStackSize = manager.getBackStackEntryCount();
		        // Clear the back stack
		        for (int i = 0; i < backStackSize; i++) {
		            manager.popBackStack();
		        }

		        //Toast.makeText(this, "onSessionStateChange "+logout, Toast.LENGTH_SHORT).show();
		        if (state.isOpened() && logout==true ) {
		        	Intent intent = new Intent();
		        	intent.setClass(this, ShowCards.class);
					this.startActivityForResult(intent, 0);
		        } 
		        else if (state.isClosed()) {
		            showFragment(SPLASH, false);
		        }
		    }
		}
		
		
		private void showFragment(int fragmentIndex, boolean addToBackStack) {
		    FragmentManager fm = getSupportFragmentManager();
		    FragmentTransaction transaction = fm.beginTransaction();
		    for (int i = 0; i < fragments.length; i++) {
		        if (i == fragmentIndex) {
		            transaction.show(fragments[i]);
		        } else {
		            transaction.hide(fragments[i]);
		        }
		    }
		    if (addToBackStack) {
		        transaction.addToBackStack(null);
		    }
		    transaction.commit();
		}
		
}
