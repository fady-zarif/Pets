package com.example.foda_.pets_sales_project;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class Main2ActivityFragment extends Fragment {
    TextView myAccount;
    LayoutInflater myinflater;
    EditText AlertEmail, AlertPass, name, password, email, phone;
    Spinner spinner;
    Button AlertSignIn, SignUp;
    ArrayList<String> list;


    public Main2ActivityFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_main2, container, false);
        name = (EditText) root.findViewById(R.id.Name);
        email = (EditText) root.findViewById(R.id.Email);
        password = (EditText) root.findViewById(R.id.Password);
        phone = (EditText) root.findViewById(R.id.Phone);
        spinner = (Spinner) root.findViewById(R.id.countries);
        list = new ArrayList<>();
        list.add("Cairo");
        list.add("Alex");
        list.add("Alfayom");
        list.add("Aldkahlya");
        list.add("Almenya");
        list.add("Asyout");
        list.add("Aswan");
        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.support_simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(R.layout.custom_text);
        spinner.setAdapter(adapter);

        SignUp = (Button) root.findViewById(R.id.SignUp);
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int z = 0;
                if (name.getText().toString().equals("")) {
                    name.setError("Requried");
                    z = 1;
                }
                if (email.getText().toString().equals("")) {
                    email.setError("Required");
                    z = 1;
                }
                if (password.getText().toString().equals("")) {
                    password.setError("Required");
                    z = 1;
                }
                if (password.getText().length() < 8) {
                    password.setError("Must be At Least 8 char");
                    z = 1;
                }
                if (phone.getText().toString().equals("")) {
                    phone.setError("Required");
                    z = 1;
                }
                if (spinner.getSelectedItem().toString() == null ||
                        spinner.getSelectedItem().toString() == "" || spinner.getSelectedItem().toString().equals(null)) {
                    Toast.makeText(getActivity(), "Please Select your Country", Toast.LENGTH_SHORT).show();
                    z = 1;
                }
                if (z == 0) {
                    final ProgressDialog dialog = new ProgressDialog(getContext());
                    dialog.setMessage("Loading ..");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference reference = database.getReference("User Data");
                                        HashMap<String, Object> hashMap = new HashMap<String, Object>();
                                        hashMap.put("Name", name.getText().toString());
                                        hashMap.put("Email", email.getText().toString());
                                        hashMap.put("Password", password.getText().toString());
                                        hashMap.put("Phone", phone.getText().toString());
                                        hashMap.put("Country", spinner.getSelectedItem().toString());
                                        hashMap.put("ImageUrl", "");
                                        hashMap.put("Rating", "0");
                                        reference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(Task<Void> task) {

                                                if (task.isSuccessful()) {

                                                    Toast.makeText(getActivity(), " Congratulation ! \n \n Account Successfully Created", Toast.LENGTH_SHORT).show();
                                                    dialog.setMessage("Signing you in .. ");
                                                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(),
                                                            password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                        @Override
                                                        public void onSuccess(AuthResult authResult) {
                                                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("My Preference", Context.MODE_PRIVATE).edit();
                                                            editor.putBoolean("LogedIn", true);
                                                            editor.commit();
                                                            startActivity(new Intent(getActivity(), MainApp.class));
                                                            dialog.dismiss();
                                                        }
                                                    });

                                                } else {
                                                    Log.e("message", task.getException().toString());
                                                }
                                            }
                                        });
                                    } else {
                                        dialog.dismiss();
                                        Toast.makeText(getActivity(), task.getException().getCause().toString()
                                                , Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });

        myAccount = (TextView) root.findViewById(R.id.Go_My_Account);
        myAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myinflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View rot = inflater.inflate(R.layout.custom_alert, null);

                builder.setView(rot);


                AlertEmail = (EditText) rot.findViewById(R.id.Alert_Email);
                AlertPass = (EditText) rot.findViewById(R.id.Alert_Password);
                AlertSignIn = (Button) rot.findViewById(R.id.Alert_SignIn);
                builder.show();
                AlertSignIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int x = 0;
                        if (AlertEmail.getText().toString().equals("")) {
                            AlertEmail.setError("Required");
                            x = 1;
                        }
                        if (AlertPass.getText().toString().equals("")) {
                            AlertPass.setError("Required");
                            x = 1;
                        }
                        if (x == 0) {
                            final ProgressDialog dialog = new ProgressDialog(getContext());
                            dialog.setMessage("Loading...");
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(AlertEmail.getText().toString(), AlertPass.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(Task<AuthResult> task) {

                                            if (task.isSuccessful()) {
                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                DatabaseReference reference = database.getReference("Advertisement");
                                                //   Pet_Data.parameter = reference.orderByChild(reference.getKey());
                                                Pet_Data.parameter = reference.orderByPriority();

                                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("My Preference", Context.MODE_PRIVATE).edit();
                                                editor.putBoolean("LogedIn", true);
                                                editor.commit();


                                                startActivity(new Intent(getActivity(), MainApp.class));
                                                dialog.dismiss();
                                                Toast.makeText(getContext(), "Successfully Log in .. ", Toast.LENGTH_SHORT).show();


                                            } else {
                                                dialog.dismiss();
                                                Toast.makeText(getContext(), "Error occured" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    }
                });

            }
        });
        return root;
    }
}
