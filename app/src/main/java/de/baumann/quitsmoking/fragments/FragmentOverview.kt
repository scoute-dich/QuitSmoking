package de.baumann.quitsmoking.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import de.baumann.quitsmoking.R
import de.baumann.quitsmoking.helper.ActivityEditNote
import de.baumann.quitsmoking.helper.HelperMain
import java.text.SimpleDateFormat
import java.util.*

class FragmentOverview : Fragment() {

    private var textView_time2: TextView? = null
    private var textView_time3: TextView? = null
    private var textView_time4: TextView? = null
    private var textView_cig2: TextView? = null
    private var textView_cig2_cost: TextView? = null
    private var textView_duration: TextView? = null
    private var textView_date2: TextView? = null
    private var textView_date3: TextView? = null
    private var currency: String? = null
    private var dateFormat: String? = null
    private var dateQuit: String? = null
    private var timeQuit: String? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)
        PreferenceManager.setDefaultValues(activity, R.xml.user_settings, false)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        textView_cig2_cost = rootView.findViewById(R.id.text_cigs2_cost)
        textView_cig2 = rootView.findViewById(R.id.text_cigs2)
        textView_duration = rootView.findViewById(R.id.text_duration)
        textView_date2 = rootView.findViewById(R.id.text_date2)
        textView_date3 = rootView.findViewById(R.id.text_date3)
        textView_time2 = rootView.findViewById(R.id.text_time2)
        textView_time3 = rootView.findViewById(R.id.text_time3)
        textView_time4 = rootView.findViewById(R.id.text_time4)
        assert(textView_date2 != null)
        assert(textView_date3 != null)
        currency = sharedPreferences.getString("currency", "1")
        dateFormat = sharedPreferences.getString("dateFormat", "1")
        dateQuit = sharedPreferences.getString("date", "")
        timeQuit = sharedPreferences.getString("time", "")
        when (dateFormat) {
            "1" -> {
                val format = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                setText(format)
            }
            "2" -> {
                val format2 = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                setText(format2)
            }
        }
        setHasOptionsMenu(true)
        return rootView
    }

    private fun setText(format: SimpleDateFormat) {
        val dateStart = format.format(sharedPreferences.getLong("startTime", 0))
        dateQuit = dateStart.substring(0, 10)
        timeQuit = dateStart.substring(11, 16)
        try {
            HelperMain.calculate(activity)
            textView_time2!!.text = sharedPreferences.getString("SPtimeDiffDays", "0") + " " + getString(R.string.time_days)
            textView_time3!!.text = sharedPreferences.getString("SPtimeDiffHours", "0") + " " + getString(R.string.time_hours)
            textView_time4!!.text = sharedPreferences.getString("SPtimeDiffMinutes", "0") + " " + getString(R.string.time_minutes)
            textView_date2!!.text = dateQuit.toString()
            textView_date3!!.text = timeQuit.toString()

            //Number of Cigarettes
            textView_cig2!!.text = sharedPreferences.getString("SPcigSavedString", "0")
            when (currency) {
                "1" -> textView_cig2_cost!!.text = sharedPreferences.getString("SPmoneySavedString", "0") + " " + getString(R.string.money_dollar)
                "2" -> textView_cig2_cost!!.text = sharedPreferences.getString("SPmoneySavedString", "0") + " " + getString(R.string.money_euro)
                "3" -> textView_cig2_cost!!.text = sharedPreferences.getString("SPmoneySavedString", "0") + " " + getString(R.string.money_pound)
                "4" -> textView_cig2_cost!!.text = sharedPreferences.getString("SPmoneySavedString", "0") + " " + getString(R.string.money_yen)
                "5" -> textView_cig2_cost!!.text = sharedPreferences.getString("SPmoneySavedString", "0") + " " + getString(R.string.money_rupees)
            }

            //Saved Time
            textView_duration!!.text = sharedPreferences.getString("SPtimeSavedString", "0") + " " + getString(R.string.stat_h)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_share).isVisible = false
        menu.findItem(R.id.action_backup).isVisible = false
        menu.findItem(R.id.action_image).isVisible = false
        menu.findItem(R.id.action_filter).isVisible = false
        menu.findItem(R.id.action_sort).isVisible = false
        menu.findItem(R.id.action_info).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val days = textView_time2!!.text.toString()
        val hours = textView_time3!!.text.toString()
        val minutes = textView_time4!!.text.toString()
        val savedCigarettes = textView_cig2!!.text.toString()
        val savedMoney = textView_cig2_cost!!.text.toString()
        val savedTime = textView_duration!!.text.toString()
        if (currency != null && currency!!.isNotEmpty() && dateFormat != null && dateFormat!!.isNotEmpty() && dateQuit != null && dateQuit!!.isNotEmpty() && timeQuit != null && timeQuit!!.isNotEmpty()) {
            when (item.itemId) {
                R.id.action_share -> {
                    val sharingIntent = Intent(Intent.ACTION_SEND)
                    sharingIntent.type = "text/plain"
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject))
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text) + " " +
                            days + " " + hours + " " + getString(R.string.share_text2) + " " + minutes + ". " +
                            getString(R.string.share_text3) + " " + savedCigarettes + " " + getString(R.string.share_text4) + ", " +
                            savedMoney + " " + getString(R.string.share_text2) + " " +
                            savedTime + " " + getString(R.string.share_text6))
                    startActivity(Intent.createChooser(sharingIntent, "Share using"))
                    return true
                }
                R.id.action_reset -> {
                    val snackbar = Snackbar
                            .make(textView_time2!!, R.string.reset_confirm, Snackbar.LENGTH_LONG)
                            .setAction(R.string.yes) {
                                val title = days + " " + hours + " " + getString(R.string.share_text2) + " " + minutes
                                val text = getString(R.string.share_text_fail) + " " +
                                        days + " " + hours + " " + getString(R.string.share_text2) + " " + minutes + ". " +
                                        getString(R.string.share_text3) + " " + savedCigarettes + " " + getString(R.string.share_text4) + ", " +
                                        savedMoney + " " + getString(R.string.share_text2) + " " +
                                        savedTime + " " + getString(R.string.share_text6)
                                sharedPreferences.edit()
                                        .putString("handleTextTitle", title)
                                        .putString("handleTextText", text)
                                        .putString("handleTextCreate", HelperMain.createDate())
                                        .apply()
                                val intent = Intent(activity, ActivityEditNote::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                                activity!!.startActivity(intent)
                                sharedPreferences.edit().putLong("startTime", Calendar.getInstance().timeInMillis).apply()
                                activity!!.finish()
                            }
                    snackbar.show()
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}