package de.baumann.quitsmoking.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.View;

import java.io.File;
import java.io.FileFilter;
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
import com.mlsdev.rximagepicker.RxImageConverters;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;

import de.baumann.quitsmoking.R;
import de.baumann.quitsmoking.helper.helper_main;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


public class FragmentGoal extends Fragment {

    private ImageView viewImage;
    @SuppressWarnings("unused")
    private String rotate;
    private SharedPreferences SP;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_goal, container, false);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.user_settings, false);
        SP = PreferenceManager.getDefaultSharedPreferences(getActivity());

        viewImage= rootView.findViewById(R.id.imageView);

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
        textView_goalTitle = rootView.findViewById(R.id.text_header1);
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
            textView_goalCost = rootView.findViewById(R.id.text_description1);
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
            textView_goalTime = rootView.findViewById(R.id.text_description2);
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
            progressBar = rootView.findViewById(R.id.progressBar);
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
            final CharSequence[] options = {
                    getString(R.string.choose_gallery),
                    getString(R.string.choose_camera)};

            final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            dialog.setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.cancel();
                }
            });
            dialog.setItems(options, new DialogInterface.OnClickListener() {
                @SuppressWarnings("ResultOfMethodCallIgnored")
                @Override
                public void onClick(DialogInterface dialog, int item) {

                    if (options[item].equals(getString(R.string.choose_gallery))) {
                        RxImagePicker.with(getActivity()).requestImage(Sources.GALLERY)
                                .flatMap(new Function<Uri, ObservableSource<File>>() {
                                    @Override
                                    public ObservableSource<File> apply(@NonNull Uri uri) throws Exception {
                                        return RxImageConverters.uriToFile(getActivity(), uri, new File(Environment.getExternalStorageDirectory() + "/Android/data/de.baumann.quitsmoking/" + helper_main.newFileName()));
                                    }
                                }).subscribe(new Consumer<File>() {
                            @Override
                            public void accept(@NonNull File file) throws Exception {
                                // Do something with your file copy
                                SP.edit().putString("image_goal", file.getAbsolutePath()).apply();
                                Glide.with(getActivity())
                                        .load(file)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .fitCenter()
                                        .into(viewImage);
                            }
                        });
                    }
                    if (options[item].equals(getString(R.string.choose_camera))) {

                        RxImagePicker.with(getActivity()).requestImage(Sources.CAMERA)
                                .flatMap(new Function<Uri, ObservableSource<File>>() {
                                    @Override
                                    public ObservableSource<File> apply(@NonNull Uri uri) throws Exception {
                                        return RxImageConverters.uriToFile(getActivity(), uri, new File(Environment.getExternalStorageDirectory() + "/Android/data/de.baumann.quitsmoking/" + helper_main.newFileName()));
                                    }
                                }).subscribe(new Consumer<File>() {
                            @Override
                            public void accept(@NonNull File file) throws Exception {
                                // Do something with your file copy
                                SP.edit().putString("image_goal", file.getAbsolutePath()).apply();
                                Glide.with(getActivity())
                                        .load(file)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .fitCenter()
                                        .into(viewImage);
                                File f = lastFileModified(Environment.getExternalStorageDirectory() + File.separator + "Pictures");
                                f.delete();
                            }
                        });
                    }
                }
            });
            dialog.show();
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

    private static File lastFileModified(String dir) {
        File fl = new File(dir);
        File[] files = fl.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile();
            }
        });
        long lastMod = Long.MIN_VALUE;
        File choice = null;
        for (File file : files) {
            if (file.lastModified() > lastMod) {
                choice = file;
                lastMod = file.lastModified();
            }
        }
        return choice;
    }
}
