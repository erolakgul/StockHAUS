package com.erolakgul.stockhaus.controller.fragment.material;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
import android.widget.Toast;

import com.erolakgul.stockhaus.R;
import com.erolakgul.stockhaus.controller.fragment.material.detail.MaterialDetailFragment;
import com.erolakgul.stockhaus.core.db.sqlite.materials;
import com.erolakgul.stockhaus.models.helpers.helpersForApp;
import com.erolakgul.stockhaus.service.sqlite.servicePoints;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MaterialFragment extends Fragment implements View.OnClickListener,MaterialDetailFragment.OnSavedAfter {

    private MaterialViewModel mViewModel;
    private String _id;
    private ListView lv;
    //general access
    private servicePoints point;

    private Button btn_detail;
    private static final int REQUEST_SIGNUP = 0;
    SimpleDateFormat dateFormat;


    public static MaterialFragment newInstance() {
        return new MaterialFragment();
    }

    public View onCreateView(final @NonNull LayoutInflater inflater,
                            final ViewGroup container,final Bundle savedInstanceState) {

        mViewModel = ViewModelProviders.of(this).get(MaterialViewModel.class);

        View root = inflater.inflate(R.layout.material_fragment, container, false);

        initViews(root);
        point = servicePoints.getInstance();
        setListeners();
        // fill the openning
        fillTheList(getActivity());
        ///////////////////////////////// fragment process ///////////////////////////

        return root;
    }

    /*
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MaterialViewModel.class);
        // TODO: Use the ViewModel
    }*/

    private void initViews(View root) {
        //button
        btn_detail = (Button) root.findViewById(R.id.btnDetail);
        // list view
        lv = (ListView) root.findViewById(R.id.lstMaterials);
        //String
        _id = "";

    }

    private void setListeners() {
        btn_detail.setOnClickListener(this);

        // listelere tıklandığında
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HashMap<String, String> map = (HashMap<String, String>) adapterView.getItemAtPosition(i);
                String _idn = map.get("id");
                //////////// login olan kullanıcı bilgileri ////////////
                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                //editor.putInt("intValue",value); //int değer ekleniyor
                editor.putString("id",_idn); //string değer ekleniyor
                //editor.putBoolean("isChecked",isChecked); //boolean değer ekleniyor
                editor.apply(); //Kayıt


                // Create fragment and give it an argument specifying the article it should show
                MaterialDetailFragment materialDetailFragment = new MaterialDetailFragment();
                materialDetailFragment.setTargetFragment(MaterialFragment.this,1);
                materialDetailFragment.show(getFragmentManager(), "MaterialDetailFragment");
            }
        });

    }

    private void initText() {
        _id = "";
    }

    private void fillTheList(Context context) {
        ArrayList<HashMap<String, String>> _matList = point.get_materialRepository().Rep_GetAll(context);


        for (HashMap<String, String> item : _matList) {
            // if there is record what want to be changed
            String _warestock = item.get("section") + "/" + item.get("stockPlace");
            item.put("warestock", _warestock);
        }
        ;

        if (_matList != null) {
            ListAdapter adapter = new SimpleAdapter(context, _matList, R.layout.listview_material,
                    new String[]{"id", "code", "name", "warestock"},// adapter deki string isimleri veritabanı tablosunda kolon isimleri nasıl ise öyle olmalı stockPlace gibi
                    new int[]{R.id.id, R.id.lst_code, R.id.lst_name, R.id.lst_warestock} // int değerler ise listview deki textbox id lerinden gelir
            );
            lv.setAdapter(adapter);
        }
        ;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDetail:
                accessToDetail();
                break;
        }
    }

    /// materials detail interface inden çağırılıyor
    @Override
    public void listMaterials() {
        fillTheList(getContext());
    }

    private void accessToDetail() {
        //////////// login olan kullanıcı bilgileri ////////////
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        //editor.putInt("intValue",value); //int değer ekleniyor
        editor.putString("id","NODATA"); //string değer ekleniyor
        //editor.putBoolean("isChecked",isChecked); //boolean değer ekleniyor
        editor.apply(); //Kayıt

        // Create fragment and give it an argument specifying the article it should show
        MaterialDetailFragment materialDetailFragment = new MaterialDetailFragment();
        materialDetailFragment.setTargetFragment(MaterialFragment.this,1);
        materialDetailFragment.show(getFragmentManager(), "MaterialDetailFragment");
    }

}
