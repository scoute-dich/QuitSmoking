/*
    This file is part of the HHS Moodle WebApp.

    HHS Moodle WebApp is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    HHS Moodle WebApp is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with the Diaspora Native WebApp.

    If not, see <http://www.gnu.org/licenses/>.
 */
package de.baumann.quitsmoking.helper

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

internal class DbAdapterFiles(//establish connection with SQLiteDataBase
        private val c: Context) {
    private class DatabaseHelper internal constructor(context: Context?) : SQLiteOpenHelper(context, dbName, null, dbVersion) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL("CREATE TABLE IF NOT EXISTS $dbTable (_id INTEGER PRIMARY KEY autoincrement, files_title, files_content, files_icon, files_attachment, files_creation, UNIQUE(files_title))")
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
    fun insert(files_title: String, files_content: String, files_icon: String, files_attachment: String, files_creation: String) {
        if (!isExist(files_title)) {
            sqlDb!!.execSQL("INSERT INTO files (files_title, files_content, files_icon, files_attachment, files_creation) VALUES('$files_title','$files_content','$files_icon','$files_attachment','$files_creation')")
        }
    }

    //check entry already in database or not
    fun isExist(files_title: String): Boolean {
        val query = "SELECT files_title FROM files WHERE files_title='$files_title' LIMIT 1"
        @SuppressLint("Recycle") val row = sqlDb!!.rawQuery(query, null)
        return row.moveToFirst()
    }

    //fetch data
    fun fetchAllData(): Cursor {
        val columns = arrayOf("_id", "files_title", "files_content", "files_icon", "files_attachment", "files_creation")
        val orderBy = "files_icon" + "," +
                "files_title" + " COLLATE NOCASE ASC;"
        return sqlDb!!.query(dbTable, columns, null, null, null, null, orderBy)
    }

    companion object {
        //define static variable
        private const val dbVersion = 6
        private const val dbName = "files_DB_v01.db"
        private const val dbTable = "files"
    }

}