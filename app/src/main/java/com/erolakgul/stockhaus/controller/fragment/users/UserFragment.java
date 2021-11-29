package com.erolakgul.stockhaus.controller.fragment.users;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.print.PageRange;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.erolakgul.stockhaus.R;
import com.erolakgul.stockhaus.core.db.sqlite.users;
import com.erolakgul.stockhaus.models.helpers.helpersForApp;
import com.erolakgul.stockhaus.service.sqlite.servicePoints;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class UserFragment extends Fragment implements View.OnClickListener {

    //general access
    private servicePoints point;
    private UserViewModel mViewModel;
    private String user_roles[];
    private String currentUser;
    private String _id;
    private ListView lv;
    private Spinner _sp_role;
    ArrayAdapter role_list;

    private Button btn_add, btn_delete, btn_delete_texts;
    private EditText _name, _surname, _mail, _password;
    SimpleDateFormat dateFormat;

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        View root = inflater.inflate(R.layout.user_fragment, container, false);

        ////////// current user  getActivity() activity class lardaki context in fragmentlerdeki kullanımıdır
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        currentUser = sharedPref.getString("currentUser", "NODATA");

        ///////////////////////////////// fragment üzerindeki işlemler ///////////////////////////
        initViews(root);
        setListeners();
        point = servicePoints.getInstance();

        // size of the array.
        user_roles = new String[6];
        user_roles[0] = "Seçiniz";
        user_roles[1] = "1000";
        user_roles[2] = "2000";
        user_roles[3] = "3000";
        user_roles[4] = "4000";
        user_roles[5] = "5000";

        // Rollerin yer aldığı dropdownlist
        _sp_role = (Spinner) root.findViewById(R.id.spinner_role);

        role_list = new ArrayAdapter(getContext(),
                android.R.layout.simple_spinner_item, user_roles);
        _sp_role.setAdapter(role_list);

        // açılırken doldursun
        fillTheList(getActivity());
        ///////////////////////////////// fragment üzerindeki işlemler ///////////////////////////

        /*
        final TextView textView = root.findViewById(R.id.text_user);
        mViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        return root;
    }

    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddUser:
                Save();
                break;
            case R.id.btnDeleteUser:
                Delete();
                break;
            case R.id.btnDeleteUserText:
                initText();
                break;
        }
    }

    private void initViews(View root) {
        //button
        btn_add = (Button) root.findViewById(R.id.btnAddUser);
        btn_delete = (Button) root.findViewById(R.id.btnDeleteUser);
        btn_delete.setVisibility(View.GONE);
        btn_delete_texts = (Button) root.findViewById(R.id.btnDeleteUserText);

        //TextView
        _name = (EditText) root.findViewById(R.id.txt_name);
        _surname = (EditText) root.findViewById(R.id.txt_surname);
        _mail = (EditText) root.findViewById(R.id.txt_mail);
        _password = (EditText) root.findViewById(R.id.txt_password);

        // list view
        lv = (ListView) root.findViewById(R.id.lstUsers);

        // date format
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //String
        _id = "";
    }

    private void setListeners() {
        btn_add.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_delete_texts.setOnClickListener(this);

        // listelere tıklandığında
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // listview den datanın alınması
                HashMap<String, String> map = (HashMap<String, String>) adapterView.getItemAtPosition(i);
                _id = map.get("id");

                // databaseden verinin alınması
                HashMap<String, String> userClass = point.get_userRepository().Rep_Get_Id(Integer.valueOf(_id), getActivity());
                String name = userClass.get("name");
                String surname = userClass.get("surname");
                String mail = userClass.get("mail"); // listview adapterinde String ismi ne ise öyle çağırılmalı
                String password = userClass.get("password");
                String role = userClass.get("role");

                _name.setText(name);
                _surname.setText(surname);
                _mail.setText(mail);
                _password.setText(password);

                // adapterdeki yerini belirleme
                int spinnerPosition = role_list.getPosition(role);
                _sp_role.setSelection(spinnerPosition);

                btn_delete.setVisibility(View.VISIBLE);
            }
        });

    }

    //////////////////////////////////// textboxlar sıfırlanır  ////////////////////////////////////
    private void initText() {
        _id = "";
        _name.setText("");
        _surname.setText("");
        _mail.setText("");
        _password.setText("");
        _sp_role.setSelection(0);
    }

    private void fillTheList(Context context) {
        ArrayList<HashMap<String, String>> _userList = point.get_userRepository().Rep_GetAll(context);

        // veritabanından gelen isim soyisim verilerini kullanıcı adı olarak birleştireceğiz
        for (HashMap<String, String> item : _userList) {
            String _user = "";
            String _rolex = "";

            // önce bir string içinde isim ve soyismi concat yapıyoruz
            _user = item.get("name") + " " + item.get("surname");
            // sonra hash map e bu veriyi "user" key i altında kaydediyoruz
            item.put("user", _user);

            switch (item.get("role")) {
                case "1000":
                    _rolex = "Admin";
                    break;
                case "2000":
                    _rolex = "";
                    break;
                case "3000":
                    _rolex = "Veri Girişi";
                    break;
                case "4000":
                    _rolex = "Alan Sorumlusu";
                    break;
                case "5000":
                    _rolex = "Sayım Görevlisi";
                    break;
            }

            // alan sorumlusunu çeviriyoruz
            item.put("rolex", _rolex);

            // son olarak arraylist e  bunu kaydediyoruz.
            // _userList.add(item);
        }


        if (_userList != null) {
            ListAdapter adapter = new SimpleAdapter(context, _userList, R.layout.listview_user,
                    new String[]{"id", "user", "mail", "rolex"},// adapter deki string isimleri veritabanı tablosunda kolon isimleri nasıl ise öyle olmalı stockPlace gibi
                    new int[]{R.id.id, R.id.lst_name, R.id.lst_mail, R.id.lst_role} // int değerler ise listview deki textbox id lerinden gelir
            );
            lv.setAdapter(adapter);
        }
    }


    //////////////////////////////////// Mesajlar  ////////////////////////////////////
    private void onSuccess() {
        Toast.makeText(getActivity(), "" + " Başarılı..", Toast.LENGTH_SHORT).show();
        initText();

        // listeyi doldur
        fillTheList(getActivity());
    }

    private void onFailure() {
        Toast.makeText(getActivity(), "" + "Kayıt Başarısız..", Toast.LENGTH_SHORT).show();
    }

    private void Save() {
        boolean _record = false;
        users _user = new users();

        _user.setName(_name.getText().toString());
        _user.setSurname(_surname.getText().toString());
        _user.setPassword(_password.getText().toString());
        _user.setMail(_mail.getText().toString());

        String _role = _sp_role.getSelectedItem().toString();
        _user.setRole(_role);

        _user.setPhone_number("");

        _user.setAge("32"); //Integer.parseInt(editText4.getText().toString());
        _user.setCompany("01");

        _user.setSignable(false);
        _user.setActive(true);
        _user.setOnline(false);
        _user.setChangedBy(_user.getMail());
        _user.setChangedDate(dateFormat.format(Calendar.getInstance().getTime()));
        _user.setCreatedBy(_user.getMail());
        _user.setCreateDate(dateFormat.format(Calendar.getInstance().getTime()));
        _user.setIpAddress(helpersForApp.getIpAddress());

        if (!_user.getName().isEmpty() && !_user.getSurname().isEmpty() && !_user.getMail().isEmpty() && !_user.getPassword().isEmpty()) {

            if (_id.equals("")) {
                _record = point.get_userRepository().Rep_Create(_user, getActivity());
            } else {
                _user.setId(Integer.valueOf(_id));
                _record = point.get_userRepository().Rep_Update(_user, getActivity());
            }

        }

        if (_record) {
            onSuccess();
        } else {
            onFailure();
        }
        ;

    }

    private void Delete() {
        final users _user = new users();
        _user.setId(Integer.valueOf(_id));

        _user.setName(_name.getText().toString());
        _user.setSurname(_surname.getText().toString());
        _user.setMail(_mail.getText().toString());

        if (!_user.getName().isEmpty() && !_user.getSurname().isEmpty() && !_user.getMail().isEmpty()) {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:

                            final boolean _record = point.get_Repository().Rep_Delete("Users", _user, getActivity());

                            if (_record) {
                                onSuccess();
                            } else {
                                onFailure();
                            }
                            ;

                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            Toast.makeText(getContext(), "İptal Edildi.", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Silmek istediğinize emin misiniz?").setPositiveButton("Evet", dialogClickListener)
                    .setNegativeButton("Hayır", dialogClickListener).show();


        }

    }
}
