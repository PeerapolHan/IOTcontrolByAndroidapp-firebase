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

public class MainActivity extends AppCompatActivity {
    EditText emailtxt_login,passwordtxt_login;
    Button btnLogin_login,btnRegister_login;

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
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("LOGIN");

        //init service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);

        //initview

        emailtxt_login = (EditText)findViewById(R.id.emailText_login);
        passwordtxt_login = (EditText)findViewById(R.id.passwordText_login);

        btnLogin_login = (Button)findViewById(R.id.buttonLogin_login);
        btnLogin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(emailtxt_login.getText().toString(),
                        passwordtxt_login.getText().toString());
            }
            //startActivity(new Intent(MainActivity.this, iotControl.class));

        });
    }

    public void loginUser(String email, String password) {
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Email is Empty",Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Password is Empty",Toast.LENGTH_SHORT).show();
        }

        compositeDisposable.add(iMyService.loginUser(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(MainActivity.this, ""+response, Toast.LENGTH_SHORT).show();
                        if((""+response).equalsIgnoreCase("\"Login Success\"")){
                            startActivity(new Intent(MainActivity.this, iotControl.class));
                        }
                    }
                }));

    }
    public void controlP(View v) {
        Intent iot = new Intent(MainActivity.this,iotControl.class);
        startActivity(iot);
    }

    public void regisButton (View v){
        Intent regis = new Intent(this , Register.class);
        startActivity(regis);
    }

}