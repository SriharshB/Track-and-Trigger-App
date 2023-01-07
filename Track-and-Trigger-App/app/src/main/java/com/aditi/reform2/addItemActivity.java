package com.aditi.reform2;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.squareup.picasso.Picasso.get;

public class addItemActivity extends AppCompatActivity {
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;
    Button additemtodatabase;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    ImageView selectedImage;
    Button galleryBtn;
    String currentPhotoPath;
    Items item;
    Uri FilePathUri;
    private EditText itemname, itemquantity, itemdescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        storageReference = FirebaseStorage.getInstance().getReference("Images");
        additemtodatabase = findViewById(R.id.additembuttontodatabase);
        itemname = findViewById(R.id.edititemname);
        itemquantity = findViewById(R.id.editquantity);
        itemdescription = findViewById(R.id.editdescription);
        selectedImage = findViewById(R.id.displayImageView);
        galleryBtn = findViewById(R.id.galleryBtn);
        item = new Items();

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });

        additemtodatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add item to database

                UploadImage();

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                FilePathUri = data.getData();
                //Uri contentUri = data.getData();
                ///String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                //String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                //Log.d("tag", "onActivityResult: Gallery Image Uri:  " + imageFileName);
                selectedImage.setImageURI(FilePathUri);

            }

        }


    }


    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    public void UploadImage() {

        if (FilePathUri != null) {
            String product = itemname.getText().toString();
            String quantity = itemquantity.getText().toString();
            String description = itemdescription.getText().toString();
            String image;
            StorageReference storageReference2 = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            storageReference2.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    //createNewPost(imageUrl);
                                    Items imageUploadInfo = new Items(product, quantity, description, imageUrl);

                                    // updating database
                                    String _USERNAME, _CATEGORY;
                                    Intent intent = getIntent();
                                    _USERNAME = intent.getStringExtra("username");
                                    _CATEGORY = intent.getStringExtra("category");
                                    databaseReference.child(_USERNAME).child("Categories").child(_CATEGORY).child(product).setValue(imageUploadInfo);

                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(addItemActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {

            Toast.makeText(addItemActivity.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }


    }
}








