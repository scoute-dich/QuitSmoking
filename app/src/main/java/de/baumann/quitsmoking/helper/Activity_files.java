/*
    This file is part of the HHS Moodle WebApp.

    HHS Moodle WebApp is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    HHS Moodle WebApp is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with the Diaspora Native WebApp.

    If not, see <http://www.gnu.org/licenses/>.
 */

package de.baumann.quitsmoking.helper;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;


import de.baumann.quitsmoking.R;

import static android.content.ContentValues.TAG;
import static java.lang.String.valueOf;

public class Activity_files extends AppCompatActivity {

    private ListView lv = null;
    private DbAdapter_Files db;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        PreferenceManager.setDefaultValues(this, R.xml.user_settings, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.edit().putString("files_startFolder",
                Environment.getExternalStorageDirectory().getPath()).apply();

        setContentView(R.layout.activity_files);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(R.string.choose_title);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        lv = (ListView)findViewById(R.id.dialogList);

        //calling Notes_DbAdapter
        db = new DbAdapter_Files(Activity_files.this);
        db.open();

        if (android.os.Build.VERSION.SDK_INT >= 23) {
            int hasWRITE_EXTERNAL_STORAGE = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWRITE_EXTERNAL_STORAGE == PackageManager.PERMISSION_GRANTED) {
                setFilesList();
            }
        } else {
            setFilesList();
        }

