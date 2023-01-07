package com.aditi.reform2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DummyPage extends AppCompatActivity {

    ListView list;
    FirebaseDatabase database;
    DatabaseReference ref;
    String _USERNAME;
    ArrayList<String> categories;
    ArrayAdapter adapter;




// ...



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        categories=  new ArrayList<String>();
        adapter= new ArrayAdapter(DummyPage.this, android.R.layout.simple_list_item_1,categories);
//
        Intent intent= getIntent();
        _USERNAME =intent.getStringExtra("username");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_page);
        database=FirebaseDatabase.getInstance();
        ref =database.getReference().child("users").child(_USERNAME).child("Categories");


        list =(ListView)findViewById(R.id.listView);



        //add category button
        Button addcat;
        addcat = (Button)findViewById(R.id.addCategory);
        addcat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                openAddCategory(_USERNAME);
            }
        });


        //adding listener to the reference
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                adapter.clear();
                System.out.println("lhvj");
                Log.d("test snapshot\n",""+snapshot.getChildren());


                for(DataSnapshot ds : snapshot.getChildren()){

                    System.out.println("enteredfunction1: ");
                    String child=ds.getKey();
                    categories.add(child);
                    adapter.notifyDataSetChanged();
                    list.setAdapter(adapter);}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DummyPage.this,"Can't Access Data",Toast.LENGTH_SHORT).show();
                }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //
                Intent intent= new Intent(DummyPage.this,ViewItemsActivity.class);
                intent.putExtra("username", _USERNAME);
                intent.putExtra("category", categories.get(position));
                Toast.makeText(DummyPage.this,categories.get(position),Toast.LENGTH_SHORT).show();

                //https://github.com/SriharshB/fin1
                startActivity(intent);
            }
        });



    }





    private void openAddCategory(String username) {
       Intent intent= new Intent(DummyPage.this, AddCategory.class);
       intent.putExtra("username",_USERNAME);
       startActivity(intent);


    }


}