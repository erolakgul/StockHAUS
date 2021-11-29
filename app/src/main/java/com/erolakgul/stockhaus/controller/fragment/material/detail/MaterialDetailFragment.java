package com.erolakgul.stockhaus.controller.fragment.material.detail;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.erolakgul.stockhaus.controller.fragment.material.MaterialFragment;
import com.erolakgul.stockhaus.core.db.sqlite.materials;
import com.erolakgul.stockhaus.models.helpers.helpersForApp;
import com.erolakgul.stockhaus.service.sqlite.servicePoints;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MaterialDetailFragment extends DialogFragment implements View.OnClickListener {

    private String currentUser;
    private String _array_spec_stock[], _array_ware[], _array_stockplace[], _array_units[];
    private String _id;
    private Spinner _sp_spec_stock, _sp_warehouse, _sp_stockplace, _sp_units;
    ArrayAdapter _adap_specstock, _adap_warehouse, _adap_stockplace, _adap_units;

    //general access
    private servicePoints point;

    private Button btn_add, btn_delete, btn_delete_text, btn_back;
    private EditText _matcode, _description, _serialnumber, _batchnum, _quantity;
    private static final int REQUEST_SIGNUP = 0;
    SimpleDateFormat dateFormat;


    private MaterialDetailViewModel mViewModel;

    public static MaterialDetailFragment newInstance() {
        return new MaterialDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this).get(MaterialDetailViewModel.class);

        View root = inflater.inflate(R.layout.material_detail_fragment, container, false); //, container, false

        initViews(root);
        point = servicePoints.getInstance();

        ////////// current user  getActivity() activity class lardaki context in fragmentlerdeki kullanımıdır
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        currentUser = sharedPref.getString("currentUser", "NODATA");
        _id = sharedPref.getString("id", "NODATA");

        ///////////////////////////////// fragment process ///////////////////////////

        ///////////////////////////////////////////// fill the spinner //////////////////////////////////////////
        // size of array for units
        _array_units = new String[4];
        _array_units[0] = "Seç";
        _array_units[1] = "AD";
        _array_units[2] = "KG";
        _array_units[3] = "MT";

        //dropdown for units
        _sp_units = (Spinner) root.findViewById(R.id.spinner_units);
        _adap_units = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, _array_units);
        _sp_units.setAdapter(_adap_units);

        // size of the array for special stock
        _array_spec_stock = new String[3];
        _array_spec_stock[0] = "Seç";
        _array_spec_stock[1] = "*";
        _array_spec_stock[2] = "2";
        // dropdownlist for roles
        _sp_spec_stock = (Spinner) root.findViewById(R.id.spinner_spec_stock);
        _adap_specstock = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, _array_spec_stock);
        _sp_spec_stock.setAdapter(_adap_specstock);

        // size of array for warehouse
        _array_ware = point.get_storeRepository().UDM_getWarehouse(getContext());
        // dropdownlist for warehouse
        _sp_warehouse = (Spinner) root.findViewById(R.id.spinner_ware);
        _adap_warehouse = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, _array_ware);
        _sp_warehouse.setAdapter(_adap_warehouse);

        // size of array for stockplace
        _array_stockplace = point.get_storeRepository().UDM_getStockPlace(_array_ware[0], getContext());
        // dropdownlist for stockplace
        _sp_stockplace = (Spinner) root.findViewById(R.id.spinner_stockplace);
        _adap_stockplace = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, _array_stockplace);
        _sp_stockplace.setAdapter(_adap_stockplace);
        ///////////////////////////////////////////// fill the spinner //////////////////////////////////////////


        // open the listener
        setListeners();

        // fill the openning
        if (!_id.equals("NODATA")) {
            fillTheForm(getActivity(), _id);
        }else {
            initText();
        }
        ///////////////////////////////// fragment process ///////////////////////////

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    /*
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MaterialDetailViewModel.class);
        // TODO: Use the ViewModel
    }*/


    private void initViews(View root) {
        //button
        btn_add = (Button) root.findViewById(R.id.btnAddMaterial);

        btn_delete = (Button) root.findViewById(R.id.btnDeleteMaterial);
        //btn_delete.setVisibility(View.GONE);
        btn_delete_text = (Button) root.findViewById(R.id.btnDeleteMaterialText);
        btn_back = (Button) root.findViewById(R.id.btnBack);

        //TextView
        _matcode = (EditText) root.findViewById(R.id.txt_code);
        _description = (EditText) root.findViewById(R.id.txt_description);
        _batchnum = (EditText) root.findViewById(R.id.txt_batchnum);
        _serialnumber = (EditText) root.findViewById(R.id.txt_serno);
        _quantity = (EditText) root.findViewById(R.id.txt_quan);

        // date format
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //String
        _id = "";

    }

    private void setListeners() {
        btn_add.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_delete_text.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        /// depo yeri seçildiğinde stockyerinin güncellenmesi
        _sp_warehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String ware_data = (String) parentView.getItemAtPosition(position);
                // size of array for stockplace
                _array_stockplace = point.get_storeRepository().UDM_getStockPlace(ware_data, getContext());

                // dropdownlist for stockplace
                _adap_stockplace = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, _array_stockplace);
                _sp_stockplace.setAdapter(_adap_stockplace);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
    }

    private void initText() {
        _id = "";

        _matcode.setText("");
        _batchnum.setText("");
        _description.setText("");
        _serialnumber.setText("");
        _quantity.setText("");

        _sp_warehouse.setSelection(0);
        _sp_stockplace.setSelection(0);
        _sp_spec_stock.setSelection(0);
        _sp_units.setSelection(0);

        //
        btn_delete.setVisibility(View.GONE);
    }

    private void fillTheForm(Context context, String id_number) {
        ArrayList<HashMap<String, String>> _matList = point.get_materialRepository().Rep_GetAll(context);
        int idx = Integer.parseInt(id_number.toString().trim());

        // databaseden verinin alınması
        HashMap<String, String> matClass = point.get_materialRepository().Rep_Get_Id(idx, getActivity());

        String code = matClass.get("code");
        String batchnum = matClass.get("batchnum");
        String serialnum = matClass.get("serialNumber"); // listview adapterinde String ismi ne ise öyle çağırılmalı
        String description = matClass.get("name");

        String specstock = matClass.get("specialStock");
        String warehouse = matClass.get("section");
        String stockplace = matClass.get("stockPlace");
        String units = matClass.get("units");
        String quantity = matClass.get("quantity");

        _matcode.setText(code);
        _batchnum.setText(batchnum);
        _serialnumber.setText(serialnum);
        _description.setText(description);
        _quantity.setText(quantity);

        // adapterdeki birim setlenir
        int spinner_unit_position = _adap_units.getPosition(units);
        _sp_units.setSelection(spinner_unit_position);

        // adapterdeki special stock yerini belirle
        int spinner_spec_position = _adap_specstock.getPosition(specstock);
        _sp_spec_stock.setSelection(spinner_spec_position);

        // adapter depo
        int spinner_ware_position = _adap_warehouse.getPosition(warehouse);
        _sp_warehouse.setSelection(spinner_ware_position);

        //adapter stockyeri
        int spinner_stockplace_position = _adap_stockplace.getPosition(stockplace);
        _sp_stockplace.setSelection(spinner_stockplace_position);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddMaterial:
                Save();
                break;
            case R.id.btnDeleteMaterial:
                Delete();
                break;
            case R.id.btnDeleteMaterialText:
                initText();
                break;
            case R.id.btnBack:
                getDialog().dismiss();
                break;
        }
    }

    //////////////////////////////////////////// Save methodu başarı ile çalıştıktan sonra çalışacak olan kısım BAŞLANGIÇ  ///////////////////////////////////////////////
    ///////////////////////////////////////////MaterialFragment class ına MaterialDetailFragment.OnSavedAfter şeklinde implemente edilir,
    /////////////////////////////////////////////////////  listMaterials methodu altında fillthelist methodu çağırılır

    private OnSavedAfter mOnSavedAfter;

    public interface OnSavedAfter{
        void listMaterials();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
            mOnSavedAfter = (OnSavedAfter) getTargetFragment();
        }catch (ClassCastException e){
            Log.e("MaterialDetail", "onAttach: ClassCastException : " + e.getMessage() );
        }
    }
    //////////////////////////////////////////// Save methodu başarı ile çalıştıktan sonra çalışacak olan kısım BİTİŞ ///////////////////////////////////////////////


    //////////////////////////////////// Mesajlar  ////////////////////////////////////
    private void onSuccess() {
        Toast.makeText(getActivity(), "" + " Başarılı..", Toast.LENGTH_SHORT).show();
        initText();

        if (mOnSavedAfter != null) mOnSavedAfter.listMaterials();
    }

    private void onFailure() {
        Toast.makeText(getActivity(), "" + "Kayıt Başarısız..", Toast.LENGTH_SHORT).show();
    }


    private void Save() {
        boolean _record = false;
        materials _materials = new materials();

        _materials.setCode(_matcode.getText().toString());
        _materials.setName(_description.getText().toString());
        _materials.setBatchnum(_batchnum.getText().toString());
        _materials.setSerialNumber(_serialnumber.getText().toString());

        _materials.setSpecialStock(_sp_spec_stock.getSelectedItem().toString());
        _materials.setSection(_sp_warehouse.getSelectedItem().toString());
        _materials.setStockPlace(_sp_stockplace.getSelectedItem().toString());
        _materials.setUnits(_sp_units.getSelectedItem().toString());

        _materials.setSernoType((_serialnumber.getText().toString().length() > 0) ? 1 : 0);  //seri no varsa 1 yoksa 0 olacak serno type
        _materials.setQuantity(_quantity.getText().toString());
        _materials.setIsWillCount(0);

        _materials.setActive(true);

        _materials.setChangedBy(currentUser);
        _materials.setChangedDate(dateFormat.format(Calendar.getInstance().getTime()));
        _materials.setCreatedBy(currentUser);
        _materials.setCreateDate(dateFormat.format(Calendar.getInstance().getTime()));
        _materials.setIpAddress(helpersForApp.getIpAddress());
        _materials.setValidFrom("01/01/1975");
        _materials.setValidUntil("01/01/2030");

        if (!_materials.getName().isEmpty() && !_materials.getCode().isEmpty() && !_materials.getBatchnum().isEmpty()
        ) {
            if (_id.equals("")) {
                _record = point.get_materialRepository().Rep_Create(_materials, getActivity());
            } else {
                _materials.setId(Integer.valueOf(_id));
                _record = point.get_materialRepository().Rep_Update(_materials, getActivity());
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
        final materials _material = new materials();

        _material.setId(Integer.valueOf(_id));

        _material.setCode(_matcode.getText().toString());
        _material.setName(_description.getText().toString());
        _material.setBatchnum(_batchnum.getText().toString());

        if (!_material.getName().isEmpty() && !_material.getCode().isEmpty()
        ) {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:

                            final boolean _record = point.get_Repository().Rep_Delete("Materials", _material, getActivity());

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
