package com.erolakgul.stockhaus.controller.fragment.inventory;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.erolakgul.stockhaus.R;
import com.erolakgul.stockhaus.service.sqlite.servicePoints;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class InventoryFragment extends Fragment implements View.OnClickListener {

    private InventoryViewModel mViewModel;
    private String currentUser;
    //general access
    private servicePoints point;

    //////////////////////////////////////////////////////////////////////////////////
    private String _array_spec_stock[], _array_ware[], _array_stockplace[], _array_units[];
    private String _id;
    private Spinner _sp_spec_stock, _sp_warehouse, _sp_stockplace, _sp_units;
    ArrayAdapter _adap_specstock, _adap_warehouse, _adap_stockplace, _adap_units;

    private Button btn_add, btn_delete_text, btn_back;
    private EditText _matcode, _description, _serialnumber, _batchnum, _quantity;
    private static final int REQUEST_SIGNUP = 0;
    SimpleDateFormat dateFormat;
    //////////////////////////////////////////////////////////////////////////////////

    //// camera
    SurfaceView surfaceView;
    TextView textViewBarCodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    String intentData = "";
    //////////////////////////////////////////////////////////////////////////////////

    public static InventoryFragment newInstance() {
        return new InventoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        currentUser = sharedPref.getString("currentUser", "NODATA");

        mViewModel = ViewModelProviders.of(this).get(InventoryViewModel.class);

        View root = inflater.inflate(R.layout.inventory_fragment, container, false);

        ///////////////////////////////// fragment üzerindeki işlemler ///////////////////////////
        point = servicePoints.getInstance();
        initViews(root);
        setListeners();

        ///////////////////////////////// fragment üzerindeki işlemler ///////////////////////////

        return root;
    }

    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }*/

    private void initViews(View root) {
        // camera
        surfaceView = root.findViewById(R.id.surfaceView);

        //button
        btn_add = (Button) root.findViewById(R.id.btnAddInventory);
        btn_delete_text = (Button) root.findViewById(R.id.btnDeleteMaterialText);
        btn_back = (Button) root.findViewById(R.id.btnBack);

        btn_add.setVisibility(View.GONE);

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

        ///////////////////////////////////////////// fill the spinners //////////////////////////////////////////
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
        ///////////////////////////////////////////// fill the spinners //////////////////////////////////////////
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
    }

    private void setListeners() {
        btn_add.setOnClickListener(this);
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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddMaterial:
                //Save();
                break;
            case R.id.btnDeleteMaterialText:
                initText();
                break;
            case R.id.btnBack:
                //getDialog().dismiss();
                break;
        }
    }

    //////////////////////////////////////////////////////  CAMERA ACTIVITIES ////////////////////////////
    private void initialiseDetectorsAndSources() {
        Toast.makeText(getContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                openCamera();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barCode = detections.getDetectedItems();
                if (barCode.size() > 0) {
                    setBarCode(barCode);
                }
            }
        });
    }

    private void openCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                cameraSource.start(surfaceView.getHolder());
            } else {
                ActivityCompat.requestPermissions(getActivity(), new
                        String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /// baroode bu methodta parçalanır, ve içindeki 0. indexteki veri text e yazılır
    private void setBarCode(final SparseArray<Barcode> barCode) {
        // barcode verisi 01$01$100$102$9100.1.101684K$C$*$
        //       FİRMA*TESİS*DEPO*STOKYERİ*MALZEMEKODU*İNDİS*ÖZELSTOK*SERİNO*BİRİM
        // array   0 , 1    , 2  , 3 ,      4          ,5 ,    6      ,7 , 8
        //copyToClipBoard(barCode.valueAt(0).displayValue);

        /////////////// SERİ NO GELMİYOR NEDENİNE BAK IASDEV604 ten  örnek kod 980320002

        final String[] separator = barCode.valueAt(0).displayValue.split("\\$");

        _matcode.post(new Runnable() {
            @Override
            public void run() {
                //intentData = barCode.valueAt(0).displayValue;
                _matcode.setText(separator[4]);
                //copyToClipBoard(intentData);
            }
        });

        _batchnum.post(new Runnable() {
            @Override
            public void run() {
                //intentData = barCode.valueAt(0).displayValue;
                _batchnum.setText(separator[5]);
                //copyToClipBoard(intentData);
            }
        });

        /*
        _serialnumber.post(new Runnable() {
            @Override
            public void run() {
                //intentData = barCode.valueAt(0).displayValue;
                _serialnumber.setText(separator[7]);
                //copyToClipBoard(intentData);
            }
        });
        */

        // adapterdeki birim setlenir
        int spinner_unit_position = _adap_units.getPosition(separator[8]);
        _sp_units.setSelection(spinner_unit_position);

        // adapterdeki special stock yerini belirle
        int spinner_spec_position = _adap_specstock.getPosition(separator[6]);
        _sp_spec_stock.setSelection(spinner_spec_position);

        // adapter depo
        int spinner_ware_position = _adap_warehouse.getPosition(separator[2]);
        _sp_warehouse.setSelection(spinner_ware_position);

        //adapter stockyeri
        int spinner_stockplace_position = _adap_stockplace.getPosition(separator[3]);
        _sp_stockplace.setSelection(spinner_stockplace_position);
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    public void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    private void copyToClipBoard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("QR code Scanner", text);
        clipboard.setPrimaryClip(clip);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                getActivity().finish();
                //Toast.makeText(getContext(), "olmadı", Toast.LENGTH_LONG).show();
            }
            else
                openCamera();
        } else {
            getActivity().finish();
            //Toast.makeText(getContext(), "olmadı", Toast.LENGTH_LONG).show();
        }
    }

}
