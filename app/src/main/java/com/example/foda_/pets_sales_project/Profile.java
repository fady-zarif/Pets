package com.example.foda_.pets_sales_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {
    TextView PersonName, PersonEmail;
    RatingBar PersonRating, RatePerson;
    Button button;
    ImageView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final UserInfo info = getIntent().getExtras().getParcelable("Object");
        String RATE=getIntent().getStringExtra("RATE");

        Log.e("Rate",RATE);
        PersonName = (TextView) findViewById(R.id.Person_Name);
        PersonEmail = (TextView) findViewById(R.id.Person_Email);
        PersonRating = (RatingBar) findViewById(R.id.Person_Rating);
        final LinearLayout layout = (LinearLayout) findViewById(R.id.profileLinear);
        RatePerson = (RatingBar) findViewById(R.id.Rate_Person);
        button = (Button) findViewById(R.id.Submit_Rate);
        view = (ImageView) findViewById(R.id.Image);

        FloatingActionsMenu menu = (FloatingActionsMenu) findViewById(R.id.Fab_Menu);
        FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.action_A);
        actionButton.setIcon(R.drawable.ic_phone_black_24dp);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", info.Phone, null));
                startActivity(intent);
            }
        });
        FloatingActionButton actionButton2 = (FloatingActionButton) findViewById(R.id.action_B);
        actionButton2.setIcon(R.drawable.ic_mail_black_24dp);
        actionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + info.Email));
                startActivity(Intent.createChooser(intent, "Send Email Via "));
            }
        });


        PersonName.setText(info.Name.toString());
        final ProgressDialog dialog = new ProgressDialog(this);

        dialog.setMessage("Submiting Your Rate");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Advertisement");
        Query query = reference.orderByChild("OwnerEmail").equalTo(info.Email);
        Pet_Data.parameter = query;
        getSupportFragmentManager().beginTransaction().add(R.id.profileFrame, new Advertisement()).commit();
        if (RATE.equals("TRUE"))
        {
            layout.setVisibility(View.GONE);
        }
        if (info.ImageUrl != null && !info.ImageUrl.equals("")) {
            Picasso.with(getApplicationContext()).load(info.ImageUrl).resize(200, 200).into(view);
        }
        PersonEmail.setText(info.Email.toString());
        PersonRating.setRating(Integer.parseInt(info.Rating));


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RatePerson.getProgress() == 0) {
                    Toast.makeText(Profile.this, "Please Select Rate First ", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    Rate rate = new Rate(info.Email.toString(), user.getEmail().toString(), (int) RatePerson.getRating());
                    dialog.show();
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Rating");
                    reference.push().setValue(rate).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {

                            if (task.isSuccessful()) {

                                Query MyQuery = reference.orderByChild("RatedPerson").equalTo(info.Email.toString());
                                Log.e("Email", info.Email.toString());
                                MyQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        int x = 0;
                                        int count = 0;
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            Rate rate = dataSnapshot1.getValue(Rate.class);
                                            x += rate.Rate;
                                            Log.e("MYRATE", String.valueOf(x));
                                            count++;
                                            Log.e("Counter", String.valueOf(count));
                                            int y = x / count;
                                            Log.e("result", String.valueOf(y));
                                            Rate.FinalRate = String.valueOf(y);
                                            Log.e("Final_Rate", Rate.FinalRate);
                                        }


                                        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User Data");
                                        Query query = ref.orderByChild("Email").equalTo(info.Email.toString());

                                        query.addValueEventListener(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    String mykey = snapshot.getKey();

                                                    ref.child(mykey).child("Rating").setValue(Rate.FinalRate);

                                                }
                                                layout.setVisibility(View.GONE);
                                                dialog.dismiss();
                                                Toast.makeText(Profile.this, "Good", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                dialog.dismiss();
                                                Toast.makeText(Profile.this, "bad", Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            }/// if successfully
                        }
                    });
                }

            }
        });


    }

}
