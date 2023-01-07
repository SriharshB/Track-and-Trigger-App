package com.aditi.reform2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateProfile extends AppCompatActivity {

    //Global variables that will hold the user data inside this UpdateProfile activity
    String _USERNAME,_PASSWORD,_EMAIL,_PROFESSION;

    Button update_btn,next_btn;
    TextInputEditText username,password,email,profession;
    TextView usernameLabel;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        reference= FirebaseDatabase.getInstance().getReference("users");//reference now has list of all users

        usernameLabel=findViewById(R.id.usernameLabel);
        username=findViewById(R.id.username_profile);
        password=findViewById(R.id.password_profile);
        email=findViewById(R.id.email_profile);
        profession=findViewById(R.id.profession_fill);

        update_btn=findViewById(R.id.update_btn);
        next_btn=findViewById(R.id.next_btn);

        showAllUserData();

        update_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                update(update_btn);
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Intent intent2= new Intent(UpdateProfile.this,DummyPage.class);
                startActivity(intent2);
            }
        });
    }

    public void showAllUserData() //Function to display all the data in UserProfile after fetching data from database
    {
        Intent intent= getIntent();
        _USERNAME=intent.getStringExtra("username");
        _PASSWORD=intent.getStringExtra("password");
        _EMAIL=intent.getStringExtra("email");
        _PROFESSION=intent.getStringExtra("profession");

        ((TextView)findViewById(R.id.usernameLabel)).setText(_USERNAME);
        ((TextInputEditText)findViewById(R.id.username_profile)).setText(_USERNAME);
        ((TextInputEditText)findViewById(R.id.password_profile)).setText(_PASSWORD);
        ((TextInputEditText)findViewById(R.id.email_profile)).setText(_EMAIL);
        ((TextInputEditText)findViewById(R.id.profession_profile)).setText(_PROFESSION);
    }

    public void update(View view)
    {
        if(isNameChanged() || isPasswordChanged())
        {
            Toast.makeText(this, "Data has been changed", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "Data is Same, can't be updated", Toast.LENGTH_LONG).show();
        }

    }
//DON'T TOUCH/PLAY WITH THIS
    private boolean isPasswordChanged() {

        if( !_PASSWORD.equals(password.getText().toString()) )
        {
            //update the password and return true
           // reference.child(_USERNAME).child("password").setValue(password.getText().toString()); //update the value of username
           // _PASSWORD =password.getText().toString();
            return true;
        }
        else
        {
            return false;
        }
    }
//DON'T TOUCH/PLAY WITH THIS
    private boolean isNameChanged() {

        if( !_USERNAME.equals(username.getText().toString()) )
        {
            //update the username and return true
            //reference.child(_USERNAME).child("username").setValue(username.getText().toString()); //update the value of username
            //_USERNAME =username.getText().toString();
            return true;
        }
        else
        {
            return false;
        }
    }
}