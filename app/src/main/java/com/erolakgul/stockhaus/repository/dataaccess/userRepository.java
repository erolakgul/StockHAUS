package com.erolakgul.stockhaus.repository.dataaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import com.erolakgul.stockhaus.core.context.sqlite.databaseHandler;
import com.erolakgul.stockhaus.core.db.sqlite.users;
import com.erolakgul.stockhaus.repository.base.IRepository;

import java.util.ArrayList;
import java.util.HashMap;

public class userRepository
        extends databaseHandler
        implements IRepository<users> {

    private static final String TABLE_NAME = "Users";
    private static users c;
    private static userRepository singleton;
    static Context _context;
    Repository<users> _instance = null;

    public userRepository(@Nullable Context context) {
        super(context);
        _context = context;
        _instance = Repository.getInstance();//new Repository<users>();
    }

    // gives a instance to servicepoints
    public static userRepository getInstance() {
       /*if (singleton == null) {
            singleton = new userRepository(_context);
        }
        // boş ise yeni instance ı gönderir dolu ise zaten dolu olan instance ı gönderir
        return singleton;*/
       return singleton = (singleton == null) ? new userRepository(_context) : singleton;
    }

    @Override
    public HashMap<String, String> Rep_Get_Id(int id, Context context) {
        databaseHandler database = new databaseHandler(context);
        SQLiteDatabase db = database.getWritableDatabase();

        String query = "SELECT id,name,surname,mail,password,role,company FROM " + TABLE_NAME + " WHERE isActive = ? AND id = ?";
        // gets just isactive
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(1), String.valueOf(id)});
        HashMap<String, String> user = new HashMap<>();

        while (cursor.moveToNext()) {
            user.put("id", cursor.getString(cursor.getColumnIndex("id")));
            user.put("name", cursor.getString(cursor.getColumnIndex("name")));
            user.put("surname", cursor.getString(cursor.getColumnIndex("surname")));
            user.put("mail", cursor.getString(cursor.getColumnIndex("mail")));
            user.put("password", cursor.getString(cursor.getColumnIndex("password")));
            user.put("role", cursor.getString(cursor.getColumnIndex("role")));
            user.put("company", cursor.getString(cursor.getColumnIndex("company")));
        }

        cursor.close();
        db.close();
        return user;
    }

    @Override
    public ArrayList<HashMap<String, String>> Rep_GetAll(Context context) {
        databaseHandler database = new databaseHandler(context);
        SQLiteDatabase db = database.getWritableDatabase();

        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT id,name,surname,mail,password,role,company FROM " + TABLE_NAME + " WHERE isActive = ?";
        // gets just isactive
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(1)});

        while (cursor.moveToNext()) {

            HashMap<String, String> user = new HashMap<>();
            user.put("id", cursor.getString(cursor.getColumnIndex("id")));
            user.put("name", cursor.getString(cursor.getColumnIndex("name")));
            user.put("surname", cursor.getString(cursor.getColumnIndex("surname")));
            user.put("mail", cursor.getString(cursor.getColumnIndex("mail")));
            user.put("password", cursor.getString(cursor.getColumnIndex("password")));
            user.put("role", cursor.getString(cursor.getColumnIndex("role")));
            user.put("company", cursor.getString(cursor.getColumnIndex("company")));

            // array list için
            user.put("user","");
            user.put("rolex","");

            userList.add(user);
        }

        cursor.close();
        db.close();

        return userList;
    }

    @Override
    public boolean Rep_Create(users entity, Context context) {
        ContentValues values = new ContentValues();

        values.put("name", entity.getName());
        values.put("surname", entity.getSurname());

        values.put("mail", entity.getMail());
        values.put("password", entity.getPassword());
        values.put("role", entity.getRole());

        values.put("isOnline", (entity.isOnline()) ? 1 : 0);  // is that true return 1
        values.put("isSignable", (entity.isSignable()) ? 1 : 0); // is that true return 1

        values.put("company", entity.getCompany());

        values.put("phone_number", entity.getPhone_number());
        values.put("age", entity.getAge());

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
    public boolean Rep_Update(users entity, Context context) {

        ContentValues values = new ContentValues();
        values.put("name", entity.getName());
        values.put("surname", entity.getSurname());
        values.put("mail", entity.getMail());
        values.put("password", entity.getPassword());
        values.put("phone_number", entity.getPhone_number());
        values.put("role",entity.getRole());

        // updating row
        if (_instance.Rep_Save(TABLE_NAME, values, 3, entity, context) > 0) return true;

        return false;
    }

    ///////////////////////////////// User Defined Method  ///////////////////////////////////

    public boolean UDM_isUser(String _mail, Context context) {
        databaseHandler database = new databaseHandler(context);
        SQLiteDatabase db = database.getWritableDatabase();

        boolean _isOk = false;

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE  isActive = ?  AND mail = ?"
                    , new String[]{String.valueOf(1), _mail}
            );

            while (cursor.moveToNext()) {

                _isOk = (Integer.valueOf(cursor.getString(cursor.getColumnIndex("id"))) > 0) ? true : false;
            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();

        return _isOk;
    };

    // login işlemi
    public boolean UDM_canLogin(String _mail, String _password ,Context context) {
        databaseHandler database = new databaseHandler(context);
        SQLiteDatabase db = database.getWritableDatabase();

        boolean _isOk = false;

        try {

            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE  isActive = ?  AND mail = ? AND password = ?"
                    , new String[]{String.valueOf(1), _mail , _password}
            );

            while (cursor.moveToNext()) {

                _isOk = (Integer.valueOf(cursor.getString(cursor.getColumnIndex("id"))) > 0) ? true : false;
            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();

        return _isOk;
    };

}
