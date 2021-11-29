package com.erolakgul.stockhaus.controller.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.erolakgul.stockhaus.R;
import com.erolakgul.stockhaus.core.db.sqlite.users;
import com.erolakgul.stockhaus.models.helpers.helpersForApp;
import com.erolakgul.stockhaus.service.sqlite.servicePoints;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_login, btn_signup;
    private EditText _mail, _password, _name, _surname, _mobile, _repassword;
    private static final int REQUEST_SIGNUP = 0;

    //
    SimpleDateFormat dateFormat;
    // general access
    private servicePoints point;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initViews();
        setListeners();

        // while opening page gets instance from service class
        point = servicePoints.getInstance();
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
        _mail = (EditText) findViewById(R.id.input_email);
        _password = (EditText) findViewById(R.id.input_password);

        _name = (EditText) findViewById(R.id.input_name);
        _surname = (EditText) findViewById(R.id.input_surname);
        _mobile = (EditText) findViewById(R.id.input_mobile);
        _repassword = (EditText) findViewById(R.id.input_reEnterPassword);

        //datetime
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    private void setListeners() {
        btn_login.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
    }

    private void initText() {
        _name.setText("");
        _surname.setText("");
        _mail.setText("");
        _password.setText("");
        _mobile.setText("");
        _repassword.setText("");
    }

    /////////////////////////////////     ACTIONS       ///////////////////////////////////////////
    private void SignUp() {

        if (!helpersForApp.emailOrPasswordValidate(_mail.getText().toString(),_password.getText().toString()))
        {
            Toast.makeText(getApplicationContext(), "" + " Mail Or Password type is wrong...", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean _isSaved = point.get_userRepository().UDM_isUser(_mail.getText().toString(),this);
        // eğer veri db de varsa mail adresi kaydı geri gönder
        if (_isSaved) {
          return;
        }

        // fill the entity
        users entity = new users();

        entity.setName(_name.getText().toString());
        entity.setSurname(_surname.getText().toString());
        entity.setMail(_mail.getText().toString());
        entity.setPassword(_password.getText().toString());
        entity.setPhone_number(_mobile.getText().toString());

        entity.setAge("32"); //Integer.parseInt(editText4.getText().toString());
        entity.setCompany("01");
        entity.setRole("5000");

        entity.setSignable(false);
        entity.setActive(true);
        entity.setOnline(false);
        entity.setChangedBy(entity.getMail());
        entity.setChangedDate(dateFormat.format(Calendar.getInstance().getTime()));
        entity.setCreatedBy(entity.getMail());
        entity.setCreateDate(dateFormat.format(Calendar.getInstance().getTime()));
        entity.setIpAddress(helpersForApp.getIpAddress());

        // fill the entity

        boolean isSuccess = false;

        if (helpersForApp.isEmpty(entity.getName()) || helpersForApp.isEmpty(entity.getSurname()) || helpersForApp.isEmpty(entity.getPhone_number())
                || helpersForApp.isEmpty(entity.getAge())) {
            Toast.makeText(getApplicationContext(), "Uyarı" + " Alanları Boş Bırakmayın..", Toast.LENGTH_SHORT).show();
        } else {
            // activity context i istediği için database işlemleri tüm methodlara eklendi
            isSuccess = point.get_userRepository().Rep_Create(entity, this);
        }


        if (isSuccess) {
            Toast.makeText(getApplicationContext(), "" + " Başarılı..", Toast.LENGTH_SHORT).show();
            initText();
            //FillTheList_Contact();
            Login();
        } else {
            Toast.makeText(getApplicationContext(), "" + " Kaydedilemedi..", Toast.LENGTH_SHORT).show();
        }
        ;

    }


    private void Login() {
        // Start the Signup activity
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
        finish();

        // animation
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }
}
