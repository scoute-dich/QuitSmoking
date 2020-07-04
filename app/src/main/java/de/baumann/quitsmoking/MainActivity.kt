package de.baumann.quitsmoking

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import de.baumann.quitsmoking.fragments.FragmentGoal
import de.baumann.quitsmoking.fragments.FragmentHealth
import de.baumann.quitsmoking.fragments.FragmentNotes
import de.baumann.quitsmoking.fragments.FragmentOverview
import de.baumann.quitsmoking.helper.HelperMain.textSpannable
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_ASK_PERMISSIONS = 123
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PreferenceManager.setDefaultValues(this@MainActivity, R.xml.user_settings, false)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val viewPager = findViewById<ViewPager>(R.id.viewpager)
        setupViewPager(viewPager)
        val tabLayout = findViewById<TabLayout>(R.id.tabs)!!
        tabLayout.setupWithViewPager(viewPager)
        if (sharedPreferences.getBoolean("first_run", true)) {
            sharedPreferences.edit().putBoolean("first_run", false).apply()
            val intentIn = Intent(this@MainActivity, UserSettingsActivity::class.java)
            startActivity(intentIn)
            overridePendingTransition(0, 0)
            finish()
        }
        val haswriteExternalStorage = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (haswriteExternalStorage != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder(this@MainActivity)
                        .setTitle(R.string.app_permissions_title)
                        .setMessage(textSpannable(getString(R.string.app_permissions)))
                        .setPositiveButton(getString(R.string.yes)) { _, _ ->
                            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                    REQUEST_CODE_ASK_PERMISSIONS)
                        }
                        .setNegativeButton(getString(R.string.cancel), null)
                        .show()
                return
            }
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE_ASK_PERMISSIONS)
            return
        }
        val directory = File(getExternalFilesDir(null).toString() + "/Android/data/quitsmoking.backup")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val directoryData = File(getExternalFilesDir(null).toString() + "/Android/data/de.baumann.quitsmoking")
        if (!directoryData.exists()) {
            directoryData.mkdirs()
        }
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        val tabDiary: String = when {
            sharedPreferences.getString("sortDB", "title") == "title" -> {
                getString(R.string.action_diary) + " | " + getString(R.string.sort_title)
            }
            sharedPreferences.getString("sortDB", "title") == "icon" -> {
                getString(R.string.action_diary) + " | " + getString(R.string.sort_pri)
            }
            else -> {
                getString(R.string.action_diary) + " | " + getString(R.string.sort_date)
            }
        }
        if (sharedPreferences.getBoolean("tab_overview", false)) {
            adapter.addFragment(FragmentOverview(), getString(R.string.action_overview))
        }
        if (sharedPreferences.getBoolean("tab_health", false)) {
            adapter.addFragment(FragmentHealth(), getString(R.string.action_health))
        }
        if (sharedPreferences.getBoolean("tab_goal", false)) {
            adapter.addFragment(FragmentGoal(), getString(R.string.action_goal))
        }
        if (sharedPreferences.getBoolean("tab_diary", false)) {
            adapter.addFragment(FragmentNotes(), tabDiary)
        }
        viewPager.adapter = adapter
    }

    private inner class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        private val mFragmentList: MutableList<Fragment> = ArrayList()
        private val mFragmentTitleList: MutableList<String> = ArrayList()
        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return mFragmentTitleList[position] // add return null; to display only icons
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        if (id == R.id.action_settings) {
            val intentIn = Intent(this@MainActivity, UserSettingsActivity::class.java)
            startActivity(intentIn)
            overridePendingTransition(0, 0)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}