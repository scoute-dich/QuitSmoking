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

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.os.Environment
import android.os.Handler

import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ImageView
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import de.baumann.quitsmoking.R
import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class ActivityFiles : AppCompatActivity() {
    private var lv: ListView? = null
    private var db: DbAdapterFiles? = null
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferenceManager.setDefaultValues(this, R.xml.user_settings, false)
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPref.edit().putString("files_startFolder",
                getExternalFilesDir(null)?.path).apply()
        setContentView(R.layout.activity_files)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        setTitle(R.string.choose_title)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        lv = findViewById(R.id.dialogList)

        //calling Notes_DbAdapter
        db = DbAdapterFiles(this@ActivityFiles)
        db!!.open()
        val haswriteExternalStorage = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (haswriteExternalStorage == PackageManager.PERMISSION_GRANTED) {
            setFilesList()
        }
        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val action = intent.action
        PreferenceManager.setDefaultValues(this, R.xml.user_settings, false)
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        if ("file_chooseAttachment" == action) {
            lv!!.onItemClickListener = OnItemClickListener { parent, view, position, id ->
                val row2 = lv!!.getItemAtPosition(position) as Cursor
                val files_attachment = row2.getString(row2.getColumnIndexOrThrow("files_attachment"))
                val files_title = row2.getString(row2.getColumnIndexOrThrow("files_title"))
                val pathFile = File(files_attachment)
                when {
                    pathFile.isDirectory -> {
                        try {
                            sharedPref.edit().putString("files_startFolder", files_attachment).apply()
                            setFilesList()
                        } catch (e: Exception) {
                            Snackbar.make(lv!!, R.string.toast_directory, Snackbar.LENGTH_LONG).show()
                        }
                    }
                    files_attachment == "" -> {
                        try {
                            val pathActual = File(sharedPref.getString("files_startFolder",
                                    getExternalFilesDir(null)?.path))
                            sharedPref.edit().putString("files_startFolder", pathActual.parent).apply()
                            setFilesList()
                        } catch (e: Exception) {
                            Snackbar.make(lv!!, R.string.toast_directory, Snackbar.LENGTH_LONG).show()
                        }
                    }
                    else -> {
                        sharedPref.edit().putString("handleTextAttachment", files_attachment).apply()
                        sharedPref.edit().putString("handleTextAttachmentTitle", files_title).apply()
                        finish()
                    }
                }
            }
        }
    }

    private fun setFilesList() {
        deleteDatabase("files_DB_v01.db")
        val f = File(sharedPref!!.getString("files_startFolder",
                getExternalFilesDir(null)?.path))
        val files = f.listFiles()

        // looping through all items <item>
        if (files.isEmpty()) {
            Snackbar.make(lv!!, R.string.toast_files, Snackbar.LENGTH_LONG).show()
        }
        for (file in files) {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val file_Name = file.name
            val file_Size = getReadableFileSize(file.length())
            val file_date = formatter.format(Date(file.lastModified()))
            val file_path = file.absolutePath
            val file_ext: String = if (file.isDirectory) {
                "."
            } else {
                file.absolutePath.substring(file.absolutePath.lastIndexOf("."))
            }
            db!!.open()
            if (db!!.isExist(file_Name)) {
                Log.i(ContentValues.TAG, "Entry exists$file_Name")
            } else {
                db!!.insert(file_Name, file_Size, file_ext, file_path, file_date)
            }
        }
        try {
            db!!.insert("...", "", "", "", "")
        } catch (e: Exception) {
            Snackbar.make(lv!!, R.string.toast_directory, Snackbar.LENGTH_LONG).show()
        }

        //display data
        val layoutstyle = R.layout.item_list
        val xml_id = intArrayOf(
                R.id.textView_title_notes,
                R.id.textView_des_notes,
                R.id.textView_create_notes
        )
        val column = arrayOf(
                "files_title",
                "files_content",
                "files_creation"
        )
        val row = db!!.fetchAllData()
        val adapter: SimpleCursorAdapter = object : SimpleCursorAdapter(this@ActivityFiles, layoutstyle, row, column, xml_id, 0) {
            override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
                val row2 = lv!!.getItemAtPosition(position) as Cursor
                val files_icon = row2.getString(row2.getColumnIndexOrThrow("files_icon"))
                val files_attachment = row2.getString(row2.getColumnIndexOrThrow("files_attachment"))
                val pathFile = File(files_attachment)
                val v = super.getView(position, convertView, parent)
                val iv = v.findViewById<ImageView>(R.id.icon_notes)
                iv.visibility = View.VISIBLE
                if (pathFile.isDirectory) {
                    iv.setImageResource(R.drawable.folder)
                } else {
                    when (files_icon) {
                        "" -> Handler().postDelayed({ iv.setImageResource(R.drawable.arrow_up) }, 200)
                        ".gif", ".bmp", ".tiff", ".svg", ".png", ".jpg", ".JPG", ".jpeg" -> try {
                            Glide.with(this@ActivityFiles)
                                    .load(files_attachment) // or URI/path
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .override(76, 76)
                                    .centerCrop()
                                    .into(iv) //imageView to set thumbnail to
                        } catch (e: Exception) {
                            Log.w("HHS_Moodle", "Error load thumbnail", e)
                            iv.setImageResource(R.drawable.file_image)
                        }
                        ".m3u8", ".mp3", ".wma", ".midi", ".wav", ".aac", ".aif", ".amp3", ".weba", ".ogg" -> iv.setImageResource(R.drawable.file_music)
                        ".mpeg", ".mp4", ".webm", ".qt", ".3gp", ".3g2", ".avi", ".f4v", ".flv", ".h261", ".h263", ".h264", ".asf", ".wmv" -> try {
                            Glide.with(this@ActivityFiles)
                                    .load(files_attachment) // or URI/path
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .override(76, 76)
                                    .centerCrop()
                                    .into(iv) //imageView to set thumbnail to
                        } catch (e: Exception) {
                            Log.w("HHS_Moodle", "Error load thumbnail", e)
                            iv.setImageResource(R.drawable.file_video)
                        }
                        ".vcs", ".vcf", ".css", ".ics", ".conf", ".config", ".java", ".html" -> iv.setImageResource(R.drawable.file_xml)
                        ".apk" -> iv.setImageResource(R.drawable.android)
                        ".pdf" -> iv.setImageResource(R.drawable.file_pdf)
                        ".rtf", ".csv", ".txt", ".doc", ".xls", ".ppt", ".docx", ".pptx", ".xlsx", ".odt", ".ods", ".odp" -> iv.setImageResource(R.drawable.file_document)
                        ".zip", ".rar" -> iv.setImageResource(R.drawable.zip_box)
                        else -> iv.setImageResource(R.drawable.file)
                    }
                }
                return v
            }
        }
        lv!!.adapter = adapter
        lv!!.onItemLongClickListener = OnItemLongClickListener { parent, view, position, id ->
            val row2 = lv!!.getItemAtPosition(position) as Cursor
            val files_attachment = row2.getString(row2.getColumnIndexOrThrow("files_attachment"))
            val pathFile = File(files_attachment)
            val delete = getString(R.string.note_remove_note) + "?"
            if (pathFile.isDirectory) {
                val snackbar = Snackbar
                        .make(lv!!, delete, Snackbar.LENGTH_LONG)
                        .setAction(R.string.yes) {
                            sharedPref.edit().putString("files_startFolder", pathFile.parent).apply()
                            deleteRecursive(pathFile)
                            setFilesList()
                        }
                snackbar.show()
            } else {
                val snackbar = Snackbar
                        .make(lv!!, delete, Snackbar.LENGTH_LONG)
                        .setAction(R.string.yes) {
                            pathFile.delete()
                            setFilesList()
                        }
                snackbar.show()
            }
            true
        }
    }

    private fun deleteRecursive(fileOrDirectory: File) {
        if (fileOrDirectory.isDirectory) {
            for (child in fileOrDirectory.listFiles()) {
                deleteRecursive(child)
            }
        }
        fileOrDirectory.delete()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private fun getReadableFileSize(size: Long): String {
            val BYTES_IN_KILOBYTES = 1024
            val dec = DecimalFormat("###.#")
            val KILOBYTES = " KB"
            val MEGABYTES = " MB"
            val GIGABYTES = " GB"
            var fileSize = 0f
            var suffix = KILOBYTES
            if (size > BYTES_IN_KILOBYTES) {
                fileSize = size / BYTES_IN_KILOBYTES.toFloat()
                if (fileSize > BYTES_IN_KILOBYTES) {
                    fileSize /= BYTES_IN_KILOBYTES
                    if (fileSize > BYTES_IN_KILOBYTES) {
                        fileSize /= BYTES_IN_KILOBYTES
                        suffix = GIGABYTES
                    } else {
                        suffix = MEGABYTES
                    }
                }
            }
            return dec.format(fileSize.toDouble()) + suffix
        }
    }
}