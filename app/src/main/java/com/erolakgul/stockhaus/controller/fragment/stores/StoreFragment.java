package com.erolakgul.stockhaus.controller.fragment.stores;

import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.erolakgul.stockhaus.R;
import com.erolakgul.stockhaus.core.db.sqlite.stores;
import com.erolakgul.stockhaus.service.sqlite.servicePoints;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class StoreFragment extends Fragment implements View.OnClickListener {

    private StoreViewModel mViewModel;
    private String currentUser;
    private String _id;
    private ListView lv;

    //general access
    private servicePoints point;

    private Button btn_add, btn_delete,btn_delete_text;
    private EditText _warehouse, _stockplace, _description;
    private static final int REQUEST_SIGNUP = 0;
    SimpleDateFormat dateFormat;

    public static StoreFragment newInstance() {
        return new StoreFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ////////// current user  getActivity() activity class lardaki context in fragmentlerdeki kullanımıdır
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        currentUser = sharedPref.getString("currentUser", "NODATA");

        mViewModel = ViewModelProviders.of(this).get(StoreViewModel.class);

        View root = inflater.inflate(R.layout.store_fragment, container, false);

        /*final TextView textView = root.findViewById(R.id.text_store);

        mViewModel.getText().observe(this, new Observer<String>() {

            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }

        });*/

        ///////////////////////////////// fragment üzerindeki işlemler ///////////////////////////
        initViews(root);
        setListeners();
        point = servicePoints.getInstance();

        // açılırken doldursun
        fillTheList(getActivity());
        ///////////////////////////////// fragment üzerindeki işlemler ///////////////////////////
        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(StoreViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddStore:
                Save();
                break;
            case R.id.btnDeleteStore:
                Delete();
                break;
            case R.id.btnDeleteStoreText:
                initText();
                break;
        }
    }

    private void initViews(View root) {
        //button
        btn_add = (Button) root.findViewById(R.id.btnAddStore);
        btn_delete = (Button) root.findViewById(R.id.btnDeleteStore);
        btn_delete.setVisibility(View.GONE);
        btn_delete_text = (Button) root.findViewById(R.id.btnDeleteStoreText);

        //TextView
        _warehouse = (EditText) root.findViewById(R.id.txt_ware);
        _stockplace = (EditText) root.findViewById(R.id.txt_stockplace);
        _description = (EditText) root.findViewById(R.id.txt_desc);

        // list view
        lv = (ListView) root.findViewById(R.id.lstStores);

        // date format
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //String
        _id = "";
    }

    private void setListeners() {
        // button
        btn_add.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_delete_text.setOnClickListener(this);

        //textview changed
        /*_warehouse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    // textbox boşaltıldı
                }
            }
        });*/

        // listelere tıklandığında
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Object result = adapterView.getItemAtPosition(i);

                HashMap<String, String> map = (HashMap<String, String>) adapterView.getItemAtPosition(i);
                _id = map.get("id");
                String _ware = map.get("warehouse");
                String _stock = map.get("stockPlace"); // listview adapterinde String ismi ne ise öyle çağırılmalı
                String _desc = map.get("description");

                _warehouse.setText(_ware);
                _stockplace.setText(_stock);
                _description.setText(_desc);

                btn_delete.setVisibility(View.VISIBLE);
            }
        });

    }

    //////////////////////////////////// textboxlar sıfırlanır  ////////////////////////////////////
    private void initText() {
        _id = "";
        _warehouse.setText("");
        _stockplace.setText("");
        _description.setText("");
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


    /////////////////////////////////// Listview Doldur ///////////////////////////////////////
    private void fillTheList(Context context) {
        ArrayList<HashMap<String, String>> _storeList = point.get_storeRepository().Rep_GetAll(context);

        // adapter deki string isimleri veritabanı tablosunda kolon isimleri nasıl ise öyle olmalı stockPlace gibi
        // int değerler ise listview deki textbox id lerinden gelir
        if (_storeList != null) {
            ListAdapter adapter = new SimpleAdapter(context, _storeList, R.layout.listview_store,
                    new String[]{"id", "warehouse", "stockPlace", "description"},   // _storelist te gelen key ler ne ise onlar yazılır
                    new int[]{R.id.id, R.id.lst_warehouse, R.id.lst_stockplace, R.id.lst_desc} // listview deki textview id leri
            );
            lv.setAdapter(adapter);
        }
    }

    ///////////////////////////////////// KAYDET/GÜNCELLE //////////////////////////////////////////
    private void Save() {
        boolean _record = false;

        stores _store = new stores();

        /////////////////////////////// dynamic ////////////////////////////
        _store.setWarehouse(_warehouse.getText().toString());
        _store.setStockPlace(_stockplace.getText().toString());
        _store.setDesc(_description.getText().toString());

        /////////////////////////////// static ////////////////////////////
        _store.setCompany("01");
        _store.setActive(true);
        _store.setIpAddress("::1");

        String dtStart = "1975-01-01";  // yıl ay gün
        String dtFinish = "2030-01-01";  // yıl ay gün

        _store.setValidFrom(dtStart);
        _store.setValidUntil(dtFinish);

        _store.setCreatedBy(currentUser);
        _store.setCreateDate(dateFormat.format(Calendar.getInstance().getTime()));
        _store.setChangedBy(currentUser);
        _store.setChangedDate(dateFormat.format(Calendar.getInstance().getTime()));


        if (!_store.getWarehouse().isEmpty() && !_store.getStockPlace().isEmpty() && !_store.getDesc().isEmpty()) {
            if (_id.equals("")) {
                _record = point.get_storeRepository().Rep_Create(_store, getActivity());
            } else {
                _store.setId(Integer.valueOf(_id));
                _record = point.get_storeRepository().Rep_Update(_store, getActivity());
            }
        }

        if (_record) {
            onSuccess();
        } else {
            onFailure();
        }
        ;
    }

    ///////////////////////////////////// SİL //////////////////////////////////////////
    private void Delete() {

        final stores _store = new stores();
        _store.setId(Integer.valueOf(_id));

        /////////////////////////////// dynamic ////////////////////////////
        _store.setWarehouse(_warehouse.getText().toString());
        _store.setStockPlace(_stockplace.getText().toString());
        _store.setDesc(_description.getText().toString());

        if (!_store.getWarehouse().isEmpty() && !_store.getStockPlace().isEmpty() && !_store.getDesc().isEmpty()) {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:

                            final boolean _record = point.get_Repository().Rep_Delete("Stores", _store, getActivity());

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
        };

    }


}
