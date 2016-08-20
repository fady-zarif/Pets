package com.example.foda_.pets_sales_project;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class Create_Ad extends android.support.v4.app.Fragment {
    Spinner Pet_spinner, Gender_spinner;
    EditText Pet_Name, Pet_Age, Pet_Price, Description;
    Button Share;
    ImageView imageView;
    public UploadImage uploadImage;
    static String URLImage = "";
    ImageButton selectPic;
    StorageReference storageRef;
    int  path=0;
    Uri uri;

    Bitmap bitmap = null;

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data)
    {
        uploadImage=new UploadImage();
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            uri = data.getData();
            path=1;
         //   path.setText(uri.toString());
         //   Toast.makeText(getActivity(), uri.toString(), Toast.LENGTH_SHORT).show();
            bitmap = uploadImage.compressImage(uri.toString(),getContext());
            imageView.setImageBitmap(bitmap);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_create__ad, container, false);

        final FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://pets-9c653.appspot.com");

        Pet_spinner = (Spinner) root.findViewById(R.id.Pets_Spinner);
        Gender_spinner = (Spinner) root.findViewById(R.id.Gender_Spinner);
        Pet_Age = (EditText) root.findViewById(R.id.PetAge);
        Pet_Price = (EditText) root.findViewById(R.id.PetPrice);
        Pet_Name = (EditText) root.findViewById(R.id.PetName);
        imageView=(ImageView)root.findViewById(R.id.petimagebitmap);
        Description = (EditText) root.findViewById(R.id.PetDescription);
        Share = (Button) root.findViewById(R.id.Share);
        String[] array = {"Dog", "Cat", "Birds", "Toutories", "hamester"};
        final ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, array);
        Pet_spinner.setAdapter(adapter);
        String[] array2 = {"Male", "Female"};
        ArrayAdapter adapter2 = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, array2);
        Gender_spinner.setAdapter(adapter2);
        selectPic = (ImageButton) root.findViewById(R.id.Select_Pic);
        selectPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 2);

            }
        });

        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int z = 0;
                if (Pet_Name.getText().toString().equals("")) {
                    Pet_Name.setError("Required");
                    z = 1;
                }
                if (Pet_Age.getText().toString().equals("")) {
                    Pet_Age.setError("Required");
                    z = 1;
                }
                if (Pet_Price.getText().toString().equals("")) {
                    Pet_Price.setError("Required");
                    z = 1;
                }
                if (Description.getText().toString().equals("")) {
                    Description.setError("Required");
                    z = 1;
                }
                if (z == 0) {
                    if (path==0) {
                        Toast.makeText(getContext(), "Select One Pic Please", Toast.LENGTH_SHORT).show();
                    } else {

                        Random random = new Random();
                        int x = random.nextInt(1000);
                        final String NewUri = uri.getLastPathSegment() + x;
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            final String mail = user.getEmail();

                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
                            SimpleDateFormat timeformat = new SimpleDateFormat("hh:mm aa");
                            String datetime = dateformat.format(c.getTime());
                            String time = timeformat.format(c.getTime());
                            final HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("OwnerEmail", mail);
                            map.put("Type", Pet_spinner.getSelectedItem().toString());
                            map.put("Gender", Gender_spinner.getSelectedItem().toString());
                            map.put("Name", Pet_Name.getText().toString());
                            map.put("Age", Pet_Age.getText().toString());
                            map.put("Price", Pet_Price.getText().toString());
                            map.put("Description", Description.getText().toString());
                            map.put("Date", datetime);
                            map.put("Time", time);

                   ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                   bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream1);
                   byte[] array = stream1.toByteArray();
                   UploadTask uploadTask = storageRef.child("images/" + NewUri).putBytes(array);


                            //  UploadTask uploadTask = storageRef.child("images/" + NewUri).putFile(uri, metadata);
                            final ProgressDialog dialog = new ProgressDialog(getContext());
                            dialog.setCanceledOnTouchOutside(false);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {

                                    Toast.makeText(getContext(), "Uploding & Sharing failed", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        /*    uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                    int x = (int) progress;
                                    if (x == 100) {
                                        dialog.setMessage(" Just Seconds .. ");
                                        dialog.show();
                                    } else {
                                        dialog.setMessage(" Uploading " + x + "  %");
                                        dialog.show();
                                    }
                                }
                            });

*/
                                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        dialog.setMessage("Loading ... ");
                                        dialog.show();
                                    }
                            });
    /*
        * Bitmap Bimg = ((BitmapDrawable) imgV.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Bimg.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        encodimg = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);*/
                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    storageRef.child("images/" + NewUri).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
/*
                                            Calendar c3 = Calendar.getInstance();
                                            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MMM-dd");
                                            String datetime = dateformat.format(c3.getTime());
*/
                                            Date date = new Date();

                                            String URLImage = uri.toString();
                                            map.put("URLImage", URLImage);
                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            DatabaseReference reference = database.getReference("Advertisement");
                                            reference.push().setValue(map, 0 - date.getTime()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    dialog.dismiss();
                                                    Toast.makeText(getContext(), "Uploading successfully ", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getActivity(), MainApp.class);
                                                    startActivity(intent);

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(Exception e) {
                                                    dialog.dismiss();
                                                    Toast.makeText(getContext(), "Uploading Failed \n \t try again", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            dialog.dismiss();
                                            Toast.makeText(getContext(), "Uploading Failed \n \t try again", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });

                        }
                    }
                }
            }
        });

        return root;

    }
}
