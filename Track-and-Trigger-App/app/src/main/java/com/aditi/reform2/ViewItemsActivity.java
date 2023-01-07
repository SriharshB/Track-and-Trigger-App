package com.aditi.reform2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlusBuilder;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewItemsActivity extends AppCompatActivity implements ItemAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private ItemAdapter mAdapter;
    Button addButton;

    private FirebaseStorage mStorage;

    private DatabaseReference mDatabaseRef, mDatabaseRef2;
    private ValueEventListener mDBListener;
    private List<Items> mItems;
    String _CATEGORY, _USERNAME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mStorage = FirebaseStorage.getInstance();
        addButton = findViewById(R.id.add_button);

        mItems = new ArrayList<>();

        mAdapter = new ItemAdapter(ViewItemsActivity.this, mItems);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(ViewItemsActivity.this);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("users");

        Intent intent = getIntent();
        _USERNAME=intent.getStringExtra("username");
        _CATEGORY=intent.getStringExtra("category");
        mDatabaseRef2 = mDatabaseRef.child(_USERNAME).child("Categories").child(_CATEGORY);
        mDBListener = mDatabaseRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mItems.clear();
                for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                    Items items = postSnapShot.getValue(Items.class);
                    items.setKey(postSnapShot.getKey());
                    mItems.add(items);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewItemsActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewItemsActivity.this, addItemActivity.class);
                intent.putExtra("username", _USERNAME);
                intent.putExtra("category", _CATEGORY);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Item Selected is : " + mItems.get(position).getItemname(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateClick(int position) {
        Items selectedItem = mItems.get(position);
        String selectedKey = selectedItem.getKey();
        DialogPlus dialogPlus = DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(R.layout.dialogcontent))
                .setExpanded(true, 900)
                .create();
        View myview = dialogPlus.getHolderView();
        //EditText uimagUrl = (EditText)dialogPlus.getHolderView().findViewById(R.id.uimgurl);
        EditText uproduct = (EditText) dialogPlus.getHolderView().findViewById(R.id.update_product);
        EditText uquantity = (EditText) dialogPlus.getHolderView().findViewById(R.id.update_quantity);
        EditText udescription = (EditText) dialogPlus.getHolderView().findViewById(R.id.update_description);
        Button ubutton = (Button) dialogPlus.getHolderView().findViewById(R.id.usubmit);

        uproduct.setText(selectedItem.getItemname());
        //uimagUrl.setText(selectedItem.getItemimage());
        uquantity.setText(selectedItem.getItemquantity());
        udescription.setText(selectedItem.getItemdescription());
        uproduct.setEnabled(false);
        uproduct.setTextColor(Color.BLACK);
        dialogPlus.show();

        ubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<>();
                map.put("itemimage", selectedItem.getItemimage());
                map.put("itemname", uproduct.getText().toString());
                map.put("itemquantity", uquantity.getText().toString());
                map.put("itemdescription", udescription.getText().toString());

                mDatabaseRef2.child(selectedItem.getItemname()).updateChildren(map).
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dialogPlus.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialogPlus.dismiss();
                            }
                        });
            }
        });
    }

    @Override
    public void onDeleteClick(int position) {
        Items selectedItem = mItems.get(position);
        String selectedKey = selectedItem.getKey();
        Toast.makeText(this, selectedItem.getItemname(), Toast.LENGTH_SHORT).show();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getItemimage());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef2.child(selectedItem.getItemname()).removeValue();
                Toast.makeText(ViewItemsActivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onShareClick(int position) {
        Items selectedItem = mItems.get(position);
        String selectedKey = selectedItem.getKey();
        String shareItemName= selectedItem.getItemname();
        String shareItemQuantity=selectedItem.getItemquantity();
        String shareItemDescription=selectedItem.getItemdescription();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBody ="\n\n~~~~~~Item Details ~~~~~\n\nItem Name : "+ shareItemName + "\n\nItem Quantity : " + shareItemQuantity + "\n\nItem Description : "+ shareItemDescription +"\n\nImage :"+ selectedItem.getItemimage()+"\n" ;
        String sharesub= "Organicer";

        shareIntent.putExtra(Intent.EXTRA_SUBJECT,sharesub);
        shareIntent.putExtra(Intent.EXTRA_TEXT,shareBody);

        startActivity(Intent.createChooser(shareIntent,"Share Using"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef2.removeEventListener(mDBListener);
    }
}