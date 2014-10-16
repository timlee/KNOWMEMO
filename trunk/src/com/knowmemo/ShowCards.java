package com.knowmemo;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.fima.cardsui.views.CardUI;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class ShowCards extends Activity{
	private CardUI mCardView;
	WordCard wordCard;
	public static final int DICTIONARY = Menu.FIRST;
	public static final int New_Word = Menu.CATEGORY_SECONDARY;
	public static boolean TTS_Speak = true;	
	

	
	//分享
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;
	private static final String TAG = "SelectionFragment";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		;
		setContentView(R.layout.activity_main);
   
		// init CardView
		mCardView = (CardUI) findViewById(R.id.cardsview);
		mCardView.setSwipeable(false);

		wordCard = new WordCard(this);
		mCardView.addCardToLastStack(wordCard);
		//mCardView.addCard(new MyWordCard(this));
		
		View wordCardView = wordCard.getCardContent(this);
//		View.findViewById(R.id.box1_button);
		
	

		mCardView.refresh();
	}
	

	protected void onActivityResult(int requestCode, int resultCode, Intent  data) {
    	if (requestCode != 0)
    		return;
    	
    	switch (resultCode) {
    	case RESULT_OK:

    		wordCard.getCardContent(this);
    		Log.d("#### onActivityResult ##### ", "OK");
    		
    		break;
    	case RESULT_CANCELED:
    		break;

    	}
    	
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		menu.add(0, 0, 0, R.string.menu_dictionary );
		menu.add(0, 1, 0, R.string.add_word);
		menu.add(0, 2, 0, R.string.menu_settings);
		menu.add(0, 3, 0, R.string.no_speak);
		menu.add(0, 4, 0, R.string.speak);
		menu.add(0, 5, 0, R.string.logout_settings);
		menu.add(0, 6, 0, R.string.Sharing);
		menu.add(0, 7, 0, R.string.finish);
		return super.onCreateOptionsMenu(menu);
		//return true;
	}
	
	
    //功能選項
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
			case 0:{//查字典
				
				String myURL = "http://tw.dictionary.yahoo.com/dictionary?p="+this.wordCard.getCurrentWord(); 
				Uri uri = Uri.parse(myURL);
				Intent it = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(it);
				
				break;
			}
			case 1 : {//新增單字  

				Intent intent = new Intent();
//				Bundle bundle = new Bundle();
//				bundle.putString("book_want_to_insert", book_want_to_learn); // 在這裡要把所選擇的辭庫傳到learning
//				intent.putExtras(bundle);
				intent.setClass(this, Insert_Cards.class);
				this.startActivityForResult(intent, 0);
				
				break;
			}
			
			case 2 :{//設定
				
				
				break;
			}
			
			case 3 :{//不發音
				TTS_Speak = false;
				//Toast.makeText(this, "TTS_Speak = "+TTS_Speak, Toast.LENGTH_SHORT).show();
				break;
			}
			case 4 :{//發音
				TTS_Speak = true;
				//Toast.makeText(this, "TTS_Speak = "+TTS_Speak, Toast.LENGTH_SHORT).show();
				break;
			}
			case 5 :{
				Intent intent = new Intent();
				intent.setClass(this, MainActivity.class);
				this.startActivityForResult(intent, 0);
				break;
				
			}
			case 6 :{
				publishStory();
				break;
			}
			case 7 :{
				finish();
				break;
			}
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	//分享
	private void publishStory() {
	    Session session = Session.getActiveSession();

	    if (session != null){

	        // Check for publish permissions    
	        List<String> permissions = session.getPermissions();
	        if (!isSubsetOf(PERMISSIONS, permissions)) {
	            pendingPublishReauthorization = true;
	            Session.NewPermissionsRequest newPermissionsRequest = new Session
	                    .NewPermissionsRequest(this, PERMISSIONS);
	        session.requestNewPublishPermissions(newPermissionsRequest);
	            return;
	        }

	        Bundle postParams = new Bundle();
	        postParams.putString("name", "Facebook SDK for Android---TREE");
	        postParams.putString("caption", "Build great social apps and get more installs.");
	        postParams.putString("description", "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
	        //分享連結
	        postParams.putString("link", "https://developers.facebook.com/apps/1482592795323742/");
	        postParams.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

	        Request.Callback callback= new Request.Callback() {
	            public void onCompleted(Response response) {
	                JSONObject graphResponse = response
	                                           .getGraphObject()
	                                           .getInnerJSONObject();
	                String postId = null;
	                try {
	                    postId = graphResponse.getString("id");
	                } catch (JSONException e) {
	                    Log.i(TAG, "JSON error "+ e.getMessage());
	                }
	                FacebookRequestError error = response.getError();
	                if (error != null) {
	                    //Toast.makeText(getActivity().getApplicationContext(), error.getErrorMessage(), Toast.LENGTH_SHORT).show();
	                    } else {
	                        //Toast.makeText(getActivity().getApplicationContext(), postId,Toast.LENGTH_LONG).show();
	                }
	            }
	        };

	        Request request = new Request(session, "me/feed", postParams, 
	                              HttpMethod.POST, callback);

	        RequestAsyncTask task = new RequestAsyncTask(request);
	        task.execute();
	    }

	}
 
   private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
	    for (String string : subset) {
	        if (!superset.contains(string)) {
	            return false;
	        }
	    }
	    return true;
	}	
   
}
