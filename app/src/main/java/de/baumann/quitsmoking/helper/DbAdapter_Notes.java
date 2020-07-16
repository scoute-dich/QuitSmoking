package de.baumann.quitsmoking.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

import de.baumann.quitsmoking.R;


public class DbAdapter_Notes {

    //define static variable
    private static final int dbVersion =6;
    private static final String dbName = "notes_DB_v01.db";
    private static final String dbTable = "notes";

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context,dbName,null, dbVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS "+dbTable+" (_id INTEGER PRIMARY KEY autoincrement, note_title, note_content, note_icon, note_attachment, note_creation, UNIQUE(note_title))");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+dbTable);
            onCreate(db);
        }
    }

    //establish connection with SQLiteDataBase
    private final Context c;
    private SQLiteDatabase sqlDb;

    public DbAdapter_Notes(Context context) {
        this.c = context;
    }
    public void open() throws SQLException {
        DatabaseHelper dbHelper = new DatabaseHelper(c);
        sqlDb = dbHelper.getWritableDatabase();
    }

    //insert data
    public void insert(String note_title,String note_content,String note_icon,String note_attachment, String note_creation) {
        if(!isExist(note_title)) {
            sqlDb.execSQL("INSERT INTO notes (note_title, note_content, note_icon, note_attachment, note_creation) VALUES('" + note_title + "','" + note_content + "','" + note_icon + "','" + note_attachment + "','" + note_creation + "')");
        }
    }
    //check entry already in database or not
    public boolean isExist(String note_title){
        String query = "SELECT note_title FROM notes WHERE note_title='"+note_title+"' LIMIT 1";
        @SuppressLint("Recycle") Cursor row = sqlDb.rawQuery(query, null);
        return row.moveToFirst();
    }
    //edit data
    public void update(int id,String note_title,String note_content,String note_icon,String note_attachment, String note_creation) {
        sqlDb.execSQL("UPDATE "+dbTable+" SET note_title='"+note_title+"', note_content='"+note_content+"', note_icon='"+note_icon+"', note_attachment='"+note_attachment+"', note_creation='"+note_creation+"'   WHERE _id=" + id);
    }

    //delete data
    public void delete(int id) {
        sqlDb.execSQL("DELETE FROM "+dbTable+" WHERE _id="+id);
    }


    //fetch data
    public Cursor fetchAllData(Context context) {

        PreferenceManager.setDefaultValues(context, R.xml.user_settings, false);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String[] columns = new String[]{"_id", "note_title", "note_content", "note_icon","note_attachment","note_creation"};

        if (sp.getString("sortDB", "title").equals("title")) {
            return sqlDb.query(dbTable, columns, null, null, null, null, "note_title" + " COLLATE NOCASE ASC;");

        } else if (sp.getString("sortDB", "title").equals("icon")) {
            String orderBy = "note_icon" + "," +
                    "note_title" + " COLLATE NOCASE ASC;";
            return sqlDb.query(dbTable, columns, null, null, null, null, orderBy);

        } else if (sp.getString("sortDB", "title").equals("create")) {
            String orderBy = "note_creation" + "," +
                    "note_title" + " COLLATE NOCASE ASC;";
            return sqlDb.query(dbTable, columns, null, null, null, null, orderBy);

        } else if (sp.getString("sortDB", "title").equals("attachment")) {
            String orderBy = "note_attachment" + "," +
                    "note_title" + " COLLATE NOCASE ASC;";
            return sqlDb.query(dbTable, columns, null, null, null, null, orderBy);
        }

        return null;
    }

    //fetch data by filter
    public Cursor fetchDataByFilter(String inputText,String filterColumn) throws SQLException {
        Cursor row;
        String query = "SELECT * FROM "+dbTable;
        if (inputText == null  ||  inputText.length () == 0)  {
            row = sqlDb.rawQuery(query, null);
        }else {
            query = "SELECT * FROM "+dbTable+" WHERE "+filterColumn+" like '%"+inputText+"%'";
            row = sqlDb.rawQuery(query, null);
        }
        if (row != null) {
            row.moveToFirst();
        }
        return row;
    }
}
