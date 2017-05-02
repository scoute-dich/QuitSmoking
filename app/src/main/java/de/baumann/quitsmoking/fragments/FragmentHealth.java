package de.baumann.quitsmoking.fragments;

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.baumann.quitsmoking.R;

public class FragmentHealth extends Fragment {

    private SharedPreferences SP;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_health, container, false);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.user_settings, false);
        SP = PreferenceManager.getDefaultSharedPreferences(getActivity());

        setProgress(rootView, (0.33),   R.id.progressBar,  R.id.text_reached1);
        setProgress(rootView,      8,  R.id.progressBar2,  R.id.text_reached2);
        setProgress(rootView,     24,  R.id.progressBar3,  R.id.text_reached3);
        setProgress(rootView,     48,  R.id.progressBar4,  R.id.text_reached4);
        setProgress(rootView,     72,  R.id.progressBar5,  R.id.text_reached5);
        setProgress(rootView,    168,  R.id.progressBar6,  R.id.text_reached6);
        setProgress(rootView,   2160,  R.id.progressBar7,  R.id.text_reached7);
        setProgress(rootView,   6480,  R.id.progressBar8,  R.id.text_reached8);
        setProgress(rootView,   8760,  R.id.progressBar9,  R.id.text_reached9);
        setProgress(rootView,  17520, R.id.progressBar10, R.id.text_reached10);
        setProgress(rootView,  43800, R.id.progressBar11, R.id.text_reached11);
        setProgress(rootView,  87600, R.id.progressBar12, R.id.text_reached12);
        setProgress(rootView, 131400, R.id.progressBar13, R.id.text_reached13);

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
        menu.findItem(R.id.action_filter).setVisible(false);
        menu.findItem(R.id.action_sort).setVisible(false);
        menu.findItem(R.id.action_reset).setVisible(false);
    }

    private void setProgress (View view, double hourTime, int progressBar_ID, int text) {

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

        String dateStart = format.format(SP.getLong("startTime", 0));
        String dateStop = format.format(date);

        ProgressBar progressBar;

        try {

            Date d1 = format.parse(dateStart);
            Date d2 = format.parse(dateStop);

            long hour = (60 * 60 * 1000);

            long date1 = d1.getTime();
            long date2 = d2.getTime();

            double plusDay = hour * hourTime;
            double plus2 = plusDay / 1000;

            double diffCount = date1 + plusDay - date2;
            double diff2 = diffCount / 1000;

            double diffDays = diffCount / (24 * 60 * 60 * 1000);
            double diffHours = diffCount / (60 * 60 * 1000) % 24;
            double diffMinutes = diffCount / (60 * 1000) % 60;

            progressBar = (ProgressBar) view.findViewById(progressBar_ID);
            assert progressBar != null;
            progressBar.setRotation(180);
            int max = (int) (plus2);
            int actual = (int) (diff2);
            progressBar.setMax(max);
            progressBar.setProgress(actual);

            String days = String.format(Locale.GERMANY, "%.0f", diffDays);
            String hours = String.format(Locale.GERMANY, "%.0f", diffHours);
            String minutes = String.format(Locale.GERMANY, "%.0f", diffMinutes);

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