package com.example.foodmates.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.foodmates.Models.Post;
import com.example.foodmates.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class ComposeActivity extends AppCompatActivity {
    public static final String TAG = "ComposeActivity";
    public final static int PICK_PHOTO_CODE = 1046;

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    public EditText etDescription;
    public Button btnSubmit;
    public ImageView ivPostImage;
    public EditText etTitle;
    public ProgressBar progressBar;
    public File photoFile;
    public ParseFile image;
    public String title;
    public String description;
    Button btnCaptureImage;
    Button btnPickImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etDescription = findViewById(R.id.etDescription);
        btnCaptureImage = findViewById(R.id.btnCaptureImage);
        btnSubmit = findViewById(R.id.btnSubmit);
        ivPostImage = findViewById(R.id.ivPostImage);
        etTitle = findViewById(R.id.etTitle);
        btnPickImage = findViewById(R.id.btnPickImage);
        progressBar = findViewById(R.id.pbLoading);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(ProgressBar.VISIBLE);

                btnSubmit.setClickable(false);

                description = etDescription.getText().toString();
                title = etTitle.getText().toString();
                if (title.isEmpty()) {
                    Toast.makeText(ComposeActivity.this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                    btnSubmit.setClickable(true);
                    return;
                }
                if (description.isEmpty()) {
                    Toast.makeText(ComposeActivity.this, "Description cannot be empty", Toast.LENGTH_SHORT).show();
                    btnSubmit.setClickable(true);
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description, currentUser, photoFile, title);

            }
        });

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        btnPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickPhoto(v);
            }
        });


    }


    public void savePost(String description, ParseUser currentUser, File photoFile, String title) {
        Toast.makeText(ComposeActivity.this, "Saving post. Please wait", Toast.LENGTH_SHORT).show();
        Post post = new Post();
        post.setDetail(description);
        post.setTitle(title);
        post.setImage(image);
        post.setUser(currentUser);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {

                    Toast.makeText(ComposeActivity.this, "Error while saving", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                }
                if (photoFile == null || ivPostImage.getDrawable() == null) {
                    Toast.makeText(ComposeActivity.this, "There is no image!", Toast.LENGTH_SHORT).show();
                    btnSubmit.setClickable(true);
                    return;
                }
                etDescription.setText("");
                etTitle.setText("");
                ivPostImage.setImageResource(0);
                btnSubmit.setClickable(true);
                progressBar.setVisibility(ProgressBar.INVISIBLE);
                finish();
            }
        });
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        photoFile = getPhotoFileUri(photoFileName);
        Uri fileProvider = FileProvider.getUriForFile(ComposeActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        if (intent.resolveActivity(ComposeActivity.this.getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }


    public void onPickPhoto(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        photoFile = getPhotoFileUri(photoFileName);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                ivPostImage.setImageBitmap(takenImage);
            } else {
                Toast.makeText(ComposeActivity.this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        } else if ((data != null) && requestCode == PICK_PHOTO_CODE) {
            File f = new File(this.getCacheDir(), photoFileName);
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Uri photoUri = data.getData();
            Bitmap selectedImage = loadFromUri(photoUri);
            ivPostImage.setImageBitmap(selectedImage);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitMapData = bos.toByteArray();

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(photoFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                fos.write(bitMapData);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public File getPhotoFileUri(String photoFileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + photoFileName);
        image = new ParseFile(file);

        return file;
    }


    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            if (Build.VERSION.SDK_INT > 27) {
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

}