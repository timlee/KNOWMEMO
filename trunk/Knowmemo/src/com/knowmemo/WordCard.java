package com.knowmemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.knowmemo.R;
import com.knowmemo.services.PictureSearch;
import com.fima.cardsui.objects.Card;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.people.User;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONException;

import learning_element.Word;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import sqllite.*;

public class WordCard extends Card implements OnInitListener {
	//private Button new_voc;
	private Button facebook_button;
	private Button box1_button;
	private Button box2_button;
	private Button box3_button;
	private Button box4_button;
	private Button box5_button;
	private TextView main_welcome_text;
	private TextView funny_thing_text;
	//private Spinner choose_box_spinner;
	private ArrayAdapter<String> adapter;
	private String[] BOOKS;
	//public static String book_want_to_learn="�춥�^���r"; // �]�w��static�O�]���n���j�a�����D�{�b�ҿ�ܪ��O���@�ӳ�r�w
	private int cards_box1_int;
	private int cards_box2_int;
	private int cards_box3_int;
	private int cards_box4_int;
	private int cards_box5_int;
	private int red_box = 1;
	SqlHelper sqlhelper;
	public static final int EXP = Menu.CATEGORY_SECONDARY;
	public static final int PERCENT = Menu.NONE;
	
	private Button button_next;
	private Button button_previous;
	private Button button_remember;
	private Button button_forget;
	private TextView text_ans;
	private TextView text_ques;
	private TextView text_word_percent;
	private ArrayList<Word> word_wordarraylist = new ArrayList<Word>();

	private int current_word = 0; // ��e����r �q0�}�l�O��
	private int total_word = 0; // �`�@����r�ƶq
	private int current_word_temp; // current_word_temp�n��current_word�h1���F��ܦb�e���Ѿl����r�ƶq
	private int total_word_temp; // �D�z�P�W
	private boolean if_next_or_previous_button_clicked = true;
	private boolean learning_over = false;
    private TextToSpeech mTts;  
    private String book_name_temp = "temp"; // book��name
    private PictureSearch flickrSearch;
    private static String TAG = "WordCard.Class";
	private LinearLayout myGallery;
	Activity myContext;
	boolean firstAnswerClick = true;

			

	public WordCard(Activity context){
		super("KNOWMEMO");
		myContext = context;
		flickrSearch = new PictureSearch();
		File dir = new File("data/data/com.knowmemo/databases");

		if (!dir.exists()) {
			dir.mkdir();
		}

		FileOutputStream fos;
		if (!(new File(SqlHelper.DB_NAME)).exists()) { // �p�G�w�g����Ʈw�F�N���A�ɤJ
			try {
				fos = new FileOutputStream(SqlHelper.DB_NAME);

				byte[] buffer = new byte[8192];
				int count = 0;
				InputStream is = context.getResources().openRawResource(R.raw.important);
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			} catch (Exception e) {
				
				e.printStackTrace();
			}

		}
		sqlhelper = new SqlHelper(context);
		
		Cursor cursor = sqlhelper.Query_BOOKS();
		cursor.moveToFirst();
		int rows_num = cursor.getCount();
		Log.e("NNNNNNNNNNNNNNNNNNNNNNNNNNN",String.valueOf(cursor.getString(0)));
		BOOKS = new String[rows_num];

		for (int i = 0; i < rows_num; i++) {
//			Word word = new Word();
			BOOKS[i] = cursor.getString(0);
			cursor.moveToNext();

			if (cursor.isLast()) {
				continue;
			}
		}
		cursor.close();
		
	}

	@Override
	public View getCardContent(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.card_word, null);

		Log.d("NNNNNNNNNNNNNNNNNNNN=  ", ""+view);
		//new_voc = (Button) view.findViewById(R.id.button_new_voc);
		//learn_button = (Button) view.findViewById(R.id.learn_button);
		//facebook_button = (Button) view.findViewById(R.id.facebook_button);
		box1_button = (Button) view.findViewById(R.id.box1_button);
		box2_button = (Button) view.findViewById(R.id.box2_button);
		box3_button = (Button) view.findViewById(R.id.box3_button);
		box4_button = (Button) view.findViewById(R.id.box4_button);
		box5_button = (Button) view.findViewById(R.id.box5_button);


		//choose_box_spinner = (Spinner) view.findViewById(R.id.main_spinner);
		mTts = new TextToSpeech(context, this);   //��l��TTS

		//facebook_button.setOnClickListener(button_listener);
//		box1_button.setOnClickListener(button_listener);
//		box2_button.setOnClickListener(button_listener);
//		box3_button.setOnClickListener(button_listener);
//		box4_button.setOnClickListener(button_listener);
//		box5_button.setOnClickListener(button_listener);	

		button_remember = (Button) view.findViewById(R.id.button_remember);
		button_forget = (Button) view.findViewById(R.id.button_forget);

		text_ques = (TextView) view.findViewById(R.id.text_ques);
		text_ans = (TextView) view.findViewById(R.id.text_ans);
		text_word_percent = (TextView) view.findViewById(R.id.text_word_percent);

		myGallery = (LinearLayout) view.findViewById(R.id.gallery);
		
		button_remember.setOnClickListener(button_listener);
		button_forget.setOnClickListener(button_listener);
		//button_dic.setOnClickListener(button_listener);
		//button_tts.setOnClickListener(button_listener);
		
		text_ans.setClickable(true);

