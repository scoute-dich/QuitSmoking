package de.baumann.quitsmoking.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import de.baumann.quitsmoking.R;


public class FragmentGoal extends Fragment {

    private ImageView viewImage;
    private String rotate;
    private SharedPreferences SP;
    private String goalTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_goal, container, false);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.user_settings, false);
        SP = PreferenceManager.getDefaultSharedPreferences(getActivity());

        viewImage=(ImageView)rootView.findViewById(R.id.imageView);

        if (SP.getBoolean (rotate, false)){
            viewImage.setRotation(0);
        } else {
            viewImage.setRotation(90);
        }

        File imgFile = new File(Environment.getExternalStorageDirectory() + "/Android/data/de.baumann.quitsmoking/goal_picture.jpg");
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            viewImage.setImageBitmap(myBitmap);
        }

        goalTitle = SP.getString("goalTitle", "");
        TextView textView_goalTitle;
        textView_goalTitle = (TextView) rootView.findViewById(R.id.text_header1);
        if (goalTitle.isEmpty()) {
            textView_goalTitle.setText(String.valueOf(getString(R.string.not_set)));
        } else {
            textView_goalTitle.setText(goalTitle);
        }

        switch (SP.getString("dateFormat", "1")) {
            case "1":
                calculate(rootView, "yyyy-MM-dd HH:mm");
                break;

            case "2":
                calculate(rootView, "dd.MM.yyyy HH:mm");
                break;
        }

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {

                Uri selectedImage = data.getData();
                viewImage.setImageURI(selectedImage);

                BitmapDrawable drawable = (BitmapDrawable) viewImage.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                File imgFile = new File(Environment.getExternalStorageDirectory() + "/Android/data/de.baumann.quitsmoking/goal_picture.jpg");

                // Encode the file as a PNG image.
                FileOutputStream outStream;
                try {

                    outStream = new FileOutputStream(imgFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                    outStream.flush();
                    outStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_backup).setVisible(false);
        menu.findItem(R.id.action_share).setVisible(false);
        menu.findItem(R.id.action_note).setVisible(false);
        menu.findItem(R.id.action_sort).setVisible(false);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (id == R.id.action_image) {
            final CharSequence[] options = {
                    getString(R.string.action_imageLoad),
                    getString(R.string.action_imageRotate),
                    getString(R.string.action_imageDelete)};

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals(getString(R.string.action_imageLoad))) {

                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 1);
                    }

                    if (options[item].equals(getString(R.string.action_imageRotate))) {

                        if (SP.getBoolean (rotate, true)){
                            viewImage.setRotation(90);
                            SP.edit().putBoolean(rotate, false).apply();
                        } else {
                            viewImage.setRotation(0);
                            SP.edit().putBoolean(rotate, true).apply();
                        }
                    }

                    if (options[item].equals(getString(R.string.action_imageDelete))) {

                        File f = new File(Environment.getExternalStorageDirectory() + "/Android/data/de.baumann.quitsmoking/goal_picture.jpg");
                        if(f.exists()){
                            f.delete();
                        }
                    }
                }
            });
            builder.setPositiveButton(R.string.goal_cancel, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void calculate(View rootView, String DateFormat) {

        String currency = SP.getString("currency", "1");
        String cigNumb = SP.getString("cig", "");
        String dateQuit = SP.getString("date", "");
        String timeQuit = SP.getString("time", "");
        String savedMoney = SP.getString("costs", "");
        String goalCosts = SP.getString("goalCosts", "");
        String goalDate = SP.getString("goalDate", "");

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(DateFormat, Locale.getDefault());

        String dateStart = dateQuit + " " + timeQuit;
        String dateStop = format.format(date);
        String dateGoal = goalDate + " " + "00:00";

        try {

            if (goalDate.isEmpty()) {

                //Time Difference
                Date d1 = format.parse(dateStart);
                Date d2 = format.parse(dateStop);
                long diff = d2.getTime() - d1.getTime();

                //Number of Cigarettes
                long cigNumber = Long.parseLong(cigNumb);
                long cigDay = 86400000 / cigNumber;
                long diffCig = diff / cigDay;
                String cigSaved = Long.toString(diffCig);

                //Saved Money
                double costCig = Double.valueOf(savedMoney.trim());
                double sa = Long.parseLong(cigSaved);
                double cost = sa * costCig;
                String costString = String.format(Locale.US, "%.2f", cost);

                //Remaining costs
                double goalCost = Long.parseLong(goalCosts);
                double remCost = goalCost - cost;
                String remCostString = String.format(Locale.US, "%.2f", remCost);
                String goalCostString = String.format(Locale.US, "%.2f", goalCost);

                //Remaining time

                double savedMoneyDay = cigNumber * costCig;
                double remTime = remCost / savedMoneyDay;

                String remTimeString = String.format(Locale.US, "%.1f", remTime);

                TextView textView_goalCost;
                textView_goalCost = (TextView) rootView.findViewById(R.id.text_description1);
                if (goalTitle.isEmpty()) {
                    textView_goalCost.setText(String.valueOf(getString(R.string.not_set)));
                } else {
                    switch (currency) {
                        case "1":
                            textView_goalCost.setText(String.valueOf((String.valueOf(getString(R.string.costs) + " " + goalCostString) + " " + getString(R.string.money_euro) +
                                    " | " + getString(R.string.alreadySaved) + " " + costString + " " + getString(R.string.money_euro) +
                                    " | " + getString(R.string.costsRemaining) + " " + remCostString + " " + getString(R.string.money_euro))));
                            break;
                        case "2":
                            textView_goalCost.setText(String.valueOf((String.valueOf(getString(R.string.costs) + " " + goalCostString) + " " + getString(R.string.money_dollar) +
                                    " | " + getString(R.string.alreadySaved) + " " + costString + " " + getString(R.string.money_dollar) +
                                    " | " + getString(R.string.costsRemaining) + " " + remCostString + " " + getString(R.string.money_dollar))));
                            break;
                        case "3":
                            textView_goalCost.setText(String.valueOf((String.valueOf(getString(R.string.costs) + " " + goalCostString) + " " + getString(R.string.money_pound) +
                                    " | " + getString(R.string.alreadySaved) + " " + costString + " " + getString(R.string.money_pound) +
                                    " | " + getString(R.string.costsRemaining) + " " + remCostString + " " + getString(R.string.money_pound))));
                            break;
                        case "4":
                            textView_goalCost.setText(String.valueOf((String.valueOf(getString(R.string.costs) + " " + goalCostString) + " " + getString(R.string.money_yen) +
                                    " | " + getString(R.string.alreadySaved) + " " + costString + " " + getString(R.string.money_yen) +
                                    " | " + getString(R.string.costsRemaining) + " " + remCostString + " " + getString(R.string.money_yen))));
                            break;
                    }
                }

                TextView textView_goalTime;
                textView_goalTime = (TextView) rootView.findViewById(R.id.text_description2);
                if (goalTitle.isEmpty()) {
                    textView_goalTime.setText(String.valueOf(getString(R.string.not_set)));
                } else {
                    if (remTime < 0) {
                        textView_goalTime.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else {
                        textView_goalTime.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + remTimeString + " " + getString(R.string.time_days)));
                    }
                }

                ProgressBar progressBar;
                progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
                assert progressBar != null;
                int max = (int) (goalCost);
                int actual = (int) (cost);
                progressBar.setMax(max);
                progressBar.setProgress(actual);

            } else {

                //Time Difference
                Date d1 = format.parse(dateGoal);
                Date d2 = format.parse(dateStop);
                long diff = d2.getTime() - d1.getTime();

                //Number of Cigarettes
                long cigNumber = Long.parseLong(cigNumb);
                long cigDay = 86400000 / cigNumber;
                long diffCig = diff / cigDay;
                String cigSaved = Long.toString(diffCig);

                //Saved Money
                double costCig = Double.valueOf(savedMoney.trim());
                double sa = Long.parseLong(cigSaved);
                double cost = sa * costCig;
                String costString = String.format(Locale.US, "%.2f", cost);

                //Remaining costs
                double goalCost = Long.parseLong(goalCosts);
                double remCost = goalCost - cost;
                String remCostString = String.format(Locale.US, "%.2f", remCost);
                String goalCostString = String.format(Locale.US, "%.2f", goalCost);

                //Remaining time

                double savedMoneyDay = cigNumber * costCig;
                double remTime = remCost / savedMoneyDay;

                String remTimeString = String.format(Locale.US, "%.1f", remTime);

                TextView textView_goalCost;
                textView_goalCost = (TextView) rootView.findViewById(R.id.text_description1);
                if (goalTitle.isEmpty()) {
                    textView_goalCost.setText(String.valueOf(getString(R.string.not_set)));
                } else {
                    switch (currency) {
                        case "1":
                            textView_goalCost.setText(String.valueOf((String.valueOf(getString(R.string.costs) + " " + goalCostString) + " " + getString(R.string.money_euro) +
                                    " | " + getString(R.string.alreadySaved) + " " + costString + " " + getString(R.string.money_euro) +
                                    " | " + getString(R.string.costsRemaining) + " " + remCostString + " " + getString(R.string.money_euro))));
                            break;
                        case "2":
                            textView_goalCost.setText(String.valueOf((String.valueOf(getString(R.string.costs) + " " + goalCostString) + " " + getString(R.string.money_dollar) +
                                    " | " + getString(R.string.alreadySaved) + " " + costString + " " + getString(R.string.money_dollar) +
                                    " | " + getString(R.string.costsRemaining) + " " + remCostString + " " + getString(R.string.money_dollar))));
                            break;
                        case "3":
                            textView_goalCost.setText(String.valueOf((String.valueOf(getString(R.string.costs) + " " + goalCostString) + " " + getString(R.string.money_pound) +
                                    " | " + getString(R.string.alreadySaved) + " " + costString + " " + getString(R.string.money_pound) +
                                    " | " + getString(R.string.costsRemaining) + " " + remCostString + " " + getString(R.string.money_pound))));
                            break;
                        case "4":
                            textView_goalCost.setText(String.valueOf((String.valueOf(getString(R.string.costs) + " " + goalCostString) + " " + getString(R.string.money_yen) +
                                    " | " + getString(R.string.alreadySaved) + " " + costString + " " + getString(R.string.money_yen) +
                                    " | " + getString(R.string.costsRemaining) + " " + remCostString + " " + getString(R.string.money_yen))));
                            break;
                    }
                }

                TextView textView_goalTime;
                textView_goalTime = (TextView) rootView.findViewById(R.id.text_description2);
                if (goalTitle.isEmpty()) {
                    textView_goalTime.setText(String.valueOf(getString(R.string.not_set)));
                } else {
                    if (remTime < 0) {
                        textView_goalTime.setText(String.valueOf(getString(R.string.health_congratulations)));
                    } else {
                        textView_goalTime.setText(String.valueOf(getString(R.string.health_reached) + " "
                                + remTimeString + " " + getString(R.string.time_days)));
                    }
                }

                ProgressBar progressBar;
                progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
                assert progressBar != null;
                int max = (int) (goalCost);
                int actual = (int) (cost);
                progressBar.setMax(max);
                progressBar.setProgress(actual);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
