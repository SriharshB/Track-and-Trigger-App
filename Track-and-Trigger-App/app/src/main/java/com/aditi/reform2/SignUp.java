package com.aditi.reform2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    TextInputLayout regUserName,regEmail,regPhoneNo,regProfession,regPassword;
    Button regBtn,callLogin;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Setting and storing all texts
        regUserName= findViewById(R.id.username_fill);
        regEmail=findViewById(R.id.emailID_fill);
        regPhoneNo=findViewById(R.id.phoneno_fill);
        regProfession=findViewById(R.id.profession_fill);
        regPassword=findViewById(R.id.pass_fill);

        //Method to go from register page to login page
        callLogin= findViewById(R.id.callLogin_button);
        callLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUp.this,Login.class);
                startActivity(intent);
            }
        });

        regBtn = (Button)findViewById(R.id.reg_btn);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //rootNode= FirebaseDatabase.getInstance();
                //reference=rootNode.getReference("users");
                registerUser(regBtn);
            }
        });
    }

    private Boolean validateUsername()
    {
        String val =regUserName.getEditText().getText().toString();
        String noWhiteSpace= "(?=\\s+$)";
        if(val.isEmpty())
        {
            regUserName.setError("Field can't be Empty");
            return false;
        }
        else if(val.length()>20)
        {
            regUserName.setError("Username is too long");
            return false;
        }
        else if(val.matches(noWhiteSpace))
        {
            regUserName.setError("Whitespace not allowed");
            return false;
        }
        else
        {
            regUserName.setError(null);
            regUserName.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean validateEmail()
    {
        String val =regEmail.getEditText().getText().toString();
        String emailPattern = "^[a-zA-Z0-9]+@[a-z]+\\.+[a-z]+$";

        if(val.isEmpty())
        {
            regEmail.setError("Field can't be Empty");
            return false;
        }
        else if(!val.matches(emailPattern))
        {
            regEmail.setError("Invalid Email Address");
            return false;
        }
        else
        {
            regEmail.setError(null);
            return true;
        }
    }
    private Boolean validatePhoneNo()
    {
        String val =regPhoneNo.getEditText().getText().toString();
        String phonePattern= "^[0-9]{10}$";

        if(val.isEmpty())
        {
            regPhoneNo.setError("Field can't be Empty");
            return false;
        }
        else if(!val.matches(phonePattern))
        {
            regPhoneNo.setError("Phone No. must be 10 digits");
            return false;
        }
        else
        {
            regPhoneNo.setError(null);
            return true;
        }
    }
    private Boolean validatePassword()
    {
        String val =regPassword.getEditText().getText().toString();
        String passVal= "^"
        + "(?=.*[a-zA-Z])" //At least 1 letter
        + "(?=.*[@!~#$%^&*+=_-])" //At least 1 special character
        + ".{6,}" //Atleast 6 characters
        + "$" ;

        if(val.isEmpty())
        {
            regPassword.setError("Field can't be Empty");
            return false;
        }
        else if(!val.matches(passVal))
        {
            regPassword.setError("Password is weak");
            return false;
        }
        else
        {
            regPassword.setError(null);
            return true;
        }
    }
    private Boolean validateProfession()
    {
        String val =regProfession.getEditText().getText().toString();

        if(val.isEmpty())
        {
            regProfession.setError("Field can't be Empty");
            return false;
        }
        else
        {
            regProfession.setError(null);
            return true;
        }
    }

    //This function saves the data into firebase on clicking the GO button
    public void registerUser(View view)
    {
        //Validate all the fields first
        if(!validateUsername() ||!validateEmail() ||!validatePassword() ||!validatePhoneNo() ||!validateProfession())
        {
            return;
        }

        //else go on, register them

        String username =regUserName.getEditText().getText().toString();
        String email =regEmail.getEditText().getText().toString();
        String phoneNo =regPhoneNo.getEditText().getText().toString();
        String profession =regProfession.getEditText().getText().toString();
        String password =regPassword.getEditText().getText().toString();


        //Send the details to VerifyPhoneNo. activity to finally register in database after phone verifcation
        Intent intent= new Intent(SignUp.this,VerifyPhoneNo.class);
        intent.putExtra("phoneNo",phoneNo); //send this phone no. to verify phone no. activity
        intent.putExtra("email",email);
        intent.putExtra("username",username);
        intent.putExtra("profession",profession);
        intent.putExtra("password",password);
        startActivity(intent);

        //UserHelperClass helperClass= new UserHelperClass(username,phoneNo,email,profession,password);
       // reference.child(username).setValue(helperClass); //child allows us to store multiple users & Phone No. acting as a key for each user

      //Toast.makeText(this,"Your Account has been successfully created",Toast.LENGTH_SHORT).show();
      //Intent intent2= new Intent(SignUp.this,Login.class);
      //startActivity(intent2);
      //finish();

    }
}