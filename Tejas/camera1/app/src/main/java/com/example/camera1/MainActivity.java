package com.example.camera1;

import android.Manifest;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.content.ContextWrapper;
import android.content.Context;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class MainActivity extends AppCompatActivity {
    public static class User {

        public String username;
        public String email;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String username, String email) {
            this.username = username;
            this.email = email;
        }

    }
    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent in;
            switch (item.getItemId()) {
                case R.id.navigation_home: {
//                    Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_LONG).show();
//                    Log.i("homeeeeeeeee","home");
////                    Intent j=new Intent(this,maps.class);
////                    startActivity(j);
//                    in=new Intent(getBaseContext(),maps.class);
//                    startActivity(in);
                    return true;
                }
                case R.id.navigation_dashboard: {
                    //Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_LONG).show();
                    in=new Intent(getBaseContext(),maps.class);
                    startActivity(in);
//                    in=new Intent(getBaseContext(),maps.class);
//                    startActivity(in);
                    return true;
                }
                case R.id.navigation_notifications:
                    //Toast.makeText(getApplicationContext(),"Image Uploaded",Toast.LENGTH_LONG).show();
                    return true;
            }
            return false;
        }
    };
    public String pictureFilePath;
    private static Context context;
    public Uri filePath;
    Bitmap photo;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    ProgressDialog progressDialog;
    private DatabaseReference mDatabase;
// ...


    public static Context getAppContext() {
        return MainActivity.context;
    }

    static final int REQUEST_IMAGE_CAPTURE=1;
    ImageView myimageview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.navigation_home);

        Button x=(Button) findViewById(R.id.button);
        myimageview=(ImageView) findViewById(R.id.imageView);
        Log.i("adhi","adhi");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.i("adhi1","adhi1");
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void cameracapture(View view)
    {
        Intent i= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i,REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        FirebaseStorage storage = FirebaseStorage.getInstance();
    if(requestCode==1&&resultCode==RESULT_OK&&data!=null&&data.getData()==null) {
        Bundle extras = data.getExtras();

        photo = (Bitmap) extras.get("data");
        filePath = getImageUri(getApplicationContext(), photo);

        if(filePath!=null)
            Log.i("hi","bye");
        myimageview.setImageBitmap(photo);
        MediaStore.Images.Media.insertImage(getContentResolver(), photo, "taklus", "timepass");
        Log.i("Takla","takal");
//        ActivityCompat.requestPermissions(MainActivity.this,
//                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                1);
//        ActivityCompat.requestPermissions(MainActivity.this,
//                new String[]{Manifest.permission.INTERNET},
//                1);
        Log.i("saved", "iamge");
        uploadImage();
        new AlertDialog.Builder(this)
                .setTitle("Complaint Registered")
                .setMessage("Thank you Citizen. Your Complaint Code is 00000")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"Image Uploaded",Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("Cancel", null).show();
        mDatabase.child("users").child("123").setValue(new User("tejas","shah"));

   }



    }

public Uri getImageUri(Context inContext, Bitmap inImage) {
    Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000,true);
    String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
    return Uri.parse(path);
}



private void uploadImage() {


    final StorageReference riversRef = storageReference.child("images/rivers3.jpg");
    UploadTask uploadTask= riversRef.putFile(filePath);
//    riversRef.putFile(filePath)
//            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    // Get a URL to the uploaded content
//                      //Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                      String x1=taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
//                      Log.i("hhhhhhh",x1);
//                    Toast.makeText(getApplicationContext(),"Image Uploaded",Toast.LENGTH_LONG).show();
//                    //Snackbar.make(findViewById(R.id.content),"Image Uploaded",Snackbar.LENGTH_LONG).show();
//                }
//            })
//            .addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Handle unsuccessful uploads
//                    // ...
//                    Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_LONG).show();
//                }
//            });
    Task<Uri> urlTask = uploadTask.continueWithTask(new
                                                            Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                                                @Override
                                                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                                                    if (!task.isSuccessful()) {
                                                                        throw task.getException();
                                                                    }

                                                                    // Continue with the task to get the download URL
                                                                    return riversRef.getDownloadUrl();
                                                                }
                                                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
        @Override
        public void onComplete(@NonNull Task<Uri> task) {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                Log.i("hhhhhhh",downloadUri.toString());
                Toast.makeText(getApplicationContext(),"Image Uploaded",Toast.LENGTH_LONG).show();

            }
            else {
                // Handle failures
                // ...
            }
        }
    });

}



    /**
     * Created by Ilya Gazman on 3/6/2016.
     */

}
