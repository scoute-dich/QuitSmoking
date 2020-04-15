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

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.text.Html
import android.text.SpannableString
import android.text.util.Linkify
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.FileProvider
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import de.baumann.quitsmoking.R
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object HelperMain {
    fun calculate(activity: Activity?) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        val cigNumb = sharedPreferences.getString("cig", "")
        val savedMoney = sharedPreferences.getString("costs", "")
        val savedTime = sharedPreferences.getString("duration", "")
        val date = Date()
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val dateStart = format.format(sharedPreferences.getLong("startTime", 0))
        val dateStop = format.format(date)
        try {
            val d1 = format.parse(dateStart)
            val d2 = format.parse(dateStop)

            //Time Difference
            val diff = d2.time - d1.time
            val diffMinutes = diff / (60 * 1000) % 60
            val diffHours = diff / (60 * 60 * 1000) % 24
            val diffDays = diff / (24 * 60 * 60 * 1000)
            val days = diffDays.toString()
            val hours = diffHours.toString()
            val minutes = diffMinutes.toString()
            sharedPreferences.edit().putString("SPtimeDiffDays", days).apply()
            sharedPreferences.edit().putString("SPtimeDiffHours", hours).apply()
            sharedPreferences.edit().putString("SPtimeDiffMinutes", minutes).apply()

            //Saved Cigarettes
            val cigNumber = cigNumb!!.toLong()
            val cigDay = 86400000 / cigNumber
            val savedCig = diff / cigDay
            val cigSavedString = savedCig.toString()
            sharedPreferences.edit().putString("SPcigSavedString", cigSavedString).apply()

            //Saved Money
            val costCig = java.lang.Double.valueOf(savedMoney!!.trim { it <= ' ' })
            val sa = cigSavedString.toLong().toDouble()
            val moneySaved = sa * costCig
            val moneySavedString = String.format(Locale.US, "%.2f", moneySaved)
            sharedPreferences.edit().putString("SPmoneySavedString", moneySavedString).apply()

            //Saved Time
            val timeMin = java.lang.Double.valueOf(savedTime!!.trim { it <= ' ' })
            val time = sa * timeMin / 60
            val timeSavedString = String.format(Locale.US, "%.1f", time)
            sharedPreferences.edit().putString("SPtimeSavedString", timeSavedString).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun newFileName(): String {
        val date = Date()
        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault())
        return dateFormat.format(date) + ".jpg"
    }

    @JvmStatic
    fun textSpannable(text: String?): SpannableString {
        val s: SpannableString = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SpannableString(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY))
        } else {
            SpannableString(Html.fromHtml(text))
        }
        Linkify.addLinks(s, Linkify.WEB_URLS)
        return s
    }

    @JvmStatic
    fun showKeyboard(from: Activity, editText: EditText) {
        Handler().postDelayed({
            val imm = from.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
            editText.setSelection(editText.length())
        }, 200)
    }

    fun openAtt(activity: Activity, view: View, fileString: String?) {
        val file = File(fileString)
        val fileExtension = file.absolutePath.substring(file.absolutePath.lastIndexOf("."))
        val text = activity.getString(R.string.toast_extension) + ": " + fileExtension
        when (fileExtension) {
            ".gif", ".bmp", ".tiff", ".svg", ".png", ".jpg", ".jpeg" -> openFile(activity, file, "image/*", view)
            ".m3u8", ".mp3", ".wma", ".midi", ".wav", ".aac", ".aif", ".amp3", ".weba" -> openFile(activity, file, "audio/*", view)
            ".mpeg", ".mp4", ".ogg", ".webm", ".qt", ".3gp", ".3g2", ".avi", ".f4v", ".flv", ".h261", ".h263", ".h264", ".asf", ".wmv" -> openFile(activity, file, "video/*", view)
            ".rtx", ".csv", ".txt", ".vcs", ".vcf", ".css", ".ics", ".conf", ".config", ".java" -> openFile(activity, file, "text/*", view)
            ".html" -> openFile(activity, file, "text/html", view)
            ".apk" -> openFile(activity, file, "application/vnd.android.package-archive", view)
            ".pdf" -> openFile(activity, file, "application/pdf", view)
            ".doc" -> openFile(activity, file, "application/msword", view)
            ".xls" -> openFile(activity, file, "application/vnd.ms-excel", view)
            ".ppt" -> openFile(activity, file, "application/vnd.ms-powerpoint", view)
            ".docx" -> openFile(activity, file, "application/vnd.openxmlformats-officedocument.wordprocessingml.document", view)
            ".pptx" -> openFile(activity, file, "application/vnd.openxmlformats-officedocument.presentationml.presentation", view)
            ".xlsx" -> openFile(activity, file, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", view)
            ".odt" -> openFile(activity, file, "application/vnd.oasis.opendocument.text", view)
            ".ods" -> openFile(activity, file, "application/vnd.oasis.opendocument.spreadsheet", view)
            ".odp" -> openFile(activity, file, "application/vnd.oasis.opendocument.presentation", view)
            ".zip" -> openFile(activity, file, "application/zip", view)
            ".rar" -> openFile(activity, file, "application/x-rar-compressed", view)
            ".epub" -> openFile(activity, file, "application/epub+zip", view)
            ".cbz" -> openFile(activity, file, "application/x-cbz", view)
            ".cbr" -> openFile(activity, file, "application/x-cbr", view)
            ".fb2" -> openFile(activity, file, "application/x-fb2", view)
            ".rtf" -> openFile(activity, file, "application/rtf", view)
            ".opml" -> openFile(activity, file, "application/opml", view)
            else -> {
                val snackbar = Snackbar
                        .make(view, text, Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        }
    }

    private fun openFile(activity: Activity, file: File, string: String, view: View) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            val contentUri = FileProvider.getUriForFile(activity, activity.applicationContext.packageName + ".provider", file)
            intent.setDataAndType(contentUri, string)
        } else {
            intent.setDataAndType(Uri.fromFile(file), string)
        }
        try {
            activity.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Snackbar.make(view, R.string.toast_install_app, Snackbar.LENGTH_LONG).show()
        }
    }

    fun createDate(): String {
        val date = Date()
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return format.format(date)
    }
}