		text_ans.setOnClickListener(new View.OnClickListener(){
//		    public void onClick(){
//		    	text_ans.setText(word_wordarraylist.get(current_word).getMeanning());
//		    	if (MainActivity.TTS_Speak == true){
//		    		mTts.speak(word_wordarraylist.get(current_word).getSpelling(), TextToSpeech.QUEUE_ADD, null);
//		    	}
//		    	if (firstAnswerClick==true){
//			    	ArrayList<String> photoURL = flickrSearch.SearchPhoto(word_wordarraylist.get(current_word).getSpelling());			
//					// show The Image
//					addPictureInGallary(photoURL);
//					firstAnswerClick = false;
//		    	}
//		    }

			@Override
			public void onClick(View arg0) {

				text_ans.setText(word_wordarraylist.get(current_word).getMeanning());
				if (MainActivity.TTS_Speak == true){
					mTts.speak(word_wordarraylist.get(current_word).getSpelling(), TextToSpeech.QUEUE_ADD, null);
				}
				if (firstAnswerClick==true){
			    	PhotoList photoList = flickrSearch.SearchFlickrPhoto(word_wordarraylist.get(current_word).getSpelling());			
					// show The Image
			    	addFlickrPhotoInGallary(photoList);
					firstAnswerClick = false;
		    	}
			}
		});
		

			book_name_temp = SqlHelper.book1;
			Cursor cursor1 = sqlhelper.Query_how_much_Words_in_box1(book_name_temp);
			cursor1.moveToFirst();
			int box1_words = cursor1.getInt(0);
			box1_button.setText(String.valueOf(box1_words));


			// cards_box1_text.setText("10");
			cards_box1_int = box1_words;
			cursor1.close();

			Cursor cursor2 = sqlhelper.Query_how_much_Words_in_box2(book_name_temp);
			cursor2.moveToFirst();
			int box2_words = cursor2.getInt(0);
			box2_button.setText(String.valueOf(box2_words));
			cards_box2_int = box2_words;
			cursor2.close();

			Cursor cursor3 = sqlhelper.Query_how_much_Words_in_box3(book_name_temp);
			cursor3.moveToFirst();
			int box3_words = cursor3.getInt(0);
			box3_button.setText(String.valueOf(box3_words));
			cards_box3_int = box3_words;
			cursor3.close();

			Cursor cursor4 = sqlhelper.Query_how_much_Words_in_box4(book_name_temp);
			cursor4.moveToFirst();
			int box4_words = cursor4.getInt(0);
			box4_button.setText(String.valueOf(box4_words));
			cards_box4_int = box4_words;
			cursor4.close();

			Cursor cursor5 = sqlhelper.Query_how_much_Words_in_box5(book_name_temp);
			cursor5.moveToFirst();
			int box5_words = cursor5.getInt(0);
			box5_button.setText(String.valueOf(box5_words));
			cards_box5_int = box5_words;
			cursor5.close();

			box1_button.setBackgroundColor(Color.LTGRAY);
			box2_button.setBackgroundColor(Color.LTGRAY);
			box3_button.setBackgroundColor(Color.LTGRAY);
			box4_button.setBackgroundColor(Color.LTGRAY);
			box5_button.setBackgroundColor(Color.LTGRAY);
			red_box = 1;
			
			if (box5_words >= Integer.parseInt(SqlHelper.box_level_5_Limit)) {
				box5_button.setBackgroundColor(Color.RED);
				red_box = 5;
			}	else if (box4_words >= Integer.parseInt(SqlHelper.box_level_4_Limit)) {
				box4_button.setBackgroundColor(Color.RED);
				red_box = 4;
			}	else if (box3_words >= Integer.parseInt(SqlHelper.box_level_3_Limit)) {
				box3_button.setBackgroundColor(Color.RED);
				red_box = 3;
			}	else if (box2_words >= Integer.parseInt(SqlHelper.box_level_2_Limit)) {
				box2_button.setBackgroundColor(Color.RED);
				red_box = 2;
			}	else {
				box1_button.setBackgroundColor(Color.RED);
			}
			
			initial_word(red_box);
						
//						funny_thing_text.setText(funny[(int) (Math.random() * 17)]); // ��r�w�ܧ󪺸ܴN��s�W���Υy

