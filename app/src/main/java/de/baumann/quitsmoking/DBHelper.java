package de.baumann.quitsmoking;

import android.content.ContentValues;
import android.database.Cursor;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by juergen on 16.06.16. Licensed under GPL.
 */
public class DBHelper {

    public static final String IMAGE_ID = "id";
    public static final String IMAGE = "image";
    private final Context mContext;

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "Images.db";
    private static final int DATABASE_VERSION = 1;

    private static final String IMAGES_TABLE = "ImagesTable";

    private static final String CREATE_IMAGES_TABLE =
            "CREATE TABLE " + IMAGES_TABLE + " (" +
                    IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + IMAGE + " BLOB NOT NULL );";


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_IMAGES_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_IMAGES_TABLE);
            onCreate(db);
        }
    }

    public void Reset() {
        mDbHelper.onUpgrade(this.mDb, 1, 1);
    }

    public DBHelper(Context ctx) {
        mContext = ctx;
        mDbHelper = new DatabaseHelper(mContext);
    }

    public DBHelper open() throws SQLException {
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    // Insert the image to the Sqlite DB
    public void insertImage(byte[] imageBytes) {
        ContentValues cv = new ContentValues();
        cv.put(IMAGE, imageBytes);
        mDb.insert(IMAGES_TABLE, null, cv);
    }

    // Get the image from SQLite DB
    // We will just get the last image we just saved for convenience...
    public byte[] retreiveImageFromDB() {
        Cursor cur = mDb.query(true, IMAGES_TABLE, new String[]{IMAGE,},
                null, null, null, null,
                IMAGE_ID + " DESC", "1");
        if (cur.moveToFirst()) {
            byte[] blob = cur.getBlob(cur.getColumnIndex(IMAGE));
            cur.close();
            return blob;
        }
        cur.close();
        return null;
    }
}
