package de.baumann.quitsmoking.fragments;

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

import de.baumann.quitsmoking.R;

public class FragmentHealth extends Fragment {

    private SharedPreferences SP;

    @SuppressLint("CutPasteId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_health, container, false);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.user_settings, false);
        SP = PreferenceManager.getDefaultSharedPreferences(getActivity());

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

                setProgressCase1(rootView,      8,  R.id.progressBar2,  R.id.text_reached2);
                setProgressCase1(rootView,     24,  R.id.progressBar3,  R.id.text_reached3);
                setProgressCase1(rootView,     48,  R.id.progressBar4,  R.id.text_reached4);
                setProgressCase1(rootView,     72,  R.id.progressBar5,  R.id.text_reached5);
                setProgressCase1(rootView,    168,  R.id.progressBar6,  R.id.text_reached6);
                setProgressCase1(rootView,   2160,  R.id.progressBar7,  R.id.text_reached7);
                setProgressCase1(rootView,   6480,  R.id.progressBar8,  R.id.text_reached8);
                setProgressCase1(rootView,   8760,  R.id.progressBar9,  R.id.text_reached9);
                setProgressCase1(rootView,  17520, R.id.progressBar10, R.id.text_reached10);
                setProgressCase1(rootView,  43800, R.id.progressBar11, R.id.text_reached11);
                setProgressCase1(rootView,  87600, R.id.progressBar12, R.id.text_reached12);
                setProgressCase1(rootView, 131400, R.id.progressBar13, R.id.text_reached13);

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

                setProgressCase2(rootView,      8,  R.id.progressBar2,  R.id.text_reached2);
                setProgressCase2(rootView,     24,  R.id.progressBar3,  R.id.text_reached3);
                setProgressCase2(rootView,     48,  R.id.progressBar4,  R.id.text_reached4);
                setProgressCase2(rootView,     72,  R.id.progressBar5,  R.id.text_reached5);
                setProgressCase2(rootView,    168,  R.id.progressBar6,  R.id.text_reached6);
                setProgressCase2(rootView,   2160,  R.id.progressBar7,  R.id.text_reached7);
                setProgressCase2(rootView,   6480,  R.id.progressBar8,  R.id.text_reached8);
                setProgressCase2(rootView,   8760,  R.id.progressBar9,  R.id.text_reached9);
                setProgressCase2(rootView,  17520, R.id.progressBar10, R.id.text_reached10);
                setProgressCase2(rootView,  43800, R.id.progressBar11, R.id.text_reached11);
                setProgressCase2(rootView,  87600, R.id.progressBar12, R.id.text_reached12);
                setProgressCase2(rootView, 131400, R.id.progressBar13, R.id.text_reached13);

                break;
        }

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_share).setVisible(false);
        menu.findItem(R.id.action_backup).setVisible(false);
        menu.findItem(R.id.action_image).setVisible(false);
        menu.findItem(R.id.action_note).setVisible(false);
        menu.findItem(R.id.action_sort).setVisible(false);
    }

    private void setProgressCase1 (View view, int hourTime, int progressBarC1, int text) {

        String dateQuit = SP.getString("date", "");
        String timeQuit = SP.getString("time", "");

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        String dateStart = dateQuit + " " + timeQuit;
        String dateStop = format.format(date);

        ProgressBar progressBar;

        try {

            Date d1 = format.parse(dateStart);
            Date d2 = format.parse(dateStop);

            long hour = (60 * 60 * 1000);

            long date1 = d1.getTime();
            long date22 = d2.getTime();

            BigInteger plusDay = BigInteger.valueOf(hour).multiply(BigInteger.valueOf(hourTime));
            long plus = plusDay.longValue();
            long plus2 = plusDay.longValue() / 1000;

            BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date22)));
            long diff = diffCount.longValue();
            long diff2 = diffCount.longValue() / 1000;

            progressBar = (ProgressBar) view.findViewById(progressBarC1);
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
            textView_reached13 = (TextView) view.findViewById(text);
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
    }

    private void setProgressCase2 (View view, int hourTime, int progressBarC2, int text) {

        String dateQuit = SP.getString("date", "");
        String timeQuit = SP.getString("time", "");

        Date date2 = new Date();
        SimpleDateFormat format2 = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

        String dateStart2 = dateQuit + " " + timeQuit;
        String dateStop2 = format2.format(date2);

        ProgressBar progressBar2;

        try {

            Date d1 = format2.parse(dateStart2);
            Date d2 = format2.parse(dateStop2);

            long hour = (60 * 60 * 1000);

            long date1 = d1.getTime();
            long date22 = d2.getTime();

            BigInteger plusDay = BigInteger.valueOf(hour).multiply(BigInteger.valueOf(hourTime));
            long plus = plusDay.longValue();
            long plus2 = plusDay.longValue() / 1000;

            BigInteger diffCount = BigInteger.valueOf(date1).add(BigInteger.valueOf(plus).subtract(BigInteger.valueOf(date22)));
            long diff = diffCount.longValue();
            long diff2 = diffCount.longValue() / 1000;

            progressBar2 = (ProgressBar) view.findViewById(progressBarC2);
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
            textView_reached13 = (TextView) view.findViewById(text);
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
    }
}
