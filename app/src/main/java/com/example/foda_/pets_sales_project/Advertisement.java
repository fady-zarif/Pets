package com.example.foda_.pets_sales_project;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Advertisement extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView;

    SwipeRefreshLayout refreshLayout;
    ArrayList<Pet_Data> list1 = new ArrayList<>();
    ArrayList<UserInfo> list2 = new ArrayList<>();
    CustomAdapter adapter;

    @Override
    public void onStart() {
        super.onStart();
        list1.clear();
        list2.clear();

        if (getActivity().getLocalClassName().equals("MainApp")) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference("Advertisement");
           // Pet_Data.parameter = reference.orderByChild(reference.getKey());
            Pet_Data.parameter = reference.orderByPriority();
        }
        Log.e("Paraameeteeer", Pet_Data.parameter.toString());
        Advertisement_Pets(Pet_Data.parameter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_advertisement, container, false);
      try {
          FirebaseDatabase.getInstance().setPersistenceEnabled(true);
      }catch (Exception e)
      {

      }
        return root;
    }

    public void Advertisement_Pets(Query myquery) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Advertisement");

        refreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swiperefresh);
        refreshLayout.setOnRefreshListener(this);


        recyclerView = (RecyclerView) getActivity().findViewById(R.id.MyRcyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(layoutManager);
        adapter = new CustomAdapter(getContext(), list1);

        recyclerView.setAdapter(adapter);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
        ///////////////////////////////////////
        Query usermailquery = myquery;

        usermailquery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("data", dataSnapshot.getValue().toString());
                Pet_Data data = dataSnapshot.getValue(Pet_Data.class);
                list1.add(data);
                Log.e("data", data.Gender);
                adapter.notifyItemInserted(list1.size() - 1);
                refreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.e("data", "hello");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
            }
        });

        usermailquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (list1.size()==0)
                {

                    Toast.makeText(getActivity(), "You Don't make Ad yet ", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
                refreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onRefresh() {

        onStart();

    }
}



/*  usermailquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Pet_Data mydata = snapshot.getValue(Pet_Data.class);
                    String email = mydata.OwnerEmail;
                    Log.e("almail", email);
                    list1.add(mydata);
                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("User Data");
                    Query User_Data = ref1.orderByChild("Email").equalTo(email);
                    User_Data.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                UserInfo info = dataSnapshot1.getValue(UserInfo.class);
                                Log.e("Rating is", info.Rating.toString());
                                list2.add(info);

                                adapter.notifyDataSetChanged();
                                refreshLayout.post(new Runnable() {
                                                       @Override
                                                       public void run() {
                                                           refreshLayout.setRefreshing(false);
                                                       }
                                                   }

                                );

                            }


                            *//*    Collections.reverse(list1);
                                Collections.reverse(list2);
                                adapter.notify();*//*

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    *//*DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("User Data");
                    final Query User_Data = ref1.orderByChild("Email").equalTo(email);
                    User_Data.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                Log.e("Alsnap", snapshot1.toString());
                                UserInfo data = snapshot1.getValue(UserInfo.class);
                                list2.add(data);
                                Log.e("Aldata", data.Rating.toString());
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                   *//*


                }


         *//*       Collections.reverse(list1);
                adapter.notifyDataSetChanged();*//*
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
*/