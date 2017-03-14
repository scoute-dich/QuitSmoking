package de.baumann.quitsmoking.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.baumann.quitsmoking.R;


public class Activity_EditNote extends AppCompatActivity {

    private Button attachment;
    private ImageButton attachmentRem;
    private ImageButton attachmentCam;
    private EditText titleInput;
    private EditText textInput;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(R.string.note_edit);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        PreferenceManager.setDefaultValues(Activity_EditNote.this, R.xml.user_settings, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(Activity_EditNote.this);

        final String priority = sharedPref.getString("handleTextIcon", "");

        String file = sharedPref.getString("handleTextAttachment", "");
        final String attName = file.substring(file.lastIndexOf("/")+1);

        attachmentRem = (ImageButton) findViewById(R.id.button_rem);
        attachmentRem.setImageResource(R.drawable.close_red);
        attachment = (Button) findViewById(R.id.button_att);
        attachmentCam = (ImageButton) findViewById(R.id.button_cam);
        attachmentCam.setImageResource(R.drawable.camera);

        String att = getString(R.string.note_attachment) + ": " + attName;

        if (attName.equals("")) {
            attachment.setText(R.string.choose_att);
            attachmentRem.setVisibility(View.GONE);
            attachmentCam.setVisibility(View.VISIBLE);
        } else {
            attachment.setText(att);
            attachmentRem.setVisibility(View.VISIBLE);
            attachmentCam.setVisibility(View.GONE);
        }
        File file2 = new File(file);
        if (!file2.exists()) {
            attachment.setText(R.string.choose_att);
            attachmentRem.setVisibility(View.GONE);
            attachmentCam.setVisibility(View.VISIBLE);
        }

        titleInput = (EditText) findViewById(R.id.note_title_input);
        textInput = (EditText) findViewById(R.id.note_text_input);
        helper_main.showKeyboard(Activity_EditNote.this, titleInput);

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

                Intent mainIntent = new Intent(Activity_EditNote.this, Activity_files.class);
                mainIntent.setAction("file_chooseAttachment");
                startActivity(mainIntent);

                attachmentRem.setVisibility(View.VISIBLE);
                attachmentCam.setVisibility(View.GONE);
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

                File f = helper_main.newFile();
                final String fileName = f.getAbsolutePath();
                sharedPref.edit().putString("handleTextAttachment", fileName).apply();

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(titleInput.getWindowToken(), 0);
                Intent intent = new Intent(Activity_EditNote.this, Popup_camera.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

        final ImageButton be = (ImageButton) findViewById(R.id.imageButtonPri);
        ImageButton ib_paste = (ImageButton) findViewById(R.id.imageButtonPaste);
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

                final Item[] items = {
                        new Item(getString(R.string.text_tit_1), R.drawable.emoticon_neutral),
                        new Item(getString(R.string.text_tit_2), R.drawable.emoticon_happy),
                        new Item(getString(R.string.text_tit_3), R.drawable.emoticon_sad),
                        new Item(getString(R.string.text_tit_4), R.drawable.emoticon),
                        new Item(getString(R.string.text_tit_5), R.drawable.emoticon_cool),
                        new Item(getString(R.string.text_tit_6), R.drawable.emoticon_dead),
                        new Item(getString(R.string.text_tit_7), R.drawable.emoticon_excited),
                        new Item(getString(R.string.text_tit_8), R.drawable.emoticon_tongue),
                        new Item(getString(R.string.text_tit_9), R.drawable.emoticon_devil)
                };

                ListAdapter adapter = new ArrayAdapter<Item>(
                        Activity_EditNote.this,
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

                new android.app.AlertDialog.Builder(Activity_EditNote.this)
                        .setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
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

        ib_paste.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final CharSequence[] options = {
                        getString(R.string.paste_date),
                        getString(R.string.paste_time),
                        getString(R.string.paste_line)};
                new android.app.AlertDialog.Builder(Activity_EditNote.this)
                        .setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        })
                        .setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                if (options[item].equals(getString(R.string.paste_date))) {
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

                                if (options[item].equals (getString(R.string.paste_time))) {
                                    Date date = new Date();
                                    SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
                                    String timeNow = format.format(date);
                                    if(sharedPref.getString("editTextFocus", "").equals("text")) {
                                        textInput.getText().insert(textInput.getSelectionStart(), timeNow);
                                    } else {
                                        titleInput.getText().insert(titleInput.getSelectionStart(), timeNow);
                                    }
                                }

                                if (options[item].equals (getString(R.string.paste_line))) {
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

    @Override
    protected void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.

        String file = sharedPref.getString("handleTextAttachment", "");
        final String attName = file.substring(file.lastIndexOf("/")+1);

        attachmentRem = (ImageButton) findViewById(R.id.button_rem);
        attachmentRem.setImageResource(R.drawable.close_red);
        attachment = (Button) findViewById(R.id.button_att);
        attachmentCam = (ImageButton) findViewById(R.id.button_cam);
        attachmentCam.setImageResource(R.drawable.camera);

        String att = getString(R.string.note_attachment) + ": " + attName;

        if (attName.equals("")) {
            attachment.setText(R.string.choose_att);
            attachmentRem.setVisibility(View.GONE);
            attachmentCam.setVisibility(View.VISIBLE);
        } else {
            attachment.setText(att);
            attachmentRem.setVisibility(View.VISIBLE);
            attachmentCam.setVisibility(View.GONE);
        }
        File file2 = new File(file);
        if (!file2.exists()) {
            attachment.setText(R.string.choose_att);
            attachmentRem.setVisibility(View.GONE);
            attachmentCam.setVisibility(View.VISIBLE);
        }
    }

    public void onBackPressed() {
        Snackbar snackbar = Snackbar
                .make(titleInput, R.string.toast_save, Snackbar.LENGTH_LONG)
                .setAction(R.string.yes, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sharedPref.edit()
                                .putString("handleTextTitle", "")
                                .putString("handleTextText", "")
                                .putString("handleTextIcon", "")
                                .putString("handleTextAttachment", "")
                                .putString("handleTextCreate", "")
                                .putString("editTextFocus", "")
                                .putString("handleTextSeqno", "")
                                .apply();
                        finish();
                    }
                });
        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Snackbar snackbar = Snackbar
                    .make(titleInput, R.string.toast_save, Snackbar.LENGTH_LONG)
                    .setAction(R.string.yes, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            sharedPref.edit()
                                    .putString("handleTextTitle", "")
                                    .putString("handleTextText", "")
                                    .putString("handleTextIcon", "")
                                    .putString("handleTextAttachment", "")
                                    .putString("handleTextCreate", "")
                                    .putString("editTextFocus", "")
                                    .putString("handleTextSeqno", "")
                                    .apply();
                            finish();
                        }
                    });
            snackbar.show();
        }

        if (id == R.id.action_save) {

            DbAdapter_Notes db = new DbAdapter_Notes(Activity_EditNote.this);
            db.open();

            String inputTitle = titleInput.getText().toString().trim();
            String inputContent = textInput.getText().toString().trim();
            String attachment = sharedPref.getString("handleTextAttachment", "");
            String create = sharedPref.getString("handleTextCreate", "");
            String seqno = sharedPref.getString("handleTextSeqno", "");

            if (seqno.isEmpty()) {
                try {
                    if(db.isExist(inputTitle)){
                        Snackbar.make(titleInput, getString(R.string.toast_newTitle), Snackbar.LENGTH_LONG).show();
                    }else{
                        db.insert(inputTitle, inputContent, sharedPref.getString("handleTextIcon", ""), attachment, create);
                        sharedPref.edit()
                                .putString("handleTextTitle", "")
                                .putString("handleTextText", "")
                                .putString("handleTextIcon", "")
                                .putString("handleTextAttachment", "")
                                .putString("handleTextCreate", "")
                                .putString("editTextFocus", "")
                                .putString("handleTextSeqno", "")
                                .apply();
                        finish();
                    }
                } catch (Exception e) {
                    Log.w("QS", "Error Package name not found ", e);
                    Snackbar snackbar = Snackbar
                            .make(titleInput, R.string.toast_notSave, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            } else {
                try {
                    db.update(Integer.parseInt(seqno), inputTitle, inputContent, sharedPref.getString("handleTextIcon", ""), attachment, create);
                    sharedPref.edit()
                            .putString("handleTextTitle", "")
                            .putString("handleTextText", "")
                            .putString("handleTextIcon", "")
                            .putString("handleTextAttachment", "")
                            .putString("handleTextCreate", "")
                            .putString("editTextFocus", "")
                            .putString("handleTextSeqno", "")
                            .apply();
                    finish();
                } catch (Exception e) {
                    Log.w("QS", "Error Package name not found ", e);
                    Snackbar snackbar = Snackbar
                            .make(titleInput, R.string.toast_notSave, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
