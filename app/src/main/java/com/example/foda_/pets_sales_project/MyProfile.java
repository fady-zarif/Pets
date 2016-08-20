package com.example.foda_.pets_sales_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MyProfile extends AppCompatActivity {
    ImageButton imageButton;
    TextView MyprofileEmail;
    EditText MyProfileName, MyProfilePhone;
    Spinner spinner;
    Button MyButton;
    StorageReference storageRef;
    Uri uri=null;
    UserInfo info;
    ArrayAdapter arrayAdapter ;
Bitmap bitmap2;
    public UploadImage uploadImage;
    String x = "";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id =item.getItemId();
        if (id==android.R.id.home)
        {
            startActivity(new Intent(this,MainApp.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
if (resultCode==-1) {
    uploadImage = new UploadImage();
    uri = data.getData();
    String filepah = compressImage(uri.toString());
    Log.e("helloWorld", filepah);
    Log.e("helloWorld", filepah);
    Log.e("helloWorld", filepah);

    bitmap2 = uploadImage.compressImage(uri.toString(), getApplicationContext());
    imageButton.setImageBitmap(bitmap2);
}
}




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://pets-9c653.appspot.com");

        imageButton = (ImageButton) findViewById(R.id.MyProfile_Image);
        MyProfileName = (EditText) findViewById(R.id.MyProfile_Name);
        MyProfilePhone = (EditText) findViewById(R.id.MyProfile_Phone);
        MyButton = (Button) findViewById(R.id.Submit_Changes);
        MyprofileEmail=(TextView)findViewById(R.id.MyProfile_Email);
        spinner = (Spinner) findViewById(R.id.MyProfile_spinner);
        ArrayList<String> list =new ArrayList<>();
        list.add("Cairo");
        list.add("Alex");
        list.add("Alfayom");
        list.add("Aldkahlya");
        list.add("Almenya");
        list.add("Asyout");
        list.add("Aswan");
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.custom_text, list);
        adapter.setDropDownViewResource(R.layout.custom_text);
        spinner.setAdapter(adapter);


        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        final String Email =user.getEmail();
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("User Data");
        Query query= reference.orderByChild("Email").equalTo(Email);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                x=dataSnapshot.getKey();
                info=dataSnapshot.getValue(UserInfo.class);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

/*                StorageReference reference1=storageRef.child("images/239657");
                final File file=new File("/sdcard/Images/239657");
                reference1.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap=BitmapFactory.decodeFile(file.getAbsolutePath());
                        imageButton.setImageBitmap(bitmap);
                    }
                });*/
                if (info.ImageUrl != null && !info.ImageUrl.equals("")) {
                    Picasso.with(getApplicationContext()).load(info.ImageUrl).resize(150, 150).placeholder(R.drawable.user)
                            .into(imageButton);
                }
                MyProfileName.setText(info.Name);
                MyProfilePhone.setText(info.Phone);
                MyprofileEmail.setText(info.Email);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

          imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI),1);
            }
        });
        final ProgressDialog dialog = new ProgressDialog(this);
        MyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User Data");
                Query query = reference.orderByChild("Email").equalTo(Email);

                query.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dialog.setMessage("Loading.. ");
                        dialog.show();
                        if (uri != null) {
                            dialog.setMessage("Loading.. ");
                            dialog.show();
                            StorageMetadata metadata = new StorageMetadata.Builder().build();
                            //UploadTask uploadTask = storageRef.child("images/" + uri.getLastPathSegment()).putFile(uri);

                            ByteArrayOutputStream stream =new ByteArrayOutputStream();
                            bitmap2.compress(Bitmap.CompressFormat.JPEG,60,stream);
                            byte[]array=stream.toByteArray() ;
                            UploadTask uploadTask = storageRef.child("images/" + uri.getLastPathSegment()).putBytes(array);
                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageRef.child("images/" + uri.getLastPathSegment()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            reference.child(x).child("Name").setValue(MyProfileName.getText().toString());
                                            reference.child(x).child("Phone").setValue(MyProfilePhone.getText().toString());
                                            reference.child(x).child("Country").setValue(spinner.getSelectedItem().toString());
                                            reference.child(x).child("ImageUrl").setValue(uri.toString());
                                            dialog.dismiss();
                                            startActivity(new Intent(getApplicationContext(), MainApp.class));
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(Exception e) {

                                            dialog.dismiss();
                                            Toast.makeText(MyProfile.this, "Data not Saved \n \t Try again later", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                        else {
                            reference.child(x).child("Name").setValue(MyProfileName.getText().toString());
                            reference.child(x).child("Phone").setValue(MyProfilePhone.getText().toString());
                            reference.child(x).child("Country").setValue(spinner.getSelectedItem().toString());
                            dialog.dismiss();
                            startActivity(new Intent(getApplicationContext(), MainApp.class));


                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


    }

    public String compressImage(String imageUri) {
        Log.e("ImageUri", imageUri) ;
        String filePath = getRealPathFromURI(imageUri);
        Log.e("ImageUri",filePath) ;
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612
        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;
//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }


    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor =  getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }


}
