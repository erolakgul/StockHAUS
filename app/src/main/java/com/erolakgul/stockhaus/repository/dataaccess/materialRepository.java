package com.erolakgul.stockhaus.repository.dataaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.erolakgul.stockhaus.core.context.sqlite.databaseHandler;
import com.erolakgul.stockhaus.core.db.sqlite.materials;
import com.erolakgul.stockhaus.core.db.sqlite.users;
import com.erolakgul.stockhaus.repository.base.IRepository;

import java.util.ArrayList;
import java.util.HashMap;

public class materialRepository
        extends databaseHandler
        implements IRepository<materials> {

    private static final String TABLE_NAME = "Materials";
    private static materials c;
    private static materialRepository singleton;
    static Context _context;
    Repository<materials> _instance = null;


    public materialRepository(@Nullable Context context) {
        super(context);
        _context = context;
        _instance = Repository.getInstance();//new Repository<materials>();
    }

    // gives a instance to servicepoints
    public static materialRepository getInstance() {
        /*if (singleton == null) {
            singleton = new materialRepository(_context);
        }
        // boş ise yeni instance ı gönderir dolu ise zaten dolu olan instance ı gönderir
        return singleton;*/
        return singleton = (singleton == null) ? new materialRepository(_context) : singleton;
    }

    @Override
    public HashMap<String, String> Rep_Get_Id(int id,Context context) {
        databaseHandler database = new databaseHandler(context);
        SQLiteDatabase db = database.getWritableDatabase();

        String query = "SELECT id,code,batchnum,specialStock,serialNumber,name,section,stockPlace,units,quantity,sernoType,isWillCount,company" +
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
            material.put("sernoType",cursor.getString(cursor.getColumnIndex("sernoType")));
            material.put("isWillCount",cursor.getString(cursor.getColumnIndex("isWillCount")));
            material.put("company",cursor.getString(cursor.getColumnIndex("company")));
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
        String query = "SELECT id,code,batchnum,specialStock,serialNumber,name,section,stockPlace,units,quantity,sernoType,isWillCount,company" +
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
            material.put("sernoType",cursor.getString(cursor.getColumnIndex("sernoType")));
            material.put("isWillCount",cursor.getString(cursor.getColumnIndex("isWillCount")));
            material.put("company",cursor.getString(cursor.getColumnIndex("company")));
            material.put("warestock","");

            userList.add(material);
        }

        cursor.close();
        db.close();

        return  userList;
    }

    @Override
    public boolean Rep_Create(materials entity, Context context) {
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
        values.put("sernoType", entity.getSernoType());
        values.put("isWillCount", entity.getIsWillCount());
        values.put("isActive", (entity.isActive()) ? 1 : 0);
        values.put("ipAddress", entity.getIpAddress());
        values.put("validFrom", String.valueOf(entity.getValidFrom()));
        values.put("validUntil", String.valueOf(entity.getValidUntil()));
        values.put("createdBy", entity.getCreatedBy());
        values.put("createDate", String.valueOf(entity.getCreateDate()));
        values.put("changedBy", entity.getName());
        values.put("changedDate", String.valueOf(entity.getChangedDate()));
        values.put("company", String.valueOf(entity.getCompany()));

        // kayıt sonrası dönen id ye göre durum kontrol
        if (_instance.Rep_Save(TABLE_NAME, values, 1, entity, context) > 0) return true;

        return false;
    }



    @Override
    public boolean Rep_Update(materials entity,Context context) {
        ContentValues values = new ContentValues();
        values.put("name",entity.getName());
        values.put("quantity",entity.getQuantity());
        values.put("batchnum",entity.getBatchnum());
        values.put("isWillCount", entity.getIsWillCount()); // 1 olduğunda 3.cü takım sayacak

        if (_instance.Rep_Save(TABLE_NAME, values, 3, entity, context) > 0) return true;

        return false;
    }


    ///////////////////////////////// User Defined Method  ///////////////////////////////////


}

