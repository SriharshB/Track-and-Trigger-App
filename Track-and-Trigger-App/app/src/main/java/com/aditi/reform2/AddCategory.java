package com.aditi.reform2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddCategory extends AppCompatActivity {
    EditText name,description;
    ListView list;
    FirebaseDatabase database;
    DatabaseReference ref;
    String _USERNAME;
    Button add, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent= getIntent();
        _USERNAME =intent.getStringExtra("username");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog);
        add =(Button)findViewById(R.id.layout_button2);
        cancel =(Button)findViewById(R.id.layout_button3);
        name=(EditText)findViewById(R.id.layout_dialog_categoryname);
        description =(EditText)findViewById(R.id.layout_dialog_description);
        database=FirebaseDatabase.getInstance();
        ref=database.getReference().child("users").child(_USERNAME).child("Categories");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String categorystr =name.getText().toString().trim();
                String descr =description.getText().toString().trim();

                if (categorystr.equals("")){
                    //TODO add warning code
                }
                else{
                    ref.child(categorystr).setValue(descr).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                //Task was successful, data written!
                                Toast.makeText(AddCategory.this, "Data saved!", Toast.LENGTH_SHORT).show();
                            }else{
                                //Task was not successful,
                                Toast.makeText(AddCategory.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                                //Log the error message
                                Log.e("cantdoit", "onComplete: ERROR: " + task.getException().getLocalizedMessage() );
                            }
                            Intent intent= new Intent(AddCategory.this, DummyPage.class);
                            intent.putExtra("username",_USERNAME);
                            startActivity(intent);

                        }
                    });

                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(AddCategory.this, DummyPage.class);
                intent.putExtra("username",_USERNAME);
                startActivity(intent);
            }
        });


    }
}