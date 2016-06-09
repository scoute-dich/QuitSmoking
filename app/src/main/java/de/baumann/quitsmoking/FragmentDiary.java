package de.baumann.quitsmoking;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by juergen on 09.06.16. Licensed under GPL.
 */
public class FragmentDiary extends Fragment {

    private EditText mEditText;
    private SwipeRefreshLayout swipeView;

    private static final String MyPREFERENCES = "MyPrefs";
    private static final String diaryText = "diaryTextKey";
    private SharedPreferences sharedpreferences;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (id == R.id.action_backup) {

            if (android.os.Build.VERSION.SDK_INT >= 23) {
                int hasWRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(getActivity(),(Manifest.permission.WRITE_EXTERNAL_STORAGE));
                if (hasWRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        new AlertDialog.Builder(getActivity())
                                .setMessage(R.string.permissions)
                                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (android.os.Build.VERSION.SDK_INT >= 23)
                                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                })
                                .setNegativeButton(getString(R.string.no), null)
                                .show();
                        return (true);
                    }
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_ASK_PERMISSIONS);
                    return (true);
                }
            }

            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd_HH-mm", Locale.getDefault());
            String editText = mEditText.getText().toString();

            try
            {
                File storageDirectory = new File((Environment.getExternalStorageDirectory() + "/diary_backup_"
                        + dateFormat.format(date) + ".txt"));
                FileOutputStream fout = new FileOutputStream(storageDirectory);
                OutputStreamWriter myoutwriter = new OutputStreamWriter(fout);
                myoutwriter.append(editText);
                myoutwriter.close();
                Snackbar.make(swipeView, R.string.backup_diary, Snackbar.LENGTH_LONG).show();
            }
            catch (Exception e)
            {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
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
}
