package de.baumann.quitsmoking;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.baumann.quitsmoking.fragments.FragmentGoal;
import de.baumann.quitsmoking.fragments.FragmentHealth;
import de.baumann.quitsmoking.fragments.FragmentNotes;
import de.baumann.quitsmoking.fragments.FragmentOverview;
import de.baumann.quitsmoking.helper.helper_main;

public class MainActivity extends AppCompatActivity {

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private static final String SPtimeDiffDays = "SPtimeDiffDays";
    private static final String SPtimeDiffHours = "SPtimeDifHours";
    private static final String SPtimeDiffMinutes = "SPtimeDiffMinutes";

    private static final String SPcigSavedString = "SPcigSavedString";
    private static final String SPmoneySavedString = "SPmoneySavedString";
    private static final String SPtimeSavedString = "SPtimeSavedString";

    private SharedPreferences SP;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(MainActivity.this, R.xml.user_settings, false);
        SP = PreferenceManager.getDefaultSharedPreferences(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);

        if (SP.getBoolean ("first_run", true)){
            SP.edit().putBoolean("first_run", false).apply();
            Intent intent_in = new Intent(MainActivity.this, UserSettingsActivity.class);
            startActivity(intent_in);
            overridePendingTransition(0, 0);
            finish();
        }

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            int hasWRITE_EXTERNAL_STORAGE = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle(R.string.app_permissions_title)
                            .setMessage(helper_main.textSpannable(getString(R.string.app_permissions)))
                            .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (android.os.Build.VERSION.SDK_INT >= 23)
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                }
                            })
                            .setNegativeButton(getString(R.string.no), null)
                            .show();
                    return;
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }

        File directory = new File(Environment.getExternalStorageDirectory() + "/Android/data/quitsmoking.backup");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        calculate();
    }

    private void calculate () {

        SP = PreferenceManager.getDefaultSharedPreferences(this);

        String dateFormat = SP.getString("dateFormat", "1");
        String cigNumb = SP.getString("cig", "");
        String dateQuit = SP.getString("date", "");
        String timeQuit = SP.getString("time", "");
        String savedMoney = SP.getString("costs", "");
        String savedTime = SP.getString("duration", "");

        switch (dateFormat) {
            case "1":
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

                String dateStart = dateQuit + " " + timeQuit;
                String dateStop = format.format(date);

                try {
                    Date d1 = format.parse(dateStart);
                    Date d2 = format.parse(dateStop);

                    //Time Difference
                    long timeDiff = d2.getTime() - d1.getTime();
                    long timeDiffMinutes = timeDiff / (60 * 1000) % 60;
                    long timeDiffHours = timeDiff / (60 * 60 * 1000) % 24;
                    long timeDiffDays = timeDiff / (24 * 60 * 60 * 1000);
                    String timeDiffDaysString = Long.toString(timeDiffDays);
                    String timeDiffHoursString = Long.toString(timeDiffHours);
                    String timeDiffMinutesString = Long.toString(timeDiffMinutes);
                    SP.edit().putString(SPtimeDiffDays, timeDiffDaysString).apply();
                    SP.edit().putString(SPtimeDiffHours, timeDiffHoursString).apply();
                    SP.edit().putString(SPtimeDiffMinutes, timeDiffMinutesString).apply();

                    //Saved Cigarettes
                    long cigNumber = Long.parseLong(cigNumb);
                    long cigDay = 86400000 / cigNumber;
                    long savedCig = timeDiff / cigDay;
                    String cigSavedString = Long.toString(savedCig);
                    SP.edit().putString(SPcigSavedString, cigSavedString).apply();

                    //Saved Money
                    double costCig = Double.valueOf(savedMoney.trim());
                    double sa = Long.parseLong(cigSavedString);
                    double moneySaved = sa * costCig;
                    String moneySavedString = String.format(Locale.US, "%.2f", moneySaved);
                    SP.edit().putString(SPmoneySavedString, moneySavedString).apply();

                    //Saved Time
                    double timeMin = Double.valueOf(savedTime.trim());
                    double time = sa * timeMin;
                    String timeSavedString = String.format(Locale.US, "%.1f", time);
                    SP.edit().putString(SPtimeSavedString, timeSavedString).apply();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "2":
                Date date2 = new Date();
                SimpleDateFormat format2 = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

                String dateStart2 = dateQuit + " " + timeQuit;
                String dateStop2 = format2.format(date2);

                try {
                    Date d1 = format2.parse(dateStart2);
                    Date d2 = format2.parse(dateStop2);

                    //Time Difference
                    long timeDiff = d2.getTime() - d1.getTime();
                    long timeDiffMinutes = timeDiff / (60 * 1000) % 60;
                    long timeDiffHours = timeDiff / (60 * 60 * 1000) % 24;
                    long timeDiffDays = timeDiff / (24 * 60 * 60 * 1000);
                    String timeDiffDaysString = Long.toString(timeDiffDays);
                    String timeDiffHoursString = Long.toString(timeDiffHours);
                    String timeDiffMinutesString = Long.toString(timeDiffMinutes);
                    SP.edit().putString(SPtimeDiffDays, timeDiffDaysString).apply();
                    SP.edit().putString(SPtimeDiffHours, timeDiffHoursString).apply();
                    SP.edit().putString(SPtimeDiffMinutes, timeDiffMinutesString).apply();

                    //Saved Cigarettes
                    long cigNumber = Long.parseLong(cigNumb);
                    long cigDay = 86400000 / cigNumber;
                    long savedCig = timeDiff / cigDay;
                    String cigSavedString = Long.toString(savedCig);
                    SP.edit().putString(SPcigSavedString, cigSavedString).apply();

                    //Saved Money
                    double costCig = Double.valueOf(savedMoney.trim());
                    double sa = Long.parseLong(cigSavedString);
                    double moneySaved = sa * costCig;
                    String moneySavedString = String.format(Locale.US, "%.2f", moneySaved);
                    SP.edit().putString(SPmoneySavedString, moneySavedString).apply();

                    //Saved Time
                    double timeMin = Double.valueOf(savedTime.trim());
                    double time = sa * timeMin;
                    String timeSavedString = String.format(Locale.US, "%.1f", time);
                    SP.edit().putString(SPtimeSavedString, timeSavedString).apply();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        String tab_diary;
        if (SP.getString("sortDB", "title").equals("title")) {
            tab_diary = getString(R.string.action_diary) + " | " + getString(R.string.sort_title);
        } else if (SP.getString("sortDB", "title").equals("icon")) {
            tab_diary = getString(R.string.action_diary) + " | " + getString(R.string.sort_pri);
        }  else {
            tab_diary = getString(R.string.action_diary) + " | " + getString(R.string.sort_date);
        }

        if (SP.getBoolean("tab_overview", false)) {
            adapter.addFragment(new FragmentOverview(), String.valueOf(getString(R.string.action_overview)));
        }
        if (SP.getBoolean("tab_health", false)) {
            adapter.addFragment(new FragmentHealth(), String.valueOf(getString(R.string.action_health)));
        }
        if (SP.getBoolean("tab_goal", false)) {
            adapter.addFragment(new FragmentGoal(), String.valueOf(getString(R.string.action_goal)));
        }
        if (SP.getBoolean("tab_diary", false)) {
            adapter.addFragment(new FragmentNotes(), tab_diary);
        }

        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);// add return null; to display only icons
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {

            Intent intent_in = new Intent(MainActivity.this, UserSettingsActivity.class);
            startActivity(intent_in);
            overridePendingTransition(0, 0);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}