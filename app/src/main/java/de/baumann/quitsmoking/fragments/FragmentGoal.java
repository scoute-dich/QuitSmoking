package de.baumann.quitsmoking.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mvc.imagepicker.ImagePicker;

import de.baumann.quitsmoking.R;


public class FragmentGoal extends Fragment {

    private ImageView viewImage;
    @SuppressWarnings("unused")
    private String rotate;
    private SharedPreferences SP;

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

        String path = SP.getString("image_goal", "");
        File imgFile = new File(path);

        if(!path.isEmpty() && imgFile.exists()){
            Glide.with(getActivity())
                    .load(path)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .fitCenter()
                    .into(viewImage); //imageView to set thumbnail to
        } else {
            viewImage.setImageResource(R.drawable.file_image_dark);
        }

        String goalTitle = SP.getString("goalTitle", "");
        TextView textView_goalTitle;
        textView_goalTitle = (TextView) rootView.findViewById(R.id.text_header1);
        if (goalTitle.isEmpty()) {
            textView_goalTitle.setText(String.valueOf(getString(R.string.not_set)));
        } else {
            textView_goalTitle.setText(goalTitle);
        }

        long goalDate_next = SP.getLong("goalDate_next", 0);

        String currency = SP.getString("currency", "1");
        String cigNumb = SP.getString("cig", "");
        String savedMoney = SP.getString("costs", "");
        String goalCosts = SP.getString("goalCosts", "");

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        String dateStart = format.format(SP.getLong("startTime", 0));
        String goalDate = format.format(SP.getLong("goalDate_next", 0));

        String dateStop = format.format(date);

        try {

            Date d1;

            if (goalDate_next == 0) {
                d1 = format.parse(dateStart);
            } else {
                d1 = format.parse(goalDate);
            }

            //Time Difference
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        String path = SP.getString("image_goal", "");
        File imgFile = new File(path);

        if(!path.isEmpty() && imgFile.exists()){
            Glide.with(getActivity())
                    .load(path)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .fitCenter()
                    .into(viewImage); //imageView to set thumbnail to
        } else {
            viewImage.setImageResource(R.drawable.file_image_dark);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_backup).setVisible(false);
        menu.findItem(R.id.action_share).setVisible(false);
        menu.findItem(R.id.action_sort).setVisible(false);
        menu.findItem(R.id.action_filter).setVisible(false);
        menu.findItem(R.id.action_reset).setVisible(false);
        menu.findItem(R.id.action_info).setVisible(false);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (id == R.id.action_imageLoad) {
            onPickImage();
        }

        if (id == R.id.action_imageRotate) {
            if (SP.getBoolean (rotate, true)){
                viewImage.setRotation(90);
                SP.edit().putBoolean(rotate, false).apply();
            } else {
                viewImage.setRotation(0);
                SP.edit().putBoolean(rotate, true).apply();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap bitmap = ImagePicker.getImageFromResult(getActivity(), requestCode, resultCode, data);

        if (bitmap != null) {
            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(getActivity().getApplicationContext(), bitmap);
            String path = getRealPathFromURI(tempUri);
            SP.edit().putString("image_goal", path).apply();
        }
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getRealPathFromURI(Uri uri) {
        @SuppressLint("Recycle") Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        assert cursor != null;
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void onPickImage() {
        // Click on image button
        ImagePicker.pickImage(this, "Select your image:");
    }
}
