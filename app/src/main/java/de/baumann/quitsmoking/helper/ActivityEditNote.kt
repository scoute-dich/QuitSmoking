package de.baumann.quitsmoking.helper

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.mlsdev.rximagepicker.RxImageConverters
import com.mlsdev.rximagepicker.RxImagePicker
import com.mlsdev.rximagepicker.Sources
import de.baumann.quitsmoking.R
import org.apache.commons.io.FilenameUtils
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ActivityEditNote : AppCompatActivity() {
    private lateinit var attachment: Button
    private lateinit var attachmentRem: ImageButton
    private lateinit var attachmentCam: ImageButton
    private lateinit var titleInput: EditText
    private lateinit var textInput: EditText
    private lateinit var sharedPref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_edit)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        setTitle(R.string.note_edit)
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        PreferenceManager.setDefaultValues(this@ActivityEditNote, R.xml.user_settings, false)
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this@ActivityEditNote)
        val priority = sharedPref.getString("handleTextIcon", "")
        val file = sharedPref.getString("handleTextAttachment", "")
        val attName = file!!.substring(file.lastIndexOf("/") + 1)
        attachmentRem = findViewById(R.id.button_rem)
        attachment = findViewById(R.id.button_att)
        attachmentCam = findViewById(R.id.button_cam)
        val att = getString(R.string.note_attachment) + ": " + attName
        if (attName == "") {
            attachment.setText(R.string.choose_att)
            attachmentRem.visibility = View.GONE
            attachmentCam.visibility = View.VISIBLE
        } else {
            attachment.text = att
            attachmentRem.visibility = View.VISIBLE
            attachmentCam.visibility = View.GONE
        }
        val file2 = File(file)
        if (!file2.exists()) {
            attachment.setText(R.string.choose_att)
            attachmentRem.visibility = View.GONE
            attachmentCam.visibility = View.VISIBLE
        }
        titleInput = findViewById(R.id.note_title_input)
        textInput = findViewById(R.id.note_text_input)
        HelperMain.showKeyboard(this@ActivityEditNote, titleInput)
        titleInput.setText(sharedPref.getString("handleTextTitle", ""))
        titleInput.setSelection(titleInput.text.length)
        textInput.setText(sharedPref.getString("handleTextText", ""))
        textInput.setSelection(textInput.text.length)
        titleInput.setOnTouchListener(OnTouchListener { arg0, _ ->
            sharedPref.edit().putString("editTextFocus", "title").apply()
            false
        })
        textInput.setOnTouchListener(OnTouchListener { arg0, arg1 ->
            sharedPref.edit().putString("editTextFocus", "text").apply()
            false
        })
        attachment.setOnClickListener(View.OnClickListener {
            val mainIntent = Intent(this@ActivityEditNote, ActivityFiles::class.java)
            mainIntent.action = "file_chooseAttachment"
            startActivity(mainIntent)
        })
        attachmentRem.setOnClickListener(View.OnClickListener {
            sharedPref.edit().putString("handleTextAttachment", "").apply()
            attachment.setText(R.string.choose_att)
            attachmentRem.visibility = View.GONE
            attachmentCam.visibility = View.VISIBLE
        })
        attachmentCam.setOnClickListener(View.OnClickListener {
            val options = arrayOf<CharSequence>(
                    getString(R.string.choose_gallery),
                    getString(R.string.choose_camera))
            val dialog = AlertDialog.Builder(this@ActivityEditNote)
            dialog.setPositiveButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
            dialog.setItems(options) { _, item ->
                if (options[item] == getString(R.string.choose_gallery)) {
                    RxImagePicker.with(this@ActivityEditNote).requestImage(Sources.GALLERY)
                            .flatMap { uri -> RxImageConverters.uriToFile(this@ActivityEditNote, uri, File(getExternalFilesDir(null).toString() + "/Android/data/de.baumann.quitsmoking/" + HelperMain.newFileName())) }.subscribe { file -> // Do something with your file copy
                                sharedPref.edit().putString("handleTextAttachment", file.absolutePath).apply()
                                attachment.text = FilenameUtils.getName(file.absolutePath)
                            }
                }
                if (options[item] == getString(R.string.choose_camera)) {
                    RxImagePicker.with(this@ActivityEditNote).requestImage(Sources.CAMERA)
                            .flatMap { uri -> RxImageConverters.uriToFile(this@ActivityEditNote, uri, File(getExternalFilesDir(null).toString() + "/Android/data/de.baumann.quitsmoking/" + HelperMain.newFileName())) }.subscribe { file -> // Do something with your file copy
                                sharedPref.edit().putString("handleTextAttachment", file.absolutePath).apply()
                                attachment.text = FilenameUtils.getName(file.absolutePath)
                                val f = lastFileModified(getExternalFilesDir(null).toString() + File.separator + "Pictures")
                                f!!.delete()
                            }
                }
            }
            dialog.show()
        })
        val be = findViewById<ImageButton>(R.id.imageButtonPri)
        val ibPaste = findViewById<ImageButton>(R.id.imageButtonPaste)
        assert(be != null)
        when (priority) {
            "1" -> {
                be!!.setImageResource(R.drawable.emoticon_neutral)
                sharedPref.edit().putString("handleTextIcon", "1").apply()
            }
            "2" -> {
                be!!.setImageResource(R.drawable.emoticon_happy)
                sharedPref.edit().putString("handleTextIcon", "2").apply()
            }
            "3" -> {
                be!!.setImageResource(R.drawable.emoticon_sad)
                sharedPref.edit().putString("handleTextIcon", "3").apply()
            }
            "4" -> {
                be!!.setImageResource(R.drawable.emoticon)
                sharedPref.edit().putString("handleTextIcon", "4").apply()
            }
            "5" -> {
                be!!.setImageResource(R.drawable.emoticon_cool)
                sharedPref.edit().putString("handleTextIcon", "5").apply()
            }
            "6" -> {
                be!!.setImageResource(R.drawable.emoticon_dead)
                sharedPref.edit().putString("handleTextIcon", "6").apply()
            }
            "7" -> {
                be!!.setImageResource(R.drawable.emoticon_excited)
                sharedPref.edit().putString("handleTextIcon", "7").apply()
            }
            "8" -> {
                be!!.setImageResource(R.drawable.emoticon_tongue)
                sharedPref.edit().putString("handleTextIcon", "8").apply()
            }
            "9" -> {
                be!!.setImageResource(R.drawable.emoticon_devil)
                sharedPref.edit().putString("handleTextIcon", "9").apply()
            }
            "" -> {
                be!!.setImageResource(R.drawable.emoticon_neutral)
                sharedPref.edit().putString("handleTextIcon", "1").apply()
            }
        }
        be!!.setOnClickListener {
            val items = arrayOf(
                    Item(getString(R.string.text_tit_1), R.drawable.emoticon_neutral),
                    Item(getString(R.string.text_tit_2), R.drawable.emoticon_happy),
                    Item(getString(R.string.text_tit_3), R.drawable.emoticon_sad),
                    Item(getString(R.string.text_tit_4), R.drawable.emoticon),
                    Item(getString(R.string.text_tit_5), R.drawable.emoticon_cool),
                    Item(getString(R.string.text_tit_6), R.drawable.emoticon_dead),
                    Item(getString(R.string.text_tit_7), R.drawable.emoticon_excited),
                    Item(getString(R.string.text_tit_8), R.drawable.emoticon_tongue),
                    Item(getString(R.string.text_tit_9), R.drawable.emoticon_devil)
            )
            val adapter: ListAdapter = object : ArrayAdapter<Item?>(
                    this@ActivityEditNote,
                    android.R.layout.select_dialog_item,
                    android.R.id.text1,
                    items) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    //Use super class to create the View
                    val v = super.getView(position, convertView, parent)
                    val tv = v.findViewById<TextView>(android.R.id.text1)
                    tv.textSize = 18f
                    tv.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0)
                    //Add margin between image and text (support various screen densities)
                    val dp5 = (24 * resources.displayMetrics.density + 0.5f).toInt()
                    tv.compoundDrawablePadding = dp5
                    return v
                }
            }
            AlertDialog.Builder(this@ActivityEditNote)
                    .setPositiveButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
                    .setAdapter(adapter) { _, item ->
                        if (item == 0) {
                            be.setImageResource(R.drawable.emoticon_neutral)
                            sharedPref.edit().putString("handleTextIcon", "1").apply()
                        } else if (item == 1) {
                            be.setImageResource(R.drawable.emoticon_happy)
                            sharedPref.edit().putString("handleTextIcon", "2").apply()
                        } else if (item == 2) {
                            be.setImageResource(R.drawable.emoticon_sad)
                            sharedPref.edit().putString("handleTextIcon", "3").apply()
                        } else if (item == 3) {
                            be.setImageResource(R.drawable.emoticon)
                            sharedPref.edit().putString("handleTextIcon", "4").apply()
                        } else if (item == 4) {
                            be.setImageResource(R.drawable.emoticon_cool)
                            sharedPref.edit().putString("handleTextIcon", "5").apply()
                        } else if (item == 5) {
                            be.setImageResource(R.drawable.emoticon_dead)
                            sharedPref.edit().putString("handleTextIcon", "6").apply()
                        } else if (item == 6) {
                            be.setImageResource(R.drawable.emoticon_excited)
                            sharedPref.edit().putString("handleTextIcon", "7").apply()
                        } else if (item == 7) {
                            be.setImageResource(R.drawable.emoticon_tongue)
                            sharedPref.edit().putString("handleTextIcon", "8").apply()
                        } else if (item == 8) {
                            be.setImageResource(R.drawable.emoticon_devil)
                            sharedPref.edit().putString("handleTextIcon", "9").apply()
                        }
                    }.show()
        }
        ibPaste.setOnClickListener {
            val options = arrayOf<CharSequence>(
                    getString(R.string.paste_date),
                    getString(R.string.paste_time),
                    getString(R.string.paste_line))
            AlertDialog.Builder(this@ActivityEditNote)
                    .setPositiveButton(R.string.cancel) { dialog, whichButton -> dialog.dismiss() }
                    .setItems(options) { dialog, item ->
                        if (options[item] == getString(R.string.paste_date)) {
                            val dateFormat = sharedPref.getString("dateFormat", "1")
                            when (dateFormat) {
                                "1" -> {
                                    val date = Date()
                                    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                    val dateNow = format.format(date)
                                    if (sharedPref.getString("editTextFocus", "") == "text") {
                                        textInput.text.insert(textInput.selectionStart, dateNow)
                                    } else {
                                        titleInput.text.insert(titleInput.selectionStart, dateNow)
                                    }
                                }
                                "2" -> {
                                    val date2 = Date()
                                    val format2 = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                                    val dateNow2 = format2.format(date2)
                                    if (sharedPref.getString("editTextFocus", "") == "text") {
                                        textInput.text.insert(textInput.selectionStart, dateNow2)
                                    } else {
                                        titleInput.text.insert(titleInput.selectionStart, dateNow2)
                                    }
                                }
                            }
                        }
                        if (options[item] == getString(R.string.paste_time)) {
                            val date = Date()
                            val format = SimpleDateFormat("HH:mm", Locale.getDefault())
                            val timeNow = format.format(date)
                            if (sharedPref.getString("editTextFocus", "") == "text") {
                                textInput.text.insert(textInput.selectionStart, timeNow)
                            } else {
                                titleInput.text.insert(titleInput.selectionStart, timeNow)
                            }
                        }
                        if (options[item] == getString(R.string.paste_line)) {
                            if (sharedPref.getString("editTextFocus", "") == "text") {
                                textInput.text.insert(textInput.selectionStart, "==========")
                            } else {
                                titleInput.text.insert(titleInput.selectionStart, "==========")
                            }
                        }
                    }.show()
        }
    }

    private class Item internal constructor(val text: String, val icon: Int) {
        override fun toString(): String {
            return text
        }

    }

    override fun onResume() {
        super.onResume() //To change body of overridden methods use File | Settings | File Templates.
        val file = sharedPref.getString("handleTextAttachment", "")
        val attName = file!!.substring(file.lastIndexOf("/") + 1)
        attachmentRem = findViewById(R.id.button_rem)
        attachment = findViewById(R.id.button_att)
        attachmentCam = findViewById(R.id.button_cam)
        val att = getString(R.string.note_attachment) + ": " + attName
        if (attName == "") {
            attachment.setText(R.string.choose_att)
            attachmentRem.visibility = View.GONE
            attachmentCam.visibility = View.VISIBLE
        } else {
            attachment.text = att
            attachmentRem.visibility = View.VISIBLE
            attachmentCam.visibility = View.GONE
        }
        val file2 = File(file)
        if (!file2.exists()) {
            attachment.setText(R.string.choose_att)
            attachmentRem.visibility = View.GONE
            attachmentCam.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        val snackbar = Snackbar
                .make(titleInput, R.string.toast_save, Snackbar.LENGTH_LONG)
                .setAction(R.string.yes) {
                    sharedPref.edit()
                            .putString("handleTextTitle", "")
                            .putString("handleTextText", "")
                            .putString("handleTextIcon", "")
                            .putString("handleTextAttachment", "")
                            .putString("handleTextCreate", "")
                            .putString("editTextFocus", "")
                            .putString("handleTextSeqno", "")
                            .apply()
                    finish()
                }
        snackbar.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == android.R.id.home) {
            val snackbar = Snackbar
                    .make(titleInput, R.string.toast_save, Snackbar.LENGTH_LONG)
                    .setAction(R.string.yes) {
                        sharedPref.edit()
                                .putString("handleTextTitle", "")
                                .putString("handleTextText", "")
                                .putString("handleTextIcon", "")
                                .putString("handleTextAttachment", "")
                                .putString("handleTextCreate", "")
                                .putString("editTextFocus", "")
                                .putString("handleTextSeqno", "")
                                .apply()
                        finish()
                    }
            snackbar.show()
        }
        if (id == R.id.action_save) {
            val db = DbAdapterNotes(this@ActivityEditNote)
            db.open()
            val inputTitle = titleInput.text.toString().trim { it <= ' ' }
            val inputContent = textInput.text.toString().trim { it <= ' ' }
            val attachment = sharedPref.getString("handleTextAttachment", "")
            val create = sharedPref.getString("handleTextCreate", "")
            val seqno = sharedPref.getString("handleTextSeqno", "")
            if (seqno!!.isEmpty()) {
                try {
                    if (db.isExist(inputTitle)) {
                        Snackbar.make(titleInput, getString(R.string.toast_newTitle), Snackbar.LENGTH_LONG).show()
                    } else {
                        db.insert(inputTitle, inputContent, sharedPref.getString("handleTextIcon", "")!!, attachment!!, create!!)
                        sharedPref.edit()
                                .putString("handleTextTitle", "")
                                .putString("handleTextText", "")
                                .putString("handleTextIcon", "")
                                .putString("handleTextAttachment", "")
                                .putString("handleTextCreate", "")
                                .putString("editTextFocus", "")
                                .putString("handleTextSeqno", "")
                                .apply()
                        finish()
                    }
                } catch (e: Exception) {
                    Log.w("QS", "Error Package name not found ", e)
                    val snackbar = Snackbar
                            .make(titleInput, R.string.toast_notSave, Snackbar.LENGTH_LONG)
                    snackbar.show()
                }
            } else {
                try {
                    db.update(seqno.toInt(), inputTitle, inputContent, sharedPref.getString("handleTextIcon", "")!!, attachment!!, create!!)
                    sharedPref.edit()
                            .putString("handleTextTitle", "")
                            .putString("handleTextText", "")
                            .putString("handleTextIcon", "")
                            .putString("handleTextAttachment", "")
                            .putString("handleTextCreate", "")
                            .putString("editTextFocus", "")
                            .putString("handleTextSeqno", "")
                            .apply()
                    finish()
                } catch (e: Exception) {
                    Log.w("QS", "Error Package name not found ", e)
                    val snackbar = Snackbar
                            .make(titleInput, R.string.toast_notSave, Snackbar.LENGTH_LONG)
                    snackbar.show()
                }
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private fun lastFileModified(dir: String): File? {
            val fl = File(dir)
            val files = fl.listFiles { file -> file.isFile }
            var lastMod = Long.MIN_VALUE
            var choice: File? = null
            for (file in files) {
                if (file.lastModified() > lastMod) {
                    choice = file
                    lastMod = file.lastModified()
                }
            }
            return choice
        }
    }
}