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
import java.util.ArrayList;
import java.util.List;

import de.baumann.quitsmoking.fragments.FragmentGoal;
import de.baumann.quitsmoking.fragments.FragmentHealth;
import de.baumann.quitsmoking.fragments.FragmentNotes;
import de.baumann.quitsmoking.fragments.FragmentOverview;
import de.baumann.quitsmoking.helper.helper_main;

public class MainActivity extends AppCompatActivity {

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private SharedPreferences SP;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        PreferenceManager.setDefaultValues(MainActivity.this, R.xml.user_settings, false);
        SP = PreferenceManager.getDefaultSharedPreferences(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPager viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
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

        File directory_data = new File(Environment.getExternalStorageDirectory() + "/Android/data/de.baumann.quitsmoking");
        if (!directory_data.exists()) {
            directory_data.mkdirs();
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