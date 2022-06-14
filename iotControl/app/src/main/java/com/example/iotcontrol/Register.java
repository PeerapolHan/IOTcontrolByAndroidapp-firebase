package com.example.iotcontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iotcontrol.Rerofit.IMyService;
import com.example.iotcontrol.Rerofit.RetrofitClient;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class Register extends AppCompatActivity {
    Button btnRegister_register;
    EditText emailtxt_register,passwordtxt_register,nametxt_register;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("REGISTER");

        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        EditText emailtxt_register =(EditText)findViewById(R.id.emailText);
        EditText nametxt_register =(EditText)findViewById(R.id.nameText);
        EditText passwordtxt_register =(EditText)findViewById(R.id.passwordText);

        btnRegister_register = (Button)findViewById(R.id.Register);
        btnRegister_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser(emailtxt_register.getText().toString(),
                        nametxt_register.getText().toString(),
                        passwordtxt_register.getText().toString());

            }
        });
    }

    private void registerUser(String email, String name, String password) {
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Email is Empty",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Name is Empty",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Password is Empty",Toast.LENGTH_SHORT).show();
        }
        compositeDisposable.add(iMyService.registerUser(email, name ,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(Register.this, ""+response, Toast.LENGTH_SHORT).show();
                    }
                }));
    }


    public void Backhome (View v){
        Intent home = new Intent(this , MainActivity.class);
        startActivity(home);
    }
}