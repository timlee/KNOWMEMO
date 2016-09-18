package sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 2016/1/30.
 */
public class SqlHelper extends SQLiteOpenHelper {

    private final String DATABASE_PATH = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/Test";
    public static final String db_name = "vocabulary.db";
    public static final int VERSION = 1;

    private static SQLiteDatabase db;

    public SqlHelper(Context context) {
        super(context, db_name, null, VERSION);
    }

    // 需要資料庫的元件呼叫這個方法，這個方法在一般的應用都不需要修改
    public static SQLiteDatabase getDatabase(Context context) {
        if (db == null || !db.isOpen()) {
            db = new SqlHelper(context).getWritableDatabase();
        }

        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建立應用程式需要的表格
        db.execSQL(tableDao.createWordsTable);
        db.execSQL(sqlite.tableDao.createMeaningTable);
        db.execSQL(sqlite.tableDao.createExpTable);
        db.execSQL(sqlite.tableDao.createCategoriesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 刪除原有的表格
        db.execSQL("DROP TABLE IF EXISTS " + sqlite.tableDao.words);
        db.execSQL("DROP TABLE IF EXISTS " + sqlite.tableDao.meaning);
        db.execSQL("DROP TABLE IF EXISTS " + sqlite.tableDao.exp);
        db.execSQL("DROP TABLE IF EXISTS " + tableDao.categories);
        //呼叫onCreate建立新版的表格
        onCreate(db);
    }

}