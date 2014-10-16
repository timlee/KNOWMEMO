package sqllite;
 
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;

import learning_element.Word;

import android.R.string;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class SqlHelper { 
	public static String book1 = "book1";
	public static String book2 = "book2";
	public static String book3 = "book3";
	public static String box_level_1_Limit = "10";
	public static String box_level_2_Limit = "20";
	public static String box_level_3_Limit = "40";
	public static String box_level_4_Limit = "80";
	public static String box_level_5_Limit = "160";
	public static String BOOKLIST_TABLE = "BOOKS";
	public static final String DB_NAME = "data/data/com.knowmemo/databases/important.db";
	
	private static final String DATABASE_NAME = "important.db";	//資料庫名稱
	private static final int DATABASE_VERSION = 2;	//資料庫版本
	private static final String TAG = "SqlHelper";  
	private SQLiteDatabase db;
	private Context mContext;
	private DatabaseHelper mDatabaseHelper;
	
    /* 取得Context */
    public SqlHelper(Context context){

         mContext = context;
         try{
  		   mDatabaseHelper = new DatabaseHelper(mContext);
  	       db = mDatabaseHelper.getWritableDatabase();
  	    } catch (Exception ex) {
  	        ex.printStackTrace();
  	    }
    }
 
    public void open() throws SQLException{
       
    }

    public void close(){

         mDatabaseHelper.close();
    }
    
       
    public boolean insert_cards(String book_name,String spelling,String meaning,String fain) {

		try {			
			
			Cursor cursor1 = Query_how_much_Words_in_box1(book_name);
			cursor1.moveToFirst();
			int words_number = cursor1.getInt(0);
			Log.e("現有單字量", String.valueOf(words_number));
			
			Calendar c = Calendar.getInstance(); 
			long milliseconds = c.getTimeInMillis();
			
			String sql_statement = "INSERT INTO " + book_name  + " (SPELLING, MEANNING, LEVEL, LAST_LEARNT_TIME)"
			+"VALUES" +"(" + "'"+spelling +"'" + "," + "'"+meaning +"'" + "," + "'0'"+ "," + milliseconds +")";
			Log.e("sql_statement", sql_statement);
			db.execSQL(sql_statement );
			return true;
 
		} catch (Exception e) {
			e.getStackTrace();
		}
		
		return false;

	}
	
	public Cursor Query_exp() {

		try {
			String querry = "";
			querry = "select  experience from exp where exp_id = '0001' ";
			Cursor cursor = db.rawQuery(querry, null);
			return cursor;
		} catch (Exception e) {
			e.getStackTrace();
		}

		return null;

	}
	
	
	public void increase_experience_1( ){ //卡片箱等級不變 也就是已經到第6級 完全會了

		try {
			
			db.execSQL("UPDATE exp SET experience = experience+1 WHERE exp_id = '0001'" );
			//UPDATE book1 SET LEVEL = LEVEL+1 WHERE ID = 1
			Log.e("YES", "change_exp");
		} catch (Exception e) { 
			Log.e("NO",e.toString());

		}			
	}
	
	public void increase_experience_2( ){ //卡片箱等級不變 也就是已經到第6級 完全會了

		try {
			
			db.execSQL("UPDATE exp SET experience = experience+2 WHERE exp_id = '0001'" );
			//UPDATE book1 SET LEVEL = LEVEL+1 WHERE ID = 1
			Log.e("YES", "change_exp");
		} catch (Exception e) { 
			Log.e("NO",e.toString());

		}			
	}
	
	public void increase_experience_5( ){ //卡片箱等級不變 也就是已經到第6級 完全會了

		try {
			
			db.execSQL("UPDATE exp SET experience = experience+5 WHERE exp_id = '0001'" );
			//UPDATE book1 SET LEVEL = LEVEL+1 WHERE ID = 1
			Log.e("YES", "change_exp");
		} catch (Exception e) { 
			Log.e("NO",e.toString());

		}			
	}
	
	public void increase_experience_8( ){ //卡片箱等級不變 也就是已經到第6級 完全會了

		try {
			
			db.execSQL("UPDATE exp SET experience = experience+8 WHERE exp_id = '0001'" );
			//UPDATE book1 SET LEVEL = LEVEL+1 WHERE ID = 1
			Log.e("YES", "change_exp");
		} catch (Exception e) { 
			Log.e("NO",e.toString());

		}			
	}
	
	
	public void no_change_word_level(String book_name, String word_id){ //卡片箱等級不變 也就是已經到第6級 完全會了

		try {
			
			db.execSQL("UPDATE " +book_name +" SET LEVEL = LEVEL WHERE ID = " + word_id );
			//UPDATE book1 SET LEVEL = LEVEL+1 WHERE ID = 1
			Log.e("YES", "no_change_word_level");
		} catch (Exception e) { 
			Log.e("NO",e.toString());

		}			
	}
	
	public void increase_word_level(String book_name, String word_id){  //增加卡片箱等級
		
		try {
			
			db.execSQL("UPDATE " +book_name +" SET LEVEL = LEVEL+1 WHERE ID = " + word_id );
			//UPDATE book1 SET LEVEL = LEVEL+1 WHERE ID = 1
			Log.e("YES", "increase_word_level");
		} catch (Exception e) { 
			Log.e("NO",e.toString());

		}			
	}
	
	public void decrease_word_level(String book_name, String word_id){   //減少卡片箱等級
		
		try {
			
			db.execSQL("UPDATE " +book_name +" SET LEVEL = LEVEL-1 WHERE ID = " + word_id );
			//UPDATE book1 SET LEVEL = LEVEL+1 WHERE ID = 1
			Log.e("YES", "decrease_word_level");
		} catch (Exception e) { 
			Log.e("NO",e.toString());

		}			
	}

																		// 表示這個單字已經學習過了
	public void update_word_learned(String book_name, String word_id) {// 對word的learned進行修改
		
		try {
			
			Calendar c = Calendar.getInstance(); 
			long milliseconds = c.getTimeInMillis();
			
			db.execSQL("UPDATE " +book_name +" SET LEARNED = LEARNED+1, LAST_LEARNT_TIME = "+ milliseconds+ " WHERE ID = " + word_id );

			Log.e("update_word_learned", "milliseconds = "+milliseconds);
		} catch (Exception e) { 
			Log.e("update_word_learned",e.toString());

		}
		// new Object[] { person.name, person.gender, person.age, person.id }
		// db.execSQL("UPDATE book1 SET LEARNED = 1 WHERE ID = 1");
		// db.execSQL("UPDATE ? SET LEARNED = 1 WHERE ID = ?", new String[]
		// {book_name,word_id});
		// db.execSQL("UPDATE book1 SET LEARNED = 1 WHERE ID = 1",null);
		// db.execSQL("UPDATE book1 SET LEARNED = 1 WHERE ID = 2",null);
		// db.execSQL("UPDATE book1 SET LEARNED = 1 WHERE ID = 3",null);
		// Log.e("UPDATE ? SET LEARNED = 1 WHERE ID = ?", "ok");

		// db.close();

	}

	public Cursor Query_BOOKS() {

		try {
			Cursor cursor = db.rawQuery("select NAME from BOOKS", null);
			return cursor;
		} catch (Exception e) {
			e.getStackTrace();
		}
		// db.close();
		return null;

	}
	
	public Cursor Query_Box(String word, int box_level) {

		String box_level_limit= box_level_1_Limit;
		if (box_level ==1) box_level_limit= box_level_1_Limit;
		else if (box_level ==2) box_level_limit= box_level_2_Limit;
		else if (box_level ==3) box_level_limit= box_level_3_Limit;
		else if (box_level ==4) box_level_limit= box_level_4_Limit;
		else if (box_level ==5) box_level_limit= box_level_5_Limit;
		
		try {
			// String querry = "select  * from"+ "book1"
			// +"where LEARNED = 0 ORDER BY ID ASC limit 0,100";
			// "select * from mytable where name_field = ?"
			// mDb.rawQuery(p_query, new String[] { uvalue });
			String querry = "";

			if (word.equals(SqlHelper.book1)) {
				querry = "select  * from book1 where LEVEL = "+ box_level+" ORDER BY ID ASC limit 0,"+box_level_limit;
			}
			if (word.equals(SqlHelper.book2)) {
				querry = "select  * from book2 where LEVEL = "+ box_level+" ORDER BY ID ASC limit 0,"+box_level_limit;
			}
			if (word.equals(SqlHelper.book3)) {
				querry = "select  * from book3 where LEVEL = "+ box_level+" ORDER BY ID ASC limit 0,"+box_level_limit;
			}

			Cursor cursor = db.rawQuery(querry, null);
			return cursor;
		} catch (Exception e) {
			e.getStackTrace();
		}
		// db.close();
		return null;

	}
	
	 public int getBoxLevel(String book_name_temp, int red_box){

			int box1_words = Query_word_numbers_in_box(book_name_temp,"1");
			int box2_words = Query_word_numbers_in_box(book_name_temp,"2");
			int box3_words = Query_word_numbers_in_box(book_name_temp,"3");
			int box4_words = Query_word_numbers_in_box(book_name_temp,"4");
			int box5_words = Query_word_numbers_in_box(book_name_temp,"5");
		

			int box_level = 1;
			if (box1_words >= 10) {
				box_level = 1;
			}
			if (box2_words >= 20) {
				box_level = 2;
			}
			if (box3_words >= 40) {
				box_level = 3;
			}
			if (box4_words >= 80) {
				box_level = 4;
			}
			if (box5_words >= 160) {
				box_level = 5;
			}
			
			return box_level;
		}

	 public int getBoxLevel(String book_name_temp){

			int box1_words = Query_word_numbers_in_box(book_name_temp,"1");
			int box2_words = Query_word_numbers_in_box(book_name_temp,"2");
			int box3_words = Query_word_numbers_in_box(book_name_temp,"3");
			int box4_words = Query_word_numbers_in_box(book_name_temp,"4");
			int box5_words = Query_word_numbers_in_box(book_name_temp,"5");
		

			int red_box = 1;
			if (box1_words >= 10) {
				red_box = 1;
			}
			if (box2_words >= 20) {
				red_box = 2;
			}
			if (box3_words >= 40) {
				red_box = 3;
			}
			if (box4_words >= 80) {
				red_box = 4;
			}
			if (box5_words >= 160) {
				red_box = 5;
			}
			
			return red_box;
		}
	 
	public Cursor Query_Words_box1(String word) {

		try {
			// String querry = "select  * from"+ "book1"
			// +"where LEARNED = 0 ORDER BY ID ASC limit 0,100";
			// "select * from mytable where name_field = ?"
			// mDb.rawQuery(p_query, new String[] { uvalue });
			String querry = "";

			if (word.equals(SqlHelper.book1)) {
				querry = "select  * from book1 where LEVEL = 1 ORDER BY LAST_LEARNT_TIME ASC limit 0,"+box_level_1_Limit;
			}
			if (word.equals(SqlHelper.book2)) {
				querry = "select  * from book2 where LEVEL = 1 ORDER BY LAST_LEARNT_TIME ASC limit 0,"+box_level_1_Limit;
			}
			if (word.equals(SqlHelper.book3)) {
				querry = "select  * from book3 where LEVEL = 1 ORDER BY LAST_LEARNT_TIME ASC limit 0,"+box_level_1_Limit;
			}

			Cursor cursor = db.rawQuery(querry, null);
			return cursor;
		} catch (Exception e) {
			e.getStackTrace();
		}

		return null;

	}
	
	public boolean Put_Words_in_Box1(String book){   //用來判斷卡片箱2的查詢是否為空
		boolean is_empty = false;

		try { 
			
			String query = "";
			Calendar c = Calendar.getInstance(); 
			long milliseconds = c.getTimeInMillis();

			if (book.equals(SqlHelper.book1)) {
				//db.execSQL("UPDATE book1 SET LEVEL = 1 WHERE LEVEL = 0",null);
//				ContentValues values = new ContentValues();
//				values.put("LEVEL", "1");
//				String whereClause = " LEVEL = 0 LIMIT 10";
//				db.update(book1, values, whereClause, null);
				 query = "update book1 set LEVEL=1 where ID in (select ID from book1 where LEVEL = 0 ORDER BY LAST_LEARNT_TIME ASC limit 0,"+box_level_1_Limit+")";
				
			}
			if (book.equals(SqlHelper.book2)) {
				query = "update book2 set LEVEL=1 where ID in (select ID from book1 where LEVEL = 0 ORDER BY LAST_LEARNT_TIME ASC limit 0,"+box_level_1_Limit+")";
			}
			if (book.equals(SqlHelper.book3)) {
				query = "update book3 set LEVEL=1 where ID in (select ID from book1 where LEVEL = 0 ORDER BY LAST_LEARNT_TIME ASC limit 0,"+box_level_1_Limit+")";
			}
			db.execSQL(query);
			return is_empty;	
		} catch (Exception e) {
			e.getStackTrace();
		}
		return false;
		
	}
	
	
	public boolean Query_Words_box1_is_empty(String word){   //用來判斷卡片箱2的查詢是否為空
		boolean is_empty = false;

		try { 
			// String querry = "select  * from"+ "book1"
			// +"where LEARNED = 0 ORDER BY ID ASC limit 0,100";
			// "select * from mytable where name_field = ?"
			// mDb.rawQuery(p_query, new String[] { uvalue });
			String querry = "";

			if (word.equals(SqlHelper.book1)) {
				querry = "select  * from book1 where LEVEL = 1 ORDER BY ID ASC limit 0,10";
			}
			if (word.equals(SqlHelper.book2)) {
				querry = "select  * from book2 where LEVEL = 1 ORDER BY ID ASC limit 0,10";
			}
			if (word.equals(SqlHelper.book3)) {
				querry = "select  * from book3 where LEVEL = 1 ORDER BY ID ASC limit 0,10";
			}
 
			Cursor cursor = db.rawQuery(querry, null);
			if(cursor.getCount()>0){
				Log.e("cursor!= null", String.valueOf(cursor.getCount()));
				is_empty = false;
			}
			else if(cursor.getCount() == 0){
				Log.e("cursor == null", String.valueOf(cursor.getCount()));
				is_empty = true;
			}
			cursor.close();
			return is_empty;	
		} catch (Exception e) {
			e.getStackTrace();
		}
		return false;
		
	}
	
	public Cursor Query_how_much_Words_in_box1(String word) {

		try {   
			// String querry = "select  * from"+ "book1"
			// +"where LEARNED = 0 ORDER BY ID ASC limit 0,100";
			// "select * from mytable where name_field = ?"
			// mDb.rawQuery(p_query, new String[] { uvalue });
			String querry = "";
 
			if (word.equals(SqlHelper.book1)) {
				querry = "select count(level) from book1 where level = 1";
			}
			if (word.equals(SqlHelper.book2)) {
				querry = "select count(level) from book2 where level = 1";
			}
			if (word.equals(SqlHelper.book3)) {
				querry = "select count(level) from book3 where level = 1";
			}

			Cursor cursor = db.rawQuery(querry, null);
			return cursor;
		} catch (Exception e) {
			Log.e("多少單字在此",e.toString());
		}
		// db.close();
		return null;

	}
	
	public Cursor Query_Words_box2(String word) {

		try { 
			// String querry = "select  * from"+ "book1"
			// +"where LEARNED = 0 ORDER BY ID ASC limit 0,100";
			// "select * from mytable where name_field = ?"
			// mDb.rawQuery(p_query, new String[] { uvalue });
			String querry = "";

			if (word.equals(SqlHelper.book1)) {
				querry = "select  * from book1 where LEVEL = 2 ORDER BY LAST_LEARNT_TIME ASC limit 0,"+box_level_2_Limit;
			}
			if (word.equals(SqlHelper.book2)) {
				querry = "select  * from book2 where LEVEL = 2 ORDER BY LAST_LEARNT_TIME ASC limit 0,"+box_level_2_Limit;
			}
			if (word.equals(SqlHelper.book3)) {
				querry = "select  * from book3 where LEVEL = 2 ORDER BY LAST_LEARNT_TIME ASC limit 0,"+box_level_2_Limit;
			}

			Cursor cursor = db.rawQuery(querry, null);
			return cursor;
		} catch (Exception e) {
			e.getStackTrace();
		}
		// db.close();
		return null;
	}
	public boolean Query_Words_box2_is_empty(String word){   //用來判斷卡片箱2的查詢是否為空
		boolean is_empty = false;

		try { 
			// String querry = "select  * from"+ "book1"
			// +"where LEARNED = 0 ORDER BY ID ASC limit 0,100";
			// "select * from mytable where name_field = ?"
			// mDb.rawQuery(p_query, new String[] { uvalue });
			String querry = "";

			if (word.equals(SqlHelper.book1)) {
				querry = "select  * from book1 where LEVEL = 2 ORDER BY ID ASC limit 0,10";
			}
			if (word.equals(SqlHelper.book2)) {
				querry = "select  * from book2 where LEVEL = 2 ORDER BY ID ASC limit 0,10";
			}
			if (word.equals(SqlHelper.book3)) {
				querry = "select  * from book3 where LEVEL = 2 ORDER BY ID ASC limit 0,10";
			}

			Cursor cursor = db.rawQuery(querry, null);
			if(cursor.getCount()>0){
				Log.e("cursor!= null", String.valueOf(cursor.getCount()));
				is_empty = false;
			}
			else if(cursor.getCount() == 0){
				Log.e("cursor == null", String.valueOf(cursor.getCount()));
				is_empty = true;
			}
			cursor.close();
			return is_empty;
		} catch (Exception e) {
			e.getStackTrace();
		}
		
		return is_empty;
	}
	
	public Cursor Query_how_much_Words_in_box2(String word) {

		try {

			String querry = "";

			if (word.equals(SqlHelper.book1)) {
				querry = "select count(level) from book1 where level = 2";
			}
			if (word.equals(SqlHelper.book2)) {
				querry = "select count(level) from book2 where level = 2";
			}
			if (word.equals(SqlHelper.book3)) {
				querry = "select count(level) from book3 where level = 2";
			}

			Cursor cursor = db.rawQuery(querry, null);
			return cursor;
		} catch (Exception e) {
			e.getStackTrace();
		}
		// db.close();
		return null;
	}
	
	public int Query_word_numbers_in_box(String word, String level) {
		
		try {

			String querry = "";
			if (word.equals(SqlHelper.book1)) {
				querry = "select count(level) from book1 where level ="+level+" ORDER BY ID ASC";
			}
			if (word.equals(SqlHelper.book2)) {
				querry = "select count(level) from book2 where level ="+level+" ORDER BY ID ASC";
			}
			if (word.equals(SqlHelper.book3)) {
				querry = "select count(level) from book3 where level ="+level+" ORDER BY ID ASC";
			}
	
			Cursor cursor = db.rawQuery(querry, null);
			cursor.moveToFirst();
			int num = cursor.getInt(0);
			cursor.close();
			return num;
		} catch (Exception e) {
			e.getStackTrace();
			return -1;
			
		}
		// db.close();
		//return -1;
	}
	
	public Cursor Query_Words_box3(String word) {

		try { 
			// String querry = "select  * from"+ "book1"
			// +"where LEARNED = 0 ORDER BY ID ASC limit 0,100";
			// "select * from mytable where name_field = ?"
			// mDb.rawQuery(p_query, new String[] { uvalue });
			String querry = "";

			if (word.equals(SqlHelper.book1)) {
				querry = "select  * from book1 where LEVEL = 3 ORDER BY LAST_LEARNT_TIME ASC limit 0,"+box_level_3_Limit;
			}
			if (word.equals(SqlHelper.book2)) {
				querry = "select  * from book2 where LEVEL = 3 ORDER BY LAST_LEARNT_TIME ASC limit 0,"+box_level_3_Limit;
			}
			if (word.equals(SqlHelper.book3)) {
				querry = "select  * from book3 where LEVEL = 3 ORDER BY LAST_LEARNT_TIME ASC limit 0,"+box_level_3_Limit;
			}

			Cursor cursor = db.rawQuery(querry, null);
			return cursor;
		} catch (Exception e) {
			e.getStackTrace();
		}
		// db.close();
		return null;
	}
	public boolean Query_Words_box3_is_empty(String word){   //用來判斷卡片箱2的查詢是否為空
		boolean is_empty = false;
		try { 
			// String querry = "select  * from"+ "book1"
			// +"where LEARNED = 0 ORDER BY ID ASC limit 0,100";
			// "select * from mytable where name_field = ?"
			// mDb.rawQuery(p_query, new String[] { uvalue });
			String querry = "";

			if (word.equals(SqlHelper.book1)) {
				querry = "select  * from book1 where LEVEL = 3 ORDER BY LAST_LEARNT_TIME ASC limit 0,"+box_level_3_Limit;
			}
			if (word.equals(SqlHelper.book2)) {
				querry = "select  * from book2 where LEVEL = 3 ORDER BY LAST_LEARNT_TIME ASC limit 0,"+box_level_3_Limit;
			}
			if (word.equals(SqlHelper.book3)) {
				querry = "select  * from book3 where LEVEL = 3 ORDER BY LAST_LEARNT_TIME ASC limit 0,"+box_level_3_Limit;
			}

			Cursor cursor = db.rawQuery(querry, null);
			if(cursor.getCount()>0){
				Log.e("cursor!= null", String.valueOf(cursor.getCount()));
				is_empty = false;
			}
			else if(cursor.getCount() == 0){
				Log.e("cursor == null", String.valueOf(cursor.getCount()));
				is_empty = true;
			}
			cursor.close();
			return is_empty;
		} catch (Exception e) {
			e.getStackTrace();
		}
		return is_empty;
			
	}
	
	public Cursor Query_how_much_Words_in_box3(String word) {

		try {
			// String querry = "select  * from"+ "book1"
			// +"where LEARNED = 0 ORDER BY ID ASC limit 0,100";
			// "select * from mytable where name_field = ?"
			// mDb.rawQuery(p_query, new String[] { uvalue });
			String querry = "";

			if (word.equals(SqlHelper.book1)) {
				querry = "select count(level) from book1 where level = 3";
			}
			if (word.equals(SqlHelper.book2)) {
				querry = "select count(level) from book2 where level = 3";
			}
			if (word.equals(SqlHelper.book3)) {
				querry = "select count(level) from book3 where level = 3";
			}

			Cursor cursor = db.rawQuery(querry, null);
			return cursor;
		} catch (Exception e) {
			e.getStackTrace();
		}
		// db.close();
		return null;

	}
	
	public Cursor Query_Words_box4(String word) {

		try { 
			// String querry = "select  * from"+ "book1"
			// +"where LEARNED = 0 ORDER BY ID ASC limit 0,100";
			// "select * from mytable where name_field = ?"
			// mDb.rawQuery(p_query, new String[] { uvalue });
			String querry = "";

			if (word.equals(SqlHelper.book1)) {
				querry = "select  * from book1 where LEVEL = 4 ORDER BY LAST_LEARNT_TIME ASC limit 0,"+box_level_4_Limit;
			}
			if (word.equals(SqlHelper.book2)) {
				querry = "select  * from book2 where LEVEL = 4 ORDER BY LAST_LEARNT_TIME ASC limit 0,"+box_level_4_Limit;
			}
			if (word.equals(SqlHelper.book3)) {
				querry = "select  * from book3 where LEVEL = 4 ORDER BY LAST_LEARNT_TIME ASC limit 0,"+box_level_4_Limit;
			}

			Cursor cursor = db.rawQuery(querry, null);
			return cursor;
		} catch (Exception e) {
			e.getStackTrace();
		}

		return null;
	}
	public boolean Query_Words_box4_is_empty(String word){   //用來判斷卡片箱2的查詢是否為空
		boolean is_empty = false;

		try { 
			// String querry = "select  * from"+ "book1"
			// +"where LEARNED = 0 ORDER BY ID ASC limit 0,100";
			// "select * from mytable where name_field = ?"
			// mDb.rawQuery(p_query, new String[] { uvalue });
			String querry = "";

			if (word.equals(SqlHelper.book1)) {
				querry = "select  * from book1 where LEVEL = 4 ORDER BY ID ASC limit 0,10";
			}
			if (word.equals(SqlHelper.book2)) {
				querry = "select  * from book2 where LEVEL = 4 ORDER BY ID ASC limit 0,10";
			}
			if (word.equals(SqlHelper.book3)) {
				querry = "select  * from book3 where LEVEL = 4 ORDER BY ID ASC limit 0,10";
			}

			Cursor cursor = db.rawQuery(querry, null);
			if(cursor.getCount()>0){
				Log.e("cursor!= null", String.valueOf(cursor.getCount()));
				is_empty = false;
			}
			else if(cursor.getCount() == 0){
				Log.e("cursor == null", String.valueOf(cursor.getCount()));
				is_empty = true;
			}
			cursor.close();
			return is_empty;
		} catch (Exception e) {
			e.getStackTrace();
		}
	
		return is_empty;	
	}
	
	
	
	public Cursor Query_how_much_Words_in_box4(String word) {

		try {
			// String querry = "select  * from"+ "book1"
			// +"where LEARNED = 0 ORDER BY ID ASC limit 0,100";
			// "select * from mytable where name_field = ?"
			// mDb.rawQuery(p_query, new String[] { uvalue });
			String querry = "";

			if (word.equals(SqlHelper.book1)) {
				querry = "select count(level) from book1 where level = 4";
			}
			if (word.equals(SqlHelper.book2)) {
				querry = "select count(level) from book2 where level = 4";
			}
			if (word.equals(SqlHelper.book3)) {
				querry = "select count(level) from book3 where level = 4";
			}

			Cursor cursor = db.rawQuery(querry, null);
			return cursor;
		} catch (Exception e) {
			e.getStackTrace();
		}

		return null;

	}
	
	
	public Cursor Query_Words_box5(String word) {

		try { 
			// String querry = "select  * from"+ "book1"
			// +"where LEARNED = 0 ORDER BY ID ASC limit 0,100";
			// "select * from mytable where name_field = ?"
			// mDb.rawQuery(p_query, new String[] { uvalue });
			String querry = "";

			if (word.equals(SqlHelper.book1)) {
				querry = "select  * from book1 where LEVEL = 5 ORDER BY LAST_LEARNT_TIME ASC limit 0,"+box_level_5_Limit;
			}
			if (word.equals(SqlHelper.book2)) {
				querry = "select  * from book2 where LEVEL = 5 ORDER BY LAST_LEARNT_TIME ASC limit 0,"+box_level_5_Limit;
			}
			if (word.equals(SqlHelper.book3)) {
				querry = "select  * from book3 where LEVEL = 5 ORDER BY LAST_LEARNT_TIME ASC limit 0,"+box_level_5_Limit;
			}

			Cursor cursor = db.rawQuery(querry, null);
			return cursor;
		} catch (Exception e) {
			e.getStackTrace();
		}

		return null;
	}
	
	public boolean Query_Words_box5_is_empty(String word){   //用來判斷卡片箱2的查詢是否為空
		boolean is_empty = false;

		try { 
			// String querry = "select  * from"+ "book1"
			// +"where LEARNED = 0 ORDER BY ID ASC limit 0,100";
			// "select * from mytable where name_field = ?"
			// mDb.rawQuery(p_query, new String[] { uvalue });
			String querry = "";

			if (word.equals(SqlHelper.book1)) {
				querry = "select  * from book1 where LEVEL = 5 ORDER BY ID ASC";
			}
			if (word.equals(SqlHelper.book2)) {
				querry = "select  * from book2 where LEVEL = 5 ORDER BY ID ASC";
			}
			if (word.equals(SqlHelper.book3)) {
				querry = "select  * from book3 where LEVEL = 5 ORDER BY ID ASC";
			}

			Cursor cursor = db.rawQuery(querry, null);
			if(cursor.getCount()>0){
				Log.e("cursor!= null", String.valueOf(cursor.getCount()));
				is_empty = false;
			}
			else if(cursor.getCount() == 0){
				Log.e("cursor == null", String.valueOf(cursor.getCount()));
				is_empty = true;
			}
			cursor.close();
			return is_empty;	
		} catch (Exception e) {
			e.getStackTrace();
		}
		return is_empty;	
	}
	
	
	public Cursor Query_how_much_Words_in_box5(String word) {

		try {
			// String querry = "select  * from"+ "book1"
			// +"where LEARNED = 0 ORDER BY ID ASC limit 0,100";
			// "select * from mytable where name_field = ?"
			// mDb.rawQuery(p_query, new String[] { uvalue });
			String querry = "";

			if (word.equals(SqlHelper.book1)) {
				querry = "select count(level) from book1 where level = 5";
			}
			if (word.equals(SqlHelper.book2)) {
				querry = "select count(level) from book2 where level = 5";
			}
			if (word.equals(SqlHelper.book3)) {
				querry = "select count(level) from book3 where level = 5";
			}

			Cursor cursor = db.rawQuery(querry, null);
			return cursor;
		} catch (Exception e) {
			e.getStackTrace();
		}

		return null;

	}
	
	
	public void Insert(Context context, String table, ContentValues values) {
		//SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_NAME, null);
		try {
			db.insert(table, null, values);
		} catch (Exception e) {
			e.getStackTrace();
		}
		//db.close();
	}

	public void Update(Context context, String table, ContentValues values,
			String whereClause, String[] whereArgs) {

		try {
			db.update(table, values, whereClause, whereArgs);
		} catch (Exception e) {
			e.getStackTrace();
		}
		//db.close();
	}

	public Cursor Query(Context context, String table, String[] columns,
			String selection, String[] selectionArgs, String groupBy,
			String having, String orderBy) {

		try {
			Cursor cursor = db.query(table, columns, selection, selectionArgs,
					groupBy, having, orderBy);
			return cursor;
		} catch (Exception e) {
			e.getStackTrace();
		}
		//db.close();
		return null;

	}

	public void Delete(Context context, String table, String whereClause, String[] whereArgs) {
		
		try {
			db.delete(table, whereClause, whereArgs);
		} catch (Exception e) {
			e.getStackTrace();
		}

	}

	public void DeleteTable(Context context, String table) {

		String sql = "drop table " + table;
		try {
			db.execSQL(sql);
		} catch (Exception e) {
			e.getStackTrace();
		}
		//db.close();
	}

	
	/**
    *
    * This class helps open, create, and upgrade the database file. Set to package visibility
    * for testing purposes.
    */
	static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
			//context=內容物件；name=傳入資料庫名稱；factory=複雜查詢時使用；version=資料庫版本

			Log.e(TAG, " ######### get DB OK!"); 
		}
		
		public DatabaseHelper(Context context){


	         super(context, DATABASE_NAME, null, DATABASE_VERSION);
	      }

		
		@Override
		public void onCreate(SQLiteDatabase db) {

	//		String DATABASE_CREATE_TABLE =
	//			    "create table config ("
	//			        + "_ID INTEGER PRIMARY KEY,"
	//			        + "name TEXT,"
	//			        + "value INTEGER"
	//			    + ");";
	//			//建立config資料表，詳情請參考SQL語法
	//			db.execSQL(DATABASE_CREATE_TABLE);
				
		}
	
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			 // Logs that the database is being upgraded
	           Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
	                   + newVersion + ", which will destroy all old data");
			
		}
		
		@Override
		public void onOpen(SQLiteDatabase db) {
			
		}
	}
	
}
