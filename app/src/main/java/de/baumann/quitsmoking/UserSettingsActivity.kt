package de.baumann.quitsmoking

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import de.baumann.quitsmoking.about.AboutActivity
import de.baumann.quitsmoking.helper.ActivityIntro
import de.baumann.quitsmoking.helper.HelperMain.showKeyboard
import java.util.*

class UserSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_user_settings);
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.action_settings)
        }
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        PreferenceManager.setDefaultValues(this@UserSettingsActivity, R.xml.user_settings, false)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val show = sharedPreferences.getBoolean("intro_notShow", true)
        if (show) {
            val mainIntent = Intent(this@UserSettingsActivity, ActivityIntro::class.java)
            startActivity(mainIntent)
            overridePendingTransition(0, 0)
        }

        // Display the fragment as the fragment_main content
        supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, SettingsFragment())
                .commit()
    }

    class SettingsFragment : PreferenceFragmentCompat(), TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.user_settings)
            addLicenseListener()
            addClearCacheListener()
            addDateListener()
            addCigListener()
            addCigCostListener()
            addGoalListener()
            addGoal2Listener()
        }

        private fun addLicenseListener() {
            val reset = findPreference<Preference>("license")
            reset!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                val intentIn = Intent(activity, AboutActivity::class.java)
                startActivity(intentIn)
                activity!!.overridePendingTransition(0, 0)
                true
            }
        }

        private fun addClearCacheListener() {
            val activity: Activity? = activity
            val reset = findPreference<Preference>("clearCache")
            reset!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", activity!!.packageName, null)
                intent.data = uri
                getActivity()!!.startActivity(intent)
                true
            }
        }

        private fun addDateListener() {
            val reset = findPreference<Preference>("time")
            reset!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                PreferenceManager.setDefaultValues(activity, R.xml.user_settings, false)
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
                sharedPref.edit().putInt("DatePicker", 0).apply()
                val now = Calendar.getInstance()
                val dpd = DatePickerDialog.newInstance(
                        this@SettingsFragment,
                        now[Calendar.YEAR],
                        now[Calendar.MONTH],
                        now[Calendar.DAY_OF_MONTH]
                )
                dpd.show(fragmentManager!!, "DatePickerDialog")
                dpd.isThemeDark = true
                true
            }
        }

        override fun onDateSet(view: DatePickerDialog, year: Int, monthOfYear: Int, dayOfMonth: Int) {
            PreferenceManager.setDefaultValues(activity, R.xml.user_settings, false)
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
            if (sharedPref.getInt("DatePicker", 0) == 0) {
                sharedPref.edit().putInt("start_year", year).apply()
                sharedPref.edit().putInt("start_month", monthOfYear).apply()
                sharedPref.edit().putInt("start_day", dayOfMonth).apply()
                val now = Calendar.getInstance()
                val tpd = TimePickerDialog.newInstance(
                        this@SettingsFragment,
                        now[Calendar.HOUR_OF_DAY],
                        now[Calendar.MINUTE],
                        true
                )
                tpd.show(fragmentManager!!, "DatePickerDialog")
                tpd.isThemeDark = true
            } else {
                val cal = Calendar.getInstance()
                cal[Calendar.YEAR] = year
                cal[Calendar.MONTH] = monthOfYear
                cal[Calendar.DAY_OF_MONTH] = dayOfMonth
                cal[Calendar.HOUR_OF_DAY] = 0
                cal[Calendar.MINUTE] = 0
                sharedPref.edit().putLong("goalDate_next", cal.timeInMillis).apply()
            }
        }

        override fun onTimeSet(view: TimePickerDialog, hourOfDay: Int, minute: Int, second: Int) {
            PreferenceManager.setDefaultValues(activity, R.xml.user_settings, false)
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
            val cal = Calendar.getInstance()
            cal[Calendar.YEAR] = sharedPref.getInt("start_year", 0)
            cal[Calendar.MONTH] = sharedPref.getInt("start_month", 0)
            cal[Calendar.DAY_OF_MONTH] = sharedPref.getInt("start_day", 0)
            cal[Calendar.HOUR_OF_DAY] = hourOfDay
            cal[Calendar.MINUTE] = minute
            sharedPref.edit().putLong("startTime", cal.timeInMillis).apply()
            sharedPref.edit().putInt("start_year", 0).apply()
            sharedPref.edit().putInt("start_month", 0).apply()
            sharedPref.edit().putInt("start_day", 0).apply()
            sharedPref.edit().putString("entry_date", "").apply()
        }

        private fun addCigListener() {
            val reset = findPreference<Preference>("cig")
            reset!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                PreferenceManager.setDefaultValues(activity, R.xml.user_settings, false)
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
                val builder = AlertDialog.Builder(activity)
                val dialogView = View.inflate(activity, R.layout.dialog_cig, null)
                val editNumber = dialogView.findViewById<EditText>(R.id.editNumber)
                editNumber.setText(sharedPref.getString("cig", ""))
                val editTime = dialogView.findViewById<EditText>(R.id.editTime)
                editTime.setText(sharedPref.getString("duration", ""))
                builder.setView(dialogView)
                builder.setTitle(R.string.a_cig)
                builder.setPositiveButton(R.string.yes) { _, _ ->
                    val inputTag = editNumber.text.toString().trim { it <= ' ' }
                    sharedPref.edit().putString("cig", inputTag).apply()
                    val inputTag2 = editTime.text.toString().trim { it <= ' ' }
                    sharedPref.edit().putString("duration", inputTag2).apply()
                }
                builder.setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
                val dialog2 = builder.create()
                // Display the custom alert dialog on interface
                dialog2.show()
                Handler().postDelayed({ showKeyboard(activity!!, editNumber) }, 200)
                true
            }
        }

        private fun addCigCostListener() {
            val reset = findPreference<Preference>("costs")
            reset!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                PreferenceManager.setDefaultValues(activity, R.xml.user_settings, false)
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
                val builder = AlertDialog.Builder(activity)
                val dialogView = View.inflate(activity, R.layout.dialog_cig_cost, null)
                val editNumber = dialogView.findViewById<EditText>(R.id.editNumber)
                editNumber.setText(sharedPref.getString("costs", ""))
                builder.setView(dialogView)
                builder.setTitle(R.string.settings_costs)
                builder.setPositiveButton(R.string.yes) { _, _ ->
                    val inputTag = editNumber.text.toString().trim { it <= ' ' }
                    sharedPref.edit().putString("costs", inputTag).apply()
                }
                builder.setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
                val dialog2 = builder.create()
                // Display the custom alert dialog on interface
                dialog2.show()
                Handler().postDelayed({ showKeyboard(activity!!, editNumber) }, 200)
                true
            }
        }

        private fun addGoalListener() {
            val reset = findPreference<Preference>("goalTitle")
            reset!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                PreferenceManager.setDefaultValues(activity, R.xml.user_settings, false)
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
                val builder = AlertDialog.Builder(activity)
                val dialogView = View.inflate(activity, R.layout.dialog_goal, null)
                val editNumber = dialogView.findViewById<EditText>(R.id.editTitle)
                editNumber.setText(sharedPref.getString("goalTitle", ""))
                val editTime = dialogView.findViewById<EditText>(R.id.editCost)
                editTime.setText(sharedPref.getString("goalCosts", ""))
                builder.setView(dialogView)
                builder.setTitle(R.string.a_goal)
                builder.setPositiveButton(R.string.yes) { _, _ ->
                    val inputTag = editNumber.text.toString().trim { it <= ' ' }
                    sharedPref.edit().putString("goalTitle", inputTag).apply()
                    val inputTag2 = editTime.text.toString().trim { it <= ' ' }
                    sharedPref.edit().putString("goalCosts", inputTag2).apply()
                }
                builder.setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }
                val dialog2 = builder.create()
                // Display the custom alert dialog on interface
                dialog2.show()
                Handler().postDelayed({ showKeyboard(activity!!, editNumber) }, 200)
                true
            }
        }

        private fun addGoal2Listener() {
            val reset = findPreference<Preference>("goalDate")
            reset!!.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                PreferenceManager.setDefaultValues(activity, R.xml.user_settings, false)
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity)
                sharedPref.edit().putInt("DatePicker", 1).apply()
                val now = Calendar.getInstance()
                val dpd = DatePickerDialog.newInstance(
                        this@SettingsFragment,
                        now[Calendar.YEAR],
                        now[Calendar.MONTH],
                        now[Calendar.DAY_OF_MONTH]
                )
                dpd.show(fragmentManager!!, "DatePickerDialog")
                dpd.isThemeDark = true
                true
            }
        }
    }

    override fun onBackPressed() {
        val intentIn = Intent(this@UserSettingsActivity, MainActivity::class.java)
        startActivity(intentIn)
        overridePendingTransition(0, 0)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == android.R.id.home) {
            val intentIn = Intent(this@UserSettingsActivity, MainActivity::class.java)
            startActivity(intentIn)
            overridePendingTransition(0, 0)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}