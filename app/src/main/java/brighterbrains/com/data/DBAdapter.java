package brighterbrains.com.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by DELL on 1/23/2016.
 */
public class DBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "Name";
    public static final String KEY_DESC = "Description";
    public static final String KEY_IMAGE = "Image";
    public static final String KEY_LOCATION = "Location";
    public static final String KEY_COST = "Cost";

    private static final String TAG = "DBAdapter";
    private static final String DATABASE_NAME = "brighterbrainsdb.db";
    private static final String DATABASE_TABLE = "ItemTable";


    private static final int DATABASE_VERSION = 1;
    private static final String DB_PATH = "data/data/brighterbrains.com.brighterbrains/databases/brighterbrainsdb.db";

    private static final String CREATE_TABLE = "create table "+ DATABASE_TABLE+" (_id integer "
            + "primary key autoincrement" + ",Name text" + ",Description text" +",Image text" +",Location text" +",Cost text" + ");";


    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");

            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    // ---opens the database---
    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    // ---closes the database---
    public void close() {
        DBHelper.close();
    }


    public long insertItem(String name,String desc,String location,String image,String cost) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_DESC, desc);
        initialValues.put(KEY_LOCATION, location);
        initialValues.put(KEY_IMAGE, image);
        initialValues.put(KEY_COST, cost);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    public void deleteItem(long id) {
        db.execSQL("delete from "+DATABASE_TABLE +" where _id=" + id);
    }


    // ---retrieves a particular title---
    public Cursor getItems() throws SQLException {
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] { KEY_ROWID,
                KEY_NAME,
                KEY_DESC,
                KEY_LOCATION,
                KEY_COST,
                KEY_IMAGE

        }, null, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }


    public boolean update(long id, String name,String desc,String location,String image,String cost) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_DESC, desc);
        initialValues.put(KEY_LOCATION, location);
        initialValues.put(KEY_IMAGE, image);
        initialValues.put(KEY_COST, cost);

        int count = db.update(DATABASE_TABLE, initialValues, "_id=" + id, null);
        if (count > 0)
            return true;
        else
            return false;

    }


}


