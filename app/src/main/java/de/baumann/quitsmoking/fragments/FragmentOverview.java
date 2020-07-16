package de.baumann.quitsmoking.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.baumann.quitsmoking.R;
import de.baumann.quitsmoking.helper.Activity_EditNote;
import de.baumann.quitsmoking.helper.helper_main;

public class FragmentOverview extends Fragment {

    private TextView textView_time2;
    private TextView textView_time3;
    private TextView textView_time4;
    private TextView textView_cig2;
    private TextView textView_cig2_cost;
    private TextView textView_duration;

    private TextView textView_date2;
    private TextView textView_date3;

    private String currency;
    private String dateFormat;
    private String dateQuit;
    private String timeQuit;

    private SharedPreferences SP;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.user_settings, false);
        SP = PreferenceManager.getDefaultSharedPreferences(getActivity());

        textView_cig2_cost = rootView.findViewById(R.id.text_cigs2_cost);
        textView_cig2 = rootView.findViewById(R.id.text_cigs2);
        textView_duration = rootView.findViewById(R.id.text_duration);
        textView_date2 = rootView.findViewById(R.id.text_date2);
        textView_date3 = rootView.findViewById(R.id.text_date3);

        textView_time2 = rootView.findViewById(R.id.text_time2);
        textView_time3 = rootView.findViewById(R.id.text_time3);
        textView_time4 = rootView.findViewById(R.id.text_time4);

        assert textView_date2 != null;
        assert textView_date3 != null;

        currency = SP.getString("currency", "1");
        dateFormat = SP.getString("dateFormat", "1");
        dateQuit = SP.getString("date", "");
        timeQuit = SP.getString("time", "");

        switch (dateFormat) {
            case "1":
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                setText(format);
                break;

            case "2":
                SimpleDateFormat format2 = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
                setText(format2);
                break;
        }

        setHasOptionsMenu(true);
        return rootView;
    }

    private void setText (SimpleDateFormat format) {

        String dateStart = format.format(SP.getLong("startTime", 0));
        dateQuit = dateStart.substring(0, 10);
        timeQuit = dateStart.substring(11, 16);

        try {

            helper_main.calculate(getActivity());

            textView_time2.setText(String.valueOf(SP.getString("SPtimeDiffDays", "0") + " " + getString(R.string.time_days)));
            textView_time3.setText(String.valueOf(SP.getString("SPtimeDiffHours", "0") + " " + getString(R.string.time_hours)));
            textView_time4.setText(String.valueOf(SP.getString("SPtimeDiffMinutes", "0") + " " + getString(R.string.time_minutes)));

            textView_date2.setText(String.valueOf(dateQuit));
            textView_date3.setText(String.valueOf(timeQuit));

            //Number of Cigarettes
            textView_cig2.setText(String.valueOf(SP.getString("SPcigSavedString", "0")));

            //Saved Money

            switch (currency) {
                case "1":
                    textView_cig2_cost.setText(String.valueOf(String.valueOf(SP.getString("SPmoneySavedString", "0"))) + " " + getString(R.string.money_euro));
                    break;
                case "2":
                    textView_cig2_cost.setText(String.valueOf(String.valueOf(SP.getString("SPmoneySavedString", "0")) + " " + getString(R.string.money_dollar)));
                    break;
                case "3":
                    textView_cig2_cost.setText(String.valueOf(String.valueOf(SP.getString("SPmoneySavedString", "0")) + " " + getString(R.string.money_pound)));
                    break;
                case "4":
                    textView_cig2_cost.setText(String.valueOf(String.valueOf(SP.getString("SPmoneySavedString", "0")) + " " + getString(R.string.money_yen)));
                    break;
            }

            //Saved Time
            textView_duration.setText(String.valueOf(SP.getString("SPtimeSavedString", "0") + " " + getString(R.string.stat_h)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_backup).setVisible(false);
        menu.findItem(R.id.action_image).setVisible(false);
        menu.findItem(R.id.action_filter).setVisible(false);
        menu.findItem(R.id.action_sort).setVisible(false);
        menu.findItem(R.id.action_info).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        final String days = textView_time2.getText().toString();
        final String hours = textView_time3.getText().toString();
        final String minutes = textView_time4.getText().toString();

        final String saved_cigarettes = textView_cig2.getText().toString();
        final String saved_money = textView_cig2_cost.getText().toString();
        final String saved_time = textView_duration.getText().toString();

        if (currency != null  && currency.length() > 0 &&
                dateFormat != null  && dateFormat.length() > 0 &&
                dateQuit != null  && dateQuit.length() > 0 &&
                timeQuit != null  && timeQuit.length() > 0) {

            switch (item.getItemId()) {

                case R.id.action_share:
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, String.valueOf(getString(R.string.share_subject)));

                    sharingIntent.putExtra(Intent.EXTRA_TEXT, String.valueOf(getString(R.string.share_text) + " " +
                            days + " " + hours + " " + getString(R.string.share_text2)) + " " + minutes + ". " +
                            getString(R.string.share_text3) + " " + saved_cigarettes + " " + getString(R.string.share_text4) + ", " +
                            saved_money + " "  + getString(R.string.share_text5) + " " +
                            saved_time + " " + getString(R.string.share_text6));
                    startActivity(Intent.createChooser(sharingIntent, "Share using"));
                    return true;

                case R.id.action_reset:
                    Snackbar snackbar = Snackbar
                            .make(textView_time2, R.string.reset_confirm, Snackbar.LENGTH_LONG)
                            .setAction(R.string.yes, new View.OnClickListener() {
                                @SuppressWarnings("ConstantConditions")
                                @Override
                                public void onClick(View view) {

                                    String title = String.valueOf(days + " " + hours + " " + getString(R.string.share_text2)) + " " + minutes;

                                    String text = String.valueOf(getString(R.string.share_text_fail) + " " +
                                            days + " " + hours + " " + getString(R.string.share_text2)) + " " + minutes + ". " +
                                            getString(R.string.share_text3) + " " + saved_cigarettes + " " + getString(R.string.share_text4) + ", " +
                                            saved_money + " "  + getString(R.string.share_text5) + " " +
                                            saved_time + " " + getString(R.string.share_text6);

                                    SP.edit()
                                            .putString("handleTextTitle", title)
                                            .putString("handleTextText", text)
                                            .putString("handleTextCreate", helper_main.createDate())
                                            .apply();
                                    Intent intent = new Intent(getActivity(), Activity_EditNote.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    getActivity().startActivity(intent);

                                    SP.edit().putLong("startTime", Calendar.getInstance().getTimeInMillis()).apply();
                                    getActivity().finish();
                                }
                            });
                    snackbar.show();
                    return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
