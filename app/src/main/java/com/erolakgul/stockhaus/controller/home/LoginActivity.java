package com.erolakgul.stockhaus.controller.home;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.erolakgul.stockhaus.R;
import com.erolakgul.stockhaus.models.helpers.helpersForApp;
import com.erolakgul.stockhaus.service.sqlite.servicePoints;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_login, btn_signup;
    private EditText _username, _password;

    private static final int REQUEST_SIGNUP = 0;

    //general access
    private servicePoints point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setListeners();

        point = servicePoints.getInstance();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                Login();
                break;
            case R.id.btn_signup:
                SignUp();
                break;
        }
    }

    private void initViews() {
        //button
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_signup = (Button) findViewById(R.id.btn_signup);

        //TextView
        _username = (EditText) findViewById(R.id.input_email);
        _password = (EditText) findViewById(R.id.input_password);
    }

    private void setListeners() {
        btn_login.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
    }

    private void initText() {
        _username.setText("");
        _password.setText("");
    }

    /////////////////////////////////     ACTIONS       ///////////////////////////////////////////
    private void Login() {

        final String email = _username.getText().toString();
        final String password = _password.getText().toString();

        boolean valid = helpersForApp.emailOrPasswordValidate(email, password);

        /* EAKGUL 23122019 KAPATILDI, SONRA TEKRAR AÇILACAK
        if (!valid) {
            onLoginFailed("Mail Address");
            return;
        }*/

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        if (!onLoginSuccess(email,password)) {
                            onLoginFailed("Email is not saved!!");
                        };
                        progressDialog.dismiss();
                    }
                }, 500); /*3000 YAPILACAK DİĞER KAPATILAN KISIMLAR AÇILINCA*/
    }

    private void onLoginFailed(String str) {
        Toast.makeText(getBaseContext(), "Login failed :" + str, Toast.LENGTH_LONG).show();

        btn_login.setEnabled(true);
    }

    private boolean onLoginSuccess(String mail,String password) {

        boolean _logging = point.get_userRepository().UDM_canLogin(mail,password,this);

        /* EAKGUL 23122019 KAPATILDI
        if (!_logging){
            return false;
        };
        */

        //////////// login olan kullanıcı bilgileri ////////////
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        //editor.putInt("intValue",value); //int değer ekleniyor
        editor.putString("currentUser",mail); //string değer ekleniyor
        //editor.putBoolean("isChecked",isChecked); //boolean değer ekleniyor
        editor.apply(); //Kayıt
        ////////////

        btn_login.setEnabled(true);

        // login olabildiyse bağlan
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("info", "OK");
        startActivity(intent);

        finish();
        return true;
    }


    private void SignUp() {
        // Start the Signup activity
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
        finish();

        // animation
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}

