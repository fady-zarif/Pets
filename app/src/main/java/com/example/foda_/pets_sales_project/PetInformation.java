package com.example.foda_.pets_sales_project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PetInformation extends AppCompatActivity {
    TextView PetInfo_Name, PetInfo_Age, PetInfo_Price, PetInfo_Description, PetInfo_Gender, PetInfo_Type, OwnerInfo_Name, OwnerInfo_Country;
    ImageButton OwnerInfo_Image;
    ImageView PetInfo_Image;
    RatingBar OwnerInfo_Rating;
    UserInfo info;
    String RATE = "";
    int x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("P.e.t.s");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//......................................................................................................
        PetInfo_Name = (TextView) findViewById(R.id.PetInfoName);
        PetInfo_Age = (TextView) findViewById(R.id.PetInfoAge);
        PetInfo_Price = (TextView) findViewById(R.id.PetInfoPrice);
        PetInfo_Description = (TextView) findViewById(R.id.PetInfoDesc);
        PetInfo_Gender = (TextView) findViewById(R.id.PetInfoGender);
        //        PetInfo_Type = (TextView) findViewById(R.id.PetInfoType);
        OwnerInfo_Name = (TextView) findViewById(R.id.OwnerInfoName);
        OwnerInfo_Country = (TextView) findViewById(R.id.OwnerInfoCountry);
        OwnerInfo_Image = (ImageButton) findViewById(R.id.OwnerInfoImage);
        OwnerInfo_Rating = (RatingBar) findViewById(R.id.OwnerInfoRating);
        LinearLayout layout = (LinearLayout) findViewById(R.id.MyLinear);
        PetInfo_Image = (ImageView) findViewById(R.id.PetInfoImage);
        View view = (View) findViewById(R.id.s);
        final Pet_Data pet_data = getIntent().getExtras().getParcelable("MyParcelabale");

        Picasso.with(getApplicationContext()).load(pet_data.URLImage).placeholder(R.drawable.loading_image).into(PetInfo_Image);
        // PetInfo_Type.setText(pet_data.Type);
        PetInfo_Price.setText(pet_data.Price + " EGP");
        PetInfo_Gender.setText(pet_data.Gender);
        PetInfo_Name.setText(pet_data.Name);
        PetInfo_Description.setText(pet_data.Description);
        PetInfo_Age.setText(pet_data.Age + " Month");
        view.bringToFront();
        layout.bringToFront();
        //...................................................................... ...................
        PetInfo_Price.bringToFront();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User Data");
        final Query query = reference.orderByChild("Email").equalTo(pet_data.OwnerEmail);
        Log.e("UserData", "hello");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                info = dataSnapshot.getValue(UserInfo.class);

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
                Log.e("datasnap", dataSnapshot.toString());
                if (info.ImageUrl.toString() != null && !info.ImageUrl.toString().equals("")) {
                    Picasso.with(getApplicationContext()).load(info.ImageUrl).placeholder(R.drawable.user).resize(100, 100).into(OwnerInfo_Image);

                }
                OwnerInfo_Image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String email = user.getEmail();
                        if (info.Email.equals(email)) {
                            startActivity(new Intent(getApplicationContext(), MyProfile.class));
                        } else {
                            /// hna hashof law ana 3aml rate lleda5lo da 2abl kda fmsh hib2a fe rate aslun
                            final Intent intent = new Intent(getApplicationContext(), Profile.class);
                            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Rating");
                            Query query1 = reference1.orderByChild("SubmitPerson").equalTo(email);
                            query1.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    Rate rate = dataSnapshot.getValue(Rate.class);
                                    Log.d("opp", dataSnapshot.toString());
                                    if (rate.RatedPerson.equals(info.Email)) {
                                        Log.e("good", "good");
                                        RATE = "TRUE";
                                        x = 1;
                                        return;
                                    } else {
                                        if (x != 1) {
                                            RATE = "FALSE";
                                        }
                                    }
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
                            query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    intent.putExtra("Object", info);
                                    intent.putExtra("RATE", RATE);
                                    startActivity(intent);

                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                        }
                    }
                });
                OwnerInfo_Rating.setRating(Float.parseFloat(info.Rating));
                FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.action_A);
                actionButton.setIcon(R.drawable.ic_phone_black_24dp);
                actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", info.Phone, null)));
                    }
                });
                FloatingActionButton actionButton1 = (FloatingActionButton) findViewById(R.id.action_B);
                actionButton1.setIcon(R.drawable.ic_mail_black_24dp);
                actionButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + info.Email)));
                    }
                });
                FloatingActionsMenu menu = (FloatingActionsMenu) findViewById(R.id.Fab_Menu);
                menu.bringToFront();

                OwnerInfo_Name.setText(info.Name);
                OwnerInfo_Country.setText(info.Country);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(getApplication(), MainApp.class));
        }
        return super.onOptionsItemSelected(item);

    }

}
