package com.aditi.reform2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    Button callSignUp,go_button;
    TextInputLayout username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Setting and storing all texts
        username= findViewById(R.id.username);
        password=findViewById(R.id.password);

        callSignUp= findViewById(R.id.signup_button);
        callSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,SignUp.class);
                startActivity(intent);
            }
        });

        go_button=findViewById(R.id.go_button);
        go_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginUser(go_button);
            }
        });
    }

    private Boolean validateUsername()
    {
        String val =username.getEditText().getText().toString();
        if(val.isEmpty())
        {
            username.setError("Field can't be Empty");
            return false;
        }
        else
        {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validatePassword()
    {
        String val =password.getEditText().getText().toString();
        if(val.isEmpty())
        {
            password.setError("Field can't be Empty");
            return false;
        }
        else
        {
            password.setError(null);
            return true;
        }
    }

    public void loginUser(View view)
    {
        if(!validateUsername() || !validatePassword())
        {
            return;
        }
        else
        {
            isUser();
        }

    }

    private void isUser() {

        String userEnteredUsername= username.getEditText().getText().toString().trim();
        String userEnteredPassword= password.getEditText().getText().toString().trim();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users"); //Reference now pointing to users

        Query checkUser= reference.orderByChild("username").equalTo(userEnteredUsername); //looks for 'username' in UIDs

        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    username.setError(null);
                    username.setErrorEnabled(false);

                    String passwordFromDB=dataSnapshot.child(userEnteredUsername).child("password").getValue(String.class);

                    if(passwordFromDB.equals(userEnteredPassword))
                    {
                        password.setError(null);
                        password.setErrorEnabled(false);

                        String emailFromDB=dataSnapshot.child(userEnteredUsername).child("email").getValue(String.class);
                        String phoneNoFromDB=dataSnapshot.child(userEnteredUsername).child("phoneNo").getValue(String.class);
                        String professionFromDB=dataSnapshot.child(userEnteredUsername).child("profession").getValue(String.class);
                        String usernameFromDB=dataSnapshot.child(userEnteredUsername).child("username").getValue(String.class);

                        Intent intent= new Intent(Login.this,DummyPage.class);

                        intent.putExtra("username",usernameFromDB); //keyName and the value passed to UpdateProfile
                        intent.putExtra("email",emailFromDB);
                        intent.putExtra("profession",professionFromDB);
                        intent.putExtra("password",passwordFromDB);

                        startActivity(intent);
                    }
                    else //means password doesn't match
                    {
                        password.setError("Wrong Password");
                        password.requestFocus();
                    }
                }
                else //no such user in snapshot
                {
                    username.setError("No such user exists");
                    password.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}