		return view;
	}
	
	private OnClickListener button_listener = new OnClickListener() {
		public void onClick(View v) {
			
			if (v == facebook_button) {

			}
			else if (v == button_next) {
				if_next_or_previous_button_clicked = true;
				current_word++;
				if (current_word > total_word) { // ����overflow�X��
					current_word--;
				}
				//box_check(Integer.parseInt(word_wordarraylist.get(current_word).getLevel())); // �]�wradiobutton
				text_ques.setText(word_wordarraylist.get(current_word).getSpelling());
				text_ans.setText(R.string.recall_word_meaning);
				word_temp_synchonize(current_word, total_word); // ���P�B��
			}			
		
			else if (v == button_previous) {
				if_next_or_previous_button_clicked = true;
				current_word--;
				if (current_word < 0) { // ����overflow�X��
					current_word = 0;
				}
				
				text_ques.setText(word_wordarraylist.get(current_word).getSpelling());
				text_ans.setText(R.string.recall_word_meaning);
				word_temp_synchonize(current_word, total_word); // ���P�B��				

			}
			else if (v == button_remember) {

			     	button_right_pressed();	
			     	myGallery.removeAllViews();
			     	if (MainActivity.TTS_Speak == true){
			     		
			     		mTts.speak(word_wordarraylist.get(current_word).getSpelling(), TextToSpeech.QUEUE_ADD, null);
			     	}
			     	firstAnswerClick = true;
			     	
			     	//ArrayList<String> photoURL = flickrSearch.SearchPhoto(word_wordarraylist.get(current_word).getSpelling());			
					// show The Image
					//new DownloadImageTask(imageWordView).execute(photoURL.get(0));
					//addPictureInGallary(photoURL);
			     	

			}
			else if (v == button_forget) {

				    button_wrong_pressed();
				    myGallery.removeAllViews();
				    if (MainActivity.TTS_Speak == true){
				    	mTts.speak(word_wordarraylist.get(current_word).getSpelling(), TextToSpeech.QUEUE_ADD, null);
				    }
				    firstAnswerClick = true;
				    //ArrayList<String> photoURL = flickrSearch.SearchPhoto(word_wordarraylist.get(current_word).getSpelling());			
					// show The Image
					//new DownloadImageTask(imageWordView).execute(photoURL.get(0));
					//addPictureInGallary(photoURL);

			}

		}
	};
	
	
	public boolean  initial_word(int level) {
		boolean is_empty = false;
		Log.d("######initial_word ", " LEVEL =  "+level);
		String temp = "temp";
//		if (book_want_to_learn.equals("�춥�^���r")) {
			temp = SqlHelper.book1;
//		}
//		if (book_want_to_learn.equals("�����^���r")) {
//			temp = SqlHelper.book2;
//		}
//		if (book_want_to_learn.equals("�i���^���r")) {
//			temp = SqlHelper.book3;
//		}
		current_word = 0; // ��e����r �q0�}�l�O��
		total_word = 0; // �`�@����r�ƶq
		
		switch (level){
		
		case 1: {
			if (sqlhelper.Query_Words_box1_is_empty(temp) == true){
				  is_empty = true;
				  sqlhelper.Put_Words_in_Box1(temp);
			  }
			Cursor cursor = sqlhelper.Query_Words_box1(temp); // ���ഫ�L�᪺��T�ǵ�sqlhelper.Query_Words
			cursor.moveToFirst();
			word_wordarraylist.clear();
			int rows_num = cursor.getCount();
			for (int i = 0; i < rows_num; i++) {
				Word word = new Word();
				word.setID(cursor.getString(0));
				word.setSpelling(cursor.getString(1));
				word.setMeanning(cursor.getString(2));
				word.setPhonetic_alphabet(cursor.getString(3));
				word.setLevel(cursor.getString(4));
				word.setLearned(cursor.getString(5));
				word.setRemenber(cursor.getString(6));
				word_wordarraylist.add(word);
				cursor.moveToNext();							  
				
				if (cursor.isLast()) {
					total_word = word_wordarraylist.size();
					continue;
				} 					
			}
			Log.e("NNNNNNNNNNNNN", "BOX Level = 1, total word = "+total_word+ ", rows_num= "+rows_num);
			cursor.close();
			break;
		}
		case 2:{
			Cursor cursor = sqlhelper.Query_Words_box2(temp); // ���ഫ�L�᪺��T�ǵ�sqlhelper.Query_Words
			cursor.moveToFirst();
			word_wordarraylist.clear();
			int rows_num = cursor.getCount();
			for (int i = 0; i < rows_num; i++) {
				Word word = new Word();
				word.setID(cursor.getString(0));
				word.setSpelling(cursor.getString(1));
				word.setMeanning(cursor.getString(2));
				word.setPhonetic_alphabet(cursor.getString(3));
				word.setLevel(cursor.getString(4));
				word.setLearned(cursor.getString(5));
				word.setRemenber(cursor.getString(6));
				word_wordarraylist.add(word);
				cursor.moveToNext();							  
				
				if (cursor.isLast()) {
					total_word = word_wordarraylist.size();
					continue;
				} 
			}
			Log.e("NNNNNNNNNNNNN", "BOX Level = 2, total word = "+total_word+ ", rows_num= "+rows_num);
			cursor.close();
			break;
		}
		case 3:{
			Cursor cursor = sqlhelper.Query_Words_box3(temp); // ���ഫ�L�᪺��T�ǵ�sqlhelper.Query_Words
			cursor.moveToFirst();
			word_wordarraylist.clear();
			int rows_num = cursor.getCount();
			for (int i = 0; i < rows_num; i++) {
				Word word = new Word();
				word.setID(cursor.getString(0));
				word.setSpelling(cursor.getString(1));
				word.setMeanning(cursor.getString(2));
				word.setPhonetic_alphabet(cursor.getString(3));
				word.setLevel(cursor.getString(4));
				word.setLearned(cursor.getString(5));
				word.setRemenber(cursor.getString(6));
				word_wordarraylist.add(word);
				cursor.moveToNext();							  
				
				if (cursor.isLast()) {
					total_word = word_wordarraylist.size();
					continue;
				} 
			}
			Log.e("NNNNNNNNNNNNN", "BOX Level = 3, total word = "+total_word);
			cursor.close();
			break;
		}
		
		case 4:{
			Cursor cursor = sqlhelper.Query_Words_box4(temp); // ���ഫ�L�᪺��T�ǵ�sqlhelper.Query_Words
			cursor.moveToFirst();
			word_wordarraylist.clear();
			int rows_num = cursor.getCount();
			for (int i = 0; i < rows_num; i++) {
				Word word = new Word();
				word.setID(cursor.getString(0));
				word.setSpelling(cursor.getString(1));
				word.setMeanning(cursor.getString(2));
				word.setPhonetic_alphabet(cursor.getString(3));
				word.setLevel(cursor.getString(4));
				word.setLearned(cursor.getString(5));
				word.setRemenber(cursor.getString(6));
				word_wordarraylist.add(word);
				cursor.moveToNext();							  
				
				if (cursor.isLast()) {
					total_word = word_wordarraylist.size();
					continue;
				} 
			}
			Log.e("NNNNNNNNNNNNN", "BOX Level = 4, total word = "+total_word);
			cursor.close();
			break;
		}
		
		case 5:{
			Cursor cursor = sqlhelper.Query_Words_box5(temp); // ���ഫ�L�᪺��T�ǵ�sqlhelper.Query_Words
			cursor.moveToFirst();
			word_wordarraylist.clear();
			int rows_num = cursor.getCount();
			for (int i = 0; i < rows_num; i++) {
				Word word = new Word();
				word.setID(cursor.getString(0));
				word.setSpelling(cursor.getString(1));
				word.setMeanning(cursor.getString(2));
				word.setPhonetic_alphabet(cursor.getString(3));
				word.setLevel(cursor.getString(4));
				word.setLearned(cursor.getString(5));
				word.setRemenber(cursor.getString(6));
				word_wordarraylist.add(word);
				cursor.moveToNext();							  
				
				if (cursor.isLast()) {
					total_word = word_wordarraylist.size();
					continue;
				} 
			}
			Log.e("NNNNNNNNNNNNN", "BOX Level = 5, total word = "+total_word);
			cursor.close();
			break;
		}
		
		}
		
		word_temp_synchonize(current_word, total_word); // �i��P�B��
		text_ques.setText(word_wordarraylist.get(current_word).getSpelling());
		text_ans.setText(R.string.recall_word_meaning);
		//mTts.speak(word_wordarraylist.get(current_word).getSpelling(), TextToSpeech.QUEUE_ADD, null);
		//ArrayList<String> photoURL = flickrSearch.SearchPhoto(word_wordarraylist.get(current_word).getSpelling());
		
		// show The Image
		//new DownloadImageTask(imageWordView).execute(photoURL.get(0));
		//addPictureInGallary(photoURL);
		
		//imageWordView.setImageBitmap(loadBitmap(photoURL));
		
		
		
		return is_empty;
	}
	
	public void addPictureInGallary(ArrayList<String> photoURL){
		
		myGallery.removeAllViews();
		if (photoURL != null){
	        for (String imageName : photoURL) {
				ImageView imageView = new ImageView(myContext);
			    imageView.setLayoutParams(new ViewGroup.LayoutParams(120, 120));
			    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			    imageView.setTag(imageName);
				
				new DownloadImageTask(imageView).execute(imageName);
	//		    final String imageurl = imageName;
			    
			    imageView.setOnClickListener(new View.OnClickListener() {
			        @Override
			        public void onClick(View view) { 
	//		        	imageView.getTag();
	//		        	Toast.makeText(WordCard.this, myGallery.get.getTag(), Toast.LENGTH_SHORT).show();
			        }
			    });
			    myGallery.addView(imageView);
			}
		}
		
	}
	
