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

package de.baumann.quitsmoking.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import de.baumann.quitsmoking.helper.Activity_EditNote;
import de.baumann.quitsmoking.helper.DbAdapter_Notes;
import de.baumann.quitsmoking.helper.helper_main;

import de.baumann.quitsmoking.R;


public class FragmentNotes extends Fragment {

    //calling variables
    private DbAdapter_Notes db;
    private SimpleCursorAdapter adapter;

    private ListView lv = null;
    private EditText filter;
    private SharedPreferences sharedPref;
    private RelativeLayout filter_layout;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_screen_notes, container, false);

        PreferenceManager.setDefaultValues(getActivity(), R.xml.user_settings, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        filter_layout = (RelativeLayout) rootView.findViewById(R.id.filter_layout);
        filter_layout.setVisibility(View.GONE);
        lv = (ListView) rootView.findViewById(R.id.listNotes);
        filter = (EditText) rootView.findViewById(R.id.myFilter);
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
        tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
        setTitle();

        ImageButton ib_hideKeyboard =(ImageButton) rootView.findViewById(R.id.ib_hideKeyboard);
        ib_hideKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                filter_layout.setVisibility(View.GONE);
                setTitle();
                setNotesList();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPref.edit().putString("handleTextCreate", helper_main.createDate()).apply();
                Intent intent = new Intent(getActivity(), Activity_EditNote.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                getActivity().startActivity(intent);
            }
        });

        //calling Notes_DbAdapter
        db = new DbAdapter_Notes(getActivity());
        db.open();

        setNotesList();
        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed() && viewPager.getCurrentItem() == 3) {
            setNotesList();
            if (sharedPref.getString("newIntent", "false").equals("true")) {
                Intent intent = new Intent(getActivity(), Activity_EditNote.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                getActivity().startActivity(intent);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (filter_layout.getVisibility() == View.GONE) {
            setNotesList();
        }
        if (sharedPref.getString("newIntent", "false").equals("true")) {
            Intent intent = new Intent(getActivity(), Activity_EditNote.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            getActivity().startActivity(intent);
        }
    }

    private void setNotesList() {

        //display data
        final int layoutstyle=R.layout.item_list;
        int[] xml_id = new int[] {
                R.id.textView_title_notes,
                R.id.textView_des_notes,
                R.id.textView_create_notes
        };
        String[] column = new String[] {
                "note_title",
                "note_content",
                "note_creation"
        };
        final Cursor row = db.fetchAllData(getActivity());
        adapter = new SimpleCursorAdapter(getActivity(), layoutstyle,row,column, xml_id, 0) {
            @Override
            public View getView (final int position, View convertView, ViewGroup parent) {

                Cursor row2 = (Cursor) lv.getItemAtPosition(position);
                final String note_icon = row2.getString(row2.getColumnIndexOrThrow("note_icon"));
                final String note_attachment = row2.getString(row2.getColumnIndexOrThrow("note_attachment"));

                View v = super.getView(position, convertView, parent);
                ImageView iv_icon = (ImageView) v.findViewById(R.id.icon_notes);
                ImageView iv_attachment = (ImageView) v.findViewById(R.id.att_notes);

                switch (note_icon) {
                    case "1":
                        iv_icon.setImageResource(R.drawable.emoticon_neutral);
                        sharedPref.edit().putString("handleTextIcon", "1").apply();
                        break;
                    case "2":
                        iv_icon.setImageResource(R.drawable.emoticon_happy);
                        sharedPref.edit().putString("handleTextIcon", "2").apply();
                        break;
                    case "3":
                        iv_icon.setImageResource(R.drawable.emoticon_sad);
                        sharedPref.edit().putString("handleTextIcon", "3").apply();
                        break;
                    case "4":
                        iv_icon.setImageResource(R.drawable.emoticon);
                        sharedPref.edit().putString("handleTextIcon", "4").apply();
                        break;
                    case "5":
                        iv_icon.setImageResource(R.drawable.emoticon_cool);
                        sharedPref.edit().putString("handleTextIcon", "5").apply();
                        break;
                    case "6":
                        iv_icon.setImageResource(R.drawable.emoticon_dead);
                        sharedPref.edit().putString("handleTextIcon", "6").apply();
                        break;
                    case "7":
                        iv_icon.setImageResource(R.drawable.emoticon_excited);
                        sharedPref.edit().putString("handleTextIcon", "7").apply();
                        break;
                    case "8":
                        iv_icon.setImageResource(R.drawable.emoticon_tongue);
                        sharedPref.edit().putString("handleTextIcon", "8").apply();
                        break;
                    case "9":
                        iv_icon.setImageResource(R.drawable.emoticon_devil);
                        sharedPref.edit().putString("handleTextIcon", "9").apply();
                        break;
                    case "":
                        iv_icon.setImageResource(R.drawable.emoticon_neutral);
                        sharedPref.edit()
                                .putString("handleTextIcon", "")
                                .apply();
                        break;
                }

                switch (note_attachment) {
                    case "":
                        iv_attachment.setVisibility(View.GONE);
                        break;
                    default:
                        iv_attachment.setVisibility(View.VISIBLE);
                        iv_attachment.setImageResource(R.drawable.ic_attachment);
                        break;
                }

                File file = new File(note_attachment);
                if (!file.exists()) {
                    iv_attachment.setVisibility(View.GONE);
                }
                return v;
            }
        };

        //display data by filter
        final String note_search = sharedPref.getString("filter_noteBY", "note_title");
        sharedPref.edit().putString("filter_noteBY", "note_title").apply();
        filter.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }
        });
        adapter.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence constraint) {
                return db.fetchDataByFilter(constraint.toString(),note_search);
            }
        });

        lv.setAdapter(adapter);
        //onClick function
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterview, View view, int position, long id) {

                Cursor row2 = (Cursor) lv.getItemAtPosition(position);
                final String _id = row2.getString(row2.getColumnIndexOrThrow("_id"));
                final String note_title = row2.getString(row2.getColumnIndexOrThrow("note_title"));
                final String note_content = row2.getString(row2.getColumnIndexOrThrow("note_content"));
                final String note_icon = row2.getString(row2.getColumnIndexOrThrow("note_icon"));
                final String note_attachment = row2.getString(row2.getColumnIndexOrThrow("note_attachment"));
                final String note_creation = row2.getString(row2.getColumnIndexOrThrow("note_creation"));

                final Button attachment;
                final TextView textInput;

                LayoutInflater inflater = getActivity().getLayoutInflater();

                final ViewGroup nullParent = null;
                final View dialogView = inflater.inflate(R.layout.dialog_note_show, nullParent);

                final String attName = note_attachment.substring(note_attachment.lastIndexOf("/")+1);
                final String att = getString(R.string.note_attachment) + ": " + attName;

                attachment = (Button) dialogView.findViewById(R.id.button_att);
                if (attName.equals("")) {
                    attachment.setVisibility(View.GONE);
                } else {
                    attachment.setText(att);
                }

                textInput = (TextView) dialogView.findViewById(R.id.note_text_input);
                if (note_content.isEmpty()) {
                    textInput.setVisibility(View.GONE);
                } else {
                    textInput.setText(note_content);
                    Linkify.addLinks(textInput, Linkify.WEB_URLS);
                }

                attachment.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        helper_main.openAtt(getActivity(), textInput, note_attachment);
                    }
                });

                final ImageView be = (ImageView) dialogView.findViewById(R.id.imageButtonPri);
                final ImageView attImage = (ImageView) dialogView.findViewById(R.id.attImage);

                File file2 = new File(note_attachment);
                if (!file2.exists()) {
                    attachment.setVisibility(View.GONE);
                    attImage.setVisibility(View.GONE);
                } else if (note_attachment.contains(".gif") ||
                            note_attachment.contains(".bmp") ||
                            note_attachment.contains(".tiff") ||
                            note_attachment.contains(".png") ||
                            note_attachment.contains(".jpg") ||
                            note_attachment.contains(".JPG") ||
                            note_attachment.contains(".jpeg") ||
                            note_attachment.contains(".mpeg") ||
                            note_attachment.contains(".mp4") ||
                            note_attachment.contains(".3gp") ||
                            note_attachment.contains(".3g2") ||
                            note_attachment.contains(".avi") ||
                            note_attachment.contains(".flv") ||
                            note_attachment.contains(".h261") ||
                            note_attachment.contains(".h263") ||
                            note_attachment.contains(".h264") ||
                            note_attachment.contains(".asf") ||
                            note_attachment.contains(".wmv")) {
                        attImage.setVisibility(View.VISIBLE);

                        try {
                            Glide.with(getActivity())
                                    .load(note_attachment) // or URI/path
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(attImage); //imageView to set thumbnail to
                        } catch (Exception e) {
                            Log.w("HHS_Moodle", "Error load thumbnail", e);
                            attImage.setVisibility(View.GONE);
                        }

                        attImage.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                helper_main.openAtt(getActivity(), attImage, note_attachment);
                            }
                        });
                }

                switch (note_icon) {
                    case "1":
                        be.setImageResource(R.drawable.emoticon_neutral);
                        sharedPref.edit().putString("handleTextIcon", "1").apply();
                        break;
                    case "2":
                        be.setImageResource(R.drawable.emoticon_happy);
                        sharedPref.edit().putString("handleTextIcon", "2").apply();
                        break;
                    case "3":
                        be.setImageResource(R.drawable.emoticon_sad);
                        sharedPref.edit().putString("handleTextIcon", "3").apply();
                        break;
                    case "4":
                        be.setImageResource(R.drawable.emoticon);
                        sharedPref.edit().putString("handleTextIcon", "4").apply();
                        break;
                    case "5":
                        be.setImageResource(R.drawable.emoticon_cool);
                        sharedPref.edit().putString("handleTextIcon", "5").apply();
                        break;
                    case "6":
                        be.setImageResource(R.drawable.emoticon_dead);
                        sharedPref.edit().putString("handleTextIcon", "6").apply();
                        break;
                    case "7":
                        be.setImageResource(R.drawable.emoticon_excited);
                        sharedPref.edit().putString("handleTextIcon", "7").apply();
                        break;
                    case "8":
                        be.setImageResource(R.drawable.emoticon_tongue);
                        sharedPref.edit().putString("handleTextIcon", "8").apply();
                        break;
                    case "9":
                        be.setImageResource(R.drawable.emoticon_devil);
                        sharedPref.edit().putString("handleTextIcon", "9").apply();
                        break;
                    case "":
                        be.setImageResource(R.drawable.emoticon_neutral);
                        sharedPref.edit()
                                .putString("handleTextIcon", "")
                                .apply();
                        break;
                }

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                        .setTitle(note_title)
                        .setView(dialogView)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton(R.string.note_edit, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                sharedPref.edit()
                                        .putString("handleTextTitle", note_title)
                                        .putString("handleTextText", note_content)
                                        .putString("handleTextIcon", note_icon)
                                        .putString("handleTextSeqno", _id)
                                        .putString("handleTextAttachment", note_attachment)
                                        .putString("handleTextCreate", note_creation)
                                        .apply();
                                Intent intent = new Intent(getActivity(), Activity_EditNote.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                getActivity().startActivity(intent);
                            }
                        });
                dialog.show();
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor row2 = (Cursor) lv.getItemAtPosition(position);
                final String _id = row2.getString(row2.getColumnIndexOrThrow("_id"));
                final String note_title = row2.getString(row2.getColumnIndexOrThrow("note_title"));
                final String note_content = row2.getString(row2.getColumnIndexOrThrow("note_content"));
                final String note_icon = row2.getString(row2.getColumnIndexOrThrow("note_icon"));
                final String note_attachment = row2.getString(row2.getColumnIndexOrThrow("note_attachment"));
                final String note_creation = row2.getString(row2.getColumnIndexOrThrow("note_creation"));

                final CharSequence[] options = {
                        getString(R.string.note_edit),
                        getString(R.string.note_share),
                        getString(R.string.note_remove_note)};
                new AlertDialog.Builder(getActivity())
                        .setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        })
                        .setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                if (options[item].equals(getString(R.string.note_edit))) {
                                    sharedPref.edit()
                                            .putString("handleTextTitle", note_title)
                                            .putString("handleTextText", note_content)
                                            .putString("handleTextIcon", note_icon)
                                            .putString("handleTextSeqno", _id)
                                            .putString("handleTextAttachment", note_attachment)
                                            .putString("handleTextCreate", note_creation)
                                            .apply();
                                    Intent intent = new Intent(getActivity(), Activity_EditNote.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    getActivity().startActivity(intent);
                                }

                                if (options[item].equals (getString(R.string.note_share))) {
                                    File attachment = new File(note_attachment);
                                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                    sharingIntent.setType("text/plain");
                                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, note_title);
                                    sharingIntent.putExtra(Intent.EXTRA_TEXT, note_content);

                                    if (attachment.exists()) {
                                        Uri bmpUri = Uri.fromFile(attachment);
                                        sharingIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                                    }

                                    startActivity(Intent.createChooser(sharingIntent, (getString(R.string.note_share_2))));
                                }

                                if (options[item].equals(getString(R.string.note_remove_note))) {
                                    Snackbar snackbar = Snackbar
                                            .make(lv, R.string.note_remove_confirmation, Snackbar.LENGTH_LONG)
                                            .setAction(R.string.yes, new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    db.delete(Integer.parseInt(_id));
                                                    setNotesList();
                                                }
                                            });
                                    snackbar.show();
                                }
                            }
                        }).show();

                return true;
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.action_image).setVisible(false);
        menu.findItem(R.id.action_share).setVisible(false);
        menu.findItem(R.id.action_reset).setVisible(false);
        menu.findItem(R.id.action_info).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        TabLayout.Tab tab = tabLayout.getTabAt(3);

        switch (item.getItemId()) {

            case R.id.filter_title:
                sharedPref.edit().putString("filter_noteBY", "note_title").apply();
                setTitle();
                setNotesList();
                filter_layout.setVisibility(View.VISIBLE);
                filter.setText("");
                filter.setHint(R.string.action_filter_title);
                filter.requestFocus();
                helper_main.showKeyboard(getActivity(), filter);
                return true;
            case R.id.filter_content:
                sharedPref.edit().putString("filter_noteBY", "note_content").apply();
                setTitle();
                setNotesList();
                filter_layout.setVisibility(View.VISIBLE);
                filter.setText("");
                filter.setHint(R.string.action_filter_cont);
                filter.requestFocus();
                helper_main.showKeyboard(getActivity(), filter);
                return true;

            case R.id.filter_today:
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar cal = Calendar.getInstance();
                final String search = dateFormat.format(cal.getTime());
                sharedPref.edit().putString("filter_noteBY", "note_creation").apply();
                assert tab != null;
                tab.setText(getString(R.string.action_diary) + " | " + getString(R.string.filter_today));
                setNotesList();
                filter_layout.setVisibility(View.VISIBLE);
                filter.setText(search);
                filter.setHint(R.string.action_filter_create);
                return true;
            case R.id.filter_yesterday:
                DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar cal2 = Calendar.getInstance();
                cal2.add(Calendar.DATE, -1);
                final String search2 = dateFormat2.format(cal2.getTime());
                sharedPref.edit().putString("filter_noteBY", "note_creation").apply();
                assert tab != null;
                tab.setText(getString(R.string.action_diary) + " | " + getString(R.string.filter_yesterday));
                setNotesList();
                filter_layout.setVisibility(View.VISIBLE);
                filter.setText(search2);
                filter.setHint(R.string.action_filter_create);
                return true;
            case R.id.filter_before:
                DateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar cal3 = Calendar.getInstance();
                cal3.add(Calendar.DATE, -2);
                final String search3 = dateFormat3.format(cal3.getTime());
                sharedPref.edit().putString("filter_noteBY", "note_creation").apply();
                assert tab != null;
                tab.setText(getString(R.string.action_diary) + " | " + getString(R.string.filter_before));
                setNotesList();
                filter_layout.setVisibility(View.VISIBLE);
                filter.setText(search3);
                filter.setHint(R.string.action_filter_create);
                return true;
            case R.id.filter_month:
                DateFormat dateFormat4 = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
                Calendar cal4 = Calendar.getInstance();
                final String search4 = dateFormat4.format(cal4.getTime());
                sharedPref.edit().putString("filter_noteBY", "note_creation").apply();
                assert tab != null;
                tab.setText(getString(R.string.action_diary) + " | " + getString(R.string.filter_month));
                setNotesList();
                filter_layout.setVisibility(View.VISIBLE);
                filter.setText(search4);
                filter.setHint(R.string.action_filter_create);
                return true;
            case R.id.filter_own:
                sharedPref.edit().putString("filter_noteBY", "note_creation").apply();
                assert tab != null;
                tab.setText(getString(R.string.action_diary) + " | " + getString(R.string.filter_own));
                setNotesList();
                filter_layout.setVisibility(View.VISIBLE);
                filter.setText("");
                filter.setHint(R.string.action_filter_create);
                filter.requestFocus();
                helper_main.showKeyboard(getActivity(), filter);
                return true;

            case R.id.sort_title:
                sharedPref.edit().putString("sortDB", "title").apply();
                setTitle();
                setNotesList();
                return true;
            case R.id.sort_icon:
                sharedPref.edit().putString("sortDB", "icon").apply();
                setTitle();
                setNotesList();
                return true;
            case R.id.sort_creation:
                sharedPref.edit().putString("sortDB", "create").apply();
                setTitle();
                setNotesList();
                return true;

            case R.id.backup_backup:
                File directory = new File(Environment.getExternalStorageDirectory() + "/QuitSmoking/backup/");
                if (!directory.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    directory.mkdirs();
                }

                try {
                    File sd = Environment.getExternalStorageDirectory();
                    File data = Environment.getDataDirectory();

                    if (sd.canWrite()) {
                        String currentDBPath = "//data//" + "de.baumann.quitsmoking"
                                + "//databases//" + "notes_DB_v01.db";
                        String backupDBPath = "//Android//" + "//data//" + "//quitsmoking.backup//" + "notes_DB_v01.db";
                        File currentDB = new File(data, currentDBPath);
                        File backupDB = new File(sd, backupDBPath);

                        FileChannel src = new FileInputStream(currentDB).getChannel();
                        FileChannel dst = new FileOutputStream(backupDB).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();

                        Snackbar snackbar = Snackbar
                                .make(lv, R.string.toast_backup, Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                } catch (Exception e) {
                    Snackbar snackbar = Snackbar
                            .make(lv, R.string.toast_backup_not, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

                return true;
            case R.id.backup_restore:

                try {
                    File sd = Environment.getExternalStorageDirectory();
                    File data = Environment.getDataDirectory();

                    if (sd.canWrite()) {

                        String currentDBPath = "//data//" + "de.baumann.quitsmoking"
                                + "//databases//" + "notes_DB_v01.db";
                        String backupDBPath = "//Android//" + "//data//" + "//quitsmoking.backup//" + "notes_DB_v01.db";
                        File currentDB = new File(data, currentDBPath);
                        File backupDB = new File(sd, backupDBPath);

                        FileChannel src = new FileInputStream(backupDB).getChannel();
                        FileChannel dst = new FileOutputStream(currentDB).getChannel();
                        dst.transferFrom(src, 0, src.size());
                        src.close();
                        dst.close();
                        setNotesList();

                        Snackbar snackbar = Snackbar
                                .make(lv, R.string.toast_restore, Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                } catch (Exception e) {
                    Snackbar snackbar = Snackbar
                            .make(lv, R.string.toast_restore_not, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                return true;

            case R.id.backup_delete:
                Snackbar snackbar = Snackbar
                        .make(lv, R.string.toast_delete, Snackbar.LENGTH_LONG)
                        .setAction(R.string.yes, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getActivity().deleteDatabase("notes_DB_v01.db");
                                getActivity().recreate();
                            }
                        });
                snackbar.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setTitle() {

        TabLayout.Tab tab = tabLayout.getTabAt(3);

        if (sharedPref.getString("sortDB", "title").equals("title")) {
            assert tab != null;
            tab.setText(getString(R.string.action_diary) + " | " + getString(R.string.sort_title));
        } else if (sharedPref.getString("sortDB", "title").equals("icon")) {
            assert tab != null;
            tab.setText(getString(R.string.action_diary) + " | " + getString(R.string.sort_pri));
        }  else if (sharedPref.getString("sortDB", "title").equals("create")) {
            assert tab != null;
            tab.setText(getString(R.string.action_diary) + " | " + getString(R.string.sort_date));
        }
    }
}