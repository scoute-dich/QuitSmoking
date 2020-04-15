package de.baumann.quitsmoking.helper

import android.os.Bundle
import androidx.preference.PreferenceManager
import com.chyrta.onboarder.OnboarderActivity
import com.chyrta.onboarder.OnboarderPage
import de.baumann.quitsmoking.R
import java.util.*

class ActivityIntro : OnboarderActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onboarderPages: MutableList<OnboarderPage> = ArrayList()

        // Create your first page
        val onboarderPage1 = OnboarderPage(getString(R.string.intro1_title), getString(R.string.intro1_text), R.mipmap.ic_launcher)
        val onboarderPage2 = OnboarderPage(getString(R.string.intro2_title), getString(R.string.intro2_text), R.drawable.s1)
        val onboarderPage3 = OnboarderPage(getString(R.string.intro3_title), getString(R.string.intro3_text), R.drawable.s2)


        // You can define title and description colors (by default white)
        onboarderPage1.setTitleColor(R.color.colorAccent)
        onboarderPage1.setBackgroundColor(R.color.colorPrimaryDark)
        onboarderPage2.setTitleColor(R.color.colorAccent)
        onboarderPage2.setBackgroundColor(R.color.colorPrimaryDark)
        onboarderPage3.setTitleColor(R.color.colorAccent)
        onboarderPage3.setBackgroundColor(R.color.colorPrimaryDark)

        // Add your pages to the list
        onboarderPages.add(onboarderPage1)
        onboarderPages.add(onboarderPage2)
        onboarderPages.add(onboarderPage3)

        // And pass your pages to 'setOnboardPagesReady' method
        setActiveIndicatorColor(android.R.color.white)
        setInactiveIndicatorColor(android.R.color.darker_gray)
        shouldUseFloatingActionButton(true)
        setOnboardPagesReady(onboarderPages)
    }

    public override fun onSkipButtonPressed() {
        // Optional: by default it skips onboarder to the end
        super.onSkipButtonPressed()
        // Define your actions when the user press 'Skip' button
        finish()
    }

    override fun onFinishButtonPressed() {
        // Define your actions when the user press 'Finish' button
        PreferenceManager.setDefaultValues(this, R.xml.user_settings, false)
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPref.edit().putBoolean("intro_notShow", false).apply()
        finish()
    }
}