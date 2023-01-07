package com.aditi.reform2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneNo extends AppCompatActivity {

    Button verify_btn;
    EditText phoneNoEnteredByUser;
    ProgressBar progressBar;
    private FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    String verificationCodeBySystem;

    FirebaseDatabase rootNode;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_no);

        verify_btn= findViewById(R.id.verify_btn);
        phoneNoEnteredByUser= findViewById(R.id.verification_code_entered_by_user);
        progressBar= findViewById(R.id.progress_bar);

        progressBar.setVisibility(View.GONE);

        String phoneNo= getIntent().getStringExtra("phoneNo");
        //Send the code first
        sendVerificationCodeToUser(phoneNo);

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code= phoneNoEnteredByUser.getText().toString();
                if(code.isEmpty() || code.length()<6)
                {
                    phoneNoEnteredByUser.setError("Wrong OTP");
                    phoneNoEnteredByUser.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        });
    }

    private void sendVerificationCodeToUser(String phoneNo) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber("+91"+phoneNo)       // Phone number to verify + INDIA's code=91
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(VerifyPhoneNo.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override//  code also sent to another device & it works every time
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationCodeBySystem=s; //A global link stores the code sent
        }

        @Override// Automatic Checking if the code sent on same device
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) //when verifaction is okay
        {
            String code= phoneAuthCredential.getSmsCode(); //code entered by user inside the text field
            if(code!=null)
            {
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code); //Now call the function to verify the code entered by user.
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) //when verification fails
        {
            Toast.makeText(VerifyPhoneNo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String codeByUser)
    {
        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(verificationCodeBySystem,codeByUser);
        signInTheUserByCredentials(credential);
    }

    private void signInTheUserByCredentials(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(VerifyPhoneNo.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    //Get details from SignUp/Registration Page & register user if task is successful
                    Intent intent= getIntent();
                    String _USERNAME = intent.getStringExtra("username");
                    String _PASSWORD = intent.getStringExtra("password");
                    String _EMAIL=intent.getStringExtra("email");
                    String _PROFESSION=intent.getStringExtra("profession");
                    String _PHONENO=intent.getStringExtra("phoneNo");

                    rootNode= FirebaseDatabase.getInstance();
                    reference=rootNode.getReference("users");

                    UserHelperClass helperClass= new UserHelperClass(_USERNAME,_PHONENO,_EMAIL,_PROFESSION,_PASSWORD);
                    reference.child(_USERNAME).setValue(helperClass); //child allows us to store multiple users & Phone No. acting as a key for each user
                    Toast.makeText(getApplicationContext() ,"Your Account has been successfully created",Toast.LENGTH_SHORT).show();

                    Intent intent2= new Intent(VerifyPhoneNo.this,DummyPage.class);
                    startActivity(intent2);
                }
                else
                {
                    Toast.makeText(VerifyPhoneNo.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}