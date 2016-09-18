package sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2016/1/30.
 */
public class tableDao {

    public static String user_id = "user_test";
    public static final String words = "words";
    public static final String meaning = "meaning";
    public static final String exp = "exp";
    public static final String categories = "categories";
    public static int box_level_1_Limit = 10;
    public static int box_level_2_Limit = 20;
    public static int box_level_3_Limit = 40;
    public static int box_level_4_Limit = 80;
    public static int box_level_5_Limit = 160;
    private SQLiteDatabase db;

    public static final String createWordsTable = "CREATE TABLE IF NOT EXISTS " + words
            + "(id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "word VARCHAR(30) NOT NULL,"
            + "phonetics VARCHAR(30),"
            + "GEPTlow INTEGER(1) NOT NULL,"
            + "GEPTmiddle INTEGER(1) NOT NULL,"
            + "GEPTmiddlehigh INTEGER(1) NOT NULL,"
            + "GEPThigh INTEGER(1) NOT NULL,"
            + "TOEFL INTEGER(1) NOT NULL,"
            + "TOEIC INTEGER(1) NOT NULL,"
            + "IELTS INTEGER(1) NOT NULL)";

    public static final String createMeaningTable = "CREATE TABLE IF NOT EXISTS " + meaning
            + "(sub_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "id INTEGER NOT NULL,"
            + "word VARCHAR(30) NOT NULL,"
            + "part_of_speech VARCHAR(4) NOT NULL,"
            + "EngChiTra VARCHAR(100) NOT NULL,"
            + "EngEng VARCHAR(100),"
            + "synonym VARCHAR(30),"
            + "antonym VARCHAR(30),"
            + "sentence VARCHAR(100),"
            + "EngChiSpl VARCHAR(100),"
            + "EngJp VARCHAR(100),"
            + "EngKorea VARCHAR(100))";


    public static final String createExpTable = "CREATE TABLE IF NOT EXISTS " + exp
            + " (user_id VARCHAR NOT NULL,"
            + "word_id INTEGER PRIMARY KEY,"
            + "level INTEGER(3) NOT NULL,"
            + "position INTEGER(5) NOT NULL,"
            + "learned INTEGER(5),"
            + "Last_Learnt_Time VARCHAR(20))";

    public static final String createCategoriesTable = "CREATE TABLE IF NOT EXISTS " + categories
            + " (sub_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "id INTEGER(10) NOT NULL,"
            + "word VARCHAR(20) NOT NULL,"
            + "catagory INTEGER(11) NOT NULL)";


    // 建構子，一般的應用都不需要修改
    public tableDao(Context context) {
        db = SqlHelper.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    /*****
     * 跟word相關的所有SQL語法
     *****/

    // 新增Words物件
    public void insertWords(sqlite.Words addWords) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put("id", addWords.getId());
        cv.put("word", addWords.getWord());
        cv.put("phonetics", addWords.getPhonetics());
        cv.put("GEPTlow", addWords.getGEPTlow());
        cv.put("GEPTmiddle", addWords.getGEPTmiddle());
        cv.put("GEPTmiddlehigh", addWords.getGEPTmiddlehigh());
        cv.put("GEPThigh", addWords.getGEPThigh());
        cv.put("TOEFL", addWords.getTOEFL());
        cv.put("TOEIC", addWords.getTOEIC());
        cv.put("IELTS", addWords.getIELTS());

        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        db.insert(words, null, cv);
    }

    // 讀取所有words記事資料
    public List<sqlite.Words> getAllWords() {
        List<sqlite.Words> result = new ArrayList<>();
        Cursor cursor = db.query(
                words, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getWordsRecord(cursor));
        }

