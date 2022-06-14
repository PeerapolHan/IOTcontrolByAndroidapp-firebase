package com.example.iotcontrol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class iotControl extends AppCompatActivity {

    Button on;
    Button off;
    Button left;
    Button right;
    TextView ldr, onOroff,leftOrright;
    DatabaseReference dref;
    String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iot_control);
        getSupportActionBar().setTitle("IOTCONTROL");


        on = (Button) findViewById(R.id.ONbutton);
        off = (Button) findViewById(R.id.OFFbutton);
        left = (Button) findViewById(R.id.Leftbutton);
        right = (Button) findViewById(R.id.Rightbutton);
        ldr = (TextView) findViewById(R.id.ldrtxt);
        onOroff = (TextView) findViewById(R.id.nORf);
        leftOrright = (TextView) findViewById(R.id.rORl);

        dref= FirebaseDatabase.getInstance().getReference();
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                status=dataSnapshot.child("Node1/ldr").getValue().toString();
                ldr.setText(status);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        left.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Node1/motor");
                myRef.setValue(1);
                leftOrright.setText("CLOSE");
            }
        });
        right.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Node1/motor");
                myRef.setValue(0);
                leftOrright.setText("OPEN");
            }
        });
//        on.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference myRef = database.getReference("Node1/btn");
//                DatabaseReference myRef2 = database.getReference("Node1/btna");
//                myRef.setValue(0);
//                myRef2.setValue(0);
//                onOroff.setText("ON");
//            }
//        });
//        off.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference myRef = database.getReference("Node1/btn");
//                DatabaseReference myRef2 = database.getReference("Node1/motor");
//                myRef.setValue(3);
//                myRef2.setValue(3);
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_iot,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logutOpt:
                startActivity(new Intent(iotControl.this, MainActivity.class));
                Toast.makeText(iotControl.this, "Logout Success", Toast.LENGTH_SHORT).show();
                break;
//            case R.id.editnameOpt:
//                startActivity(new Intent(iotControl.this, editAnddelete.class));
//                Toast.makeText(iotControl.this, "editAnddelete", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.graphOpt:
//                startActivity(new Intent(iotControl.this, graph.class));
//                Toast.makeText(iotControl.this, "Graph", Toast.LENGTH_SHORT).show();
//                break;
        }
        return super.onOptionsItemSelected(item);
    }
}