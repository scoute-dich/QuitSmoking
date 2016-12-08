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

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.baumann.quitsmoking.R;
import de.baumann.quitsmoking.filechooser.ChooserDialog;

public class helper_notes {

    public static void editNote (final Activity from) {

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(from);
        final Button attachment;
        final ImageButton attachmentRem;
        final ImageButton attachmentCam;
        final ImageButton buttPaste;
        final EditText titleInput;
        final EditText textInput;
        final String priority = sharedPref.getString("handleTextIcon", "");

        LayoutInflater inflater = from.getLayoutInflater();

        final ViewGroup nullParent = null;
        View dialogView = inflater.inflate(R.layout.dialog_note_edit, nullParent);

        String file = sharedPref.getString("handleTextAttachment", "");
        String attName = file.substring(file.lastIndexOf("/")+1);

        buttPaste = (ImageButton) dialogView.findViewById(R.id.imageButtonPaste);
        attachmentRem = (ImageButton) dialogView.findViewById(R.id.button_rem);
        attachmentRem.setImageResource(R.drawable.close_red);
        attachment = (Button) dialogView.findViewById(R.id.button_att);
        attachmentCam = (ImageButton) dialogView.findViewById(R.id.button_cam);
        attachmentCam.setImageResource(R.drawable.camera);

        if (attName.equals("")) {
            attachment.setText(R.string.choose_att);
            attachmentRem.setVisibility(View.GONE);
            attachmentCam.setVisibility(View.VISIBLE);
        } else {
            attachment.setText(attName);
            attachmentRem.setVisibility(View.VISIBLE);
            attachmentCam.setVisibility(View.GONE);
        }

        File file2 = new File(file);
        if (!file2.exists()) {
            attachment.setText(R.string.choose_att);
            attachmentRem.setVisibility(View.GONE);
            attachmentCam.setVisibility(View.VISIBLE);
        }

        titleInput = (EditText) dialogView.findViewById(R.id.note_title_input);
        textInput = (EditText) dialogView.findViewById(R.id.note_text_input);
        titleInput.setText(sharedPref.getString("handleTextTitle", ""));
        titleInput.setSelection(titleInput.getText().length());
        textInput.setText(sharedPref.getString("handleTextText", ""));
        textInput.setSelection(textInput.getText().length());

        titleInput.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                sharedPref.edit().putString("editTextFocus", "title").apply();
                return false;
            }
        });

        textInput.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View arg0, MotionEvent arg1) {
                sharedPref.edit().putString("editTextFocus", "text").apply();
                return false;
            }
        });

        attachment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String startDir = Environment.getExternalStorageDirectory() + "/QuitSmoking/";
                new ChooserDialog().with(from)
                        .withStartFile(startDir)
                        .withChosenListener(new ChooserDialog.Result() {
                            @Override
                            public void onChoosePath(final File pathFile) {
                                final String fileName = pathFile.getAbsolutePath();
                                String attName = fileName.substring(fileName.lastIndexOf("/")+1);
                                attachment.setText(attName);
                                attachmentRem.setVisibility(View.VISIBLE);
                                attachmentCam.setVisibility(View.GONE);
                                sharedPref.edit().putString("handleTextAttachment", fileName).apply();
                            }
                        })
                        .build()
                        .show();
            }
        });

        attachmentRem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                sharedPref.edit().putString("handleTextAttachment", "").apply();
                attachment.setText(R.string.choose_att);
                attachmentRem.setVisibility(View.GONE);
                attachmentCam.setVisibility(View.VISIBLE);
            }
        });

        attachmentCam.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(from, Popup_camera.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                from.startActivity(intent);
            }
        });

        buttPaste.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final CharSequence[] options = {
                        from.getString(R.string.paste_date),
                        from.getString(R.string.paste_time),
                        from.getString(R.string.paste_line)};
                new android.app.AlertDialog.Builder(from)
                        .setPositiveButton(R.string.goal_cancel, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        })
                        .setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                if (options[item].equals(from.getString(R.string.paste_date))) {
                                    String dateFormat = sharedPref.getString("dateFormat", "1");

                                    switch (dateFormat) {
                                        case "1":

                                            Date date = new Date();
                                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                            String dateNow = format.format(date);

                                            if(sharedPref.getString("editTextFocus", "").equals("text")) {
                                                textInput.getText().insert(textInput.getSelectionStart(), dateNow);
                                            } else {
                                                titleInput.getText().insert(titleInput.getSelectionStart(), dateNow);
                                            }

                                            break;

                                        case "2":

                                            Date date2 = new Date();
                                            SimpleDateFormat format2 = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                                            String dateNow2 = format2.format(date2);

                                            if(sharedPref.getString("editTextFocus", "").equals("text")) {
                                                textInput.getText().insert(textInput.getSelectionStart(), dateNow2);
                                            } else {
                                                titleInput.getText().insert(titleInput.getSelectionStart(), dateNow2);
                                            }

                                            break;
                                    }
                                }

                                if (options[item].equals (from.getString(R.string.paste_time))) {
                                    Date date = new Date();
                                    SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
                                    String timeNow = format.format(date);
                                    if(sharedPref.getString("editTextFocus", "").equals("text")) {
                                        textInput.getText().insert(textInput.getSelectionStart(), timeNow);
                                    } else {
                                        titleInput.getText().insert(titleInput.getSelectionStart(), timeNow);
                                    }
                                }

                                if (options[item].equals(from.getString(R.string.paste_line))) {
                                    if(sharedPref.getString("editTextFocus", "").equals("text")) {
                                        textInput.getText().insert(textInput.getSelectionStart(), "==========");
                                    } else {
                                        titleInput.getText().insert(titleInput.getSelectionStart(), "==========");
                                    }
                                }
                            }
                        }).show();
            }
        });

        new Handler().postDelayed(new Runnable() {
            public void run() {
                helper_main.showKeyboard(from,titleInput);
            }
        }, 200);

        final ImageButton be = (ImageButton) dialogView.findViewById(R.id.imageButtonPri);
        assert be != null;

        switch (priority) {
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
                sharedPref.edit().putString("handleTextIcon", "1").apply();
                break;
        }

        be.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                final helper_notes.Item[] items = {
                        new helper_notes.Item(from.getString(R.string.text_tit_1), R.drawable.emoticon_neutral),
                        new helper_notes.Item(from.getString(R.string.text_tit_2), R.drawable.emoticon_happy),
                        new helper_notes.Item(from.getString(R.string.text_tit_3), R.drawable.emoticon_sad),
                        new helper_notes.Item(from.getString(R.string.text_tit_4), R.drawable.emoticon),
                        new helper_notes.Item(from.getString(R.string.text_tit_5), R.drawable.emoticon_cool),
                        new helper_notes.Item(from.getString(R.string.text_tit_6), R.drawable.emoticon_dead),
                        new helper_notes.Item(from.getString(R.string.text_tit_7), R.drawable.emoticon_excited),
                        new helper_notes.Item(from.getString(R.string.text_tit_8), R.drawable.emoticon_tongue),
                        new helper_notes.Item(from.getString(R.string.text_tit_9), R.drawable.emoticon_devil)
                };

                ListAdapter adapter = new ArrayAdapter<helper_notes.Item>(
                        from,
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
                        int dp5 = (int) (24 * from.getResources().getDisplayMetrics().density + 0.5f);
                        tv.setCompoundDrawablePadding(dp5);

                        return v;
                    }
                };

                new AlertDialog.Builder(from)
                        .setPositiveButton(R.string.goal_cancel, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        })
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int item) {
                                if (item == 0) {
                                    be.setImageResource(R.drawable.emoticon_neutral);
                                    sharedPref.edit().putString("handleTextIcon", "1").apply();
                                } else if (item == 1) {
                                    be.setImageResource(R.drawable.emoticon_happy);
                                    sharedPref.edit().putString("handleTextIcon", "2").apply();
                                } else if (item == 2) {
                                    be.setImageResource(R.drawable.emoticon_sad);
                                    sharedPref.edit().putString("handleTextIcon", "3").apply();
                                } else if (item == 3) {
                                    be.setImageResource(R.drawable.emoticon);
                                    sharedPref.edit().putString("handleTextIcon", "4").apply();
                                } else if (item == 4) {
                                    be.setImageResource(R.drawable.emoticon_cool);
                                    sharedPref.edit().putString("handleTextIcon", "5").apply();
                                } else if (item == 5) {
                                    be.setImageResource(R.drawable.emoticon_dead);
                                    sharedPref.edit().putString("handleTextIcon", "6").apply();
                                } else if (item == 6) {
                                    be.setImageResource(R.drawable.emoticon_excited);
                                    sharedPref.edit().putString("handleTextIcon", "7").apply();
                                } else if (item == 7) {
                                    be.setImageResource(R.drawable.emoticon_tongue);
                                    sharedPref.edit().putString("handleTextIcon", "8").apply();
                                } else if (item == 8) {
                                    be.setImageResource(R.drawable.emoticon_devil);
                                    sharedPref.edit().putString("handleTextIcon", "9").apply();
                                }
                            }
                        }).show();
            }
        });

        android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(from)
                .setTitle(R.string.note_edit)
                .setView(dialogView)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        String seqno = sharedPref.getString("handleTextSeqno", "");

                        try {

                            final Database_Notes db = new Database_Notes(from);
                            String inputTitle = titleInput.getText().toString().trim();
                            String inputContent = textInput.getText().toString().trim();
                            String attachment = sharedPref.getString("handleTextAttachment", "");

                            db.addBookmark(inputTitle, inputContent, sharedPref.getString("handleTextIcon", ""), attachment);
                            db.close();

                            if (seqno.length() > 0) {
                                db.deleteNote((Integer.parseInt(seqno)));
                                sharedPref.edit()
                                        .putString("handleTextSeqno", "")
                                        .apply();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        sharedPref.edit()
                                .putString("handleTextTitle", "")
                                .putString("handleTextText", "")
                                .putString("handleTextIcon", "")
                                .putString("handleTextAttachment", "")
                                .putString("editTextFocus", "")
                                .apply();
                        helper_notes.setNotesList(from);

                    }
                })
                .setNegativeButton(R.string.goal_cancel, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        sharedPref.edit()
                                .putString("handleTextTitle", "")
                                .putString("handleTextText", "")
                                .putString("handleTextIcon", "")
                                .putString("handleTextAttachment", "")
                                .putString("editTextFocus", "")
                                .apply();
                        dialog.cancel();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(final DialogInterface dialog) {
                        sharedPref.edit()
                                .putString("handleTextTitle", "")
                                .putString("handleTextText", "")
                                .putString("handleTextIcon", "")
                                .putString("handleTextAttachment", "")
                                .putString("editTextFocus", "")
                                .apply();
            }
        });
        dialog.show();
    }

    private static class Item{
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

    private static void setNotesList(final Activity from) {

        final ListView listView = (ListView)from.findViewById(R.id.notes);

        ArrayList<HashMap<String,String>> mapList = new ArrayList<>();

        try {
            Database_Notes db = new Database_Notes(from);
            ArrayList<String[]> bookmarkList = new ArrayList<>();
            db.getBookmarks(bookmarkList, from);
            if (bookmarkList.size() == 0) {
                db.loadInitialData();
                db.getBookmarks(bookmarkList, from);
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
                    from,
                    mapList,
                    R.layout.list_item_notes,
                    new String[] {"title", "cont"},
                    new int[] {R.id.textView_title_notes, R.id.textView_des_notes}
            ) {
                @Override
                public View getView (final int position, View convertView, ViewGroup parent) {

                    @SuppressWarnings("unchecked")
                    HashMap<String,String> map = (HashMap<String,String>)listView.getItemAtPosition(position);
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

                    i2.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {

                            File file = new File(attachment);
                            final String fileExtension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));
                            String text = (from.getString(R.string.toast_extension) + ": " + fileExtension);

                            switch (fileExtension) {
                                case ".gif":
                                case ".bmp":
                                case ".tiff":
                                case ".svg":
                                case ".png":
                                case ".jpg":
                                case ".jpeg":
                                    helper_main.openFile(from, file, "image/*", listView);
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
                                    helper_main.openFile(from, file, "audio/*", listView);
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
                                    helper_main.openFile(from, file, "video/*", listView);
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
                                    helper_main.openFile(from, file, "text/*", listView);
                                    break;
                                case ".html":
                                    helper_main.openFile(from, file, "text/html", listView);
                                    break;
                                case ".apk":
                                    helper_main.openFile(from, file, "application/vnd.android.package-archive", listView);
                                    break;
                                case ".pdf":
                                    helper_main.openFile(from, file, "application/pdf", listView);
                                    break;
                                case ".doc":
                                    helper_main.openFile(from, file, "application/msword", listView);
                                    break;
                                case ".xls":
                                    helper_main.openFile(from, file, "application/vnd.ms-excel", listView);
                                    break;
                                case ".ppt":
                                    helper_main.openFile(from, file, "application/vnd.ms-powerpoint", listView);
                                    break;
                                case ".docx":
                                    helper_main.openFile(from, file, "application/vnd.openxmlformats-officedocument.wordprocessingml.document", listView);
                                    break;
                                case ".pptx":
                                    helper_main.openFile(from, file, "application/vnd.openxmlformats-officedocument.presentationml.presentation", listView);
                                    break;
                                case ".xlsx":
                                    helper_main.openFile(from, file, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", listView);
                                    break;
                                case ".odt":
                                    helper_main.openFile(from, file, "application/vnd.oasis.opendocument.text", listView);
                                    break;
                                case ".ods":
                                    helper_main.openFile(from, file, "application/vnd.oasis.opendocument.spreadsheet", listView);
                                    break;
                                case ".odp":
                                    helper_main.openFile(from, file, "application/vnd.oasis.opendocument.presentation", listView);
                                    break;
                                case ".zip":
                                    helper_main.openFile(from, file, "application/zip", listView);
                                    break;
                                case ".rar":
                                    helper_main.openFile(from, file, "application/x-rar-compressed", listView);
                                    break;
                                case ".epub":
                                    helper_main.openFile(from, file, "application/epub+zip", listView);
                                    break;
                                case ".cbz":
                                    helper_main.openFile(from, file, "application/x-cbz", listView);
                                    break;
                                case ".cbr":
                                    helper_main.openFile(from, file, "application/x-cbr", listView);
                                    break;
                                case ".fb2":
                                    helper_main.openFile(from, file, "application/x-fb2", listView);
                                    break;
                                case ".rtf":
                                    helper_main.openFile(from, file, "application/rtf", listView);
                                    break;
                                case ".opml":
                                    helper_main.openFile(from, file, "application/opml", listView);
                                    break;

                                default:
                                    Toast.makeText(from, text, Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    });

                    return v;
                }
            };

            if (listView != null) {
                listView.setAdapter(simpleAdapter);
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
