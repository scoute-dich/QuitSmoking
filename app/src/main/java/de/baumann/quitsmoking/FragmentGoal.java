package de.baumann.quitsmoking;

import android.annotation.SuppressLint;
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
import android.provider.MediaStore;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class FragmentGoal extends Fragment {

    private ImageView viewImage;
    private String rotate;

    @SuppressLint("CutPasteId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_goal, container, false);
        setHasOptionsMenu(true);

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getActivity());

        Button b = (Button) rootView.findViewById(R.id.btnSelectPhoto);
        viewImage=(ImageView)rootView.findViewById(R.id.imageView);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        if (SP.getBoolean (rotate, false)){
            viewImage.setRotation(0);
        } else {
            viewImage.setRotation(90);
        }

        File imgFile = new File(Environment.getExternalStorageDirectory() + "/Android/data/de.baumann.quitsmoking/goal_picture.jpg");
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            viewImage.setImageBitmap(myBitmap);
            b.setVisibility(View.GONE);
        }

        String goalTitle = SP.getString("goalTitle", "");
        TextView textView_goalTitle;
        textView_goalTitle = (TextView) rootView.findViewById(R.id.text_header1);
        if (goalTitle.isEmpty()) {
            textView_goalTitle.setText(String.valueOf(getString(R.string.not_set)));
        } else {
            textView_goalTitle.setText(goalTitle);
        }

        String currency = SP.getString("currency", "1");
        String dateFormat = SP.getString("dateFormat", "1");
        String cigNumb = SP.getString("cig", "");
        String dateQuit = SP.getString("date", "");
        String timeQuit = SP.getString("time", "");
        String savedMoney = SP.getString("costs", "");
        String goalCosts = SP.getString("goalCosts", "");
        String goalDate = SP.getString("goalDate", "");

        switch (dateFormat) {
            case "1":
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

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

                break;

            case "2":

                Date date2 = new Date();
                SimpleDateFormat format2 = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

                String dateStart2 = dateQuit + " " + timeQuit;
                String dateStop2 = format2.format(date2);
                String dateGoal2 = goalDate + " " + "00:00";

                try {

                    if (goalDate.isEmpty()) {

                        //Time Difference
                        Date d1 = format2.parse(dateStart2);
                        Date d2 = format2.parse(dateStop2);
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

                        ProgressBar progressBar2;
                        progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar);
                        assert progressBar2 != null;
                        int max = (int) (goalCost);
                        int actual = (int) (cost);
                        progressBar2.setMax(max);
                        progressBar2.setProgress(actual);

                    } else {

                        //Time Difference
                        Date d1 = format2.parse(dateGoal2);
                        Date d2 = format2.parse(dateStop2);
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

                        ProgressBar progressBar2;
                        progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar);
                        assert progressBar2 != null;
                        int max = (int) (goalCost);
                        int actual = (int) (cost);
                        progressBar2.setMax(max);
                        progressBar2.setProgress(actual);
                    }




                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }



        return rootView;
    }

    private void selectImage() {

        final CharSequence[] options = {getString(R.string.goal_camera),getString(R.string.goal_gallery), getString(R.string.goal_cancel)};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(getString(R.string.goal_camera))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(Environment.getExternalStorageDirectory() + "/Android/data/de.baumann.quitsmoking/goal_picture.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals(getString(R.string.goal_gallery))) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 2);

                } else if (options[item].equals(getString(R.string.goal_cancel))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {

                File imgFile = new File(Environment.getExternalStorageDirectory() + "/Android/data/de.baumann.quitsmoking/goal_picture.jpg");
                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    viewImage.setImageBitmap(myBitmap);
                }

            } else if (requestCode == 2) {
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
        menu.findItem(R.id.action_date).setVisible(false);
        menu.findItem(R.id.action_time).setVisible(false);
        menu.findItem(R.id.action_line).setVisible(false);
        menu.findItem(R.id.action_backup).setVisible(false);
        menu.findItem(R.id.action_share).setVisible(false);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (id == R.id.action_imageLoad) {
            selectImage();
        }

        if (id == R.id.action_imageRotate) {

            final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

            if (sharedPref.getBoolean (rotate, true)){
                viewImage.setRotation(90);
                sharedPref.edit()
                        .putBoolean(rotate, false)
                        .apply();
            } else {
                viewImage.setRotation(0);
                sharedPref.edit()
                        .putBoolean(rotate, true)
                        .apply();
            }
        }

        if (id == R.id.action_imageDelete) {
            File f = new File(Environment.getExternalStorageDirectory() + "/Android/data/de.baumann.quitsmoking/goal_picture.jpg");
            if(f.exists()){
                f.delete();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
