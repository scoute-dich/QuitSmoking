package de.baumann.quitsmoking;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class FragmentDiary extends Fragment {

    private EditText mEditText;
    private SwipeRefreshLayout swipeView;

    private static final String MyPREFERENCES = "MyPrefs";
    private static final String diaryText = "diaryTextKey";
    private SharedPreferences sharedpreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_diary, container, false);

        setHasOptionsMenu(true);

        mEditText = (EditText) rootView.findViewById(R.id.editText);
        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        swipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);
        assert swipeView != null;
        swipeView.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mEditText.setText(sharedpreferences.getString(diaryText, ""));
                swipeView.setRefreshing(false);
            }
        });

        return rootView;
    }

    @Override
    public void onDetach() {
        String editText = mEditText.getText().toString();
        if (editText.isEmpty()) {
            super.onDetach();
        } else {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(diaryText, editText);
            editor.apply();
            super.onDetach();
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_imageDelete).setVisible(false);
        menu.findItem(R.id.action_imageRotate).setVisible(false);
        menu.findItem(R.id.action_imageLoad).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_backup) {

            final CharSequence[] options = {getString(R.string.action_backup),getString(R.string.action_restore), getString(R.string.action_delete),getString(R.string.goal_cancel)};

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals(getString(R.string.action_backup))) {

                        Date date = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd_HH-mm", Locale.getDefault());
                        String editText = mEditText.getText().toString();

                        try
                        {
                            File storageDirectory = new File((Environment.getExternalStorageDirectory() + "/Android/data/de.baumann.quitsmoking/diary_backup_"
                                    + dateFormat.format(date) + ".txt"));
                            FileOutputStream fout = new FileOutputStream(storageDirectory);
                            OutputStreamWriter myoutwriter = new OutputStreamWriter(fout);
                            myoutwriter.append(editText);
                            myoutwriter.close();
                            Snackbar.make(swipeView, R.string.backup_diary, Snackbar.LENGTH_LONG).show();
                        }
                        catch (Exception e)
                        {
                            Snackbar.make(swipeView, e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    } else if (options[item].equals(getString(R.string.action_restore))) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                                + "/Android/data/de.baumann.quitsmoking/");
                        intent.setDataAndType(uri, "text/*");
                        startActivityForResult(intent, 1);

                    } else if (options[item].equals(getString(R.string.action_delete))) {
                        mEditText.setText("");
                        String editText = mEditText.getText().toString();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(diaryText, editText);
                        editor.apply();

                    } else if (options[item].equals(getString(R.string.goal_cancel))) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }

        if (id == R.id.action_share) {
            String editText = mEditText.getText().toString();
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, String.valueOf(getString(R.string.share_subject2)));
            sharingIntent.putExtra(Intent.EXTRA_TEXT, String.valueOf(editText));
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
            return true;
        }
        if (id == R.id.action_date) {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String dateNow = format.format(date);
            mEditText.append(String.valueOf(dateNow));
            return true;
        }
        if (id == R.id.action_time) {
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String timeNow = format.format(date);
            mEditText.append(String.valueOf(timeNow));
            return true;
        }
        if (id == R.id.action_line) {
            mEditText.append(String.valueOf("=========="));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {

                String FilePath = data.getData().getPath();
                File file = new File(FilePath);
                StringBuilder text = new StringBuilder();

                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;

                    while ((line = br.readLine()) != null) {
                        text.append(line);
                        text.append('\n');
                    }
                    br.close();
                }
                catch (IOException e) {
                    Snackbar.make(swipeView, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
                mEditText.setText(text.toString());
                String editText = mEditText.getText().toString();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(diaryText, editText);
                editor.apply();
            }
        }
    }
}
