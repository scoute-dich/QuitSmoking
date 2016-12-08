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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;

import de.baumann.quitsmoking.helper.Database_Notes;
import de.baumann.quitsmoking.helper.helper_main;
import de.baumann.quitsmoking.helper.helper_notes;

import de.baumann.quitsmoking.R;


public class FragmentNotes extends Fragment {

    private ListView listView = null;
    private SharedPreferences sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_screen_notes, container, false);

        PreferenceManager.setDefaultValues(getActivity(), R.xml.user_settings, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        setHasOptionsMenu(true);

        listView = (ListView)rootView.findViewById(R.id.notes);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                @SuppressWarnings("unchecked")
                HashMap<String,String> map = (HashMap<String,String>)listView.getItemAtPosition(position);

                final String title = map.get("title");
                final String cont = map.get("cont");
                final String seqnoStr = map.get("seqno");
                final String icon = map.get("icon");
                final String attachment = map.get("attachment");

                final Button attachment2;
                final TextView textInput;

                LayoutInflater inflater = getActivity().getLayoutInflater();

                final ViewGroup nullParent = null;
                View dialogView = inflater.inflate(R.layout.dialog_note_show, nullParent);

                final String attName = attachment.substring(attachment.lastIndexOf("/")+1);
                final String att = getString(R.string.note_attachment) + ": " + attName;

                attachment2 = (Button) dialogView.findViewById(R.id.button_att);
                if (attName.equals("")) {
                    attachment2.setVisibility(View.GONE);
                } else {
                    attachment2.setText(att);
                }
                File file2 = new File(attachment);
                if (!file2.exists()) {
                    attachment2.setVisibility(View.GONE);
                }

                textInput = (TextView) dialogView.findViewById(R.id.note_text_input);
                textInput.setText(cont);

                attachment2.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        openAtt(attachment);
                    }
                });

                final ImageView be = (ImageView) dialogView.findViewById(R.id.imageButtonPri);
                assert be != null;

                switch (icon) {
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

                android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(getActivity())
                        .setTitle(title)
                        .setView(dialogView)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton(R.string.note_edit, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                sharedPref.edit()
                                        .putString("handleTextTitle", title)
                                        .putString("handleTextText", cont)
                                        .putString("handleTextIcon", icon)
                                        .putString("handleTextSeqno", seqnoStr)
                                        .putString("handleTextAttachment", attachment)
                                        .apply();
                                helper_notes.editNote(getActivity());
                            }
                        });
                dialog.show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                @SuppressWarnings("unchecked")
                HashMap<String,String> map = (HashMap<String,String>)listView.getItemAtPosition(position);

                final String seqnoStr = map.get("seqno");
                final String title = map.get("title");
                final String cont = map.get("cont");
                final String icon = map.get("icon");
                final String attachment = map.get("attachment");

                final CharSequence[] options = {
                        getString(R.string.note_edit),
                        getString(R.string.note_share),
                        getString(R.string.note_remove_note)};
                new AlertDialog.Builder(getActivity())
                        .setPositiveButton(R.string.goal_cancel, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        })
                        .setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                if (options[item].equals(getString(R.string.note_edit))) {
                                    sharedPref.edit()
                                            .putString("handleTextTitle", title)
                                            .putString("handleTextText", cont)
                                            .putString("handleTextIcon", icon)
                                            .putString("handleTextSeqno", seqnoStr)
                                            .putString("handleTextAttachment", attachment)
                                            .apply();
                                    helper_notes.editNote(getActivity());
                                }

                                if (options[item].equals (getString(R.string.note_share))) {
                                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                    sharingIntent.setType("text/plain");
                                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, title);
                                    sharingIntent.putExtra(Intent.EXTRA_TEXT, cont);
                                    startActivity(Intent.createChooser(sharingIntent, (getString(R.string.note_share_2))));
                                }

                                if (options[item].equals(getString(R.string.note_remove_note))) {
                                    try {
                                        Database_Notes db = new Database_Notes(getActivity());
                                        final int count = db.getRecordCount();
                                        db.close();

                                        if (count == 1) {
                                            Snackbar snackbar = Snackbar
                                                    .make(listView, R.string.note_remove_cannot, Snackbar.LENGTH_LONG);
                                            snackbar.show();

                                        } else {
                                            Snackbar snackbar = Snackbar
                                                    .make(listView, R.string.note_remove_confirmation, Snackbar.LENGTH_LONG)
                                                    .setAction(R.string.yes, new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            try {
                                                                Database_Notes db = new Database_Notes(getActivity());
                                                                db.deleteNote(Integer.parseInt(seqnoStr));
                                                                db.close();
                                                                setNotesList();
                                                            } catch (PackageManager.NameNotFoundException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    });
                                            snackbar.show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).show();

                return true;
            }
        });

        setHasOptionsMenu(true);
        setNotesList();
        return rootView;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_image).setVisible(false);
        menu.findItem(R.id.action_share).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_backup) {

            final CharSequence[] options = {
                    getString(R.string.action_backup),
                    getString(R.string.action_restore),
                    getString(R.string.action_delete)};

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals(getString(R.string.action_backup))) {

                        sharedPref.edit().putString("sortDB", "title").apply();
                        setNotesList();

                        File directory = new File(Environment.getExternalStorageDirectory() + "/QuitSmoking/backup/");
                        if (!directory.exists()) {
                            //noinspection ResultOfMethodCallIgnored
                            directory.mkdirs();
                        }

                        try {
                            File sd = Environment.getExternalStorageDirectory();
                            File data = Environment.getDataDirectory();

                            if (sd.canWrite()) {

                                String currentDBPath2 = "//data//" + "de.baumann.quitsmoking"
                                        + "//databases//" + "notes.db";
                                String backupDBPath2 = "//QuitSmoking//" + "//backup//" + "notes.db";
                                File currentDB2 = new File(data, currentDBPath2);
                                File backupDB2 = new File(sd, backupDBPath2);

                                FileChannel src2 = new FileInputStream(currentDB2).getChannel();
                                FileChannel dst2 = new FileOutputStream(backupDB2).getChannel();
                                dst2.transferFrom(src2, 0, src2.size());
                                src2.close();
                                dst2.close();

                                Snackbar snackbar = Snackbar
                                        .make(listView, R.string.toast_backup, Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        } catch (Exception e) {
                            Snackbar snackbar = Snackbar
                                    .make(listView, R.string.toast_backup_not, Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }

                    if (options[item].equals(getString(R.string.action_restore))) {

                        sharedPref.edit().putString("sortDB", "seqno").apply();
                        setNotesList();

                        try {
                            File sd = Environment.getExternalStorageDirectory();
                            File data = Environment.getDataDirectory();

                            if (sd.canWrite()) {

                                String currentDBPath2 = "//data//" + "de.baumann.quitsmoking"
                                        + "//databases//" + "notes.db";
                                String backupDBPath2 = "//QuitSmoking//" + "//backup//" + "notes.db";
                                File currentDB2 = new File(data, currentDBPath2);
                                File backupDB2 = new File(sd, backupDBPath2);

                                FileChannel src2 = new FileInputStream(backupDB2).getChannel();
                                FileChannel dst2 = new FileOutputStream(currentDB2).getChannel();
                                dst2.transferFrom(src2, 0, src2.size());
                                src2.close();
                                dst2.close();
                                Snackbar snackbar = Snackbar
                                        .make(listView, R.string.toast_restore, Snackbar.LENGTH_LONG);
                                snackbar.show();
                                setNotesList();
                            }
                        } catch (Exception e) {
                            Snackbar snackbar = Snackbar
                                    .make(listView, R.string.toast_restore_not, Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }

                    if (options[item].equals(getString(R.string.action_delete))) {

                        sharedPref.edit().putString("sortDB", "icon").apply();
                        setNotesList();

                        Snackbar snackbar = Snackbar
                                .make(listView, R.string.note_delete_confirmation, Snackbar.LENGTH_LONG)
                                .setAction(R.string.yes, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        getActivity().deleteDatabase("notes.db");
                                        setNotesList();
                                    }
                                });
                        snackbar.show();
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

        if (id == R.id.action_sort) {

            final CharSequence[] options = {
                    getString(R.string.action_sort_title),
                    getString(R.string.action_sort_edit),
                    getString(R.string.action_sort_icon)};

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals(getString(R.string.action_sort_title))) {
                        sharedPref.edit().putString("sortDB", "title").apply();
                        setNotesList();
                    }

                    if (options[item].equals(getString(R.string.action_sort_edit))) {
                        sharedPref.edit().putString("sortDB", "seqno").apply();
                        setNotesList();
                    }

                    if (options[item].equals(getString(R.string.action_sort_icon))) {
                        sharedPref.edit().putString("sortDB", "icon").apply();
                        setNotesList();
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

        if (id == R.id.action_note) {
            helper_notes.editNote(getActivity());
        }

        return super.onOptionsItemSelected(item);
    }

    private void setNotesList() {

        ArrayList<HashMap<String,String>> mapList = new ArrayList<>();

        try {
            Database_Notes db = new Database_Notes(getActivity());
            ArrayList<String[]> bookmarkList = new ArrayList<>();
            db.getBookmarks(bookmarkList, getActivity());
            if (bookmarkList.size() == 0) {
                db.loadInitialData();
                db.getBookmarks(bookmarkList, getActivity());
            }
            db.close();

            for (String[] strAry : bookmarkList) {
                HashMap<String, String> map = new HashMap<>();
                map.put("seqno", strAry[0]);
                map.put("title", strAry[1]);
                map.put("cont", strAry[2]);
                map.put("icon", strAry[3]);
                map.put("attachment", strAry[4]);
                mapList.add(map);
            }

            SimpleAdapter simpleAdapter = new SimpleAdapter(
                    getActivity(),
                    mapList,
                    R.layout.list_item_notes,
                    new String[] {"title", "cont"},
                    new int[] {R.id.textView_title_notes, R.id.textView_des_notes}
            ) {
                @Override
                public View getView (final int position, View convertView, ViewGroup parent) {

                    @SuppressWarnings("unchecked")
                    HashMap<String,String> map = (HashMap<String,String>)listView.getItemAtPosition(position);
                    final String title = map.get("title");
                    final String cont = map.get("cont");
                    final String seqnoStr = map.get("seqno");
                    final String icon = map.get("icon");
                    final String attachment = map.get("attachment");

                    View v = super.getView(position, convertView, parent);
                    ImageView i=(ImageView) v.findViewById(R.id.icon_notes);
                    ImageView i2=(ImageView) v.findViewById(R.id.att_notes);

                    switch (icon) {
                        case "1":
                            i.setImageResource(R.drawable.emoticon_neutral);
                            break;
                        case "2":
                            i.setImageResource(R.drawable.emoticon_happy);
                            break;
                        case "3":
                            i.setImageResource(R.drawable.emoticon_sad);
                            break;
                        case "4":
                            i.setImageResource(R.drawable.emoticon);
                            break;
                        case "5":
                            i.setImageResource(R.drawable.emoticon_cool);
                            break;
                        case "6":
                            i.setImageResource(R.drawable.emoticon_dead);
                            break;
                        case "7":
                            i.setImageResource(R.drawable.emoticon_excited);
                            break;
                        case "8":
                            i.setImageResource(R.drawable.emoticon_tongue);
                            break;
                        case "9":
                            i.setImageResource(R.drawable.emoticon_devil);
                            break;
                    }
                    switch (attachment) {
                        case "":
                            i2.setVisibility(View.GONE);
                            break;
                        default:
                            i2.setVisibility(View.VISIBLE);
                            break;
                    }

                    File file = new File(attachment);
                    if (!file.exists()) {
                        i2.setVisibility(View.GONE);
                    }

                    i.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {

                            final FragmentNotes.Item[] items = {
                                    new FragmentNotes.Item(getString(R.string.text_tit_1), R.drawable.emoticon_neutral),
                                    new FragmentNotes.Item(getString(R.string.text_tit_2), R.drawable.emoticon_happy),
                                    new FragmentNotes.Item(getString(R.string.text_tit_3), R.drawable.emoticon_sad),
                                    new FragmentNotes.Item(getString(R.string.text_tit_4), R.drawable.emoticon),
                                    new FragmentNotes.Item(getString(R.string.text_tit_5), R.drawable.emoticon_cool),
                                    new FragmentNotes.Item(getString(R.string.text_tit_6), R.drawable.emoticon_dead),
                                    new FragmentNotes.Item(getString(R.string.text_tit_7), R.drawable.emoticon_excited),
                                    new FragmentNotes.Item(getString(R.string.text_tit_8), R.drawable.emoticon_tongue),
                                    new FragmentNotes.Item(getString(R.string.text_tit_9), R.drawable.emoticon_devil)
                            };

                            ListAdapter adapter = new ArrayAdapter<FragmentNotes.Item>(
                                    getActivity(),
                                    android.R.layout.select_dialog_item,
                                    android.R.id.text1,
                                    items){
                                @NonNull
                                public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                                    //Use super class to create the View
                                    View v = super.getView(position, convertView, parent);
                                    TextView tv = (TextView)v.findViewById(android.R.id.text1);
                                    tv.setTextSize(18);
                                    tv.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0);
                                    //Add margin between image and text (support various screen densities)
                                    int dp5 = (int) (24 * getResources().getDisplayMetrics().density + 0.5f);
                                    tv.setCompoundDrawablePadding(dp5);

                                    return v;
                                }
                            };

                            new AlertDialog.Builder(getActivity())
                                    .setPositiveButton(R.string.goal_cancel, new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                dialog.dismiss();
                                                }
                                    })
                                    .setAdapter(adapter, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int item) {
                                            if (item == 0) {
                                                changeIcon(seqnoStr, title, cont, "1", attachment);
                                            } else if (item == 1) {
                                                changeIcon(seqnoStr, title, cont, "2", attachment);
                                            } else if (item == 2) {
                                                changeIcon(seqnoStr, title, cont, "3", attachment);
                                            } else if (item == 3) {
                                                changeIcon(seqnoStr, title, cont, "4", attachment);
                                            } else if (item == 4) {
                                                changeIcon(seqnoStr, title, cont, "5", attachment);
                                            } else if (item == 5) {
                                                changeIcon(seqnoStr, title, cont, "6", attachment);
                                            } else if (item == 6) {
                                                changeIcon(seqnoStr, title, cont, "7", attachment);
                                            } else if (item == 7) {
                                                changeIcon(seqnoStr, title, cont, "8", attachment);
                                            } else if (item == 8) {
                                                changeIcon(seqnoStr, title, cont, "9", attachment);
                                            } else if (item == 9) {
                                                changeIcon(seqnoStr, title, cont, "10", attachment);
                                            }
                                        }
                                    }).show();
                        }
                    });
                    i2.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            openAtt(attachment);
                        }
                    });

                    return v;
                }
            };

            listView.setAdapter(simpleAdapter);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void changeIcon(String seqno, String title, String url, String icon, String attachment) {
        try {

            final Database_Notes db = new Database_Notes(getActivity());
            db.deleteNote((Integer.parseInt(seqno)));
            db.addBookmark(title, url, icon, attachment);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {
            public void run() {
                setNotesList();
            }
        }, 500);
    }

    private void openAtt (String fileString) {
        File file = new File(fileString);
        final String fileExtension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));
        String text = (getActivity().getString(R.string.toast_extension) + ": " + fileExtension);

        switch (fileExtension) {
            case ".gif":
            case ".bmp":
            case ".tiff":
            case ".svg":
            case ".png":
            case ".jpg":
            case ".jpeg":
                helper_main.openFile(getActivity(), file, "image/*", listView);
                break;
            case ".m3u8":
            case ".mp3":
            case ".wma":
            case ".midi":
            case ".wav":
            case ".aac":
            case ".aif":
            case ".amp3":
            case ".weba":
                helper_main.openFile(getActivity(), file, "audio/*", listView);
                break;
            case ".mpeg":
            case ".mp4":
            case ".ogg":
            case ".webm":
            case ".qt":
            case ".3gp":
            case ".3g2":
            case ".avi":
            case ".f4v":
            case ".flv":
            case ".h261":
            case ".h263":
            case ".h264":
            case ".asf":
            case ".wmv":
                helper_main.openFile(getActivity(), file, "video/*", listView);
                break;
            case ".rtx":
            case ".csv":
            case ".txt":
            case ".vcs":
            case ".vcf":
            case ".css":
            case ".ics":
            case ".conf":
            case ".config":
            case ".java":
                helper_main.openFile(getActivity(), file, "text/*", listView);
                break;
            case ".html":
                helper_main.openFile(getActivity(), file, "text/html", listView);
                break;
            case ".apk":
                helper_main.openFile(getActivity(), file, "application/vnd.android.package-archive", listView);
                break;
            case ".pdf":
                helper_main.openFile(getActivity(), file, "application/pdf", listView);
                break;
            case ".doc":
                helper_main.openFile(getActivity(), file, "application/msword", listView);
                break;
            case ".xls":
                helper_main.openFile(getActivity(), file, "application/vnd.ms-excel", listView);
                break;
            case ".ppt":
                helper_main.openFile(getActivity(), file, "application/vnd.ms-powerpoint", listView);
                break;
            case ".docx":
                helper_main.openFile(getActivity(), file, "application/vnd.openxmlformats-officedocument.wordprocessingml.document", listView);
                break;
            case ".pptx":
                helper_main.openFile(getActivity(), file, "application/vnd.openxmlformats-officedocument.presentationml.presentation", listView);
                break;
            case ".xlsx":
                helper_main.openFile(getActivity(), file, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", listView);
                break;
            case ".odt":
                helper_main.openFile(getActivity(), file, "application/vnd.oasis.opendocument.text", listView);
                break;
            case ".ods":
                helper_main.openFile(getActivity(), file, "application/vnd.oasis.opendocument.spreadsheet", listView);
                break;
            case ".odp":
                helper_main.openFile(getActivity(), file, "application/vnd.oasis.opendocument.presentation", listView);
                break;
            case ".zip":
                helper_main.openFile(getActivity(), file, "application/zip", listView);
                break;
            case ".rar":
                helper_main.openFile(getActivity(), file, "application/x-rar-compressed", listView);
                break;
            case ".epub":
                helper_main.openFile(getActivity(), file, "application/epub+zip", listView);
                break;
            case ".cbz":
                helper_main.openFile(getActivity(), file, "application/x-cbz", listView);
                break;
            case ".cbr":
                helper_main.openFile(getActivity(), file, "application/x-cbr", listView);
                break;
            case ".fb2":
                helper_main.openFile(getActivity(), file, "application/x-fb2", listView);
                break;
            case ".rtf":
                helper_main.openFile(getActivity(), file, "application/rtf", listView);
                break;
            case ".opml":
                helper_main.openFile(getActivity(), file, "application/opml", listView);
                break;

            default:
                Snackbar snackbar = Snackbar
                        .make(listView, text, Snackbar.LENGTH_LONG);
                snackbar.show();
                break;
        }
    }

    public static class Item{
        public final String text;
        public final int icon;
        Item(String text, Integer icon) {
            this.text = text;
            this.icon = icon;
        }
        @Override
        public String toString() {
            return text;
        }
    }
}