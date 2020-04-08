package de.baumann.quitsmoking.fragments

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import de.baumann.quitsmoking.R
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

class FragmentHealth : Fragment() {
    private var sharedPreferences: SharedPreferences? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_health, container, false)
        PreferenceManager.setDefaultValues(activity, R.xml.user_settings, false)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        setProgress(rootView, 0.33, R.id.progressBar, R.id.text_reached1)
        setProgress(rootView, 8.0, R.id.progressBar2, R.id.text_reached2)
        setProgress(rootView, 24.0, R.id.progressBar3, R.id.text_reached3)
        setProgress(rootView, 48.0, R.id.progressBar4, R.id.text_reached4)
        setProgress(rootView, 72.0, R.id.progressBar5, R.id.text_reached5)
        setProgress(rootView, 168.0, R.id.progressBar6, R.id.text_reached6)
        setProgress(rootView, 2160.0, R.id.progressBar7, R.id.text_reached7)
        setProgress(rootView, 6480.0, R.id.progressBar8, R.id.text_reached8)
        setProgress(rootView, 8760.0, R.id.progressBar9, R.id.text_reached9)
        setProgress(rootView, 17520.0, R.id.progressBar10, R.id.text_reached10)
        setProgress(rootView, 43800.0, R.id.progressBar11, R.id.text_reached11)
        setProgress(rootView, 87600.0, R.id.progressBar12, R.id.text_reached12)
        setProgress(rootView, 131400.0, R.id.progressBar13, R.id.text_reached13)
        setHasOptionsMenu(true)
        return rootView
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_share).isVisible = false
        menu.findItem(R.id.action_backup).isVisible = false
        menu.findItem(R.id.action_image).isVisible = false
        menu.findItem(R.id.action_filter).isVisible = false
        menu.findItem(R.id.action_sort).isVisible = false
        menu.findItem(R.id.action_reset).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_info) {
            val s: SpannableString = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                SpannableString(Html.fromHtml(getString(R.string.info_text), Html.FROM_HTML_MODE_LEGACY))
            } else {
                SpannableString(Html.fromHtml(getString(R.string.info_text)))
            }
            Linkify.addLinks(s, Linkify.WEB_URLS)
            val d = AlertDialog.Builder(activity)
                    .setTitle(R.string.info_title)
                    .setMessage(s)
                    .setPositiveButton(getString(R.string.yes)
                    ) { dialog, _ -> dialog.cancel() }.show()
            d.show()
            (d.findViewById<View>(android.R.id.message) as TextView).movementMethod = LinkMovementMethod.getInstance()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setProgress(view: View, hourTime: Double, progressBar_ID: Int, text: Int) {
        val date = Date()
        val format = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val dateStart = format.format(sharedPreferences!!.getLong("startTime", 0))
        val dateStop = format.format(date)
        val progressBar: ProgressBar?
        try {
            val d1 = format.parse(dateStart)
            val d2 = format.parse(dateStop)
            val hour = (60 * 60 * 1000).toLong()
            val date1 = d1.time
            val date2 = d2.time
            val plusDay = hour * hourTime
            val plus2 = plusDay / 1000
            val diffCount = date1 + plusDay - date2
            val diff2 = diffCount / 1000
            val diffDays = floor(diffCount / (24 * 60 * 60 * 1000))
            val diffHours = diffCount / (60 * 60 * 1000) % 24
            val diffMinutes = diffCount / (60 * 1000) % 60
            progressBar = view.findViewById(progressBar_ID)
            assert(progressBar != null)
            progressBar.rotation = 180f
            val max = plus2.toInt()
            val actual = diff2.toInt()
            progressBar.max = max
            progressBar.progress = actual
            val days = String.format(Locale.GERMANY, "%.0f", diffDays)
            val hours = String.format(Locale.GERMANY, "%.0f", diffHours)
            val minutes = String.format(Locale.GERMANY, "%.0f", diffMinutes)
            val textviewReached13: TextView? = view.findViewById(text)
            assert(textviewReached13 != null)
            when {
                diffMinutes < 0 -> {
                    textviewReached13?.text = getString(R.string.health_congratulations)
                }
                diffHours < 0 -> {
                    textviewReached13?.text = (getString(R.string.health_reached) + " "
                            + minutes + " " + getString(R.string.time_minutes))
                }
                diffDays <= 0 -> {
                    textviewReached13?.text = (getString(R.string.health_reached) + " "
                            + hours + " " + getString(R.string.time_hours) + " "
                            + minutes + " " + getString(R.string.time_minutes))
                }
                else -> {
                    textviewReached13?.text = (getString(R.string.health_reached) + " "
                            + days + " " + getString(R.string.time_days) + " "
                            + hours + " " + getString(R.string.time_hours) + " "
                            + minutes + " " + getString(R.string.time_minutes))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}