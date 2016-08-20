package com.example.foda_.pets_sales_project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

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

import java.util.ArrayList;

/**
 * Created by foda_ on 2016-07-16.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder> {
    Context context;
    ArrayList<Pet_Data> petdata;
    UserInfo MyInfo;
    UserInfo info;
    int x = 0;
    public String RATE = "";
    private int lastPosition = -1;
    private final static int FADE_DURATION = 500;

    public CustomAdapter(Context context, ArrayList<Pet_Data> petdata) {
        this.context = context;
        this.petdata = petdata;

    }


    public class Holder extends RecyclerView.ViewHolder {
        public ImageView Pet_Picture;
        public TextView Pet_Cost, Pet_Age, Pet_Gender, Pet_Type, Pet_Date;
        public ImageButton personImage;
        public RatingBar ratingBar;
        public CardView cardView;

        public Holder(View itemView) {
            super(itemView);
            Pet_Picture = (ImageView) itemView.findViewById(R.id.Pet_Image);
            Pet_Cost = (TextView) itemView.findViewById(R.id.Pet_Price);
            Pet_Gender = (TextView) itemView.findViewById(R.id.Pet_Gender);
            Pet_Type = (TextView) itemView.findViewById(R.id.Pet_Type);
            Pet_Date = (TextView) itemView.findViewById(R.id.Pet_Date);
            Pet_Age = (TextView) itemView.findViewById(R.id.Pet_Age);
            ratingBar = (RatingBar) itemView.findViewById(R.id.Rating_View);
            personImage = (ImageButton) itemView.findViewById(R.id.PersonIcon);
            cardView = (CardView) itemView.findViewById(R.id.card);

        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View root = inflater.inflate(R.layout.cardview2, parent, false);
        Holder holder = new Holder(root);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.cardView.setElevation(25);
            holder.cardView.setRadius(20);
            holder.cardView.setPreventCornerOverlap(false);
        } else {
            holder.cardView.setCardBackgroundColor(R.drawable.viewshadow);

        }

        return holder;
    }

    @Override
    public void onViewDetachedFromWindow(Holder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        if (!context.getClass().getName().substring(37, context.getClass().getName().length()).equals("MainApp")) {
            holder.personImage.setVisibility(View.GONE);
            holder.ratingBar.setVisibility(View.GONE);

        } else {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User Data");
            Query query = reference.orderByChild("Email").equalTo(petdata.get(position).OwnerEmail);
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    MyInfo = dataSnapshot.getValue(UserInfo.class);

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
                    holder.ratingBar.setRating(Integer.parseInt(MyInfo.Rating));
                    if (MyInfo.ImageUrl != null && !MyInfo.ImageUrl.equals("")) {
                        Picasso.with(context)
                                .load(MyInfo.ImageUrl)
                                .placeholder(R.drawable.user)
                                .resize(200, 200)
                                .into(holder.personImage);
                    } else {
                        holder.personImage.setImageResource(R.drawable.user);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //  Log.e("pathImage", petdata.get(position).UserImage.toString());
            holder.personImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User Data");
                    final Query query = reference.orderByChild("Email").equalTo(petdata.get(position).OwnerEmail);
                    final ProgressDialog dialog = new ProgressDialog(context);
                    dialog.setMessage("loading");
                    dialog.show();
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
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String email = user.getEmail();
                            if (info.Email.equals(email)) {
                                Log.e("Current User", email);
                                Log.e(" User", info.Email);
                                context.startActivity(new Intent(context, MyProfile.class));
                                dialog.dismiss();
                            } else {
                                /// hna hashof law ana 3aml rate lleda5lo da 2abl kda fmsh hib2a fe rate aslun
                                final Intent intent = new Intent(context, Profile.class);
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
                                query1.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        intent.putExtra("Object", info);
                                        intent.putExtra("RATE", RATE);
                                        context.startActivity(intent);
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });

        }
        holder.Pet_Type.setText(petdata.get(position).Type.toString());
        holder.Pet_Gender.setText(petdata.get(position).Gender.toString());
        holder.Pet_Cost.setText(petdata.get(position).Price.toString() + " L.E");
        holder.Pet_Age.setText(petdata.get(position).Age.toString() + " Month");
        holder.Pet_Date.setText(petdata.get(position).Date.toString());
        Log.e("MYCLASSNAME", context.getClass().getName().substring(37, context.getClass().getName().length()));


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PetInformation.class);

                // intent.putParcelableArrayListExtra("MyParcelable",petdata.);
                intent.putExtra("MyParcelabale", petdata.get(position));
                context.startActivity(intent);

            }
        });
        Picasso.with(context)
                .load(petdata.get(position).URLImage).placeholder(R.drawable.loading_image).resize(200, 200)
                .into(holder.Pet_Picture);

        setAnimation(holder.itemView, position);
        //  setScaleAnimation(holder.itemView);
        //  setFadeAnimation(holder.itemView);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public int getItemCount() {
        return petdata.size();
    }

/*      private void setFadeAnimation(View view) {
          AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
          anim.setDuration(FADE_DURATION);
          view.startAnimation(anim);
      }

      private void setScaleAnimation(View view) {
          ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

          anim.setDuration(FADE_DURATION);
          view.startAnimation(anim);
      }
    */


    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
