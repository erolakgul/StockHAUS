package com.erolakgul.stockhaus.repository.dataaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.erolakgul.stockhaus.core.context.sqlite.databaseHandler;
import com.erolakgul.stockhaus.core.db.sqlite.stores;
import com.erolakgul.stockhaus.core.db.sqlite.users;
import com.erolakgul.stockhaus.repository.base.IRepository;

import java.util.ArrayList;
import java.util.HashMap;

public class storeRepository
        extends databaseHandler
        implements IRepository<stores> {

    private static final String TABLE_NAME = "Stores";
    private static stores c;
    private static storeRepository singleton;
    static Context _context;
    Repository<stores> _instance = null;

    public storeRepository(@Nullable Context context) {
        super(context);
        _context = context;
        _instance = Repository.getInstance();//new Repository<stores>();
    }

    // gives a instance to servicepoints
    public static storeRepository getInstance() {
        /*if (singleton == null) {
            singleton = new storeRepository(_context);
        }
        return singleton;*/
        return singleton = (singleton == null) ? new storeRepository(_context) : singleton;
    }

    @Override
    public HashMap<String, String> Rep_Get_Id(int id, Context context) {
        databaseHandler database = new databaseHandler(context);
        SQLiteDatabase db = database.getWritableDatabase();

        String query = "SELECT id,warehouse,stockPlace,description FROM " + TABLE_NAME + " WHERE isActive = ? AND id = ?";

        // gets just isactive
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(1), String.valueOf(id)});
        HashMap<String, String> ware = new HashMap<>();

        while (cursor.moveToNext()) {
            ware.put("id", cursor.getString(cursor.getColumnIndex("id")));
            ware.put("warehouse", cursor.getString(cursor.getColumnIndex("warehouse")));
            ware.put("stockPlace", cursor.getString(cursor.getColumnIndex("stockPlace")));
            ware.put("description", cursor.getString(cursor.getColumnIndex("description")));
        }

        cursor.close();
        db.close();
        return ware;
    }

    @Override
    public ArrayList<HashMap<String, String>> Rep_GetAll(Context context) {
        databaseHandler database = new databaseHandler(context);
        SQLiteDatabase db = database.getWritableDatabase();

        ArrayList<HashMap<String, String>> storeList = new ArrayList<>();
        String query = "SELECT id,warehouse,stockPlace,description FROM " + TABLE_NAME + " WHERE isActive = ?";

        // gets just isactive
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(1)});

        while (cursor.moveToNext()) {

            HashMap<String, String> user = new HashMap<>();
            user.put("id", cursor.getString(cursor.getColumnIndex("id")));
            user.put("warehouse", cursor.getString(cursor.getColumnIndex("warehouse")));
            user.put("stockPlace", cursor.getString(cursor.getColumnIndex("stockPlace")));
            user.put("description", cursor.getString(cursor.getColumnIndex("description")));

            storeList.add(user);
        }

        cursor.close();
        db.close();

        return storeList;
    }

    @Override
    public boolean Rep_Create(stores entity, Context context) {

        ContentValues values = new ContentValues();

        values.put("warehouse", entity.getWarehouse());
        values.put("stockPlace", entity.getStockPlace());
        values.put("description", entity.getDesc());

        values.put("company", entity.getCompany());

        values.put("isActive", (entity.isActive()) ? 1 : 0); // is that true return 1

        values.put("ipAddress", entity.getIpAddress());
        values.put("validFrom", entity.getValidFrom());
        values.put("validUntil", entity.getValidUntil());
        values.put("createdBy", entity.getCreatedBy());
        values.put("createDate", entity.getCreateDate());
        values.put("changedBy", entity.getChangedBy());
        values.put("changedDate", entity.getChangedDate());

        // kayıt sonrası dönen id ye göre durum kontrol
        if (_instance.Rep_Save(TABLE_NAME, values, 1, entity, context) > 0) return true;

        return false;
    }

    @Override
    public boolean Rep_Update(stores entity, Context context) {
        ContentValues values = new ContentValues();
        values.put("description", entity.getDesc());

        // updating row
        if (_instance.Rep_Save(TABLE_NAME, values, 3, entity, context) > 0) return true;
        return false;
    }

    ///////////////////////////////// User Defined Method  ///////////////////////////////////

    public String[] UDM_getWarehouse(Context context){

        databaseHandler database = new databaseHandler(context);
        SQLiteDatabase db = database.getWritableDatabase();

        String query = "SELECT DISTINCT warehouse FROM " + TABLE_NAME + " WHERE isActive = ?";

        // gets just isactive
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(1)});

        String[] ware = new String[cursor.getCount()];

        int i = 0;
        while (cursor.moveToNext()) {
            ware[i] = cursor.getString(cursor.getColumnIndex("warehouse"));
            i = i + 1;
        }
        cursor.close();
        db.close();

        return ware;
    }

    public String[] UDM_getStockPlace(String warehouse,Context context){

        databaseHandler database = new databaseHandler(context);
        SQLiteDatabase db = database.getWritableDatabase();

        String query = "SELECT DISTINCT stockPlace FROM " + TABLE_NAME + " WHERE isActive = ? and warehouse = ?";

        // gets just isactive
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(1),warehouse});

        String[] stockplace = new String[cursor.getCount()];

        int i = 0;
        while (cursor.moveToNext()) {
            stockplace[i] = cursor.getString(cursor.getColumnIndex("stockPlace"));
            i = i + 1;
        }

        cursor.close();
        db.close();
        return stockplace;
    }



}
