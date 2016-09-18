package com.knowmemo;

import sqllite.SqlHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Insert_Cards extends Activity {

	private Button sentlala;
	private Button cancel;
	private TextView spelling;
	private TextView meaning;
	private TextView fain;
	private TextView noti;
	private SqlHelper sqlhelper;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.insert_cards);
		sqlhelper = new SqlHelper(this);
		sqlhelper.open();
		initWidgets();
	}

	private void initWidgets() {

		//Bundle bundle = this.getIntent().getExtras();
		sentlala = (Button) findViewById(R.id.sentlala);
		cancel = (Button) findViewById(R.id.button_cancel);
		spelling = (TextView) findViewById(R.id.spelling);
		meaning = (TextView) findViewById(R.id.meaning);
		fain = (TextView) findViewById(R.id.fain);
		noti= (TextView) findViewById(R.id.noti);

		sentlala.setOnClickListener(button_listener);
		cancel.setOnClickListener(button_listener);
	}

	private OnClickListener button_listener = new OnClickListener() {
		public void onClick(View v) {
 
			if (v == sentlala) {  
				
				String S_spelling = spelling.getText().toString();
				String S_meanig= meaning.getText().toString();
				String S_fain= fain.getText().toString();
				noti.setText(R.string.add_a_word+spelling.getText().toString());
				sqlhelper.insert_cards(SqlHelper.book1, S_spelling, S_meanig, S_fain);
				spelling.setText("");
				meaning.setText("");
				
			}
			else if (v == cancel) {  
				
				Intent intent = new Intent();
				intent.setClass(Insert_Cards.this, MainActivity.class);
				startActivity(intent);
				
			}

		}
	};

}
