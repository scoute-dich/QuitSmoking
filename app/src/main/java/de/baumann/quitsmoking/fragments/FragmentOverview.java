package de.baumann.quitsmoking.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.baumann.quitsmoking.R;

public class FragmentOverview extends Fragment {

    private TextView textView_time2;
    private TextView textView_time3;
    private TextView textView_time4;
    private TextView textView_cig2;
    private TextView textView_cig2_cost;
    private TextView textView_duration;

    private String currency;
    private String dateFormat;
    private String cigNumb;
    private String dateQuit;
    private String timeQuit;
    private String savedMoney;
    private String savedTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.user_settings, false);
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity());

        currency = SP.getString("currency", "1");
        dateFormat = SP.getString("dateFormat", "1");
        cigNumb = SP.getString("cig", "");
        dateQuit = SP.getString("date", "");
        timeQuit = SP.getString("time", "");
        savedMoney = SP.getString("costs", "");
        savedTime = SP.getString("duration", "");

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
                    long diff = d2.getTime() - d1.getTime();
                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_date2 = (TextView) rootView.findViewById(R.id.text_date2);
                    TextView textView_date3 = (TextView) rootView.findViewById(R.id.text_date3);

                    textView_time2 = (TextView) rootView.findViewById(R.id.text_time2);
                    textView_time3 = (TextView) rootView.findViewById(R.id.text_time3);
                    textView_time4 = (TextView) rootView.findViewById(R.id.text_time4);

                    textView_time2.setText(String.valueOf(days + " " + getString(R.string.time_days)));
                    textView_time3.setText(String.valueOf(hours + " " + getString(R.string.time_hours)));
                    textView_time4.setText(String.valueOf(minutes + " " + getString(R.string.time_minutes)));

                    assert textView_date2 != null;
                    textView_date2.setText(String.valueOf(dateQuit));
                    assert textView_date3 != null;
                    textView_date3.setText(String.valueOf(timeQuit));

                    //Number of Cigarettes
                    long cigNumber = Long.parseLong(cigNumb);
                    long cigDay = 86400000 / cigNumber;
                    long diffCig = diff / cigDay;
                    String cigSaved = Long.toString(diffCig);
                    textView_cig2 = (TextView) rootView.findViewById(R.id.text_cigs2);
                    textView_cig2.setText(String.valueOf(cigSaved));

                    //Saved Money
                    double costCig = Double.valueOf(savedMoney.trim());
                    double sa = Long.parseLong(cigSaved);
                    double cost = sa * costCig;
                    String cigCost = String.format(Locale.US, "%.2f", cost);
                    textView_cig2_cost = (TextView) rootView.findViewById(R.id.text_cigs2_cost);
                    switch (currency) {
                        case "1":
                            textView_cig2_cost.setText(String.valueOf(String.valueOf(cigCost) + " " + getString(R.string.money_euro)));
                            break;
                        case "2":
                            textView_cig2_cost.setText(String.valueOf(String.valueOf(cigCost) + " " + getString(R.string.money_dollar)));
                            break;
                        case "3":
                            textView_cig2_cost.setText(String.valueOf(String.valueOf(cigCost) + " " + getString(R.string.money_pound)));
                            break;
                        case "4":
                            textView_cig2_cost.setText(String.valueOf(String.valueOf(cigCost) + " " + getString(R.string.money_yen)));
                            break;
                    }

                    //Saved Time
                    double timeMin = Double.valueOf(savedTime.trim());
                    double time = sa * timeMin;
                    String savedTimeMinutes = String.format(Locale.US, "%.1f", time);
                    textView_duration = (TextView) rootView.findViewById(R.id.text_duration);
                    textView_duration.setText(String.valueOf(savedTimeMinutes + " " + getString(R.string.stat_h)));

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
                    long diff = d2.getTime() - d1.getTime();
                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_date2 = (TextView) rootView.findViewById(R.id.text_date2);
                    TextView textView_date3 = (TextView) rootView.findViewById(R.id.text_date3);

                    textView_time2 = (TextView) rootView.findViewById(R.id.text_time2);
                    textView_time3 = (TextView) rootView.findViewById(R.id.text_time3);
                    textView_time4 = (TextView) rootView.findViewById(R.id.text_time4);

                    textView_time2.setText(String.valueOf(days + " " + getString(R.string.time_days)));
                    textView_time3.setText(String.valueOf(hours + " " + getString(R.string.time_hours)));
                    textView_time4.setText(String.valueOf(minutes + " " + getString(R.string.time_minutes)));

                    assert textView_date2 != null;
                    textView_date2.setText(String.valueOf(dateQuit));
                    assert textView_date3 != null;
                    textView_date3.setText(String.valueOf(timeQuit));

                    //Number of Cigarettes
                    long cigNumber = Long.parseLong(cigNumb);
                    long cigDay = 86400000 / cigNumber;
                    long diffCig = diff / cigDay;
                    String cigSaved = Long.toString(diffCig);
                    textView_cig2 = (TextView) rootView.findViewById(R.id.text_cigs2);
                    textView_cig2.setText(String.valueOf(cigSaved));

                    //Saved Money
                    double costCig = Double.valueOf(savedMoney.trim());
                    double sa = Long.parseLong(cigSaved);
                    double cost = sa * costCig;
                    String cigCost = String.format(Locale.GERMANY, "%.2f", cost);
                    textView_cig2_cost = (TextView) rootView.findViewById(R.id.text_cigs2_cost);
                    switch (currency) {
                        case "1":
                            textView_cig2_cost.setText(String.valueOf(String.valueOf(cigCost) + " " + getString(R.string.money_euro)));
                            break;
                        case "2":
                            textView_cig2_cost.setText(String.valueOf(String.valueOf(cigCost) + " " + getString(R.string.money_dollar)));
                            break;
                        case "3":
                            textView_cig2_cost.setText(String.valueOf(String.valueOf(cigCost) + " " + getString(R.string.money_pound)));
                            break;
                        case "4":
                            textView_cig2_cost.setText(String.valueOf(String.valueOf(cigCost) + " " + getString(R.string.money_yen)));
                            break;
                    }

                    //Saved Time
                    double timeMin = Double.valueOf(savedTime.trim());
                    double time = sa * timeMin;
                    String savedTimeMinutes = String.format(Locale.GERMANY, "%.1f", time);
                    textView_duration = (TextView) rootView.findViewById(R.id.text_duration);
                    textView_duration.setText(String.valueOf(savedTimeMinutes + " " + getString(R.string.stat_h)));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_backup).setVisible(false);
        menu.findItem(R.id.action_image).setVisible(false);
        menu.findItem(R.id.action_note).setVisible(false);
        menu.findItem(R.id.action_sort).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_share:

                if (currency != null  && currency.length() > 0 &&
                    dateFormat != null  && dateFormat.length() > 0 &&
                    cigNumb != null  && cigNumb.length() > 0 &&
                    dateQuit != null  && dateQuit.length() > 0 &&
                    timeQuit != null  && timeQuit.length() > 0 &&
                    savedMoney != null  && savedMoney.length() > 0 &&
                    savedTime != null  && savedTime.length() > 0 ) {

                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, String.valueOf(getString(R.string.share_subject)));

                    String days = textView_time2.getText().toString();
                    String hours = textView_time3.getText().toString();
                    String minutes = textView_time4.getText().toString();

                    String saved_cigarettes = textView_cig2.getText().toString();
                    String saved_money = textView_cig2_cost.getText().toString();
                    String saved_time = textView_duration.getText().toString();

                    sharingIntent.putExtra(Intent.EXTRA_TEXT, String.valueOf(getString(R.string.share_text) + " " +
                            days + " " + hours + " " + getString(R.string.share_text2)) + " " + minutes + ". " +
                            getString(R.string.share_text3) + " " + saved_cigarettes + " " + getString(R.string.share_text4) + ", " +
                            saved_money + " "  + getString(R.string.share_text5) + " " +
                            saved_time + " " + getString(R.string.share_text6));
                    startActivity(Intent.createChooser(sharingIntent, "Share using"));

                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
