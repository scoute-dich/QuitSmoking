package de.baumann.quitsmoking.helper

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.preference.PreferenceManager
import de.baumann.quitsmoking.R

class DbAdapterNotes(//establish connection with SQLiteDataBase
        private val c: Context) {
    private class DatabaseHelper internal constructor(context: Context?) : SQLiteOpenHelper(context, dbName, null, dbVersion) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL("CREATE TABLE IF NOT EXISTS $dbTable (_id INTEGER PRIMARY KEY autoincrement, note_title, note_content, note_icon, note_attachment, note_creation, UNIQUE(note_title))")
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $dbTable")
            onCreate(db)
        }
    }

    private var sqlDb: SQLiteDatabase? = null

    @Throws(SQLException::class)
    fun open() {
        val dbHelper = DatabaseHelper(c)
        sqlDb = dbHelper.writableDatabase
    }

    //insert data
    fun insert(note_title: String, note_content: String, note_icon: String, note_attachment: String, note_creation: String) {
        if (!isExist(note_title)) {
            sqlDb!!.execSQL("INSERT INTO notes (note_title, note_content, note_icon, note_attachment, note_creation) VALUES('$note_title','$note_content','$note_icon','$note_attachment','$note_creation')")
        }
    }

    //check entry already in database or not
    fun isExist(note_title: String): Boolean {
        val query = "SELECT note_title FROM notes WHERE note_title='$note_title' LIMIT 1"
        @SuppressLint("Recycle") val row = sqlDb!!.rawQuery(query, null)
        return row.moveToFirst()
    }

    //edit data
    fun update(id: Int, note_title: String, note_content: String, note_icon: String, note_attachment: String, note_creation: String) {
        sqlDb!!.execSQL("UPDATE $dbTable SET note_title='$note_title', note_content='$note_content', note_icon='$note_icon', note_attachment='$note_attachment', note_creation='$note_creation'   WHERE _id=$id")
    }

    //delete data
    fun delete(id: Int) {
        sqlDb!!.execSQL("DELETE FROM $dbTable WHERE _id=$id")
    }

    //fetch data
    fun fetchAllData(context: Context?): Cursor? {
        PreferenceManager.setDefaultValues(context, R.xml.user_settings, false)
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val columns = arrayOf("_id", "note_title", "note_content", "note_icon", "note_attachment", "note_creation")
        when {
            sp.getString("sortDB", "title") == "title" -> {
                return sqlDb!!.query(dbTable, columns, null, null, null, null, "note_title" + " COLLATE NOCASE ASC;")
            }
            sp.getString("sortDB", "title") == "icon" -> {
                val orderBy = "note_icon" + "," +
                        "note_title" + " COLLATE NOCASE ASC;"
                return sqlDb!!.query(dbTable, columns, null, null, null, null, orderBy)
            }
            sp.getString("sortDB", "title") == "create" -> {
                val orderBy = "note_creation" + "," +
                        "note_title" + " COLLATE NOCASE ASC;"
                return sqlDb!!.query(dbTable, columns, null, null, null, null, orderBy)
            }
            sp.getString("sortDB", "title") == "attachment" -> {
                val orderBy = "note_attachment" + "," +
                        "note_title" + " COLLATE NOCASE ASC;"
                return sqlDb!!.query(dbTable, columns, null, null, null, null, orderBy)
            }
            else -> return null
        }
    }

    //fetch data by filter
    @Throws(SQLException::class)
    fun fetchDataByFilter(inputText: String?, filterColumn: String): Cursor? {
        val row: Cursor?
        var query = "SELECT * FROM $dbTable"
        if (inputText == null || inputText.isEmpty()) {
            row = sqlDb!!.rawQuery(query, null)
        } else {
            query = "SELECT * FROM $dbTable WHERE $filterColumn like '%$inputText%'"
            row = sqlDb!!.rawQuery(query, null)
        }
        row?.moveToFirst()
        return row
    }

    companion object {
        //define static variable
        private const val dbVersion = 6
        private const val dbName = "notes_DB_v01.db"
        private const val dbTable = "notes"
    }

}