package de.baumann.quitsmoking;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Html;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by juergen on 09.06.16. Licensed under GPL.
 */
public class FragmentGoal extends Fragment {

    ImageView viewImage;
    Button b;
    private String rotate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_goal, container, false);
        setHasOptionsMenu(true);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        b = (Button) rootView.findViewById(R.id.btnSelectPhoto);
        viewImage=(ImageView)rootView.findViewById(R.id.imageView);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        if (sharedPref.getBoolean (rotate, false)){
            viewImage.setRotation(0);
        } else {
            viewImage.setRotation(90);
        }

        File imgFile = new File(Environment.getExternalStorageDirectory() + "/.test/temp.jpg");
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            viewImage.setImageBitmap(myBitmap);
            b.setVisibility(View.GONE);
        }

        return rootView;
    }

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(Environment.getExternalStorageDirectory() + "/.test/temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    String state = Environment.getExternalStorageState();
                    if (state.equals(Environment.MEDIA_MOUNTED)) {
                        Toast.makeText(getActivity().getApplicationContext(), " mounted ", Toast.LENGTH_LONG).show();

                    }
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
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

                File imgFile = new File(Environment.getExternalStorageDirectory() + "/.test/temp.jpg");
                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    viewImage.setImageBitmap(myBitmap);
                }

            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                viewImage.setImageURI(selectedImage);

                BitmapDrawable drawable = (BitmapDrawable) viewImage.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                File imgFile = new File(Environment.getExternalStorageDirectory() + "/.test/temp.jpg");

                boolean success = false;

                // Encode the file as a PNG image.
                FileOutputStream outStream;
                try {

                    outStream = new FileOutputStream(imgFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        /* 100 to keep full quality of the image */

                    outStream.flush();
                    outStream.close();
                    success = true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (id == R.id.action_backup) {
            selectImage();
        }

        if (id == R.id.action_share) {

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

        return super.onOptionsItemSelected(item);
    }
}
