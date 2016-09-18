package com.knowmemo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.knowmemo.R;
import com.fima.cardsui.objects.CardStack;
import com.fima.cardsui.views.CardUI;

public class MainActivity extends Activity {

	private CardUI mCardView;
	WordCard wordCard;
	public static final int DICTIONARY = Menu.FIRST;
	public static final int New_Word = Menu.CATEGORY_SECONDARY;
	public static boolean TTS_Speak = true;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		return super.onCreateOptionsMenu(menu);
		//return true;
	}
	
	

	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
			case 0:{
				
				String myURL = "http://tw.dictionary.yahoo.com/dictionary?p="+this.wordCard.getCurrentWord(); 
				Uri uri = Uri.parse(myURL);
				Intent it = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(it);
				
				break;
			}
			case 1 : {   

				Intent intent = new Intent();
//				Bundle bundle = new Bundle();
//				bundle.putString("book_want_to_insert", book_want_to_learn); // 在這裡要把所選擇的辭庫傳到learning
//				intent.putExtras(bundle);
				intent.setClass(this, Insert_Cards.class);
				this.startActivityForResult(intent, 0);
				
				break;
			}
			
			case 2 :{
				
				
				break;
			}
			
			case 3 :{
				TTS_Speak = false;
				//Toast.makeText(this, "TTS_Speak = "+TTS_Speak, Toast.LENGTH_SHORT).show();
				break;
			}
			case 4 :{
				TTS_Speak = true;
				//Toast.makeText(this, "TTS_Speak = "+TTS_Speak, Toast.LENGTH_SHORT).show();
				break;
			}
		}
		
		return super.onOptionsItemSelected(item);
	}
}