public void addFlickrPhotoInGallary(PhotoList photoList){
		
		myGallery.removeAllViews();
		
		if (photoList != null){
			
			for (int i = 0 ; i< photoList.size(); i++){
				 //photo = photoList.get(i);
				String photoURL = photoList.get(i).getSmallUrl();		
				ImageView imageView = new ImageView(myContext);
			    imageView.setLayoutParams(new ViewGroup.LayoutParams(120, 120));
			    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			    imageView.setTag(photoURL);
			    imageView.setTag(R.id.photo_tag, photoList.get(i));
				
				new DownloadImageTask(imageView).execute(photoURL);	 
			    imageView.setOnClickListener(new View.OnClickListener() {
			        @Override
			        public void onClick(View view) {
			        	Photo photo = (Photo) view.getTag(R.id.photo_tag);

			        	Log.d(TAG, photo.toString());
			        	Log.d(TAG, photo.getUrl());
			        	Log.d(TAG, photo.getOwner().toString());
			        	Log.d(TAG, photo.getOwner().getId());
			        	try {
							String username = flickrSearch.getFlickr().getPeopleInterface().getInfo(photo.getOwner().getId()).getUsername();
							String realname = flickrSearch.getFlickr().getPeopleInterface().getInfo(photo.getOwner().getId()).getRealName();
							if (username!=null) Log.d(TAG, username);
							if (realname!=null) Log.d(TAG, realname);
							String message = "Author user name: "+username+", author real name: "+realname;
//							Log.d(TAG, message);
//							Toast.makeText(WordCard.this,message , Toast.LENGTH_SHORT).show();
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (FlickrException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        	
			        	//Log.d(TAG, photo.getOwner().getRealName());
			        	//Log.d(TAG, photo);
			        	//Toast.makeText(WordCard.this, photo.getOwner().getUsername(), Toast.LENGTH_SHORT).show();
			        }
			    });
			    myGallery.addView(imageView);
			}
		}
		
	}
	
	
	public String getCurrentWord(){
		return word_wordarraylist.get(current_word).getSpelling();
	}
 
	public Bitmap loadBitmap(String url) {
	
	    Bitmap bitmap = null;
 
	    try {
	    	bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
	    } catch (IOException e) {
	        Log.e(TAG, "Could not load Bitmap from: " + url);
	    } finally {

	    }

	    return bitmap;
	}
	
		//�Ncurrent_word_temp total_word_temp �P�B
		private void word_temp_synchonize(int c_word, int t_word) {
			current_word_temp = c_word + 1;
			total_word_temp = t_word + 1;
			text_word_percent.setText(String.valueOf(current_word_temp) + " / "	+ String.valueOf(total_word_temp)); // ��ƭȳ]�w��text_word_percent
		}

		private void word_already_learned(String word_id) { // ��update_word_learned�i��i�@�B���]��

			String word_id_temp = word_id;
			String book_name_temp = "temp"; // book��name
//			if (book_want_to_learn.equals("�춥�^���r")) {

				book_name_temp = SqlHelper.book1;
//			}
//			if (book_want_to_learn.equals("�����^���r")) {
//				book_name_temp = SqlHelper.book2;
//			}
//			if (book_want_to_learn.equals("�i���^���r")) {
//				book_name_temp = SqlHelper.book3;
//			}

			sqlhelper.update_word_learned(book_name_temp, word_id_temp);
		}
		
		private void word_level_update(String word_id,String operation){    //�P�_word level update�n������

			String word_id_temp = word_id;
			
			int level = Integer.parseInt(word_wordarraylist.get(current_word).getLevel());
			
			if (if_next_or_previous_button_clicked == true) {
				try {
//					if (book_want_to_learn.equals("�춥�^���r")) {
						book_name_temp = SqlHelper.book1;
//					}
//					if (book_want_to_learn.equals("�����^���r")) {
//						book_name_temp = SqlHelper.book2;
//					}
//					if (book_want_to_learn.equals("�i���^���r")) {
//						book_name_temp = SqlHelper.book3;
//					}
					if(operation.equals("right")){
						if(level == 5){ 
							sqlhelper.increase_word_level(book_name_temp, word_id_temp);
							int temp = level++;
							String level_temp = String.valueOf(temp);
							word_wordarraylist.get(current_word).setLevel(level_temp);
							Log.e("word_level_update execption", word_wordarraylist.get(current_word).getLevel());
	
						}
						else if(level < 5){
							sqlhelper.increase_word_level(book_name_temp, word_id_temp);
							int temp = level++;
							String level_temp = String.valueOf(temp);
							word_wordarraylist.get(current_word).setLevel(level_temp);
							Log.e("word_level_update execption", word_wordarraylist.get(current_word).getLevel());
						}
					}
			        if(operation.equals("wrong")){
			        	if(level==1){
							/*sqlhelper.no_change_word_level(book_name_temp, word_id_temp);
							int temp = level;
							String level_temp = String.valueOf(temp);
							word_wordarraylist.get(current_word).setLevel("level_temp");*/
						}
			        	else if(level > 1){
			        		sqlhelper.decrease_word_level(book_name_temp, word_id_temp);
			        		int temp = level--;
							String level_temp = String.valueOf(temp);
							word_wordarraylist.get(current_word).setLevel(level_temp);
							Log.e("word_level_update execption", word_wordarraylist.get(current_word).getLevel());
			        	}
					}
					
				} catch (Exception e) {
					Log.e("word_level_update execption", e.toString());
				}
			}
			if_next_or_previous_button_clicked = false;		
		}
		
		public void button_right_pressed() {
			Log.e("#######  button_right_pressed #######", "learning_over= "+ learning_over+" , current_word= "+current_word_temp+ ", word.size= "+word_wordarraylist.size());
	
				//�o�Ӯ�l����r�ǧ��F
				if (current_word_temp == word_wordarraylist.size()) {
					learning_over_dialog();
					learning_over = true;
				}
				int level = Integer.parseInt(word_wordarraylist.get(current_word).getLevel());
				word_already_learned(word_wordarraylist.get(current_word).getID());
				String word_id = String.valueOf(word_wordarraylist.get(current_word).getID());
				
				//increase word level
				word_level_update(word_id, "right");				
				
				if_next_or_previous_button_clicked = true;
				current_word++;
			
				//�P�_�U�Ӯ�l�O�_���F
				switch (level) {

				case 1: 	{
					int temp1 = sqlhelper.Query_word_numbers_in_box(book_name_temp, "1");
					int temp = sqlhelper.Query_word_numbers_in_box(book_name_temp, "2");
					box2_button.setText(String.valueOf(temp));
					
					if (temp >= Integer.parseInt(SqlHelper.box_level_2_Limit)){
						box1_button.setBackgroundColor(Color.LTGRAY);
						box2_button.setBackgroundColor(Color.RED);
						box3_button.setBackgroundColor(Color.LTGRAY);
						box4_button.setBackgroundColor(Color.LTGRAY);
						box5_button.setBackgroundColor(Color.LTGRAY);						
						initial_word(2);
					} else if (temp1 ==0 || temp1 >= Integer.parseInt(SqlHelper.box_level_1_Limit)||learning_over==true){
						box1_button.setBackgroundColor(Color.RED);
						box2_button.setBackgroundColor(Color.LTGRAY);
						box3_button.setBackgroundColor(Color.LTGRAY);
						box4_button.setBackgroundColor(Color.LTGRAY);
						box5_button.setBackgroundColor(Color.LTGRAY);							
						initial_word(1);
						temp1 = sqlhelper.Query_word_numbers_in_box(book_name_temp, "1");
						}					
					box1_button.setText(String.valueOf(temp1));	
					
					break;
				}
				case 2: 	{
					int temp2 = sqlhelper.Query_word_numbers_in_box(book_name_temp, "2");
					int temp = sqlhelper.Query_word_numbers_in_box(book_name_temp, "3");
					
					if (temp >= Integer.parseInt(SqlHelper.box_level_3_Limit)){
						box1_button.setBackgroundColor(Color.LTGRAY);
						box2_button.setBackgroundColor(Color.LTGRAY);
						box3_button.setBackgroundColor(Color.RED);
						box4_button.setBackgroundColor(Color.LTGRAY);
						box5_button.setBackgroundColor(Color.LTGRAY);						
						initial_word(3);
					}else if (temp2 ==0 || learning_over==true){
						box1_button.setBackgroundColor(Color.RED);
						box2_button.setBackgroundColor(Color.LTGRAY);
						box3_button.setBackgroundColor(Color.LTGRAY);
						box4_button.setBackgroundColor(Color.LTGRAY);
						box5_button.setBackgroundColor(Color.LTGRAY);							
						initial_word(1);
						
					}	
					int temp1 = sqlhelper.Query_word_numbers_in_box(book_name_temp, "1");
					box1_button.setText(String.valueOf(temp1));	
					box2_button.setText(String.valueOf(temp2));
					box3_button.setText(String.valueOf(temp));					

					break;
				}
				case 3: 	{
					int temp3 = sqlhelper.Query_word_numbers_in_box(book_name_temp, "3");
					int temp = sqlhelper.Query_word_numbers_in_box(book_name_temp, "4");
					
					if (temp >= Integer.parseInt(SqlHelper.box_level_4_Limit)){
						box1_button.setBackgroundColor(Color.LTGRAY);
						box2_button.setBackgroundColor(Color.LTGRAY);
						box3_button.setBackgroundColor(Color.LTGRAY);
						box4_button.setBackgroundColor(Color.RED);
						box5_button.setBackgroundColor(Color.LTGRAY);
						initial_word(4);
					}else if (temp3 ==0 || learning_over==true){
						box1_button.setBackgroundColor(Color.RED);
						box2_button.setBackgroundColor(Color.LTGRAY);
						box3_button.setBackgroundColor(Color.LTGRAY);
						box4_button.setBackgroundColor(Color.LTGRAY);
						box5_button.setBackgroundColor(Color.LTGRAY);							
						initial_word(1);
						
					}	
					int temp1 = sqlhelper.Query_word_numbers_in_box(book_name_temp, "1");
					box1_button.setText(String.valueOf(temp1));	
					box4_button.setText(String.valueOf(temp));
					box3_button.setText(String.valueOf(temp3));
					break;
				}
				case 4: 	{
					int temp = sqlhelper.Query_word_numbers_in_box(book_name_temp, "5");
					int temp4 = sqlhelper.Query_word_numbers_in_box(book_name_temp, "4");		
					
					if (temp >= Integer.parseInt(SqlHelper.box_level_5_Limit)){
						box1_button.setBackgroundColor(Color.LTGRAY);
						box2_button.setBackgroundColor(Color.LTGRAY);
						box3_button.setBackgroundColor(Color.LTGRAY);
						box4_button.setBackgroundColor(Color.LTGRAY);
						box5_button.setBackgroundColor(Color.RED);						
						initial_word(5);						
					}	else if (temp4 ==0 || learning_over==true){
						box1_button.setBackgroundColor(Color.RED);
						box2_button.setBackgroundColor(Color.LTGRAY);
						box3_button.setBackgroundColor(Color.LTGRAY);
						box4_button.setBackgroundColor(Color.LTGRAY);
						box5_button.setBackgroundColor(Color.LTGRAY);						
						initial_word(1);						
					}	
					int temp1 = sqlhelper.Query_word_numbers_in_box(book_name_temp, "1");
					box1_button.setText(String.valueOf(temp1));				
					box5_button.setText(String.valueOf(temp));
					box4_button.setText(String.valueOf(temp4));
					break;
				}
				case 5: 	{
					int temp = sqlhelper.Query_word_numbers_in_box(book_name_temp, "5");
					box5_button.setText(String.valueOf(temp));
					if (temp==0 || learning_over==true){						
						initial_word(1);		
						box1_button.setBackgroundColor(Color.RED);
						box2_button.setBackgroundColor(Color.LTGRAY);
						box3_button.setBackgroundColor(Color.LTGRAY);
						box4_button.setBackgroundColor(Color.LTGRAY);
						box5_button.setBackgroundColor(Color.LTGRAY);												
					}	
					int temp1 = sqlhelper.Query_word_numbers_in_box(book_name_temp, "1");
					box1_button.setText(String.valueOf(temp1));	
			
					break;
				}
			}				
		
				text_ques.setText(word_wordarraylist.get(current_word).getSpelling());
				text_ans.setText(R.string.recall_word_meaning);
				word_temp_synchonize(current_word, total_word); // �P�B��
				
				sqlhelper.increase_experience_1(); //�W�[�g���
			
			learning_over=false;

		}

		public void button_wrong_pressed() {

				if (current_word_temp == word_wordarraylist.size()) {
					learning_over_dialog();
					learning_over = true;
				}
			

			word_already_learned(word_wordarraylist.get(current_word).getID());
			String word_id = String.valueOf(word_wordarraylist.get(current_word).getID());
			word_level_update(word_id, "wrong");

			
			if_next_or_previous_button_clicked = true;
			current_word++;
			if (current_word > total_word) { // ����overflow�X��
				current_word--;
			}

			
			int level = Integer.parseInt(word_wordarraylist.get(current_word).getLevel());
			
			//�P�_�e�@�Ӯ�l�O�_���F�ΪŤF
			switch (level) {
			case 1: 	{
				int temp = sqlhelper.Query_word_numbers_in_box(book_name_temp, "1");
				
				if (temp==0 || learning_over==true){						
					initial_word(1);
					current_word = 0;
					current_word_temp = current_word+1;					
				}
				int temp2 = sqlhelper.Query_word_numbers_in_box(book_name_temp, "2");
				temp = sqlhelper.Query_word_numbers_in_box(book_name_temp, "1");
				box1_button.setText(String.valueOf(temp));
				box2_button.setText(String.valueOf(temp2));
				break;
			}
			case 2: 	{
				int temp2 = sqlhelper.Query_word_numbers_in_box(book_name_temp, "2");
				int temp = sqlhelper.Query_word_numbers_in_box(book_name_temp, "1");
								
				if (temp >= Integer.parseInt(SqlHelper.box_level_1_Limit)){
					box1_button.setBackgroundColor(Color.RED);
					box2_button.setBackgroundColor(Color.LTGRAY);
					box3_button.setBackgroundColor(Color.LTGRAY);
					box4_button.setBackgroundColor(Color.LTGRAY);
					box5_button.setBackgroundColor(Color.LTGRAY);							
					initial_word(1);
					
				} else if (temp2==0 || learning_over==true ){
					box1_button.setBackgroundColor(Color.RED);
					box2_button.setBackgroundColor(Color.LTGRAY);
					box3_button.setBackgroundColor(Color.LTGRAY);
					box4_button.setBackgroundColor(Color.LTGRAY);
					box5_button.setBackgroundColor(Color.LTGRAY);							
					initial_word(1);
				}
				int temp1 = sqlhelper.Query_word_numbers_in_box(book_name_temp, "1");
				if (temp1 ==0) box_empty_dialog();
				box1_button.setText(String.valueOf(temp1));	
				temp2 = sqlhelper.Query_word_numbers_in_box(book_name_temp, "2");
				box2_button.setText(String.valueOf(temp2));

				break;
			}
			case 3: 	{
				int temp3 = sqlhelper.Query_word_numbers_in_box(book_name_temp, "3");
				int temp = sqlhelper.Query_word_numbers_in_box(book_name_temp, "2");				
				
				if (temp >= Integer.parseInt(SqlHelper.box_level_2_Limit)){
					box1_button.setBackgroundColor(Color.LTGRAY);
					box2_button.setBackgroundColor(Color.RED);
					box3_button.setBackgroundColor(Color.LTGRAY);
					box4_button.setBackgroundColor(Color.LTGRAY);
					box5_button.setBackgroundColor(Color.LTGRAY);							
					initial_word(2);
					
				} else if (temp3==0 || learning_over==true){
					box1_button.setBackgroundColor(Color.RED);
					box2_button.setBackgroundColor(Color.LTGRAY);
					box3_button.setBackgroundColor(Color.LTGRAY);
					box4_button.setBackgroundColor(Color.LTGRAY);
					box5_button.setBackgroundColor(Color.LTGRAY);							
					initial_word(1);
					int temp1 = sqlhelper.Query_word_numbers_in_box(book_name_temp, "1");
					if (temp1 ==0) box_empty_dialog();
					box1_button.setText(String.valueOf(temp1));	
				}

				box2_button.setText(String.valueOf(temp));
				box3_button.setText(String.valueOf(temp3));
				break;
			}
			case 4: 	{
				int temp = sqlhelper.Query_word_numbers_in_box(book_name_temp, "3");
				int temp4 = sqlhelper.Query_word_numbers_in_box(book_name_temp, "4");
					
				
				if (temp >= Integer.parseInt(SqlHelper.box_level_3_Limit)){
					box1_button.setBackgroundColor(Color.LTGRAY);
					box2_button.setBackgroundColor(Color.LTGRAY);
					box3_button.setBackgroundColor(Color.RED);
					box4_button.setBackgroundColor(Color.LTGRAY);
					box5_button.setBackgroundColor(Color.LTGRAY);						
					initial_word(3);		
					
				}	else if (temp4==0 || learning_over==true){
					box1_button.setBackgroundColor(Color.RED);
					box2_button.setBackgroundColor(Color.LTGRAY);
					box3_button.setBackgroundColor(Color.LTGRAY);
					box4_button.setBackgroundColor(Color.LTGRAY);
					box5_button.setBackgroundColor(Color.LTGRAY);						
					initial_word(1);
					int temp1 = sqlhelper.Query_word_numbers_in_box(book_name_temp, "1");
					if (temp1 ==0) box_empty_dialog();
					box1_button.setText(String.valueOf(temp1));	
				}
				
				box3_button.setText(String.valueOf(temp));
				box4_button.setText(String.valueOf(temp4));			
				break;
			}
			case 5: 	{
				int temp = sqlhelper.Query_word_numbers_in_box(book_name_temp, "4");
				int temp5 = sqlhelper.Query_word_numbers_in_box(book_name_temp, "5");
						
				
				if (temp >= Integer.parseInt(SqlHelper.box_level_4_Limit)){
					box1_button.setBackgroundColor(Color.LTGRAY);
					box2_button.setBackgroundColor(Color.LTGRAY);
					box3_button.setBackgroundColor(Color.LTGRAY);
					box4_button.setBackgroundColor(Color.RED);
					box5_button.setBackgroundColor(Color.LTGRAY);						
					initial_word(4);					
				}else if (temp5==0 || learning_over==true){
					box1_button.setBackgroundColor(Color.RED);
					box2_button.setBackgroundColor(Color.LTGRAY);
					box3_button.setBackgroundColor(Color.LTGRAY);
					box4_button.setBackgroundColor(Color.LTGRAY);
					box5_button.setBackgroundColor(Color.LTGRAY);						
					initial_word(1);
					int temp1 = sqlhelper.Query_word_numbers_in_box(book_name_temp, "1");
					if (temp1 ==0) box_empty_dialog();
					box1_button.setText(String.valueOf(temp1));	
				}
				box4_button.setText(String.valueOf(temp));
				box5_button.setText(String.valueOf(temp5));		
				break;
				}	
			}	
			text_ques.setText(word_wordarraylist.get(current_word).getSpelling());
			text_ans.setText(R.string.recall_word_meaning);
			word_temp_synchonize(current_word, total_word); // �P�B��

			learning_over=false;
		}


		private void learning_over_dialog() {
			
			Toast.makeText(myContext, R.string.finish_phase_learning , Toast.LENGTH_SHORT).show();

		}
		
		//Todo
		private void box_empty_dialog() {
			AlertDialog.Builder dialog = new AlertDialog.Builder(myContext);
			dialog.setTitle("���d���c����");
			dialog.setMessage("�I�����s�^��D���");
			dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialoginterface, int i) {
					
				}
			});
			dialog.show();
		}
		
		public void calculate_how_much_complete() {
			float cards_box1_float = Float.parseFloat(String.valueOf(cards_box1_int));
			float cards_box2_float = Float.parseFloat(String.valueOf(cards_box2_int));
			float cards_box3_float = Float.parseFloat(String.valueOf(cards_box3_int));
			float cards_box4_float = Float.parseFloat(String.valueOf(cards_box4_int));
			float cards_box5_float = Float.parseFloat(String.valueOf(cards_box5_int));
			float complete_percent = cards_box5_float
					/ (cards_box1_float + cards_box2_float + cards_box3_float
							+ cards_box4_float + cards_box5_float);
			complete_percent = complete_percent * 100;
			String text_complete = "";

			if (String.valueOf(complete_percent).length() <= 3) {
				text_complete = String.valueOf(complete_percent).substring(0, 3);
			}
			if (String.valueOf(complete_percent).length() > 3) {
				text_complete = String.valueOf(complete_percent).substring(0, 4);
			}

			String encouragement_message ="";
			if (complete_percent >= 0 && complete_percent <= 20) {
				encouragement_message = "�ǲߤ~��}�l�A�n�����H��@!!";
			}
			if (complete_percent > 20 && complete_percent <= 40) {
				encouragement_message = "�ڥX�F�Ĥ@�B�A�n���}�l�O���\���@�b!!";
			}
			if (complete_percent > 40 && complete_percent <= 60) {
				encouragement_message = "�ӼF�`�F�A�ǲߧ��@�b�o!!";
			}
			if (complete_percent > 60 && complete_percent <= 80) {
				encouragement_message = "��Ǩ�o�̤w�g���@�w����O�F�A�~��[�o";
			}
			if (complete_percent > 80 && complete_percent <= 99) {
				encouragement_message = "�Z�����\���ؼдN�b�����B�A�[�o�I";
			}
			if (complete_percent == 100) {
				encouragement_message = "�w�g���\���N��r�c�c���O�b�����F!!";
			}
			
			AlertDialog.Builder dialog = new AlertDialog.Builder(myContext);
			dialog.setTitle("�ثe������ " + text_complete + " %");
			dialog.setMessage(encouragement_message);
			dialog.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialoginterface, int i) {		
					
	
				}
			});
			
			dialog.setNegativeButton(R.string.post_to_facebook, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialoginterface, int i) {
					
					String facebook_message ="";
					for (int j = 0 ; j <= total_word; j++) {
					facebook_message = facebook_message + word_wordarraylist.get(j).getSpelling() +"    "+word_wordarraylist.get(j).getMeanning()+"    " ;
					}
							
//					Intent intent = new Intent();
//					Bundle bundle = new Bundle();
//					bundle.putString("facebook_message",facebook_message); 
//					intent.putExtras(bundle);
//					intent.setClass(Main.this, FB.class);
//					startActivity(intent);

				}
			});
			dialog.show();

//			complete_percent_text.setText("�ثe������ " + text_complete + " %");
			
			// encouragement_text
			Log.e("�ƭȬO�h��", String.valueOf(cards_box1_float));
		}


		@Override
		public void onInit(int status) {  //�]�� TTS �nimplements OnInitListener �ҥH�n���o��

			if(status != TextToSpeech.ERROR){
				mTts.setLanguage(Locale.US);
		    }
			
		}
		
		
		private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		    ImageView bmImage;

		    public DownloadImageTask(ImageView bmImage) {
		        this.bmImage = bmImage;
		    }

		    protected Bitmap doInBackground(String... urls) {
		        String urldisplay = urls[0];
		        Bitmap mIcon11 = null;
		        try {
		            InputStream in = new java.net.URL(urldisplay).openStream();
		            mIcon11 = BitmapFactory.decodeStream(in);
		        } catch (Exception e) {
		            Log.e("Error", e.getMessage());
		            e.printStackTrace();
		        }
		        return mIcon11;
		    }

		    protected void onPostExecute(Bitmap result) {
		        bmImage.setImageBitmap(result);
		    }
		}


		@Override
		public boolean convert(View convertCardView) {
			// TODO Auto-generated method stub
			return false;
		}
	
}