        onNewIntent(getIntent());
    }

    protected void onNewIntent(final Intent intent) {

        String action = intent.getAction();
        PreferenceManager.setDefaultValues(this, R.xml.user_settings, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        if ("file_chooseAttachment".equals(action)) {
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Cursor row2 = (Cursor) lv.getItemAtPosition(position);
                    final String files_attachment = row2.getString(row2.getColumnIndexOrThrow("files_attachment"));
                    final String files_title = row2.getString(row2.getColumnIndexOrThrow("files_title"));
                    final File pathFile = new File(files_attachment);

                    if(pathFile.isDirectory()) {
                        try {
                            sharedPref.edit().putString("files_startFolder", files_attachment).apply();
                            setFilesList();
                        } catch (Exception e) {
                            Snackbar.make(lv, R.string.toast_directory, Snackbar.LENGTH_LONG).show();
                        }
                    } else if(files_attachment.equals("")) {
                        try {
                            final File pathActual = new File(sharedPref.getString("files_startFolder",
                                    Environment.getExternalStorageDirectory().getPath()));
                            sharedPref.edit().putString("files_startFolder", pathActual.getParent()).apply();
                            setFilesList();
                        } catch (Exception e) {
                            Snackbar.make(lv, R.string.toast_directory, Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        sharedPref.edit().putString("handleTextAttachment", files_attachment).apply();
                        sharedPref.edit().putString("handleTextAttachmentTitle", files_title).apply();
                        finish();
                    }
                }
            });

        }
    }

    private void setFilesList() {

        Activity_files.this.deleteDatabase("files_DB_v01.db");

        File f = new File(sharedPref.getString("files_startFolder",
                Environment.getExternalStorageDirectory().getPath()));
        final File[] files = f.listFiles();

        // looping through all items <item>
        if (files.length == 0) {
            Snackbar.make(lv, R.string.toast_files, Snackbar.LENGTH_LONG).show();
        }

        for (File file : files) {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            String file_Name = file.getName();
            String file_Size = getReadableFileSize(file.length());
            String file_date = formatter.format(new Date(file.lastModified()));
            String file_path = file.getAbsolutePath();

            String file_ext;
            if (file.isDirectory()) {
                file_ext = ".";
            } else {
                file_ext = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));
            }

            db.open();
            if(db.isExist(file_Name)) {
                Log.i(TAG, "Entry exists" + file_Name);
            } else {
                db.insert(file_Name, file_Size, file_ext, file_path, file_date);
            }
        }

        try {
            db.insert("...", "", "", "", "");
        } catch (Exception e) {
            Snackbar.make(lv, R.string.toast_directory, Snackbar.LENGTH_LONG).show();
        }

        //display data
        final int layoutstyle=R.layout.item_list;
        int[] xml_id = new int[] {
                R.id.textView_title_notes,
                R.id.textView_des_notes,
                R.id.textView_create_notes
        };
        String[] column = new String[] {
                "files_title",
                "files_content",
                "files_creation"
        };
        final Cursor row = db.fetchAllData();
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(Activity_files.this, layoutstyle, row, column, xml_id, 0) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                Cursor row2 = (Cursor) lv.getItemAtPosition(position);
                final String files_icon = row2.getString(row2.getColumnIndexOrThrow("files_icon"));
                final String files_attachment = row2.getString(row2.getColumnIndexOrThrow("files_attachment"));
                final File pathFile = new File(files_attachment);

                View v = super.getView(position, convertView, parent);
                final ImageView iv = (ImageView) v.findViewById(R.id.icon_notes);

                iv.setVisibility(View.VISIBLE);

                if (pathFile.isDirectory()) {
                    iv.setImageResource(R.drawable.folder);
                } else {
                    switch (files_icon) {
                        case "":
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    iv.setImageResource(R.drawable.arrow_up);
                                }
                            }, 200);
                            break;
                        case ".gif":case ".bmp":case ".tiff":case ".svg":
                        case ".png":case ".jpg":case ".JPG":case ".jpeg":
                            try {
                                Glide.with(Activity_files.this)
                                        .load(files_attachment) // or URI/path
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .override(76, 76)
                                        .centerCrop()
                                        .into(iv); //imageView to set thumbnail to
                            } catch (Exception e) {
                                Log.w("HHS_Moodle", "Error load thumbnail", e);
                                iv.setImageResource(R.drawable.file_image);
                            }
                            break;
                        case ".m3u8":case ".mp3":case ".wma":case ".midi":case ".wav":case ".aac":
                        case ".aif":case ".amp3":case ".weba":case ".ogg":
                            iv.setImageResource(R.drawable.file_music);
                            break;
                        case ".mpeg":case ".mp4":case ".webm":case ".qt":case ".3gp":
                        case ".3g2":case ".avi":case ".f4v":case ".flv":case ".h261":case ".h263":
                        case ".h264":case ".asf":case ".wmv":
                            try {
                                Glide.with(Activity_files.this)
                                        .load(files_attachment) // or URI/path
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .override(76, 76)
                                        .centerCrop()
                                        .into(iv); //imageView to set thumbnail to
                            } catch (Exception e) {
                                Log.w("HHS_Moodle", "Error load thumbnail", e);
                                iv.setImageResource(R.drawable.file_video);
                            }
                            break;
                        case ".vcs":case ".vcf":case ".css":case ".ics":case ".conf":case ".config":
                        case ".java":case ".html":
                            iv.setImageResource(R.drawable.file_xml);
                            break;
                        case ".apk":
                            iv.setImageResource(R.drawable.android);
                            break;
                        case ".pdf":
                            iv.setImageResource(R.drawable.file_pdf);
                            break;
                        case ".rtf":case ".csv":case ".txt":
                        case ".doc":case ".xls":case ".ppt":case ".docx":case ".pptx":case ".xlsx":
                        case ".odt":case ".ods":case ".odp":
                            iv.setImageResource(R.drawable.file_document);
                            break;
                        case ".zip":
                        case ".rar":
                            iv.setImageResource(R.drawable.zip_box);
                            break;
                        default:
                            iv.setImageResource(R.drawable.file);
                            break;
                    }
                }
                return v;
            }
        };
        lv.setAdapter(adapter);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor row2 = (Cursor) lv.getItemAtPosition(position);
                final String files_attachment = row2.getString(row2.getColumnIndexOrThrow("files_attachment"));

                final File pathFile = new File(files_attachment);
                String delete = getString(R.string.note_remove_note) + "?";

                if (pathFile.isDirectory()) {
                    Snackbar snackbar = Snackbar
                            .make(lv, delete, Snackbar.LENGTH_LONG)
                            .setAction(R.string.yes, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    sharedPref.edit().putString("files_startFolder", pathFile.getParent()).apply();
                                    deleteRecursive(pathFile);
                                    setFilesList();
                                }
                            });
                    snackbar.show();

                } else {
                    Snackbar snackbar = Snackbar
                            .make(lv, delete, Snackbar.LENGTH_LONG)
                            .setAction(R.string.yes, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //noinspection ResultOfMethodCallIgnored
                                    pathFile.delete();
                                    setFilesList();
                                }
                            });
                    snackbar.show();
                }

                return true;
            }
        });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        fileOrDirectory.delete();
    }

    private static String getReadableFileSize(long size) {
        final int BYTES_IN_KILOBYTES = 1024;
        final DecimalFormat dec = new DecimalFormat("###.#");
        final String KILOBYTES = " KB";
        final String MEGABYTES = " MB";
        final String GIGABYTES = " GB";
        float fileSize = 0;
        String suffix = KILOBYTES;

        if (size > BYTES_IN_KILOBYTES) {
            fileSize = size / BYTES_IN_KILOBYTES;
            if (fileSize > BYTES_IN_KILOBYTES) {
                fileSize = fileSize / BYTES_IN_KILOBYTES;
                if (fileSize > BYTES_IN_KILOBYTES) {
                    fileSize = fileSize / BYTES_IN_KILOBYTES;
                    suffix = GIGABYTES;
                } else {
                    suffix = MEGABYTES;
                }
            }
        }
        return valueOf(dec.format(fileSize) + suffix);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}