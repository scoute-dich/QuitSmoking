package de.baumann.quitsmoking;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import de.baumann.quitsmoking.about.About_activity;
import de.baumann.quitsmoking.helper.Activity_intro;
import de.baumann.quitsmoking.helper.helper_main;


public class UserSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_user_settings);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            setTitle(R.string.action_settings);
        }

        PreferenceManager.setDefaultValues(UserSettingsActivity.this, R.xml.user_settings, false);
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(this);
        boolean show = SP.getBoolean("intro_notShow", true);

        if (show){
            Intent mainIntent = new Intent(UserSettingsActivity.this, Activity_intro.class);
            startActivity(mainIntent);
            overridePendingTransition(0,0);
        }

        // Display the fragment as the fragment_main content
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragment {


        private void addLicenseListener() {

            Preference reset = findPreference("license");
            reset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference pref) {

                    Intent intent_in = new Intent(getActivity(), About_activity.class);
                    startActivity(intent_in);
                    getActivity().overridePendingTransition(0, 0);

                    return true;
                }
            });
        }

        private void addClearCacheListener() {

            final Activity activity = getActivity();

            Preference reset = findPreference("clearCache");
            reset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
            {
                public boolean onPreferenceClick(Preference pref)
                {

                    Intent intent = new Intent();
                    intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                    intent.setData(uri);
                    getActivity().startActivity(intent);

                    return true;
                }
            });
        }

        private void addDateListener() {

            Preference reset = findPreference("time");
            reset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference pref) {

                    PreferenceManager.setDefaultValues(getActivity(), R.xml.user_settings, false);
                    final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View dialogView = View.inflate(getActivity(), R.layout.dialog_date, null);

                    final EditText editDate = (EditText) dialogView.findViewById(R.id.textDate);
                    editDate.setText(sharedPref.getString("date", ""));
                    final EditText editTime = (EditText) dialogView.findViewById(R.id.textTime);
                    editTime.setText(sharedPref.getString("time", ""));

                    builder.setView(dialogView);
                    builder.setTitle(R.string.a_time);
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            String inputTag = editDate.getText().toString().trim();
                            sharedPref.edit().putString("date", inputTag).apply();

                            String inputTag2 = editTime.getText().toString().trim();
                            sharedPref.edit().putString("time", inputTag2).apply();
                        }
                    });
                    builder.setNegativeButton(R.string.goal_cancel, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });

                    final AlertDialog dialog2 = builder.create();
                    // Display the custom alert dialog on interface
                    dialog2.show();

                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            helper_main.showKeyboard(getActivity(), editDate);
                        }
                    }, 200);

                    return true;
                }
            });
        }

        private void addCigListener() {

            Preference reset = findPreference("cig");
            reset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference pref) {

                    PreferenceManager.setDefaultValues(getActivity(), R.xml.user_settings, false);
                    final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View dialogView = View.inflate(getActivity(), R.layout.dialog_cig, null);

                    final EditText editNumber = (EditText) dialogView.findViewById(R.id.editNumber);
                    editNumber.setText(sharedPref.getString("cig", ""));
                    final EditText editTime = (EditText) dialogView.findViewById(R.id.editTime);
                    editTime.setText(sharedPref.getString("duration", ""));

                    builder.setView(dialogView);
                    builder.setTitle(R.string.a_cig);
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            String inputTag = editNumber.getText().toString().trim();
                            sharedPref.edit().putString("cig", inputTag).apply();
                            String inputTag2 = editTime.getText().toString().trim();
                            sharedPref.edit().putString("duration", inputTag2).apply();
                        }
                    });
                    builder.setNegativeButton(R.string.goal_cancel, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });

                    final AlertDialog dialog2 = builder.create();
                    // Display the custom alert dialog on interface
                    dialog2.show();

                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            helper_main.showKeyboard(getActivity(), editNumber);
                        }
                    }, 200);

                    return true;
                }
            });
        }

        private void addCigCostListener() {

            Preference reset = findPreference("costs");
            reset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference pref) {

                    PreferenceManager.setDefaultValues(getActivity(), R.xml.user_settings, false);
                    final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View dialogView = View.inflate(getActivity(), R.layout.dialog_cig_cost, null);

                    final EditText editNumber = (EditText) dialogView.findViewById(R.id.editNumber);
                    editNumber.setText(sharedPref.getString("costs", ""));

                    builder.setView(dialogView);
                    builder.setTitle(R.string.settings_costs);
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            String inputTag = editNumber.getText().toString().trim();
                            sharedPref.edit().putString("costs", inputTag).apply();
                        }
                    });
                    builder.setNegativeButton(R.string.goal_cancel, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });

                    final AlertDialog dialog2 = builder.create();
                    // Display the custom alert dialog on interface
                    dialog2.show();

                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            helper_main.showKeyboard(getActivity(), editNumber);
                        }
                    }, 200);

                    return true;
                }
            });
        }

        private void addGoalListener() {

            Preference reset = findPreference("goalTitle");
            reset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference pref) {

                    PreferenceManager.setDefaultValues(getActivity(), R.xml.user_settings, false);
                    final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View dialogView = View.inflate(getActivity(), R.layout.dialog_goal, null);

                    final EditText editNumber = (EditText) dialogView.findViewById(R.id.editTitle);
                    editNumber.setText(sharedPref.getString("goalTitle", ""));
                    final EditText editTime = (EditText) dialogView.findViewById(R.id.editCost);
                    editTime.setText(sharedPref.getString("goalCosts", ""));

                    builder.setView(dialogView);
                    builder.setTitle(R.string.a_goal);
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            String inputTag = editNumber.getText().toString().trim();
                            sharedPref.edit().putString("goalTitle", inputTag).apply();
                            String inputTag2 = editTime.getText().toString().trim();
                            sharedPref.edit().putString("goalCosts", inputTag2).apply();
                        }
                    });
                    builder.setNegativeButton(R.string.goal_cancel, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });

                    final AlertDialog dialog2 = builder.create();
                    // Display the custom alert dialog on interface
                    dialog2.show();

                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            helper_main.showKeyboard(getActivity(), editNumber);
                        }
                    }, 200);

                    return true;
                }
            });
        }

        private void addGoal2Listener() {

            Preference reset = findPreference("goalDate");
            reset.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference pref) {

                    PreferenceManager.setDefaultValues(getActivity(), R.xml.user_settings, false);
                    final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View dialogView = View.inflate(getActivity(), R.layout.dialog_goal_date, null);

                    final EditText editDate = (EditText) dialogView.findViewById(R.id.textDate);
                    editDate.setText(sharedPref.getString("goalDate", ""));

                    builder.setView(dialogView);
                    builder.setTitle(R.string.settings_goalDate);
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {

                            String inputTag = editDate.getText().toString().trim();
                            sharedPref.edit().putString("goalDate", inputTag).apply();
                        }
                    });
                    builder.setNegativeButton(R.string.goal_cancel, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });

                    final AlertDialog dialog2 = builder.create();
                    // Display the custom alert dialog on interface
                    dialog2.show();

                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            helper_main.showKeyboard(getActivity(), editDate);
                        }
                    }, 200);

                    return true;
                }
            });
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.user_settings);
            addLicenseListener();
            addClearCacheListener();
            addDateListener();
            addCigListener();
            addCigCostListener();
            addGoalListener();
            addGoal2Listener();
        }
    }

    public void onBackPressed() {
        Intent intent_in = new Intent(UserSettingsActivity.this, MainActivity.class);
        startActivity(intent_in);
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            Intent intent_in = new Intent(UserSettingsActivity.this, MainActivity.class);
            startActivity(intent_in);
            overridePendingTransition(0, 0);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
