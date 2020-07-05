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
package de.baumann.quitsmoking.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.text.util.Linkify
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import de.baumann.quitsmoking.R
import de.baumann.quitsmoking.helper.ActivityEditNote
import de.baumann.quitsmoking.helper.DbAdapterNotes
import de.baumann.quitsmoking.helper.HelperMain
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class FragmentNotes : Fragment() {
    //calling variables
    private var db: DbAdapterNotes? = null
    private var adapter: SimpleCursorAdapter? = null
    private var lv: ListView? = null
    private var filter: EditText? = null
    private lateinit var sharedPref: SharedPreferences
    private lateinit var filterLayout: RelativeLayout
    private var viewPager: ViewPager? = null
    private var tabLayout: TabLayout? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_screen_notes, container, false)
        PreferenceManager.setDefaultValues(activity, R.xml.user_settings, false)
        sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
        filterLayout = rootView.findViewById(R.id.filter_layout)
        filterLayout.visibility = View.GONE
        lv = rootView.findViewById(R.id.listNotes)
        filter = rootView.findViewById(R.id.myFilter)
        viewPager = activity!!.findViewById(R.id.viewpager)
        tabLayout = activity!!.findViewById(R.id.tabs)
        setTitle()
        val ibHidekeyboard = rootView.findViewById<ImageButton>(R.id.ib_hideKeyboard)
        ibHidekeyboard.setOnClickListener { view ->
            val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            filterLayout.visibility = View.GONE
            setTitle()
            setNotesList()
        }
        val fab: FloatingActionButton = rootView.findViewById(R.id.fab)
        fab.setOnClickListener {
            sharedPref.edit().putString("handleTextCreate", HelperMain.createDate()).apply()
            val intent = Intent(activity, ActivityEditNote::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            activity!!.startActivity(intent)
        }

        //calling Notes_DbAdapter
        db = DbAdapterNotes(activity!!)
        db!!.open()
        setNotesList()
        setHasOptionsMenu(true)
        return rootView
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isResumed && viewPager!!.currentItem == 3) {
            setNotesList()
            if (sharedPref.getString("newIntent", "false") == "true") {
                val intent = Intent(activity, ActivityEditNote::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                activity!!.startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (filterLayout.visibility == View.GONE) {
            setNotesList()
        }
        if (sharedPref.getString("newIntent", "false") == "true") {
            val intent = Intent(activity, ActivityEditNote::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            activity!!.startActivity(intent)
        }
    }

    private fun setNotesList() {

        //display data
        val layoutstyle = R.layout.item_list
        val xmlId = intArrayOf(
                R.id.textView_title_notes,
                R.id.textView_des_notes,
                R.id.textView_create_notes
        )
        val column = arrayOf(
                "note_title",
                "note_content",
                "note_creation"
        )
        val row = db!!.fetchAllData(activity)
        adapter = object : SimpleCursorAdapter(activity, layoutstyle, row, column, xmlId, 0) {
            override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
                val row2 = lv!!.getItemAtPosition(position) as Cursor
                val noteIcon = row2.getString(row2.getColumnIndexOrThrow("note_icon"))
                val noteAttachment = row2.getString(row2.getColumnIndexOrThrow("note_attachment"))
                val v = super.getView(position, convertView, parent)
                val ivIcon = v.findViewById<ImageView>(R.id.icon_notes)
                val ivAttachment = v.findViewById<ImageView>(R.id.att_notes)
                when (noteIcon) {
                    "1" -> {
                        ivIcon.setImageResource(R.drawable.emoticon_neutral)
                        sharedPref.edit().putString("handleTextIcon", "1").apply()
                    }
                    "2" -> {
                        ivIcon.setImageResource(R.drawable.emoticon_happy)
                        sharedPref.edit().putString("handleTextIcon", "2").apply()
                    }
                    "3" -> {
                        ivIcon.setImageResource(R.drawable.emoticon_sad)
                        sharedPref.edit().putString("handleTextIcon", "3").apply()
                    }
                    "4" -> {
                        ivIcon.setImageResource(R.drawable.emoticon)
                        sharedPref.edit().putString("handleTextIcon", "4").apply()
                    }
                    "5" -> {
                        ivIcon.setImageResource(R.drawable.emoticon_cool)
                        sharedPref.edit().putString("handleTextIcon", "5").apply()
                    }
                    "6" -> {
                        ivIcon.setImageResource(R.drawable.emoticon_dead)
                        sharedPref.edit().putString("handleTextIcon", "6").apply()
                    }
                    "7" -> {
                        ivIcon.setImageResource(R.drawable.emoticon_excited)
                        sharedPref.edit().putString("handleTextIcon", "7").apply()
                    }
                    "8" -> {
                        ivIcon.setImageResource(R.drawable.emoticon_tongue)
                        sharedPref.edit().putString("handleTextIcon", "8").apply()
                    }
                    "9" -> {
                        ivIcon.setImageResource(R.drawable.emoticon_devil)
                        sharedPref.edit().putString("handleTextIcon", "9").apply()
                    }
                    "" -> {
                        ivIcon.setImageResource(R.drawable.emoticon_neutral)
                        sharedPref.edit()
                                .putString("handleTextIcon", "")
                                .apply()
                    }
                }
                if ("" == noteAttachment) {
                    ivAttachment.visibility = View.GONE
                } else {
                    ivAttachment.visibility = View.VISIBLE
                    ivAttachment.setImageResource(R.drawable.ic_attachment)
                }
                val file = File(noteAttachment)
                if (!file.exists()) {
                    ivAttachment.visibility = View.GONE
                }
                return v
            }
        }

        //display data by filter
        val noteSearch = sharedPref.getString("filter_noteBY", "note_title")
        sharedPref.edit().putString("filter_noteBY", "note_title").apply()
        filter!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapter?.filter?.filter(s.toString())
            }
        })
        adapter?.filterQueryProvider = FilterQueryProvider { constraint -> db!!.fetchDataByFilter(constraint.toString(), noteSearch!!) }
        lv!!.adapter = adapter
        //onClick function
        lv!!.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            val row2 = lv!!.getItemAtPosition(position) as Cursor
            val _id = row2.getString(row2.getColumnIndexOrThrow("_id"))
            val noteTitle = row2.getString(row2.getColumnIndexOrThrow("note_title"))
            val noteContent = row2.getString(row2.getColumnIndexOrThrow("note_content"))
            val noteIcon = row2.getString(row2.getColumnIndexOrThrow("note_icon"))
            val noteAttachment = row2.getString(row2.getColumnIndexOrThrow("note_attachment"))
            val noteCreation = row2.getString(row2.getColumnIndexOrThrow("note_creation"))
            val attachment: Button
            val textInput: TextView
            val inflater = activity!!.layoutInflater
            val nullParent: ViewGroup? = null
            val dialogView = inflater.inflate(R.layout.dialog_note_show, nullParent)
            val attName = noteAttachment.substring(noteAttachment.lastIndexOf("/") + 1)
            val att = getString(R.string.note_attachment) + ": " + attName
            attachment = dialogView.findViewById(R.id.button_att)
            if (attName == "") {
                attachment.visibility = View.GONE
            } else {
                attachment.text = att
            }
            textInput = dialogView.findViewById(R.id.note_text_input)
            if (noteContent.isEmpty()) {
                textInput.visibility = View.GONE
            } else {
                textInput.text = noteContent
                Linkify.addLinks(textInput, Linkify.WEB_URLS)
            }
            attachment.setOnClickListener { HelperMain.openAtt(activity!!, textInput, noteAttachment) }
            val be = dialogView.findViewById<ImageView>(R.id.imageButtonPri)
            val attImage = dialogView.findViewById<ImageView>(R.id.attImage)
            val file2 = File(noteAttachment)
            if (!file2.exists()) {
                attachment.visibility = View.GONE
                attImage.visibility = View.GONE
            } else if (noteAttachment.contains(".gif") ||
                    noteAttachment.contains(".bmp") ||
                    noteAttachment.contains(".tiff") ||
                    noteAttachment.contains(".png") ||
                    noteAttachment.contains(".jpg") ||
                    noteAttachment.contains(".JPG") ||
                    noteAttachment.contains(".jpeg") ||
                    noteAttachment.contains(".mpeg") ||
                    noteAttachment.contains(".mp4") ||
                    noteAttachment.contains(".3gp") ||
                    noteAttachment.contains(".3g2") ||
                    noteAttachment.contains(".avi") ||
                    noteAttachment.contains(".flv") ||
                    noteAttachment.contains(".h261") ||
                    noteAttachment.contains(".h263") ||
                    noteAttachment.contains(".h264") ||
                    noteAttachment.contains(".asf") ||
                    noteAttachment.contains(".wmv")) {
                attImage.visibility = View.VISIBLE
                try {
                    Glide.with(activity!!)
                            .load(noteAttachment) // or URI/path
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(attImage) //imageView to set thumbnail to
                } catch (e: Exception) {
                    Log.w("HHS_Moodle", "Error load thumbnail", e)
                    attImage.visibility = View.GONE
                }
                attImage.setOnClickListener { HelperMain.openAtt(activity!!, attImage, noteAttachment) }
            }
            when (noteIcon) {
                "1" -> {
                    be.setImageResource(R.drawable.emoticon_neutral)
                    sharedPref.edit().putString("handleTextIcon", "1").apply()
                }
                "2" -> {
                    be.setImageResource(R.drawable.emoticon_happy)
                    sharedPref.edit().putString("handleTextIcon", "2").apply()
                }
                "3" -> {
                    be.setImageResource(R.drawable.emoticon_sad)
                    sharedPref.edit().putString("handleTextIcon", "3").apply()
                }
                "4" -> {
                    be.setImageResource(R.drawable.emoticon)
                    sharedPref.edit().putString("handleTextIcon", "4").apply()
                }
                "5" -> {
                    be.setImageResource(R.drawable.emoticon_cool)
                    sharedPref.edit().putString("handleTextIcon", "5").apply()
                }
                "6" -> {
                    be.setImageResource(R.drawable.emoticon_dead)
                    sharedPref.edit().putString("handleTextIcon", "6").apply()
                }
                "7" -> {
                    be.setImageResource(R.drawable.emoticon_excited)
                    sharedPref.edit().putString("handleTextIcon", "7").apply()
                }
                "8" -> {
                    be.setImageResource(R.drawable.emoticon_tongue)
                    sharedPref.edit().putString("handleTextIcon", "8").apply()
                }
                "9" -> {
                    be.setImageResource(R.drawable.emoticon_devil)
                    sharedPref.edit().putString("handleTextIcon", "9").apply()
                }
                "" -> {
                    be.setImageResource(R.drawable.emoticon_neutral)
                    sharedPref.edit()
                            .putString("handleTextIcon", "")
                            .apply()
                }
            }
            val dialog = AlertDialog.Builder(activity)
                    .setTitle(noteTitle)
                    .setView(dialogView)
                    .setPositiveButton(R.string.yes) { dialog, _ -> dialog.cancel() }
                    .setNegativeButton(R.string.note_edit) { _, _ ->
                        sharedPref.edit()
                                .putString("handleTextTitle", noteTitle)
                                .putString("handleTextText", noteContent)
                                .putString("handleTextIcon", noteIcon)
                                .putString("handleTextSeqno", _id)
                                .putString("handleTextAttachment", noteAttachment)
                                .putString("handleTextCreate", noteCreation)
                                .apply()
                        val intent = Intent(activity, ActivityEditNote::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        activity!!.startActivity(intent)
                    }
            dialog.show()
        }
        lv!!.onItemLongClickListener = OnItemLongClickListener { _, _, position, _ ->
            val row2 = lv!!.getItemAtPosition(position) as Cursor
            val _id = row2.getString(row2.getColumnIndexOrThrow("_id"))
            val noteTitle = row2.getString(row2.getColumnIndexOrThrow("note_title"))
            val noteContent = row2.getString(row2.getColumnIndexOrThrow("note_content"))
            val noteIcon = row2.getString(row2.getColumnIndexOrThrow("note_icon"))
            val noteAttachment = row2.getString(row2.getColumnIndexOrThrow("note_attachment"))
            val noteCreation = row2.getString(row2.getColumnIndexOrThrow("note_creation"))
            val options = arrayOf<CharSequence>(
                    getString(R.string.note_edit),
                    getString(R.string.note_share),
                    getString(R.string.note_remove_note))
            AlertDialog.Builder(activity)
                    .setPositiveButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
                    .setItems(options) { _, item ->
                        if (options[item] == getString(R.string.note_edit)) {
                            sharedPref.edit()
                                    .putString("handleTextTitle", noteTitle)
                                    .putString("handleTextText", noteContent)
                                    .putString("handleTextIcon", noteIcon)
                                    .putString("handleTextSeqno", _id)
                                    .putString("handleTextAttachment", noteAttachment)
                                    .putString("handleTextCreate", noteCreation)
                                    .apply()
                            val intent = Intent(activity, ActivityEditNote::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            activity!!.startActivity(intent)
                        }
                        if (options[item] == getString(R.string.note_share)) {
                            val attachment = File(noteAttachment)
                            val sharingIntent = Intent(Intent.ACTION_SEND)
                            sharingIntent.type = "text/plain"
                            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, noteTitle)
                            sharingIntent.putExtra(Intent.EXTRA_TEXT, noteContent)
                            if (attachment.exists()) {
                                val bmpUri = Uri.fromFile(attachment)
                                sharingIntent.putExtra(Intent.EXTRA_STREAM, bmpUri)
                            }
                            startActivity(Intent.createChooser(sharingIntent, getString(R.string.note_share_2)))
                        }
                        if (options[item] == getString(R.string.note_remove_note)) {
                            val snackbar = Snackbar
                                    .make(lv!!, R.string.note_remove_confirmation, Snackbar.LENGTH_LONG)
                                    .setAction(R.string.yes) {
                                        db!!.delete(_id.toInt())
                                        setNotesList()
                                    }
                            snackbar.show()
                        }
                    }.show()
            true
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_image).isVisible = false
        menu.findItem(R.id.action_share).isVisible = false
        menu.findItem(R.id.action_reset).isVisible = false
        menu.findItem(R.id.action_info).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val tab = tabLayout!!.getTabAt(3)
        when (item.itemId) {
            R.id.filter_title -> {
                sharedPref.edit().putString("filter_noteBY", "note_title").apply()
                setTitle()
                setNotesList()
                filterLayout.visibility = View.VISIBLE
                filter!!.setText("")
                filter!!.setHint(R.string.action_filter_title)
                filter!!.requestFocus()
                HelperMain.showKeyboard(activity!!, filter!!)
                return true
            }
            R.id.filter_content -> {
                sharedPref.edit().putString("filter_noteBY", "note_content").apply()
                setTitle()
                setNotesList()
                filterLayout.visibility = View.VISIBLE
                filter!!.setText("")
                filter!!.setHint(R.string.action_filter_cont)
                filter!!.requestFocus()
                HelperMain.showKeyboard(activity!!, filter!!)
                return true
            }
            R.id.filter_today -> {
                val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val cal = Calendar.getInstance()
                val search = dateFormat.format(cal.time)
                sharedPref.edit().putString("filter_noteBY", "note_creation").apply()
                assert(tab != null)
                tab!!.text = getString(R.string.action_diary) + " | " + getString(R.string.filter_today)
                setNotesList()
                filterLayout.visibility = View.VISIBLE
                filter!!.setText(search)
                filter!!.setHint(R.string.action_filter_create)
                return true
            }
            R.id.filter_yesterday -> {
                val dateFormat2: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val cal2 = Calendar.getInstance()
                cal2.add(Calendar.DATE, -1)
                val search2 = dateFormat2.format(cal2.time)
                sharedPref.edit().putString("filter_noteBY", "note_creation").apply()
                assert(tab != null)
                tab!!.text = getString(R.string.action_diary) + " | " + getString(R.string.filter_yesterday)
                setNotesList()
                filterLayout.visibility = View.VISIBLE
                filter!!.setText(search2)
                filter!!.setHint(R.string.action_filter_create)
                return true
            }
            R.id.filter_before -> {
                val dateFormat3: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val cal3 = Calendar.getInstance()
                cal3.add(Calendar.DATE, -2)
                val search3 = dateFormat3.format(cal3.time)
                sharedPref.edit().putString("filter_noteBY", "note_creation").apply()
                assert(tab != null)
                tab!!.text = getString(R.string.action_diary) + " | " + getString(R.string.filter_before)
                setNotesList()
                filterLayout.visibility = View.VISIBLE
                filter!!.setText(search3)
                filter!!.setHint(R.string.action_filter_create)
                return true
            }
            R.id.filter_month -> {
                val dateFormat4: DateFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
                val cal4 = Calendar.getInstance()
                val search4 = dateFormat4.format(cal4.time)
                sharedPref.edit().putString("filter_noteBY", "note_creation").apply()
                assert(tab != null)
                tab!!.text = getString(R.string.action_diary) + " | " + getString(R.string.filter_month)
                setNotesList()
                filterLayout.visibility = View.VISIBLE
                filter!!.setText(search4)
                filter!!.setHint(R.string.action_filter_create)
                return true
            }
            R.id.filter_own -> {
                sharedPref.edit().putString("filter_noteBY", "note_creation").apply()
                assert(tab != null)
                tab!!.text = getString(R.string.action_diary) + " | " + getString(R.string.filter_own)
                setNotesList()
                filterLayout.visibility = View.VISIBLE
                filter!!.setText("")
                filter!!.setHint(R.string.action_filter_create)
                filter!!.requestFocus()
                HelperMain.showKeyboard(activity!!, filter!!)
                return true
            }
            R.id.sort_title -> {
                sharedPref.edit().putString("sortDB", "title").apply()
                setTitle()
                setNotesList()
                return true
            }
            R.id.sort_icon -> {
                sharedPref.edit().putString("sortDB", "icon").apply()
                setTitle()
                setNotesList()
                return true
            }
            R.id.sort_creation -> {
                sharedPref.edit().putString("sortDB", "create").apply()
                setTitle()
                setNotesList()
                return true
            }
            R.id.backup_backup -> {
                val directory = File(context?.getExternalFilesDir(null).toString() + "/QuitSmoking/backup/")
                if (!directory.exists()) {
                    directory.mkdirs()
                }
                try {
                    val sd = context?.getExternalFilesDir(null)
                    val data = Environment.getDataDirectory()
                    if (sd?.canWrite()!!) {
                        val currentDBPath = ("//data//" + "de.baumann.quitsmoking"
                                + "//databases//" + "notes_DB_v01.db")
                        val backupDBPath = "//Android//" + "//data//" + "//quitsmoking.backup//" + "notes_DB_v01.db"
                        val currentDB = File(data, currentDBPath)
                        val backupDB = File(sd, backupDBPath)
                        val src = FileInputStream(currentDB).channel
                        val dst = FileOutputStream(backupDB).channel
                        dst.transferFrom(src, 0, src.size())
                        src.close()
                        dst.close()
                        val snackbar = Snackbar
                                .make(lv!!, R.string.toast_backup, Snackbar.LENGTH_LONG)
                        snackbar.show()
                    }
                } catch (e: Exception) {
                    val snackbar = Snackbar
                            .make(lv!!, R.string.toast_backup_not, Snackbar.LENGTH_LONG)
                    snackbar.show()
                }
                return true
            }
            R.id.backup_restore -> {
                try {
                    val sd = context?.getExternalFilesDir(null)
                    val data = Environment.getDataDirectory()
                    if (sd?.canWrite()!!) {
                        val currentDBPath = ("//data//" + "de.baumann.quitsmoking"
                                + "//databases//" + "notes_DB_v01.db")
                        val backupDBPath = "//Android//" + "//data//" + "//quitsmoking.backup//" + "notes_DB_v01.db"
                        val currentDB = File(data, currentDBPath)
                        val backupDB = File(sd, backupDBPath)
                        val src = FileInputStream(backupDB).channel
                        val dst = FileOutputStream(currentDB).channel
                        dst.transferFrom(src, 0, src.size())
                        src.close()
                        dst.close()
                        setNotesList()
                        val snackbar = Snackbar
                                .make(lv!!, R.string.toast_restore, Snackbar.LENGTH_LONG)
                        snackbar.show()
                    }
                } catch (e: Exception) {
                    val snackbar = Snackbar
                            .make(lv!!, R.string.toast_restore_not, Snackbar.LENGTH_LONG)
                    snackbar.show()
                }
                return true
            }
            R.id.backup_delete -> {
                val snackbar = Snackbar
                        .make(lv!!, R.string.toast_delete, Snackbar.LENGTH_LONG)
                        .setAction(R.string.yes) {
                            activity!!.deleteDatabase("notes_DB_v01.db")
                            activity!!.recreate()
                        }
                snackbar.show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setTitle() {
        val tab = tabLayout!!.getTabAt(3)
        if (sharedPref.getString("sortDB", "title") == "title") {
            assert(tab != null)
            tab!!.text = getString(R.string.action_diary) + " | " + getString(R.string.sort_title)
        } else if (sharedPref.getString("sortDB", "title") == "icon") {
            assert(tab != null)
            tab!!.text = getString(R.string.action_diary) + " | " + getString(R.string.sort_pri)
        } else if (sharedPref.getString("sortDB", "title") == "create") {
            assert(tab != null)
            tab!!.text = getString(R.string.action_diary) + " | " + getString(R.string.sort_date)
        }
    }
}