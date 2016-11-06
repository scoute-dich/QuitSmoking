package de.baumann.quitsmoking;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FragmentHealth extends Fragment {

    @SuppressLint("CutPasteId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_health, container, false);
        
        //1
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String dateFormat = SP.getString("dateFormat", "1");
        String dateQuit = SP.getString("date", "");
        String timeQuit = SP.getString("time", "");



        switch (dateFormat) {
            case "1":

                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

                String dateStart = dateQuit + " " + timeQuit;
                String dateStop = format.format(date);

                ProgressBar progressBar;

                try {

                    Date d1 = format.parse(dateStart);
                    Date d2 = format.parse(dateStop);

                    long minute = (60 * 1000);
                    long minuteCount = 20;

                    long date1 = d1.getTime();
                    long date2 = d2.getTime();

                    BigInteger plusDay = BigInteger.valueOf(minute).multiply(BigInteger.valueOf(minuteCount));
                    long plus = plusDay.longValue();
                    long plus2 = plusDay.longValue() / 1000;

                    BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date2)));
                    long diff = diffCount.longValue();
                    long diff2 = diffCount.longValue() / 1000;

                    progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
                    assert progressBar != null;
                    progressBar.setRotation(180);
                    int max = (int) (plus2);
                    int actual = (int) (diff2);
                    progressBar.setMax(max);
                    progressBar.setProgress(actual);

                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffMinutes = diff / (60 * 1000) % 60;

                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_reached1;
                    textView_reached1 = (TextView) rootView.findViewById(R.id.text_reached1);
                    assert textView_reached1 != null;

                    if (diffMinutes < 0) {
                        textView_reached1.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else if (diffHours < 0) {
                        textView_reached1.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else if (diffDays <= 0) {
                        textView_reached1.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else {
                        textView_reached1.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + days + " " + getString(R.string.time_days) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //2
                try {

                    Date d1 = format.parse(dateStart);
                    Date d2 = format.parse(dateStop);

                    long hour = (60 * 60 * 1000);
                    long hourCount = 8;

                    long date1 = d1.getTime();
                    long date2 = d2.getTime();

                    BigInteger plusDay = BigInteger.valueOf(hour).multiply(BigInteger.valueOf(hourCount));
                    long plus = plusDay.longValue();
                    long plus2 = plusDay.longValue() / 1000;

                    BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date2)));
                    long diff = diffCount.longValue();
                    long diff2 = diffCount.longValue() / 1000;

                    progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar2);
                    assert progressBar != null;
                    progressBar.setRotation(180);
                    int max = (int) (plus2);
                    int actual = (int) (diff2);
                    progressBar.setMax(max);
                    progressBar.setProgress(actual);

                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffMinutes = diff / (60 * 1000) % 60;

                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_reached2;
                    textView_reached2 = (TextView) rootView.findViewById(R.id.text_reached2);
                    assert textView_reached2 != null;

                    if (diffMinutes < 0) {
                        textView_reached2.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else if (diffHours < 0) {
                        textView_reached2.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else if (diffDays <= 0) {
                        textView_reached2.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else {
                        textView_reached2.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + days + " " + getString(R.string.time_days) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //3
                try {

                    Date d1 = format.parse(dateStart);
                    Date d2 = format.parse(dateStop);

                    long hour = (60 * 60 * 1000);
                    long hourCount = 24;

                    long date1 = d1.getTime();
                    long date2 = d2.getTime();

                    BigInteger plusDay = BigInteger.valueOf(hour).multiply(BigInteger.valueOf(hourCount));
                    long plus = plusDay.longValue();
                    long plus2 = plusDay.longValue() / 1000;

                    BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date2)));
                    long diff = diffCount.longValue();
                    long diff2 = diffCount.longValue() / 1000;

                    progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar3);
                    assert progressBar != null;
                    progressBar.setRotation(180);
                    int max = (int) (plus2);
                    int actual = (int) (diff2);
                    progressBar.setMax(max);
                    progressBar.setProgress(actual);

                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffMinutes = diff / (60 * 1000) % 60;

                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_reached3;
                    textView_reached3 = (TextView) rootView.findViewById(R.id.text_reached3);
                    assert textView_reached3 != null;

                    if (diffMinutes < 0) {
                        textView_reached3.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else if (diffHours < 0) {
                        textView_reached3.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else if (diffDays <= 0) {
                        textView_reached3.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else {
                        textView_reached3.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + days + " " + getString(R.string.time_days) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //4
                try {

                    Date d1 = format.parse(dateStart);
                    Date d2 = format.parse(dateStop);

                    long hour = (60 * 60 * 1000);
                    long hourCount = 48;

                    long date1 = d1.getTime();
                    long date2 = d2.getTime();

                    BigInteger plusDay = BigInteger.valueOf(hour).multiply(BigInteger.valueOf(hourCount));
                    long plus = plusDay.longValue();
                    long plus2 = plusDay.longValue() / 1000;

                    BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date2)));
                    long diff = diffCount.longValue();
                    long diff2 = diffCount.longValue() / 1000;

                    progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar4);
                    assert progressBar != null;
                    progressBar.setRotation(180);
                    int max = (int) (plus2);
                    int actual = (int) (diff2);
                    progressBar.setMax(max);
                    progressBar.setProgress(actual);

                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffMinutes = diff / (60 * 1000) % 60;

                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_reached4;
                    textView_reached4 = (TextView) rootView.findViewById(R.id.text_reached4);
                    assert textView_reached4 != null;

                    if (diffMinutes < 0) {
                        textView_reached4.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else if (diffHours < 0) {
                        textView_reached4.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else if (diffDays <= 0) {
                        textView_reached4.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else {
                        textView_reached4.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + days + " " + getString(R.string.time_days) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //5
                try {

                    Date d1 = format.parse(dateStart);
                    Date d2 = format.parse(dateStop);

                    long hour = (60 * 60 * 1000);
                    long hourCount = 72;

                    long date1 = d1.getTime();
                    long date2 = d2.getTime();

                    BigInteger plusDay = BigInteger.valueOf(hour).multiply(BigInteger.valueOf(hourCount));
                    long plus = plusDay.longValue();
                    long plus2 = plusDay.longValue() / 1000;

                    BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date2)));
                    long diff = diffCount.longValue();
                    long diff2 = diffCount.longValue() / 1000;

                    progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar5);
                    assert progressBar != null;
                    progressBar.setRotation(180);
                    int max = (int) (plus2);
                    int actual = (int) (diff2);
                    progressBar.setMax(max);
                    progressBar.setProgress(actual);

                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffMinutes = diff / (60 * 1000) % 60;

                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_reached5;
                    textView_reached5 = (TextView) rootView.findViewById(R.id.text_reached5);
                    assert textView_reached5 != null;

                    if (diffMinutes < 0) {
                        textView_reached5.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else if (diffHours < 0) {
                        textView_reached5.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else if (diffDays <= 0) {
                        textView_reached5.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else {
                        textView_reached5.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + days + " " + getString(R.string.time_days) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //6
                try {

                    Date d1 = format.parse(dateStart);
                    Date d2 = format.parse(dateStop);

                    long hour = (60 * 60 * 1000);
                    long hourCount = 168;

                    long date1 = d1.getTime();
                    long date2 = d2.getTime();

                    BigInteger plusDay = BigInteger.valueOf(hour).multiply(BigInteger.valueOf(hourCount));
                    long plus = plusDay.longValue();
                    long plus2 = plusDay.longValue() / 1000;

                    BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date2)));
                    long diff = diffCount.longValue();
                    long diff2 = diffCount.longValue() / 1000;

                    progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar6);
                    assert progressBar != null;
                    progressBar.setRotation(180);
                    int max = (int) (plus2);
                    int actual = (int) (diff2);
                    progressBar.setMax(max);
                    progressBar.setProgress(actual);

                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffMinutes = diff / (60 * 1000) % 60;

                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_reached6;
                    textView_reached6 = (TextView) rootView.findViewById(R.id.text_reached6);
                    assert textView_reached6 != null;

                    if (diffMinutes < 0) {
                        textView_reached6.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else if (diffHours < 0) {
                        textView_reached6.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else if (diffDays <= 0) {
                        textView_reached6.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else {
                        textView_reached6.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + days + " " + getString(R.string.time_days) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //7
                try {

                    Date d1 = format.parse(dateStart);
                    Date d2 = format.parse(dateStop);

                    long hour = (60 * 60 * 1000);
                    long hourCount = 2160;

                    long date1 = d1.getTime();
                    long date2 = d2.getTime();

                    BigInteger plusDay = BigInteger.valueOf(hour).multiply(BigInteger.valueOf(hourCount));
                    long plus = plusDay.longValue();
                    long plus2 = plusDay.longValue() / 1000;

                    BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date2)));
                    long diff = diffCount.longValue();
                    long diff2 = diffCount.longValue() / 1000;

                    progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar7);
                    assert progressBar != null;
                    progressBar.setRotation(180);
                    int max = (int) (plus2);
                    int actual = (int) (diff2);
                    progressBar.setMax(max);
                    progressBar.setProgress(actual);

                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffMinutes = diff / (60 * 1000) % 60;

                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_reached7;
                    textView_reached7 = (TextView) rootView.findViewById(R.id.text_reached7);
                    assert textView_reached7 != null;

                    if (diffMinutes < 0) {
                        textView_reached7.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else if (diffHours < 0) {
                        textView_reached7.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else if (diffDays <= 0) {
                        textView_reached7.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else {
                        textView_reached7.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + days + " " + getString(R.string.time_days) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //8
                try {

                    Date d1 = format.parse(dateStart);
                    Date d2 = format.parse(dateStop);

                    long hour = (60 * 60 * 1000);
                    long hourCount = 6480;

                    long date1 = d1.getTime();
                    long date2 = d2.getTime();

                    BigInteger plusDay = BigInteger.valueOf(hour).multiply(BigInteger.valueOf(hourCount));
                    long plus = plusDay.longValue();
                    long plus2 = plusDay.longValue() / 1000;

                    BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date2)));
                    long diff = diffCount.longValue();
                    long diff2 = diffCount.longValue() / 1000;

                    progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar8);
                    assert progressBar != null;
                    progressBar.setRotation(180);
                    int max = (int) (plus2);
                    int actual = (int) (diff2);
                    progressBar.setMax(max);
                    progressBar.setProgress(actual);

                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffMinutes = diff / (60 * 1000) % 60;

                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_reached8;
                    textView_reached8 = (TextView) rootView.findViewById(R.id.text_reached8);
                    assert textView_reached8 != null;

                    if (diffMinutes < 0) {
                        textView_reached8.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else if (diffHours < 0) {
                        textView_reached8.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else if (diffDays <= 0) {
                        textView_reached8.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else {
                        textView_reached8.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + days + " " + getString(R.string.time_days) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //9
                try {

                    Date d1 = format.parse(dateStart);
                    Date d2 = format.parse(dateStop);

                    long hour = (60 * 60 * 1000);
                    long hourCount = 8760;

                    long date1 = d1.getTime();
                    long date2 = d2.getTime();

                    BigInteger plusDay = BigInteger.valueOf(hour).multiply(BigInteger.valueOf(hourCount));
                    long plus = plusDay.longValue();
                    long plus2 = plusDay.longValue() / 1000;

                    BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date2)));
                    long diff = diffCount.longValue();
                    long diff2 = diffCount.longValue() / 1000;

                    progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar9);
                    assert progressBar != null;
                    progressBar.setRotation(180);
                    int max = (int) (plus2);
                    int actual = (int) (diff2);
                    progressBar.setMax(max);
                    progressBar.setProgress(actual);

                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffMinutes = diff / (60 * 1000) % 60;

                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_reached9;
                    textView_reached9 = (TextView) rootView.findViewById(R.id.text_reached9);
                    assert textView_reached9 != null;

                    if (diffMinutes < 0) {
                        textView_reached9.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else if (diffHours < 0) {
                        textView_reached9.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else if (diffDays <= 0) {
                        textView_reached9.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else {
                        textView_reached9.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + days + " " + getString(R.string.time_days) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //10
                try {

                    Date d1 = format.parse(dateStart);
                    Date d2 = format.parse(dateStop);

                    long hour = (60 * 60 * 1000);
                    long hourCount = 17520;

                    long date1 = d1.getTime();
                    long date2 = d2.getTime();

                    BigInteger plusDay = BigInteger.valueOf(hour).multiply(BigInteger.valueOf(hourCount));
                    long plus = plusDay.longValue();
                    long plus2 = plusDay.longValue() / 1000;

                    BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date2)));
                    long diff = diffCount.longValue();
                    long diff2 = diffCount.longValue() / 1000;

                    progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar10);
                    assert progressBar != null;
                    progressBar.setRotation(180);
                    int max = (int) (plus2);
                    int actual = (int) (diff2);
                    progressBar.setMax(max);
                    progressBar.setProgress(actual);

                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffMinutes = diff / (60 * 1000) % 60;

                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_reached10;
                    textView_reached10 = (TextView) rootView.findViewById(R.id.text_reached10);
                    assert textView_reached10 != null;

                    if (diffMinutes < 0) {
                        textView_reached10.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else if (diffHours < 0) {
                        textView_reached10.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else if (diffDays <= 0) {
                        textView_reached10.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else {
                        textView_reached10.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + days + " " + getString(R.string.time_days) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //11
                try {

                    Date d1 = format.parse(dateStart);
                    Date d2 = format.parse(dateStop);

                    long hour = (60 * 60 * 1000);
                    long hourCount = 43800;

                    long date1 = d1.getTime();
                    long date2 = d2.getTime();

                    BigInteger plusDay = BigInteger.valueOf(hour).multiply(BigInteger.valueOf(hourCount));
                    long plus = plusDay.longValue();
                    long plus2 = plusDay.longValue() / 1000;

                    BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date2)));
                    long diff = diffCount.longValue();
                    long diff2 = diffCount.longValue() / 1000;

                    progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar11);
                    assert progressBar != null;
                    progressBar.setRotation(180);
                    int max = (int) (plus2);
                    int actual = (int) (diff2);
                    progressBar.setMax(max);
                    progressBar.setProgress(actual);

                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffMinutes = diff / (60 * 1000) % 60;

                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_reached11;
                    textView_reached11 = (TextView) rootView.findViewById(R.id.text_reached11);
                    assert textView_reached11 != null;

                    if (diffMinutes < 0) {
                        textView_reached11.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else if (diffHours < 0) {
                        textView_reached11.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else if (diffDays <= 0) {
                        textView_reached11.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else {
                        textView_reached11.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + days + " " + getString(R.string.time_days) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //12
                try {

                    Date d1 = format.parse(dateStart);
                    Date d2 = format.parse(dateStop);

                    long hour = (60 * 60 * 1000);
                    long hourCount = 87600;

                    long date1 = d1.getTime();
                    long date2 = d2.getTime();

                    BigInteger plusDay = BigInteger.valueOf(hour).multiply(BigInteger.valueOf(hourCount));
                    long plus = plusDay.longValue();
                    long plus2 = plusDay.longValue() / 1000;

                    BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date2)));
                    long diff = diffCount.longValue();
                    long diff2 = diffCount.longValue() / 1000;

                    progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar12);
                    assert progressBar != null;
                    progressBar.setRotation(180);
                    int max = (int) (plus2);
                    int actual = (int) (diff2);
                    progressBar.setMax(max);
                    progressBar.setProgress(actual);

                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffMinutes = diff / (60 * 1000) % 60;

                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_reached12;
                    textView_reached12 = (TextView) rootView.findViewById(R.id.text_reached12);
                    assert textView_reached12 != null;

                    if (diffMinutes < 0) {
                        textView_reached12.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else if (diffHours < 0) {
                        textView_reached12.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else if (diffDays <= 0) {
                        textView_reached12.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else {
                        textView_reached12.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + days + " " + getString(R.string.time_days) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //13
                try {

                    Date d1 = format.parse(dateStart);
                    Date d2 = format.parse(dateStop);

                    long hour = (60 * 60 * 1000);
                    long hourCount = 131400;

                    long date1 = d1.getTime();
                    long date2 = d2.getTime();

                    BigInteger plusDay = BigInteger.valueOf(hour).multiply(BigInteger.valueOf(hourCount));
                    long plus = plusDay.longValue();
                    long plus2 = plusDay.longValue() / 1000;

                    BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date2)));
                    long diff = diffCount.longValue();
                    long diff2 = diffCount.longValue() / 1000;

                    progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar13);
                    assert progressBar != null;
                    progressBar.setRotation(180);
                    int max = (int) (plus2);
                    int actual = (int) (diff2);
                    progressBar.setMax(max);
                    progressBar.setProgress(actual);

                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffMinutes = diff / (60 * 1000) % 60;

                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_reached13;
                    textView_reached13 = (TextView) rootView.findViewById(R.id.text_reached13);
                    assert textView_reached13 != null;

                    if (diffMinutes < 0) {
                        textView_reached13.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else if (diffHours < 0) {
                        textView_reached13.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else if (diffDays <= 0) {
                        textView_reached13.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else {
                        textView_reached13.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + days + " " + getString(R.string.time_days) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case "2":

                Date date2 = new Date();
                SimpleDateFormat format2 = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

                String dateStart2 = dateQuit + " " + timeQuit;
                String dateStop2 = format2.format(date2);

                ProgressBar progressBar2;

                try {

                    Date d1 = format2.parse(dateStart2);
                    Date d2 = format2.parse(dateStop2);

                    long minute = (60 * 1000);
                    long minuteCount = 20;

                    long date1 = d1.getTime();
                    long date22 = d2.getTime();

                    BigInteger plusDay = BigInteger.valueOf(minute).multiply(BigInteger.valueOf(minuteCount));
                    long plus = plusDay.longValue();
                    long plus2 = plusDay.longValue() / 1000;

                    BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date22)));
                    long diff = diffCount.longValue();
                    long diff2 = diffCount.longValue() / 1000;

                    progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar);
                    assert progressBar2 != null;
                    progressBar2.setRotation(180);
                    int max = (int) (plus2);
                    int actual = (int) (diff2);
                    progressBar2.setMax(max);
                    progressBar2.setProgress(actual);

                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffMinutes = diff / (60 * 1000) % 60;

                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_reached1;
                    textView_reached1 = (TextView) rootView.findViewById(R.id.text_reached1);
                    assert textView_reached1 != null;

                    if (diffMinutes < 0) {
                        textView_reached1.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else if (diffHours < 0) {
                        textView_reached1.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else if (diffDays <= 0) {
                        textView_reached1.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else {
                        textView_reached1.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + days + " " + getString(R.string.time_days) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //2
                try {

                    Date d1 = format2.parse(dateStart2);
                    Date d2 = format2.parse(dateStop2);

                    long hour = (60 * 60 * 1000);
                    long hourCount = 8;

                    long date1 = d1.getTime();
                    long date22 = d2.getTime();

                    BigInteger plusDay = BigInteger.valueOf(hour).multiply(BigInteger.valueOf(hourCount));
                    long plus = plusDay.longValue();
                    long plus2 = plusDay.longValue() / 1000;

                    BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date22)));
                    long diff = diffCount.longValue();
                    long diff2 = diffCount.longValue() / 1000;

                    progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar2);
                    assert progressBar2 != null;
                    progressBar2.setRotation(180);
                    int max = (int) (plus2);
                    int actual = (int) (diff2);
                    progressBar2.setMax(max);
                    progressBar2.setProgress(actual);

                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffMinutes = diff / (60 * 1000) % 60;

                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_reached2;
                    textView_reached2 = (TextView) rootView.findViewById(R.id.text_reached2);
                    assert textView_reached2 != null;

                    if (diffMinutes < 0) {
                        textView_reached2.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else if (diffHours < 0) {
                        textView_reached2.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else if (diffDays <= 0) {
                        textView_reached2.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else {
                        textView_reached2.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + days + " " + getString(R.string.time_days) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //3
                try {

                    Date d1 = format2.parse(dateStart2);
                    Date d2 = format2.parse(dateStop2);

                    long hour = (60 * 60 * 1000);
                    long hourCount = 24;

                    long date1 = d1.getTime();
                    long date22 = d2.getTime();

                    BigInteger plusDay = BigInteger.valueOf(hour).multiply(BigInteger.valueOf(hourCount));
                    long plus = plusDay.longValue();
                    long plus2 = plusDay.longValue() / 1000;

                    BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date22)));
                    long diff = diffCount.longValue();
                    long diff2 = diffCount.longValue() / 1000;

                    progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar3);
                    assert progressBar2 != null;
                    progressBar2.setRotation(180);
                    int max = (int) (plus2);
                    int actual = (int) (diff2);
                    progressBar2.setMax(max);
                    progressBar2.setProgress(actual);

                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffMinutes = diff / (60 * 1000) % 60;

                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_reached3;
                    textView_reached3 = (TextView) rootView.findViewById(R.id.text_reached3);
                    assert textView_reached3 != null;

                    if (diffMinutes < 0) {
                        textView_reached3.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else if (diffHours < 0) {
                        textView_reached3.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else if (diffDays <= 0) {
                        textView_reached3.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else {
                        textView_reached3.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + days + " " + getString(R.string.time_days) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //4
                try {

                    Date d1 = format2.parse(dateStart2);
                    Date d2 = format2.parse(dateStop2);

                    long hour = (60 * 60 * 1000);
                    long hourCount = 48;

                    long date1 = d1.getTime();
                    long date22 = d2.getTime();

                    BigInteger plusDay = BigInteger.valueOf(hour).multiply(BigInteger.valueOf(hourCount));
                    long plus = plusDay.longValue();
                    long plus2 = plusDay.longValue() / 1000;

                    BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date22)));
                    long diff = diffCount.longValue();
                    long diff2 = diffCount.longValue() / 1000;

                    progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar4);
                    assert progressBar2 != null;
                    progressBar2.setRotation(180);
                    int max = (int) (plus2);
                    int actual = (int) (diff2);
                    progressBar2.setMax(max);
                    progressBar2.setProgress(actual);

                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffMinutes = diff / (60 * 1000) % 60;

                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_reached4;
                    textView_reached4 = (TextView) rootView.findViewById(R.id.text_reached4);
                    assert textView_reached4 != null;

                    if (diffMinutes < 0) {
                        textView_reached4.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else if (diffHours < 0) {
                        textView_reached4.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else if (diffDays <= 0) {
                        textView_reached4.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else {
                        textView_reached4.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + days + " " + getString(R.string.time_days) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //5
                try {

                    Date d1 = format2.parse(dateStart2);
                    Date d2 = format2.parse(dateStop2);

                    long hour = (60 * 60 * 1000);
                    long hourCount = 72;

                    long date1 = d1.getTime();
                    long date22 = d2.getTime();

                    BigInteger plusDay = BigInteger.valueOf(hour).multiply(BigInteger.valueOf(hourCount));
                    long plus = plusDay.longValue();
                    long plus2 = plusDay.longValue() / 1000;

                    BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date22)));
                    long diff = diffCount.longValue();
                    long diff2 = diffCount.longValue() / 1000;

                    progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar5);
                    assert progressBar2 != null;
                    progressBar2.setRotation(180);
                    int max = (int) (plus2);
                    int actual = (int) (diff2);
                    progressBar2.setMax(max);
                    progressBar2.setProgress(actual);

                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffMinutes = diff / (60 * 1000) % 60;

                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_reached5;
                    textView_reached5 = (TextView) rootView.findViewById(R.id.text_reached5);
                    assert textView_reached5 != null;

                    if (diffMinutes < 0) {
                        textView_reached5.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else if (diffHours < 0) {
                        textView_reached5.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else if (diffDays <= 0) {
                        textView_reached5.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else {
                        textView_reached5.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + days + " " + getString(R.string.time_days) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //6
                try {

                    Date d1 = format2.parse(dateStart2);
                    Date d2 = format2.parse(dateStop2);

                    long hour = (60 * 60 * 1000);
                    long hourCount = 168;

                    long date1 = d1.getTime();
                    long date22 = d2.getTime();

                    BigInteger plusDay = BigInteger.valueOf(hour).multiply(BigInteger.valueOf(hourCount));
                    long plus = plusDay.longValue();
                    long plus2 = plusDay.longValue() / 1000;

                    BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date22)));
                    long diff = diffCount.longValue();
                    long diff2 = diffCount.longValue() / 1000;

                    progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar6);
                    assert progressBar2 != null;
                    progressBar2.setRotation(180);
                    int max = (int) (plus2);
                    int actual = (int) (diff2);
                    progressBar2.setMax(max);
                    progressBar2.setProgress(actual);

                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffMinutes = diff / (60 * 1000) % 60;

                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_reached6;
                    textView_reached6 = (TextView) rootView.findViewById(R.id.text_reached6);
                    assert textView_reached6 != null;

                    if (diffMinutes < 0) {
                        textView_reached6.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else if (diffHours < 0) {
                        textView_reached6.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else if (diffDays <= 0) {
                        textView_reached6.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else {
                        textView_reached6.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + days + " " + getString(R.string.time_days) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //7
                try {

                    Date d1 = format2.parse(dateStart2);
                    Date d2 = format2.parse(dateStop2);

                    long hour = (60 * 60 * 1000);
                    long hourCount = 2160;

                    long date1 = d1.getTime();
                    long date22 = d2.getTime();

                    BigInteger plusDay = BigInteger.valueOf(hour).multiply(BigInteger.valueOf(hourCount));
                    long plus = plusDay.longValue();
                    long plus2 = plusDay.longValue() / 1000;

                    BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date22)));
                    long diff = diffCount.longValue();
                    long diff2 = diffCount.longValue() / 1000;

                    progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar7);
                    assert progressBar2 != null;
                    progressBar2.setRotation(180);
                    int max = (int) (plus2);
                    int actual = (int) (diff2);
                    progressBar2.setMax(max);
                    progressBar2.setProgress(actual);

                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffMinutes = diff / (60 * 1000) % 60;

                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_reached7;
                    textView_reached7 = (TextView) rootView.findViewById(R.id.text_reached7);
                    assert textView_reached7 != null;

                    if (diffMinutes < 0) {
                        textView_reached7.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else if (diffHours < 0) {
                        textView_reached7.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else if (diffDays <= 0) {
                        textView_reached7.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else {
                        textView_reached7.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + days + " " + getString(R.string.time_days) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //8
                try {

                    Date d1 = format2.parse(dateStart2);
                    Date d2 = format2.parse(dateStop2);

                    long hour = (60 * 60 * 1000);
                    long hourCount = 6480;

                    long date1 = d1.getTime();
                    long date22 = d2.getTime();

                    BigInteger plusDay = BigInteger.valueOf(hour).multiply(BigInteger.valueOf(hourCount));
                    long plus = plusDay.longValue();
                    long plus2 = plusDay.longValue() / 1000;

                    BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date22)));
                    long diff = diffCount.longValue();
                    long diff2 = diffCount.longValue() / 1000;

                    progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar8);
                    assert progressBar2 != null;
                    progressBar2.setRotation(180);
                    int max = (int) (plus2);
                    int actual = (int) (diff2);
                    progressBar2.setMax(max);
                    progressBar2.setProgress(actual);

                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffMinutes = diff / (60 * 1000) % 60;

                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_reached8;
                    textView_reached8 = (TextView) rootView.findViewById(R.id.text_reached8);
                    assert textView_reached8 != null;

                    if (diffMinutes < 0) {
                        textView_reached8.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else if (diffHours < 0) {
                        textView_reached8.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else if (diffDays <= 0) {
                        textView_reached8.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else {
                        textView_reached8.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + days + " " + getString(R.string.time_days) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //9
                try {

                    Date d1 = format2.parse(dateStart2);
                    Date d2 = format2.parse(dateStop2);

                    long hour = (60 * 60 * 1000);
                    long hourCount = 8760;

                    long date1 = d1.getTime();
                    long date22 = d2.getTime();

                    BigInteger plusDay = BigInteger.valueOf(hour).multiply(BigInteger.valueOf(hourCount));
                    long plus = plusDay.longValue();
                    long plus2 = plusDay.longValue() / 1000;

                    BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date22)));
                    long diff = diffCount.longValue();
                    long diff2 = diffCount.longValue() / 1000;

                    progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar9);
                    assert progressBar2 != null;
                    progressBar2.setRotation(180);
                    int max = (int) (plus2);
                    int actual = (int) (diff2);
                    progressBar2.setMax(max);
                    progressBar2.setProgress(actual);

                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffMinutes = diff / (60 * 1000) % 60;

                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_reached9;
                    textView_reached9 = (TextView) rootView.findViewById(R.id.text_reached9);
                    assert textView_reached9 != null;

                    if (diffMinutes < 0) {
                        textView_reached9.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else if (diffHours < 0) {
                        textView_reached9.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else if (diffDays <= 0) {
                        textView_reached9.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else {
                        textView_reached9.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + days + " " + getString(R.string.time_days) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //10
                try {

                    Date d1 = format2.parse(dateStart2);
                    Date d2 = format2.parse(dateStop2);

                    long hour = (60 * 60 * 1000);
                    long hourCount = 17520;

                    long date1 = d1.getTime();
                    long date22 = d2.getTime();

                    BigInteger plusDay = BigInteger.valueOf(hour).multiply(BigInteger.valueOf(hourCount));
                    long plus = plusDay.longValue();
                    long plus2 = plusDay.longValue() / 1000;

                    BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date22)));
                    long diff = diffCount.longValue();
                    long diff2 = diffCount.longValue() / 1000;

                    progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar10);
                    assert progressBar2 != null;
                    progressBar2.setRotation(180);
                    int max = (int) (plus2);
                    int actual = (int) (diff2);
                    progressBar2.setMax(max);
                    progressBar2.setProgress(actual);

                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffMinutes = diff / (60 * 1000) % 60;

                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_reached10;
                    textView_reached10 = (TextView) rootView.findViewById(R.id.text_reached10);
                    assert textView_reached10 != null;

                    if (diffMinutes < 0) {
                        textView_reached10.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else if (diffHours < 0) {
                        textView_reached10.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else if (diffDays <= 0) {
                        textView_reached10.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else {
                        textView_reached10.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + days + " " + getString(R.string.time_days) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //11
                try {

                    Date d1 = format2.parse(dateStart2);
                    Date d2 = format2.parse(dateStop2);

                    long hour = (60 * 60 * 1000);
                    long hourCount = 43800;

                    long date1 = d1.getTime();
                    long date22 = d2.getTime();

                    BigInteger plusDay = BigInteger.valueOf(hour).multiply(BigInteger.valueOf(hourCount));
                    long plus = plusDay.longValue();
                    long plus2 = plusDay.longValue() / 1000;

                    BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date22)));
                    long diff = diffCount.longValue();
                    long diff2 = diffCount.longValue() / 1000;

                    progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar11);
                    assert progressBar2 != null;
                    progressBar2.setRotation(180);
                    int max = (int) (plus2);
                    int actual = (int) (diff2);
                    progressBar2.setMax(max);
                    progressBar2.setProgress(actual);

                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffMinutes = diff / (60 * 1000) % 60;

                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_reached11;
                    textView_reached11 = (TextView) rootView.findViewById(R.id.text_reached11);
                    assert textView_reached11 != null;

                    if (diffMinutes < 0) {
                        textView_reached11.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else if (diffHours < 0) {
                        textView_reached11.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else if (diffDays <= 0) {
                        textView_reached11.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else {
                        textView_reached11.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + days + " " + getString(R.string.time_days) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //12
                try {

                    Date d1 = format2.parse(dateStart2);
                    Date d2 = format2.parse(dateStop2);

                    long hour = (60 * 60 * 1000);
                    long hourCount = 87600;

                    long date1 = d1.getTime();
                    long date22 = d2.getTime();

                    BigInteger plusDay = BigInteger.valueOf(hour).multiply(BigInteger.valueOf(hourCount));
                    long plus = plusDay.longValue();
                    long plus2 = plusDay.longValue() / 1000;

                    BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date22)));
                    long diff = diffCount.longValue();
                    long diff2 = diffCount.longValue() / 1000;

                    progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar12);
                    assert progressBar2 != null;
                    progressBar2.setRotation(180);
                    int max = (int) (plus2);
                    int actual = (int) (diff2);
                    progressBar2.setMax(max);
                    progressBar2.setProgress(actual);

                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffMinutes = diff / (60 * 1000) % 60;

                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_reached12;
                    textView_reached12 = (TextView) rootView.findViewById(R.id.text_reached12);
                    assert textView_reached12 != null;

                    if (diffMinutes < 0) {
                        textView_reached12.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else if (diffHours < 0) {
                        textView_reached12.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else if (diffDays <= 0) {
                        textView_reached12.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else {
                        textView_reached12.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + days + " " + getString(R.string.time_days) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                //13
                try {

                    Date d1 = format2.parse(dateStart2);
                    Date d2 = format2.parse(dateStop2);

                    long hour = (60 * 60 * 1000);
                    long hourCount = 131400;

                    long date1 = d1.getTime();
                    long date22 = d2.getTime();

                    BigInteger plusDay = BigInteger.valueOf(hour).multiply(BigInteger.valueOf(hourCount));
                    long plus = plusDay.longValue();
                    long plus2 = plusDay.longValue() / 1000;

                    BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date22)));
                    long diff = diffCount.longValue();
                    long diff2 = diffCount.longValue() / 1000;

                    progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar13);
                    assert progressBar2 != null;
                    progressBar2.setRotation(180);
                    int max = (int) (plus2);
                    int actual = (int) (diff2);
                    progressBar2.setMax(max);
                    progressBar2.setProgress(actual);

                    long diffDays = diff / (24 * 60 * 60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffMinutes = diff / (60 * 1000) % 60;

                    String days = Long.toString(diffDays);
                    String hours = Long.toString(diffHours);
                    String minutes = Long.toString(diffMinutes);

                    TextView textView_reached13;
                    textView_reached13 = (TextView) rootView.findViewById(R.id.text_reached13);
                    assert textView_reached13 != null;

                    if (diffMinutes < 0) {
                        textView_reached13.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else if (diffHours < 0) {
                        textView_reached13.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else if (diffDays <= 0) {
                        textView_reached13.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    } else {
                        textView_reached13.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + days + " " + getString(R.string.time_days) + " "
                                + hours + " " + getString(R.string.time_hours) + " "
                                + minutes + " " + getString(R.string.time_minutes)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }



        return rootView;
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_share).setVisible(false);
        menu.findItem(R.id.action_date).setVisible(false);
        menu.findItem(R.id.action_time).setVisible(false);
        menu.findItem(R.id.action_line).setVisible(false);
        menu.findItem(R.id.action_backup).setVisible(false);
        menu.findItem(R.id.action_imageDelete).setVisible(false);
        menu.findItem(R.id.action_imageRotate).setVisible(false);
        menu.findItem(R.id.action_imageLoad).setVisible(false);
    }

}
