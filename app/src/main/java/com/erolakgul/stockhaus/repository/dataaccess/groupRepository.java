package com.erolakgul.stockhaus.repository.dataaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.erolakgul.stockhaus.core.context.sqlite.databaseHandler;
import com.erolakgul.stockhaus.core.db.sqlite.groups;
import com.erolakgul.stockhaus.core.db.sqlite.stores;
import com.erolakgul.stockhaus.repository.base.IRepository;

import java.util.ArrayList;
import java.util.HashMap;

public class groupRepository
        extends databaseHandler
        implements IRepository<groups> {

    private static final String TABLE_NAME = "Groups";
    private static groups c;
    private static groupRepository singleton;
    static Context _context;
    Repository<stores> _instance = null;

    public groupRepository(@Nullable Context context) {
        super(context);
        _context = context;
        _instance = Repository.getInstance();//new Repository<stores>();
    }

    // gives a instance to servicepoints
    public static groupRepository getInstance() {
        return singleton = (singleton == null) ? new groupRepository(_context) : singleton;
    }

    @Override
    public HashMap<String, String> Rep_Get_Id(int id, Context context) {
        databaseHandler database = new databaseHandler(context);
        SQLiteDatabase db = database.getWritableDatabase();

        String query = "SELECT id,teamNo,groupNo,name,mission,userId,storeStartId,storeFinishId FROM " + TABLE_NAME + " WHERE isActive = ? AND id = ?";

        // gets just isactive
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(1), String.valueOf(id)});
        HashMap<String, String> group = new HashMap<>();

        while (cursor.moveToNext()) {
            group.put("id", cursor.getString(cursor.getColumnIndex("id")));
            group.put("teamNo", cursor.getString(cursor.getColumnIndex("teamNo")));
            group.put("groupNo", cursor.getString(cursor.getColumnIndex("groupNo")));
            group.put("name", cursor.getString(cursor.getColumnIndex("name")));
            group.put("mission", cursor.getString(cursor.getColumnIndex("mission")));
            group.put("userId", cursor.getString(cursor.getColumnIndex("userId")));
            group.put("storeStartId", cursor.getString(cursor.getColumnIndex("storeStartId")));
            group.put("storeFinishId", cursor.getString(cursor.getColumnIndex("storeFinishId")));
        }

        cursor.close();
        db.close();
        return group;
    }

    @Override
    public ArrayList<HashMap<String, String>> Rep_GetAll(Context context) {
        databaseHandler database = new databaseHandler(context);
        SQLiteDatabase db = database.getWritableDatabase();

        ArrayList<HashMap<String, String>> groupList = new ArrayList<>();
        String query = "SELECT id,teamNo,groupNo,name,mission,userId,storeStartId,storeFinishId FROM " + TABLE_NAME + " WHERE isActive = ?";

        // gets just isactive
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(1)});

        while (cursor.moveToNext()) {

            HashMap<String, String> group = new HashMap<>();
            group.put("id", cursor.getString(cursor.getColumnIndex("id")));
            group.put("teamNo", cursor.getString(cursor.getColumnIndex("teamNo")));
            group.put("groupNo", cursor.getString(cursor.getColumnIndex("groupNo")));
            group.put("name", cursor.getString(cursor.getColumnIndex("name")));
            group.put("mission", cursor.getString(cursor.getColumnIndex("mission")));
            group.put("userId", cursor.getString(cursor.getColumnIndex("userId")));
            group.put("storeStartId", cursor.getString(cursor.getColumnIndex("storeStartId")));
            group.put("storeFinishId", cursor.getString(cursor.getColumnIndex("storeFinishId")));

            groupList.add(group);
        }

        cursor.close();
        db.close();

        return groupList;
    }

    @Override
    public boolean Rep_Create(groups entity, Context context) {

        ContentValues values = new ContentValues();

        values.put("teamNo", entity.getTeamNo());
        values.put("groupNo", entity.getGroupNo());
        values.put("mission", entity.getMission());

        //////////////////////   bu veriler diğer tablolardan çekilecek olan verilerle doldurulacak //////////////////////
        values.put("name", entity.getName());
        values.put("userId", entity.getUserId());
        values.put("storeStartId", entity.getStoreStartId());
        values.put("storeFinishId", entity.getStoreFinishId());
        //////////////////////   bu veriler diğer tablolardan çekilecek olan verilerle doldurulacak //////////////////////

        values.put("company", entity.getCompany());
        values.put("isActive", (entity.isActive()) ? 1 : 0); // is that true return 1
        values.put("ipAddress", entity.getIpAddress());
        values.put("validFrom", String.valueOf(entity.getValidFrom()));
        values.put("validUntil", String.valueOf(entity.getValidUntil()));
        values.put("createdBy", entity.getCreatedBy());
        values.put("createDate", String.valueOf(entity.getCreateDate()));
        values.put("changedBy", entity.getChangedBy());
        values.put("changedDate", String.valueOf(entity.getChangedDate()));

        // kayıt sonrası dönen id ye göre durum kontrol
        if (_instance.Rep_Save(TABLE_NAME, values, 1, entity, context) > 0) return true;

        return false;
    }

    @Override
    public boolean Rep_Update(groups entity, Context context) {
        return false;
    }

    ///////////////////////////////// User Defined Method  ///////////////////////////////////
}
