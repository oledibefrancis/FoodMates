package com.example.foodmates.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

//import com.example.careermatch.R;
import com.example.foodmates.Models.Post;
import com.example.foodmates.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class ComposeActivity extends AppCompatActivity {
    public static final String  TAG = "ComposeActivity";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    EditText etDescription;
    Button btnCaptureImage;
    Button btnSubmit;
    ImageView ivPostImage;
    EditText etTitle;
    public String photoFileName = "photo.jpg";
    private File photoFile;
    private ParseFile image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etDescription = findViewById(R.id.etDescription);
        btnCaptureImage = findViewById(R.id.btnCaptureImage);
        btnSubmit = findViewById(R.id.btnSubmit);
        ivPostImage = findViewById(R.id.ivPostImage);
        etTitle = findViewById(R.id.etTitle);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSubmit.setClickable(false);

                String description = etDescription.getText().toString();
                String title = etTitle.getText().toString();
                if (description.isEmpty()){
                    Toast.makeText( ComposeActivity.this,"Description cannot be empty",Toast.LENGTH_SHORT).show();
                    btnSubmit.setClickable(true);
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description,currentUser,photoFile,title);

            }

            private void savePost(String description, ParseUser currentUser, File photoFile, String title) {
                Toast.makeText(ComposeActivity.this, "Saving post. Please wait", Toast.LENGTH_SHORT).show();
                Post post = new Post();
                post.setDescription(description);
                post.setTitle(title);
                post.setImage(image);
                post.setUser(currentUser);
                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null){

                            Toast.makeText(ComposeActivity.this, "Error while saving", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                        }
                        if (photoFile == null || ivPostImage.getDrawable() ==null){
                            Toast.makeText(ComposeActivity.this,"There is no image!",Toast.LENGTH_SHORT).show();
                            btnSubmit.setClickable(true);
                            return;
                        }
                        etDescription.setText("");
                        etTitle.setText("");
                        ivPostImage.setImageResource(0);
                        btnSubmit.setClickable(true);
                        finish();
                    }
                });
            }
        });
        
        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(ComposeActivity.this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(ComposeActivity.this.getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                ivPostImage.setImageBitmap(takenImage);
                Toast.makeText(ComposeActivity.this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public File getPhotoFileUri(String photoFileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + photoFileName);
        image = new ParseFile(file);

        return file;
    }


}