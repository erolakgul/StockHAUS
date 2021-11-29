package com.erolakgul.stockhaus.repository.dataaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.erolakgul.stockhaus.core.context.sqlite.databaseHandler;
import com.erolakgul.stockhaus.core.db.sqlite.inventories;
import com.erolakgul.stockhaus.core.db.sqlite.materials;
import com.erolakgul.stockhaus.repository.base.IRepository;

import java.util.ArrayList;
import java.util.HashMap;

public class inventoryRepository
        extends databaseHandler
        implements IRepository<inventories> {

    private static final String TABLE_NAME = "Inventories";
    private static inventories c;
    private static inventoryRepository singleton;
    static Context _context;
    Repository<inventories> _instance = null;

    // gives a instance to servicepoints
    public static inventoryRepository getInstance() {
        /*if (singleton == null) {
            singleton = new inventoriesRepository(_context);
        }
        // boş ise yeni instance ı gönderir dolu ise zaten dolu olan instance ı gönderir
        return singleton;*/
        return singleton = (singleton == null) ? new inventoryRepository(_context) : singleton;
    }

    public inventoryRepository(@Nullable Context context) {
        super(context);
        _context = context;
        _instance = Repository.getInstance();//new Repository<inventories>();
    }

    @Override
    public HashMap<String, String> Rep_Get_Id(int id, Context context) {
        databaseHandler database = new databaseHandler(context);
        SQLiteDatabase db = database.getWritableDatabase();

        String query = "SELECT id,code,batchnum,specialStock,serialNumber,name,section,stockPlace,units,quantity,sernoType,isWillCount" +
                " FROM " + TABLE_NAME + " WHERE isActive = ? AND id = ?";

        // get just isActive
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(1),String.valueOf(id)});

        HashMap<String,String> material = new HashMap<>();

        while (cursor.moveToNext()){
            material.put("id",cursor.getString(cursor.getColumnIndex("id")));
            material.put("code",cursor.getString(cursor.getColumnIndex("code")));
            material.put("batchnum",cursor.getString(cursor.getColumnIndex("batchnum")));
            material.put("specialStock",cursor.getString(cursor.getColumnIndex("specialStock")));
            material.put("serialNumber",cursor.getString(cursor.getColumnIndex("serialNumber")));
            material.put("name",cursor.getString(cursor.getColumnIndex("name")));
            material.put("section",cursor.getString(cursor.getColumnIndex("section")));
            material.put("stockPlace",cursor.getString(cursor.getColumnIndex("stockPlace")));
            material.put("units",cursor.getString(cursor.getColumnIndex("units")));
            material.put("quantity",cursor.getString(cursor.getColumnIndex("quantity")));
        }

        cursor.close();
        db.close();
        return material;
    }

    @Override
    public ArrayList<HashMap<String, String>> Rep_GetAll(Context context) {
        databaseHandler database = new databaseHandler(context);
        SQLiteDatabase db = database.getWritableDatabase();

        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT id,code,batchnum,specialStock,serialNumber,name,section,stockPlace,units,quantity,sernoType,isWillCount" +
                " FROM " + TABLE_NAME + " WHERE isActive = ?";

        // get just isActive
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(1)});

        while (cursor.moveToNext()){

            HashMap<String,String> material = new HashMap<>();
            material.put("id",cursor.getString(cursor.getColumnIndex("id")));
            material.put("code",cursor.getString(cursor.getColumnIndex("code")));
            material.put("batchnum",cursor.getString(cursor.getColumnIndex("batchnum")));
            material.put("specialStock",cursor.getString(cursor.getColumnIndex("specialStock")));
            material.put("serialNumber",cursor.getString(cursor.getColumnIndex("serialNumber")));
            material.put("name",cursor.getString(cursor.getColumnIndex("name")));
            material.put("section",cursor.getString(cursor.getColumnIndex("section")));
            material.put("stockPlace",cursor.getString(cursor.getColumnIndex("stockPlace")));
            material.put("units",cursor.getString(cursor.getColumnIndex("units")));
            material.put("quantity",cursor.getString(cursor.getColumnIndex("quantity")));

            userList.add(material);
        }

        cursor.close();
        db.close();

        return  userList;
    }

    @Override
    public boolean Rep_Create(inventories entity, Context context) {
        ContentValues values = new ContentValues();

        values.put("code", entity.getCode());
        values.put("batchnum", entity.getBatchnum());
        values.put("specialStock", entity.getSpecialStock());
        values.put("serialNumber", entity.getSerialNumber());
        values.put("name", entity.getName());
        values.put("section", entity.getSection());
        values.put("stockPlace", entity.getStockPlace());
        values.put("units", entity.getUnits());
        values.put("quantity", entity.getQuantity());
        values.put("isActive", (entity.isActive()) ? 1 : 0);
        values.put("ipAddress", entity.getIpAddress());
        values.put("validFrom", String.valueOf(entity.getValidFrom()));
        values.put("validUntil", String.valueOf(entity.getValidUntil()));
        values.put("createdBy", entity.getCreatedBy());
        values.put("createDate", String.valueOf(entity.getCreateDate()));
        values.put("changedBy", entity.getName());
        values.put("changedDate", String.valueOf(entity.getChangedDate()));

        // kayıt sonrası dönen id ye göre durum kontrol
        if (_instance.Rep_Save(TABLE_NAME, values, 1, entity, context) > 0) return true;

        return false;
    }

    @Override
    public boolean Rep_Update(inventories entity, Context context) {
        ContentValues values = new ContentValues();
        values.put("quantity", entity.getQuantity());

        if (_instance.Rep_Save(TABLE_NAME, values, 3, entity, context) > 0) return true;

        return false;
    }

    ///////////////////////////////// User Defined Method  ///////////////////////////////////

}