        cursor.close();
        return result;
    }

    // 讀取相對應的words記事資料
    public List<sqlite.Words> getWords(String wordsType) {
        List<sqlite.Words> result = new ArrayList<>();
        String where = "TOEIC" + "= " + "1";
        Cursor cursor = db.query(
                words, null, where, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getWordsRecord(cursor));
        }

        cursor.close();
        return result;
    }

    public List<sqlite.Words> getWordsById(List<Exp> expReturnList) {
        // 準備回傳結果用的物件
        List<sqlite.Words> result = new ArrayList<>();
        Cursor cursor = null;
        for (int i = 0; i < expReturnList.size(); i++) {
            // 使用編號為查詢條件
            String where = "id" + "=" + expReturnList.get(i).getWord_id();
            // 執行查詢
            cursor = db.query(
                    words, null, where, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                result.add(getWordsRecord(cursor));
            }
        }
        // 關閉Cursor物件
        cursor.close();
        // 回傳結果
        return result;
    }

    //選擇10張未學習過的卡片，新增至Exp table讓使用者學習
    public List<sqlite.Words> top10Words(int max) {
        List<sqlite.Words> result = new ArrayList<>();
        System.out.println("going to query 10 data");
        Cursor cursor;
        String query = "";
        if (max == 0) {
            query = "SELECT * FROM words ORDER BY id ASC limit 0,10";

        } else {
            query = "SELECT * FROM words ORDER BY id ASC limit " + max + ",10";
        }
        cursor = db.rawQuery(query, null);
        System.out.println("query = " + query);
        while (cursor.moveToNext()) {
            result.add(getWordsRecord(cursor));
        }
        for (int i = 0; i < 10; i++) {
            Exp expAdd = new Exp(user_id, result.get(i).getId(), 1, i, 1, "");
            this.insertExp(expAdd);
        }
        cursor.close();
        return result;
    }

    // 把Cursor目前的資料包裝為Words物件
    public sqlite.Words getWordsRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        sqlite.Words result = new sqlite.Words();

        result.setId(cursor.getInt(0));
        result.setWord(cursor.getString(1));
        result.setPhonetics(cursor.getString(2));
        result.setGEPTlow(cursor.getInt(3));
        result.setGEPTmiddle(cursor.getInt(4));
        result.setGEPTmiddlehigh(cursor.getInt(5));
        result.setGEPThigh(cursor.getInt(6));
        result.setTOEFL(cursor.getInt(7));
        result.setTOEIC(cursor.getInt(8));
        result.setIELTS(cursor.getInt(9));

        // 回傳結果
        return result;
    }

    // 取得words資料數量
    public int getWordsCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + words, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        cursor.close();
        return result;
    }

    // 刪除參數指定編號的資料
    public boolean deleteWords(int id) {
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = id + "=" + id;
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(words, where, null) > 0;
    }

    // 刪除words的資料
    public boolean deleteWords() {
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(words, null, null) > 0;
    }


    /*****
     * 跟meaning相關的SQL語法
     *****/

    // 新增Meaning物件
    public void insertMeaning(Meaning addMeaning) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put("sub_id", addMeaning.getSub_id());
        cv.put("id", addMeaning.getId());
        cv.put("word", addMeaning.getWord());
        cv.put("part_of_speech", addMeaning.getPart_of_speech());
        cv.put("EngChiTra", addMeaning.getEngChiTra());
        cv.put("EngEng", addMeaning.getEngEng());
        cv.put("synonym", addMeaning.getSynonym());
        cv.put("antonym", addMeaning.getAntonym());
        cv.put("sentence", addMeaning.getSentence());
        cv.put("EngChiSpl", addMeaning.getEngChiSpl());
        cv.put("EngJp", addMeaning.getEngJp());
        cv.put("EngKorea", addMeaning.getEngKorea());

        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        db.insert(meaning, null, cv);
    }

    //透過ID搜尋meaning
    public List<Meaning> getMeaningById(int id) {
        // 準備回傳結果用的物件
        List<Meaning> result = new ArrayList<>();
        // 使用編號為查詢條件
        String where = "id" + "=" + id;
        // 執行查詢
        Cursor cursor = db.query(
                meaning, null, where, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getMeaningRecord(cursor));
        }

        // 關閉Cursor物件
        cursor.close();
        // 回傳結果
        return result;
    }

    // 把Cursor目前的資料包裝為Meaning物件
    public Meaning getMeaningRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        Meaning result = new Meaning();

        result.setSub_id(cursor.getInt(0));
        result.setId(cursor.getInt(1));
        result.setWord(cursor.getString(2));
        result.setPart_of_speech(cursor.getString(3));
        result.setEngChiTra(cursor.getString(4));
        result.setEngEng(cursor.getString(5));
        result.setSynonym(cursor.getString(6));
        result.setAntonym(cursor.getString(7));
        result.setSentence(cursor.getString(8));
        result.setEngChiSpl(cursor.getString(9));
        result.setEngJp(cursor.getString(10));
        result.setEngKorea(cursor.getString(11));

        // 回傳結果
        return result;
    }

    // 取得meaning資料數量
    public int getMeaningCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + meaning, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        cursor.close();
        return result;
    }

    // 刪除meaning的資料
    public boolean deleteMeaning() {
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(meaning, null, null) > 0;
    }

    /*****
     * 跟exp相關的SQL語法
     *****/

    // 新增Exp物件
    public void insertExp(Exp addExp) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put("user_id", addExp.getUser_id());
        cv.put("word_id", addExp.getWord_id());
        cv.put("level", addExp.getLevel());
        cv.put("position", addExp.getPosition());
        cv.put("learned", addExp.getLearned());
        cv.put("Last_Learnt_Time", addExp.getLast_Learnt_Time());

        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        db.insert(exp, null, cv);
    }

    // 取得exp資料數量
    public int getExpCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + exp, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        cursor.close();
        return result;
    }

    // 取得exp中word_id最大為多少
    public int getExpMaxWordId() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT Max(word_id) FROM " + exp, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        System.out.println("exp max word_id :" + result);
        cursor.close();
        return result;
    }

    // 刪除參數指定編號的資料
    public boolean deleteExp() {
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(exp, null, null) > 0;
    }

    public Exp getExpById(int id) {
        // 準備回傳結果用的物件
        Exp result = new Exp();
        // 使用編號為查詢條件
        String where = "word_id" + "=" + id;
        // 執行查詢
        Cursor cursor = db.query(
                exp, null, where, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            result = getExpRecord(cursor);
        }

        // 關閉Cursor物件
        cursor.close();
        // 回傳結果
        return result;
    }

    // 把Cursor目前的資料包裝為Exp物件
    public Exp getExpRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        Exp result = new Exp();

        result.setUser_id(cursor.getString(0));
        result.setWord_id(cursor.getInt(1));
        result.setLevel(cursor.getInt(2));
        result.setPosition(cursor.getInt(3));
        result.setLearned(cursor.getInt(4));
        result.setLast_Learnt_Time(cursor.getString(5));

        // 回傳結果
        return result;
    }

    // 取得exp資料數量
    public int getBoxCount(int level) {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM exp WHERE level=" + level + "", null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }
        cursor.close();
        return result;
    }

    //取得箱子內該level的卡片
    public List<Exp> boxLevelData(int level) {
        // 準備回傳結果用的物件
        List<Exp> result = new ArrayList<>();
        // 使用編號為查詢條件
        String where = " level = " + level;
        // 執行查詢
        Cursor cursor = db.query(
                exp, null, where, null, null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getExpRecord(cursor));
        }

        // 關閉Cursor物件
        cursor.close();
        // 回傳結果
        return result;
    }

    //確認要前往的箱子單字數
    public boolean checkBoxCount(int level){
        int count = this.getBoxCount(level)+1;
        System.out.println("method count : " + count);
        boolean insertOk = false;

        if(level==1) {

            if (count < box_level_1_Limit)
                insertOk = true;
            else
                insertOk = false;
        }else if(level==2) {
            if (count < box_level_2_Limit)
                insertOk = true;
            else
                insertOk = false;
        } else if(level==3) {
            if (count < box_level_3_Limit)
                insertOk = true;
            else
                insertOk = false;
        } else if(level==4) {
            if (count < box_level_4_Limit)
                insertOk = true;
            else
                insertOk = false;
        }else if(level==5) {
            if (count < box_level_5_Limit)
                insertOk = true;
            else
                insertOk = false;
        }

        System.out.println(insertOk);
        return insertOk;
    }

    // 修改參數指定的物件
    public void updateExp(Exp expUpdate) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = new ContentValues();

        cv.put("level", expUpdate.getLevel());
        cv.put("position", expUpdate.getPosition());
        cv.put("learned", expUpdate.getLearned());
        cv.put("Last_Learnt_Time", expUpdate.getLast_Learnt_Time());

        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = "word_id =" + expUpdate.getWord_id();

        // 執行修改資料並回傳修改的資料數量是否成功
        db.update(exp, cv, where, null);
    }

    public void sampleWord() {
        String data = "1,affair,,0,0,0,0,0,1,0\n" +
                "2,affluent,,0,0,0,0,0,1,0\n" +
                "3,attain,,0,0,0,0,0,1,0\n" +
                "4,authentic,,0,0,0,0,0,1,0\n" +
                "5,audit,,0,0,0,0,0,1,0\n" +
                "6,auction,,0,0,0,0,0,1,0\n" +
                "7,barter,,0,0,0,0,0,1,0\n" +
                "8,boost,,0,0,0,0,0,1,0\n" +
                "9,cargo,,0,0,0,0,0,1,0\n" +
                "10,circumstance,,0,0,0,0,0,1,0\n" +
                "11,consignment,,0,0,0,0,0,1,0\n" +
                "12,counsel,,0,0,0,0,0,1,0\n" +
                "13,diversification,,0,0,0,0,0,1,0\n" +
                "14,expiration,,0,0,0,0,0,1,0\n" +
                "15,exploitation,,0,0,0,0,0,1,0\n" +
                "16,exemption,,0,0,0,0,0,1,0\n" +
                "17,flourish,,0,0,0,0,0,1,0\n" +
                "18,freight,,0,0,0,0,0,1,0\n" +
                "19,genuine,,0,0,0,0,0,1,0\n" +
                "20,grant,,0,0,0,0,0,1,0\n" +
                "21,imposition,,0,0,0,0,0,1,0\n" +
                "22,inevitable,,0,0,0,0,0,1,0\n" +
                "23,lease,,0,0,0,0,0,1,0\n" +
                "24,merchandise,,0,0,0,0,0,1,0\n" +
                "25,monetary,,0,0,0,0,0,1,0\n" +
                "26,overdue,,0,0,0,0,0,1,0\n" +
                "27,phony,,0,0,0,0,0,1,0\n" +
                "28,preliminary,,0,0,0,0,0,1,0\n" +
                "29,prosperity,,0,0,0,0,0,1,0\n" +
                "30,quits,,0,0,0,0,0,1,0\n" +
                "31,ratify,,0,0,0,0,0,1,0\n" +
                "32,reimburse,,0,0,0,0,0,1,0\n" +
                "33,restraint,,0,0,0,0,0,1,0\n" +
                "34,resumption,,0,0,0,0,0,1,0\n" +
                "35,retailer,,0,0,0,0,0,1,0\n" +
                "36,sanction,,0,0,0,0,0,1,0\n" +
                "37,significant,,0,0,0,0,0,1,0\n" +
                "38,smuggle,,0,0,0,0,0,1,0\n" +
                "39,specimen,,0,0,0,0,0,1,0\n" +
                "40,subsidiary,,0,0,0,0,0,1,0\n" +
                "41,tackle,,0,0,0,0,0,1,0\n" +
                "42,tariff,,0,0,0,0,0,1,0\n" +
                "43,adequate,,0,0,0,0,0,1,0\n" +
                "44,anticipation,,0,0,0,0,0,1,0\n" +
                "45,allotment,,0,0,0,0,0,1,0\n" +
                "46,antique,,0,0,0,0,0,1,0\n" +
                "47,assess,,0,0,0,0,0,1,0\n" +
                "48,behalf,,0,0,0,0,0,1,0\n" +
                "49,bid,,0,0,0,0,0,1,0\n" +
                "50,boast,,0,0,0,0,0,1,0\n" +
                "51,bond,,0,0,0,0,0,1,0\n" +
                "52,cease,,0,0,0,0,0,1,0\n" +
                "53,collide,,0,0,0,0,0,1,0\n" +
                "54,compensate,,0,0,0,0,0,1,0\n" +
                "55,convey,,0,0,0,0,0,1,0\n" +
                "56,council,,0,0,0,0,0,1,0\n" +
                "57,counselor,,0,0,0,0,0,1,0\n" +
                "58,deliberation,,0,0,0,0,0,1,0\n" +
                "59,diminish,,0,0,0,0,0,1,0\n" +
                "60,diversify,,0,0,0,0,0,1,0\n" +
                "61,dividend,,0,0,0,0,0,1,0\n" +
                "62,endorsement,,0,0,0,0,0,1,0\n" +
                "63,estate,,0,0,0,0,0,1,0\n" +
                "64,forecast,,0,0,0,0,0,1,0\n" +
                "65,hazardous,,0,0,0,0,0,1,0\n" +
                "66,inferior,,0,0,0,0,0,1,0\n" +
                "67,infrastructure,,0,0,0,0,0,1,0\n" +
                "68,inflation,,0,0,0,0,0,1,0\n" +
                "69,inventory,,0,0,0,0,0,1,0\n" +
                "70,investigate,,0,0,0,0,0,1,0\n" +
                "71,invoice,,0,0,0,0,0,1,0\n" +
                "72,lucrative,,0,0,0,0,0,1,0\n" +
                "73,mortgage,,0,0,0,0,0,1,0\n" +
                "74,patronage,,0,0,0,0,0,1,0\n" +
                "75,precise,,0,0,0,0,0,1,0\n" +
                "76,profitable,,0,0,0,0,0,1,0\n" +
                "77,remittance,,0,0,0,0,0,1,0\n" +
                "78,rebate,,0,0,0,0,0,1,0\n" +
                "79,revenue,,0,0,0,0,0,1,0\n" +
                "80,refund,,0,0,0,0,0,1,0\n" +
                "81,renovate,,0,0,0,0,0,1,0\n" +
                "82,severe,,0,0,0,0,0,1,0\n" +
                "83,setback,,0,0,0,0,0,1,0\n" +
                "84,settlement,,0,0,0,0,0,1,0\n" +
                "85,speculate,,0,0,0,0,0,1,0\n" +
                "86,stake,,0,0,0,0,0,1,0\n" +
                "87,surpass,,0,0,0,0,0,1,0\n" +
                "88,surplus,,0,0,0,0,0,1,0\n" +
                "89,tangible,,0,0,0,0,0,1,0\n" +
                "90,urgent,,0,0,0,0,0,1,0\n" +
                "91,vibration,,0,0,0,0,0,1,0\n" +
                "92,withdrawal,,0,0,0,0,0,1,0\n" +
                "93,wealth,,0,0,0,0,0,1,0\n" +
                "94,amendment,,0,0,0,0,0,1,0\n" +
                "95,absence,,0,0,0,0,0,1,0\n" +
                "96,adjourn,,0,0,0,0,0,1,0\n" +
                "97,advisory,,0,0,0,0,0,1,0\n" +
                "98,affiliate,,0,0,0,0,0,1,0\n" +
                "99,alter,,0,0,0,0,0,1,0\n" +
                "100,barrier,,0,0,0,0,0,1,0\n" +
                "101,coherent,,0,0,0,0,0,1,0\n" +
                "102,coincide,,0,0,0,0,0,1,0\n" +
                "103,compatible,,0,0,0,0,0,1,0\n" +
                "104,competence,,0,0,0,0,0,1,0\n" +
                "105,compulsion,,0,0,0,0,0,1,0\n" +
                "106,consent,,0,0,0,0,0,1,0\n" +
                "107,concealment,,0,0,0,0,0,1,0\n" +
                "108,concede,,0,0,0,0,0,1,0\n" +
                "109,confidential,,0,0,0,0,0,1,0\n" +
                "110,consultant,,0,0,0,0,0,1,0\n" +
                "111,correspondence,,0,0,0,0,0,1,0\n" +
                "112,detain,,0,0,0,0,0,1,0\n" +
                "113,devise,,0,0,0,0,0,1,0\n" +
                "114,deputy,,0,0,0,0,0,1,0\n" +
                "115,dispose,,0,0,0,0,0,1,0\n" +
                "116,eligible,,0,0,0,0,0,1,0\n" +
                "117,illustrate,,0,0,0,0,0,1,0\n" +
                "118,obligation,,0,0,0,0,0,1,0\n" +
                "119,omit,,0,0,0,0,0,1,0\n" +
                "120,opposition,,0,0,0,0,0,1,0\n" +
                "121,pending,,0,0,0,0,0,1,0\n" +
                "122,pact,,0,0,0,0,0,1,0\n" +
                "123,plod,,0,0,0,0,0,1,0\n" +
                "124,precede,,0,0,0,0,0,1,0\n" +
                "125,predecessor,,0,0,0,0,0,1,0\n" +
                "126,prestigious,,0,0,0,0,0,1,0\n" +
                "127,proclaim,,0,0,0,0,0,1,0\n" +
                "128,provisional,,0,0,0,0,0,1,0\n" +
                "129,punctual,,0,0,0,0,0,1,0\n" +
                "130,reconciliation,,0,0,0,0,0,1,0\n" +
                "131,spoil,,0,0,0,0,0,1,0\n" +
                "132,supervise,,0,0,0,0,0,1,0\n" +
                "133,unanimous,,0,0,0,0,0,1,0\n" +
                "134,warranty,,0,0,0,0,0,1,0\n" +
                "135,aviation,,0,0,0,0,0,1,0\n" +
                "136,aggravation,,0,0,0,0,0,1,0\n" +
                "137,allergy,,0,0,0,0,0,1,0\n" +
                "138,bruise,,0,0,0,0,0,1,0\n" +
                "139,clinic,,0,0,0,0,0,1,0\n" +
                "140,complexion,,0,0,0,0,0,1,0\n" +
                "141,deteriorate,,0,0,0,0,0,1,0\n" +
                "142,diabetes,,0,0,0,0,0,1,0\n" +
                "143,diagnose,,0,0,0,0,0,1,0\n" +
                "144,dimension,,0,0,0,0,0,1,0\n" +
                "145,dominant,,0,0,0,0,0,1,0\n" +
                "146,emission,,0,0,0,0,0,1,0\n" +
                "147,evaporate,,0,0,0,0,0,1,0\n" +
                "148,epidemic,,0,0,0,0,0,1,0\n" +
                "149,exhaustion,,0,0,0,0,0,1,0\n" +
                "150,friction,,0,0,0,0,0,1,0\n" +
                "151,gravity,,0,0,0,0,0,1,0\n" +
                "152,handicapped,,0,0,0,0,0,1,0\n" +
                "153,immunity,,0,0,0,0,0,1,0\n" +
                "154,impulsive,,0,0,0,0,0,1,0\n" +
                "155,infant,,0,0,0,0,0,1,0\n" +
                "156,meteorological,,0,0,0,0,0,1,0\n" +
                "157,molecule,,0,0,0,0,0,1,0\n" +
                "158,orbit,,0,0,0,0,0,1,0\n" +
                "159,prescription,,0,0,0,0,0,1,0\n" +
                "160,prevail,,0,0,0,0,0,1,0\n" +
                "161,precedent,,0,0,0,0,0,1,0\n" +
                "162,prevalent,,0,0,0,0,0,1,0\n" +
                "163,psychiatrist,,0,0,0,0,0,1,0\n" +
                "164,remedy,,0,0,0,0,0,1,0\n" +
                "165,saturate,,0,0,0,0,0,1,0\n" +
                "166,simulate,,0,0,0,0,0,1,0\n" +
                "167,skeleton,,0,0,0,0,0,1,0\n" +
                "168,symptom,,0,0,0,0,0,1,0\n" +
                "169,tentative,,0,0,0,0,0,1,0\n" +
                "170,unconscious,,0,0,0,0,0,1,0\n" +
                "171,vaccine,,0,0,0,0,0,1,0\n" +
                "172,vulnerable,,0,0,0,0,0,1,0\n" +
                "173,vitality,,0,0,0,0,0,1,0\n" +
                "174,wholesome,,0,0,0,0,0,1,0\n" +
                "175,absurd,,0,0,0,0,0,1,0\n" +
                "176,agricultural,,0,0,0,0,0,1,0\n" +
                "177,apt,,0,0,0,0,0,1,0\n" +
                "178,articulate,,0,0,0,0,0,1,0\n" +
                "179,civilization,,0,0,0,0,0,1,0\n" +
                "180,citation,,0,0,0,0,0,1,0\n" +
                "181,concise,,0,0,0,0,0,1,0\n" +
                "182,curriculum,,0,0,0,0,0,1,0\n" +
                "183,dormitory,,0,0,0,0,0,1,0\n" +
                "184,dictate,,0,0,0,0,0,1,0\n" +
                "185,essay,,0,0,0,0,0,1,0\n" +
                "186,essence,,0,0,0,0,0,1,0\n" +
                "187,ethics,,0,0,0,0,0,1,0\n" +
                "188,fabulous,,0,0,0,0,0,1,0\n" +
                "189,faculty,,0,0,0,0,0,1,0\n" +
                "190,heritage,,0,0,0,0,0,1,0\n" +
                "191,indispensable,,0,0,0,0,0,1,0\n" +
                "192,integrity,,0,0,0,0,0,1,0\n" +
                "193,interpret,,0,0,0,0,0,1,0\n" +
                "194,literature,,0,0,0,0,0,1,0\n" +
                "195,manuscript,,0,0,0,0,0,1,0\n" +
                "196,monotonous,,0,0,0,0,0,1,0\n" +
                "197,obscure,,0,0,0,0,0,1,0\n" +
                "198,primitive,,0,0,0,0,0,1,0\n" +
                "199,racial,,0,0,0,0,0,1,0\n" +
                "200,religious,,0,0,0,0,0,1,0\n" +
                "201,respective,,0,0,0,0,0,1,0\n" +
                "202,revision,,0,0,0,0,0,1,0\n" +
                "203,sanitary,,0,0,0,0,0,1,0\n" +
                "204,seminar,,0,0,0,0,0,1,0\n" +
                "205,species,,0,0,0,0,0,1,0\n" +
                "206,stationery,,0,0,0,0,0,1,0\n" +
                "207,stimulation,,0,0,0,0,0,1,0\n" +
                "208,transcript,,0,0,0,0,0,1,0\n" +
                "209,acclaimed,,0,0,0,0,0,1,0\n" +
                "210,ally,,0,0,0,0,0,1,0\n" +
                "211,appointment,,0,0,0,0,0,1,0\n" +
                "212,banquet,,0,0,0,0,0,1,0\n" +
                "213,betray,,0,0,0,0,0,1,0\n" +
                "214,clarification,,0,0,0,0,0,1,0\n" +
                "215,comprehension,,0,0,0,0,0,1,0\n" +
                "216,conviction,,0,0,0,0,0,1,0\n" +
                "217,deceive,,0,0,0,0,0,1,0\n" +
                "218,decent,,0,0,0,0,0,1,0\n" +
                "219,decline,,0,0,0,0,0,1,0\n" +
                "220,deliberation,,0,0,0,0,0,1,0\n" +
                "221,disclose,,0,0,0,0,0,1,0\n" +
                "222,discrimination,,0,0,0,0,0,1,0\n" +
                "223,disguise,,0,0,0,0,0,1,0\n" +
                "224,dull,,0,0,0,0,0,1,0\n" +
                "225,earnest,,0,0,0,0,0,1,0\n" +
                "226,enthusiastic,,0,0,0,0,0,1,0\n" +
                "227,flattery,,0,0,0,0,0,1,0\n" +
                "228,frank,,0,0,0,0,0,1,0\n" +
                "229,frustrate,,0,0,0,0,0,1,0\n" +
                "230,glimpse,,0,0,0,0,0,1,0\n" +
                "231,humiliate,,0,0,0,0,0,1,0\n" +
                "232,implication,,0,0,0,0,0,1,0\n" +
                "233,insult,,0,0,0,0,0,1,0\n" +
                "234,intimacy,,0,0,0,0,0,1,0\n" +
                "235,neglect,,0,0,0,0,0,1,0\n" +
                "236,odor,,0,0,0,0,0,1,0\n" +
                "237,polite,,0,0,0,0,0,1,0\n" +
                "238,precaution,,0,0,0,0,0,1,0\n" +
                "239,prevailing,,0,0,0,0,0,1,0\n" +
                "240,rigid,,0,0,0,0,0,1,0\n" +
                "241,rigorous,,0,0,0,0,0,1,0\n" +
                "242,slash,,0,0,0,0,0,1,0\n" +
                "243,smash,,0,0,0,0,0,1,0\n" +
                "244,snatch,,0,0,0,0,0,1,0\n" +
                "245,spontaneous,,0,0,0,0,0,1,0\n" +
                "246,tedious,,0,0,0,0,0,1,0\n" +
                "247,tolerance,,0,0,0,0,0,1,0\n" +
                "248,tame,,0,0,0,0,0,1,0\n" +
                "249,withhold,,0,0,0,0,0,1,0\n" +
                "250,withstand,,0,0,0,0,0,1,0\n" +
                "251,acute,,0,0,0,0,0,1,0\n" +
                "252,advocate,,0,0,0,0,0,1,0\n" +
                "253,affirmation,,0,0,0,0,0,1,0\n" +
                "254,allegation,,0,0,0,0,0,1,0\n" +
                "255,assertion,,0,0,0,0,0,1,0\n" +
                "256,ballot,,0,0,0,0,0,1,0\n" +
                "257,cabinet,,0,0,0,0,0,1,0\n" +
                "258,civilian,,0,0,0,0,0,1,0\n" +
                "259,complexion,,0,0,0,0,0,1,0\n" +
                "260,condemn,,0,0,0,0,0,1,0\n" +
                "261,confrontation,,0,0,0,0,0,1,0\n" +
                "262,congress,,0,0,0,0,0,1,0\n" +
                "263,cruel,,0,0,0,0,0,1,0\n" +
                "264,democracy,,0,0,0,0,0,1,0\n" +
                "265,denounce,,0,0,0,0,0,1,0\n" +
                "266,demonstration,,0,0,0,0,0,1,0\n" +
                "267,diplomacy,,0,0,0,0,0,1,0\n" +
                "268,dictator,,0,0,0,0,0,1,0\n" +
                "269,evacuate,,0,0,0,0,0,1,0\n" +
                "270,enclosure,,0,0,0,0,0,1,0\n" +
                "271,envelop,,0,0,0,0,0,1,0\n" +
                "272,extinguish,,0,0,0,0,0,1,0\n" +
                "273,fatality,,0,0,0,0,0,1,0\n" +
                "274,famine,,0,0,0,0,0,1,0\n" +
                "275,hamper,,0,0,0,0,0,1,0\n" +
                "276,hazardous,,0,0,0,0,0,1,0\n" +
                "277,hostage,,0,0,0,0,0,1,0\n" +
                "278,ironic,,0,0,0,0,0,1,0\n" +
                "279,immense,,0,0,0,0,0,1,0\n" +
                "280,immigration,,0,0,0,0,0,1,0\n" +
                "281,inflation,,0,0,0,0,0,1,0\n" +
                "282,interference,,0,0,0,0,0,1,0\n" +
                "283,invasion,,0,0,0,0,0,1,0\n" +
                "284,mourn,,0,0,0,0,0,1,0\n" +
                "285,morale,,0,0,0,0,0,1,0\n" +
                "286,petition,,0,0,0,0,0,1,0\n" +
                "287,protest,,0,0,0,0,0,1,0\n" +
                "288,provoke,,0,0,0,0,0,1,0\n" +
                "289,rash,,0,0,0,0,0,1,0\n" +
                "290,refute,,0,0,0,0,0,1,0\n" +
                "291,reinforce,,0,0,0,0,0,1,0\n" +
                "292,reproach,,0,0,0,0,0,1,0\n" +
                "293,resistance,,0,0,0,0,0,1,0\n" +
                "294,rout,,0,0,0,0,0,1,0\n" +
                "295,sophisticated,,0,0,0,0,0,1,0\n" +
                "296,suppress,,0,0,0,0,0,1,0\n" +
                "297,surcharge,,0,0,0,0,0,1,0\n" +
                "298,suspension,,0,0,0,0,0,1,0\n" +
                "299,territorial,,0,0,0,0,0,1,0\n" +
                "300,turbulence,,0,0,0,0,0,1,0\n";

        String[] dataArray = data.split("\n");
        for (int i = 0; i < dataArray.length; i++) {

            String[] wordsArray = dataArray[i].split(",");
            int id = Integer.parseInt(wordsArray[0]);
            sqlite.Words wordsAdd = new sqlite.Words(id, wordsArray[1], "", 0, 0, 0, 0, 0, 1, 0);
            insertWords(wordsAdd);

        }

    }

    public void sampleMeaning() {
        String data = "1,1,affair,[n.],事情、業務, , , , , , \n" +
                "2,2,affluent,[adj.] ,豐富的、富裕的, , , , , , \n" +
                "3,3,attain,[v.],達到, , , , , , \n" +
                "4,4,authentic,[adj.] ,可信的、貨真價實的, , , , , , \n" +
                "5,5,audit,[n.],查帳, , , , , , \n" +
                "6,6,auction,[n.],拍賣, , , , , , \n" +
                "7,6,auction,[v.],拍賣, , , , , , \n" +
                "8,7,barter,[n.],以物易物, , , , , , \n" +
                "9,8,boost,[n.],促進、推進, , , , , , \n" +
                "10,9,cargo,[n.],貨物、船貨, , , , , , \n" +
                "11,10,circumstance,[n.],情形、環境, , , , , , \n" +
                "12,11,consignment,[n.],委託, , , , , , \n" +
                "13,12,counsel,[n.],忠告、商議, , , , , , \n" +
                "14,13,diversification,[adj.] ,變化、多樣化, , , , , , \n" +
                "15,14,expiration,[n.],期滿、終止, , , , , , \n" +
                "16,15,exploitation,[n.],開發、開採, , , , , , \n" +
                "17,16,exemption,[n.],解除、免除、免稅, , , , , , \n" +
                "18,17,flourish,[v.],興隆、活躍, , , , , , \n" +
                "19,18,freight,[n.],貨物, , , , , , \n" +
                "20,18,freight,[v.],裝貨、使充滿, , , , , , \n" +
                "21,19,genuine,[adj.] ,真正的, , , , , , \n" +
                "22,20,grant,[v.],許可、授予, , , , , , \n" +
                "23,21,imposition,[n.],強迫接受, , , , , , \n" +
                "24,22,inevitable,[adj.] ,不可避免的, , , , , , \n" +
                "25,23,lease,[n.],租約、租契 , , , , , , \n" +
                "26,23,lease,[v.],出租, , , , , , \n" +
                "27,24,merchandise,[n.],商品、貨物, , , , , , \n" +
                "28,24,merchandise,[v.],買賣、經營, , , , , , \n" +
                "29,25,monetary,[adj.] ,貨幣的、金融的, , , , , , \n" +
                "30,26,overdue,[adj.] ,遲到的、到期未付的, , , , , , \n" +
                "31,27,phony,[adj.] ,假冒的、偽造的, , , , , , \n" +
                "32,28,preliminary,[n.],開端、初步行動, , , , , , \n" +
                "33,28,preliminary,[adj.],初步的、預備的, , , , , , \n" +
                "34,29,prosperity,[n.],繁榮、興旺, , , , , , \n" +
                "35,30,quits,[adj.] ,相等的、兩相抵銷的, , , , , , \n" +
                "36,31,ratify,[v.],批准、認可, , , , , , \n" +
                "37,32,reimburse,[v.],償還、付還, , , , , , \n" +
                "38,33,restraint,[n.],抑制、克制, , , , , , \n" +
                "39,34,resumption,[n.],取回、恢復, , , , , , \n" +
                "40,35,retailer,[n.],零售商人, , , , , , \n" +
                "41,36,sanction,[n.],核准、約束力, , , , , , \n" +
                "42,36,sanction,[v.],批准、許可, , , , , , \n" +
                "43,37,significant,[adj.] ,重大的、有意義的, , , , , , \n" +
                "44,38,smuggle,[v.],走私, , , , , , \n" +
                "45,39,specimen,[n.],樣品、標本, , , , , , \n" +
                "46,40,subsidiary,[n.],子公司、附屬機構, , , , , , \n" +
                "47,40,subsidiary,[adj.],輔助的、補充的, , , , , , \n" +
                "48,41,tackle,[n.],用具, , , , , , \n" +
                "49,41,tackle,[v.],處理解決, , , , , , \n" +
                "50,42,tariff,[n.],關稅、價目表 , , , , , , \n" +
                "51,42,tariff,[v.], 課以關稅, , , , , , \n" +
                "52,43,adequate,[adj.] ,足夠的, , , , , , \n" +
                "53,44,anticipation,[n.],預料、預期, , , , , , \n" +
                "54,45,allotment,[n.],分配, , , , , , \n" +
                "55,46,antique,[n.],古董, , , , , , \n" +
                "56,47,assess,[v.],評估、估價, , , , , , \n" +
                "57,48,behalf,[n.],代表、利益、方面, , , , , , \n" +
                "58,49,bid,[n.],出價、努力、企圖, , , , , , \n" +
                "59,49,bid,[v.],出價、投標, , , , , , \n" +
                "60,50,boast,[v.],自誇、自豪, , , , , , \n" +
                "61,51,bond,[n.],連結、公債、合同, , , , , , \n" +
                "62,52,cease,[v.],終止、結束, , , , , , \n" +
                "63,53,collide,[v.],碰撞、衝突, , , , , , \n" +
                "64,54,compensate,[v.],補償、賠償、報酬, , , , , , \n" +
                "65,55,convey,[v.],搬運、轉讓, , , , , , \n" +
                "66,56,council,[n.],會議、商討, , , , , , \n" +
                "67,57,counselor,[n.],顧問、法律顧問, , , , , , \n" +
                "68,58,deliberation,[n.],考慮、從容、商議, , , , , , \n" +
                "69,59,diminish,[v.],減少縮減, , , , , , \n" +
                "70,60,diversify,[v.],使多樣化、使多樣性的投資, , , , , , \n" +
                "71,61,dividend,[n.],被除數、股息、額外津貼、年息, , , , , , \n" +
                "72,62,endorsement,[n.],背書、認可, , , , , , \n" +
                "73,63,estate,[n.],地產, , , , , , \n" +
                "74,64,forecast,[v.],預言、預報, , , , , , \n" +
                "75,65,hazardous,[adj.] ,冒險的, , , , , , \n" +
                "76,66,inferior,[n.],部下、晚輩, , , , , , \n" +
                "77,66,inferior,[adj.],較低的、較差的, , , , , , \n" +
                "78,67,infrastructure,[n.],公共設施、基礎建設, , , , , , \n" +
                "79,68,inflation,[n.],通貨膨脹, , , , , , \n" +
                "80,69,inventory,[n.],詳細目錄、存貨、財產清冊, , , , , , \n" +
                "81,70,investigate,[v.],調查、研究, , , , , , \n" +
                "82,71,invoice,[n.],發票、貨物, , , , , , \n" +
                "83,71,invoice,[v.],開發票、記清單, , , , , , \n" +
                "84,72,lucrative,[adj.] ,有利的、賺錢的, , , , , , \n" +
                "85,73,mortgage,[n.],抵押, , , , , , \n" +
                "86,74,patronage,[n.],保護、贊助、光顧, , , , , , \n" +
                "87,75,precise,[adj.] ,精確的, , , , , , \n" +
                "88,76,profitable,[adj.] ,有利可圖的, , , , , , \n" +
                "89,77,remittance,[n.],匯款, , , , , , \n" +
                "90,78,rebate,[n.],回扣、折扣, , , , , , \n" +
                "91,78,rebate,[v.],減少、打折, , , , , , \n" +
                "92,79,revenue,[n.],稅收、國家的收入, , , , , , \n" +
                "93,80,refund,[n.],歸還、償還額、退款, , , , , , \n" +
                "94,80,refund,[v.],退還、償還, , , , , , \n" +
                "95,81,renovate,[v.],革新、刷新, , , , , , \n" +
                "96,82,severe,[adj.] ,嚴格的、嚴峻的、嚴重的, , , , , , \n" +
                "97,83,setback,[n.],挫折, , , , , , \n" +
                "98,84,settlement,[n.],解決、殖民地, , , , , , \n" +
                "99,85,speculate,[v.],推測、思索、投機, , , , , , \n" +
                "100,86,stake,[n.],樹樁、危險, , , , , , \n" +
                "101,87,surpass,[v.],超越、勝過, , , , , , \n" +
                "102,88,surplus,[n.],剩餘、過剩, , , , , , \n" +
                "103,89,tangible,[adj.] ,切實地、有形的, , , , , , \n" +
                "104,90,urgent,[adj.] ,急迫的, , , , , , \n" +
                "105,91,vibration,[v.],振動、顫動, , , , , , \n" +
                "106,92,withdrawal,[n.],收回、撤銷、取消, , , , , , \n" +
                "107,93,wealth,[n.],財富, , , , , , \n" +
                "108,94,amendment,[n.],改正、修正, , , , , , \n" +
                "109,95,absence,[n.],不在、缺席、缺乏, , , , , , \n" +
                "110,96,adjourn,[v.],延期、休會, , , , , , \n" +
                "111,97,advisory,[adj.] ,顧問的、諮詢的, , , , , , \n" +
                "112,98,affiliate,[n.],會員, , , , , , \n" +
                "113,98,affiliate,[v.],參加、接受為會員, , , , , , \n" +
                "114,99,alter,[v.],變更、更改, , , , , , \n" +
                "115,100,barrier,[n.],障礙、隔閡, , , , , , \n" +
                "116,101,coherent,[adj.] ,一致的, , , , , , \n" +
                "117,102,coincide,[v.],一致、同時發生, , , , , , \n" +
                "118,103,compatible,[adj.] ,協調的、相容的、一致的, , , , , , \n" +
                "119,104,competence,[n.],能力、技能、許可證, , , , , , \n" +
                "120,105,compulsion,[n.],強迫、被迫, , , , , , \n" +
                "121,106,consent,[n.],承認、贊成, , , , , , \n" +
                "122,107,consent,[v.],服從、同意, , , , , , \n" +
                "123,107,concealment,[n.],隱藏、隱蔽, , , , , , \n" +
                "124,108,concede,[v.],勉強、承認、退讓, , , , , , \n" +
                "125,109,confidential,[adj.] ,機密的、祕密的, , , , , , \n" +
                "126,110,consultant,[n.],顧問、商議者、諮詢者, , , , , , \n" +
                "127,111,correspondence,[n.],相應、通信、信件, , , , , , \n" +
                "128,112,detain,[v.],使延遲、耽擱, , , , , , \n" +
                "129,113,devise,[n.],遺贈, , , , , , \n" +
                "130,113,devise,[v.],遺贈給…、設計發明、想出做出, , , , , , \n" +
                "131,114,deputy,[n.],代理人、代表, , , , , , \n" +
                "132,115,dispose,[v.],處理、安排、布置, , , , , , \n" +
                "133,116,eligible,[adj.] ,符合條件的、合格的, , , , , , \n" +
                "134,117,illustrate,[v.],闡明、圖解, , , , , , \n" +
                "135,118,obligation,[n.],義務、責任, , , , , , \n" +
                "136,119,omit,[v.],遺漏、省略, , , , , , \n" +
                "137,120,opposition,[n.],反抗、對抗, , , , , , \n" +
                "138,121,pending,[adj.] ,未決的, , , , , , \n" +
                "139,122,pact,[n.],合約、協定, , , , , , \n" +
                "140,123,plod,[v.],蹣跚地走著、努力從事, , , , , , \n" +
                "141,124,precede,[v.],在…之前, , , , , , \n" +
                "142,125,predecessor,[n.],前輩、前任, , , , , , \n" +
                "143,126,prestigious,[adj.] ,聲望很高的, , , , , , \n" +
                "144,127,proclaim,[v.],宣布、顯示, , , , , , \n" +
                "145,128,provisional,[adj.] ,臨時的、暫時的, , , , , , \n" +
                "146,129,punctual,[adj.] ,準時的、嚴守時刻的, , , , , , \n" +
                "147,130,reconciliation,[v.],使和解、使和諧, , , , , , \n" +
                "148,131,spoil,[v.],寵壞、損壞, , , , , , \n" +
                "149,132,supervise,[v.],監督、管理、指導, , , , , , \n" +
                "150,133,unanimous,[adj.] ,意見一致的、無異議的, , , , , , \n" +
                "151,134,warranty,[n.],擔保、正式授權、合理根據, , , , , , \n" +
                "152,135,aviation,[n.],飛行、航空, , , , , , \n" +
                "153,136,aggravation,[n.],加重(病情、危機)、惱怒, , , , , , \n" +
                "154,137,allergy,[n.],(醫療)敏感症、反感, , , , , , \n" +
                "155,138,bruise,[n.],瘀傷、擦傷, , , , , , \n" +
                "156,138,bruise,[v.],打傷、撞傷, , , , , , \n" +
                "157,139,clinic,[n.],診所、門診, , , , , , \n" +
                "158,140,complexion,[n.],情況、局面, , , , , , \n" +
                "159,141,deteriorate,[v.],(使)惡化, , , , , , \n" +
                "160,142,diabetes,[n.],糖尿病、多尿症, , , , , , \n" +
                "161,143,diagnose,[v.],診斷, , , , , , \n" +
                "162,144,dimension,[n.],尺寸, , , , , , \n" +
                "163,145,dominant,[adj.] ,有統治權的、占優勢的、支配的, , , , , , \n" +
                "164,146,emission,[n.],(光、熱)散發、發射、噴射, , , , , , \n" +
                "165,147,evaporate,[v.],蒸發, , , , , , \n" +
                "166,148,epidemic,[n.],流行病, , , , , , \n" +
                "167,149,exhaustion,[n.],筋疲力盡、疲憊, , , , , , \n" +
                "168,150,friction,[n.],摩擦力, , , , , , \n" +
                "169,151,gravity,[n.],地心引力, , , , , , \n" +
                "170,152,handicapped,[adj.] ,殘障的, , , , , , \n" +
                "171,153,immunity,[n.],免疫力、豁免權, , , , , , \n" +
                "172,154,impulsive,[adj.] ,衝動的, , , , , , \n" +
                "173,155,infant,[n.],幼兒, , , , , , \n" +
                "174,156,meteorological,[adj.] ,氣象的, , , , , , \n" +
                "175,157,molecule,[n.],(化)分子、微粒, , , , , , \n" +
                "176,158,orbit,[n.] / [v.],軌道 / 沿軌道而行, , , , , , \n" +
                "177,158,orbit,[n.] / [v.],軌道 / 沿軌道而行, , , , , , \n" +
                "178,159,prescription,[n.],藥方, , , , , , \n" +
                "179,160,prevail,[v.],盛行, , , , , , \n" +
                "180,161,precedent,[n.],先例, , , , , , \n" +
                "181,162,prevalent,[adj.] ,普遍的、流行的, , , , , , \n" +
                "182,163,psychiatrist,[n.],精神病學家, , , , , , \n" +
                "183,164,remedy,[n.],藥物、治療法, , , , , , \n" +
                "184,165,saturate,[v.],使飽和、使充滿, , , , , , \n" +
                "185,166,simulate,[v.],模擬、模仿, , , , , , \n" +
                "186,167,skeleton,[n.],骨架、骨骼, , , , , , \n" +
                "187,168,symptom,[n.],症狀, , , , , , \n" +
                "188,169,tentative,[adj.] ,試驗性的、暫定的, , , , , , \n" +
                "189,170,unconscious,[adj.] ,不省人事的、未發覺的, , , , , , \n" +
                "190,171,vaccine,[n.],疫苗, , , , , , \n" +
                "191,171,vaccine,[adj.],疫苗的, , , , , , \n" +
                "192,172,vulnerable,[adj.] ,易受攻擊的、有弱點的, , , , , , \n" +
                "193,173,vitality,[n.],活力、生命力、生動性, , , , , , \n" +
                "194,174,wholesome,[adj.] ,有益健康的, , , , , , \n" +
                "195,175,absurd,[adj.] ,荒謬的、可笑的, , , , , , \n" +
                "196,176,agricultural,[adj.] ,農業的, , , , , , \n" +
                "197,177,apt,[adj.] ,易於…的、有..傾向的, , , , , , \n" +
                "198,178,articulate,[v.],用關節連接、清晰明白的說 , , , , , , \n" +
                "199,178,articulate,[adj.],有關節的、發音清晰的, , , , , , \n" +
                "200,179,civilization,[n.],文明、教化, , , , , , \n" +
                "201,180,citation,[n.],引用, , , , , , \n" +
                "202,181,concise,[adj.] ,簡明的、幹練的, , , , , , \n" +
                "203,182,curriculum,[n.],課程, , , , , , \n" +
                "204,183,dormitory,[n.],宿舍, , , , , , \n" +
                "205,184,dictate,[n.],命令、要求, , , , , , \n" +
                "206,184,dictate,[v.],口述, , , , , , \n" +
                "207,185,essay,[n.],企圖、散文, , , , , , \n" +
                "208,185,essay,[v.],企圖、嘗試, , , , , , \n" +
                "209,186,essence,[n.],本質, , , , , , \n" +
                "210,187,ethics,[n.],倫理、道德規範, , , , , , \n" +
                "211,188,fabulous,[adj.] ,難以言喻的, , , , , , \n" +
                "212,189,faculty,[n.],才能、全體教員、學院科系, , , , , , \n" +
                "213,190,heritage,[n.],遺產, , , , , , \n" +
                "214,191,indispensable,[adj.] ,不可缺少的, , , , , , \n" +
                "215,192,integrity,[n.],正直、誠實、完整性, , , , , , \n" +
                "216,193,interpret,[v.],解釋、闡明, , , , , , \n" +
                "217,194,literature,[n.],文學、文獻, , , , , , \n" +
                "218,195,manuscript,[n.],手稿、原稿, , , , , , \n" +
                "219,196,monotonous,[adj.] ,單調的, , , , , , \n" +
                "220,197,obscure,[n.],遮蔽、使模糊, , , , , , \n" +
                "221,197,obscure,[adj.],含糊不清的, , , , , , \n" +
                "222,198,primitive,[adj.] ,原始的、簡單粗糙的, , , , , , \n" +
                "223,199,racial,[adj.] ,人種的、種族的, , , , , , \n" +
                "224,200,religious,[n.],僧侶、尼姑、傳道士, , , , , , \n" +
                "225,200,religious,[adj.],信奉宗教的、虔誠的、嚴謹的, , , , , , \n" +
                "226,201,respective,[adj.] ,分別的、各自的 , , , , , , \n" +
                "227,202,revision,[n.],修訂、修改, , , , , , \n" +
                "228,203,sanitary,[adj.] ,衛生的、清潔的, , , , , , \n" +
                "229,204,seminar,[n.],研討會、討論會, , , , , , \n" +
                "230,205,species,[n.],種類、物種, , , , , , \n" +
                "231,206,stationery,[n.],文具、信紙, , , , , , \n" +
                "232,207,stimulation,[n.],激勵、鼓舞、刺激, , , , , , \n" +
                "233,208,transcript,[n.],抄本, , , , , , \n" +
                "234,209,acclaimed,[adj.] ,受到讚揚的, , , , , , \n" +
                "235,210,ally,[n.],同盟者、同盟國, , , , , , \n" +
                "236,211,appointment,[n.],約定, , , , , , \n" +
                "237,212,banquet,[n.],宴會, , , , , , \n" +
                "238,213,betray,[v.],洩漏、出賣, , , , , , \n" +
                "239,214,clarification,[n.],澄清、淨化, , , , , , \n" +
                "240,215,comprehension,[n.],理解、包含 , , , , , , \n" +
                "241,216,conviction,[n.],堅定的信仰、定罪, , , , , , \n" +
                "242,217,deceive,[v.],欺騙、蒙蔽, , , , , , \n" +
                "243,218,decent,[adj.] ,體面的、合適的, , , , , , \n" +
                "244,219,decline,[v.],拒絕, , , , , , \n" +
                "245,220,deliberation,[n.],熟思、商議, , , , , , \n" +
                "246,221,disclose,[v.],透露、說出, , , , , , \n" +
                "247,222,discrimination,[n.],歧視, , , , , , \n" +
                "248,223,disguise,[n.],偽裝, , , , , , \n" +
                "249,223,disguise,[v.],偽裝、掩飾, , , , , , \n" +
                "250,224,dull,[v.],使遲鈍、使陰暗, , , , , , \n" +
                "251,224,dull,[adj.],感覺遲鈍的、無趣的, , , , , , \n" +
                "252,225,earnest,[adj.] ,認真的、嚴肅的, , , , , , \n" +
                "253,226,enthusiastic,[adj.] ,熱心的、狂熱的, , , , , , \n" +
                "254,227,flattery,[n.],奉承、恭維話, , , , , , \n" +
                "255,228,frank,[adj.] ,率直的、坦白的, , , , , , \n" +
                "256,229,frustrate,[n.],挫折、使感到灰心, , , , , , \n" +
                "257,230,glimpse,[n.],一瞥、一看, , , , , , \n" +
                "258,230,glimpse,[v.],瞥見, , , , , , \n" +
                "259,231,humiliate,[v.],羞辱、使丟臉, , , , , , \n" +
                "260,232,implication,[n.],含意、暗示, , , , , , \n" +
                "261,233,insult,[n.],侮辱, , , , , , \n" +
                "262,234,intimacy,[n.],親密, , , , , , \n" +
                "263,235,neglect,[v.],忽視、疏忽, , , , , , \n" +
                "264,236,odor,[n.],氣味、名聲, , , , , , \n" +
                "265,237,polite,[adj.] ,有禮貌的, , , , , , \n" +
                "266,238,precaution,[n.],預防、防範, , , , , , \n" +
                "267,239,prevailing,[adj.] ,占優勢的、主要的、流行的, , , , , , \n" +
                "268,240,rigid,[adj.] ,僵直的, , , , , , \n" +
                "269,241,rigorous,[adj.] ,嚴格的、嚴厲的, , , , , , \n" +
                "270,242,slash,[n.],猛砍, , , , , , \n" +
                "271,242,slash,[v.],大量削減、嚴厲批評, , , , , , \n" +
                "272,243,smash,[v.],破壞、粉碎, , , , , , \n" +
                "273,244,snatch,[v.],奪取、粉碎, , , , , , \n" +
                "274,245,spontaneous,[adj.] ,自發的、隨興的, , , , , , \n" +
                "275,246,tedious,[adj.] ,單調乏味的、沉悶的, , , , , , \n" +
                "276,247,tolerance,[n.],寬容、容忍, , , , , , \n" +
                "277,248,tame,[v.],馴服、服從, , , , , , \n" +
                "278,248,tame,[adj.],馴服的, , , , , , \n" +
                "279,249,withhold,[v.],拒給、抑制, , , , , , \n" +
                "280,250,withstand,[v.],承受住, , , , , , \n" +
                "281,251,acute,[adj.] ,尖銳的、敏銳的、急性的, , , , , , \n" +
                "282,252,advocate,[v.],主張、提倡, , , , , , \n" +
                "283,253,affirmation,[n.],斷言、主張, , , , , , \n" +
                "284,254,allegation,[n.],主張、辯解, , , , , , \n" +
                "285,255,assertion,[n.],主張、聲明, , , , , , \n" +
                "286,256,ballot,[n.],選票, , , , , , \n" +
                "287,256,ballot,[v.],投票表決, , , , , , \n" +
                "288,257,cabinet,[n.],櫥櫃、內閣, , , , , , \n" +
                "289,257,cabinet,[adj.],內閣的、小巧的, , , , , , \n" +
                "290,258,civilian,[n.],平民、百姓, , , , , , \n" +
                "291,259,complexion,[n.],膚色、局面, , , , , , \n" +
                "292,260,condemn,[v.],譴責、反對, , , , , , \n" +
                "293,261,confrontation,[n.],面對、對抗, , , , , , \n" +
                "294,262,congress,[n.],國會、代表大會, , , , , , \n" +
                "295,263,cruel,[adj.] ,殘酷的、殘忍的, , , , , , \n" +
                "296,264,democracy,[n.],民主政治、民主主義, , , , , , \n" +
                "297,265,denounce,[v.],公開指責、公然抨擊, , , , , , \n" +
                "298,266,demonstration,[n.],證明、遊行示威, , , , , , \n" +
                "299,267,diplomacy,[n.],外交, , , , , , \n" +
                "300,268,dictator,[n.],獨裁者, , , , , , \n" +
                "301,269,evacuate,[v.],撤退、疏散, , , , , , \n" +
                "302,270,enclosure,[n.], 圍住、圍欄, , , , , , \n" +
                "303,271,envelop,[v.],包圍、隱藏, , , , , , \n" +
                "304,272,extinguish,[v.],消滅、使不存在, , , , , , \n" +
                "305,273,fatality,[n.],災難、意外死亡, , , , , , \n" +
                "306,274,famine,[n.],飢荒, , , , , , \n" +
                "307,275,hamper,[n.],大籃子, , , , , , \n" +
                "308,275,hamper,[v.],妨礙、牽制, , , , , , \n" +
                "309,276,hazardous,[adj.] ,冒險的, , , , , , \n" +
                "310,277,hostage,[n.],人質、抵押品, , , , , , \n" +
                "311,278,ironic,[adj.] ,諷刺的、具有諷刺意義的, , , , , , \n" +
                "312,279,immense,[adj.] ,廣大的、巨大的, , , , , , \n" +
                "313,280,immigration,[n.],移民、移居, , , , , , \n" +
                "314,281,inflation,[n.],通貨膨脹, , , , , , \n" +
                "315,282,interference,[n.],衝突、干涉, , , , , , \n" +
                "316,283,invasion,[n.],入侵、侵犯, , , , , , \n" +
                "317,284,mourn,[v.],哀悼、服喪, , , , , , \n" +
                "318,285,morale,[n.],民心、士氣、鬥志, , , , , , \n" +
                "319,286,petition,[n.],請願、請願書, , , , , , \n" +
                "320,286,petition,[v.],請願、祈求, , , , , , \n" +
                "321,287,protest,[n.],抗議、反對, , , , , , \n" +
                "322,287,protest,[v.],聲明、抗議, , , , , , \n" +
                "323,288,provoke,[v.],激怒、挑撥、煽動, , , , , , \n" +
                "324,289,rash,[n.],疹子, , , , , , \n" +
                "325,289,rash,[adj.],輕率的、魯莽的, , , , , , \n" +
                "326,290,refute,[v.],駁倒、反駁, , , , , , \n" +
                "327,291,reinforce,[v.],增援、加強, , , , , , \n" +
                "328,292,reproach,[n.],責備、恥辱, , , , , , \n" +
                "329,292,reproach,[v.],責備、恥辱, , , , , , \n" +
                "330,293,resistance,[n.],反抗、抵抗力, , , , , , \n" +
                "331,294,rout,[n.],潰敗、大敗, , , , , , \n" +
                "332,294,rout,[v.],擊敗、擊潰, , , , , , \n" +
                "333,295,sophisticated,[adj.] ,久經事故的、精密的, , , , , , \n" +
                "334,296,suppress,[v.],抑制、鎮壓, , , , , , \n" +
                "335,297,surcharge,[n.],超載、額外費, , , , , , \n" +
                "336,297,surcharge,[v.],使裝載過多、對..額外收費, , , , , , \n" +
                "337,298,suspension,[n.],懸掛、終止、未決, , , , , , \n" +
                "338,299,territorial,[adj.] ,領土的, , , , , , \n" +
                "339,300,turbulence,[n.],騷動、動盪, , , , , , \n";
        String[] dataArray = data.split("\n");
        for (int i = 0; i < dataArray.length; i++) {
            String[] wordsArray = dataArray[i].split(",");
            int sub_id = Integer.parseInt(wordsArray[0]);
            int id = Integer.parseInt(wordsArray[1]);
            Meaning meaningAdd = new Meaning(sub_id, id, wordsArray[2], wordsArray[3], wordsArray[4], "", "", "", "", "", "", "");
            insertMeaning(meaningAdd);
        }

    }

    public void sampleExp() {
        for (int i = 1; i <= 300; i++) {
            Exp expAdd = new Exp("user_test", i, 0, 0, 0, "");
            insertExp(expAdd);
        }
    }


}
