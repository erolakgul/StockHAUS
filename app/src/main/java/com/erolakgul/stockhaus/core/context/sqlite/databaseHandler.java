package com.erolakgul.stockhaus.core.context.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.erolakgul.stockhaus.core.db.sqlite.groups;
import com.erolakgul.stockhaus.core.db.sqlite.inventories;
import com.erolakgul.stockhaus.core.db.sqlite.materials;
import com.erolakgul.stockhaus.core.db.sqlite.stores;
import com.erolakgul.stockhaus.core.db.sqlite.users;
import com.erolakgul.stockhaus.models.app.mapping;

import java.util.ArrayList;
import java.util.List;

public class databaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "STOCKHAUSDB";

    private static final String KEY_ID = "id";


    public databaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        List<String> initialize = DbSet();
        Enable_Migrations(initialize, db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + "tablename");

        // Create tables again
        onCreate(db);
    }


    /// oluşturulan veri tabanı modelleri burada bir liste halinde alınır
    private List<String> DbSet() {
        // oluşturulacak olan tablo isimleri bu kısma alınır, bir nevi c sharp taki dbset class ı gibi
        List<String> _table = new ArrayList<String>();

        _table.add("Users");
        _table.add("Materials");
        _table.add("Inventories");
        _table.add("Groups");
        _table.add("Stores");

        return _table;
    }

    private void Enable_Migrations(List<String> _table, SQLiteDatabase db) {


        for (String item : _table) {

            if (item.equals("Users")) {
                users  u = new users();

                String sqlCommand = "CREATE TABLE IF NOT EXISTS " + item + "(";
                String _types = " ";

                for (mapping data : u.Map()) {
                    _types = _types + data.name + data.type;
                }

                sqlCommand = sqlCommand + _types + ")";

                CreateTable(sqlCommand, db);
            }else if (item.equals("Materials")){
                materials m = new materials();

                String sqlCommand = "CREATE TABLE IF NOT EXISTS " + item + "(";
                String _types = " ";

                for (mapping data : m.Map()) {
                    _types = _types + data.name + data.type;
                }

                sqlCommand = sqlCommand + _types + ")";

                Log.d("scriptssssss",sqlCommand);

                CreateTable(sqlCommand, db);
            }else if (item.equals("Inventories")) {
               inventories i = new inventories();

                String sqlCommand = "CREATE TABLE IF NOT EXISTS " + item + "(";
                String _types = " ";

                for (mapping data : i.Map()) {
                    _types = _types + data.name + data.type;
                }

                sqlCommand = sqlCommand + _types + ")";

                CreateTable(sqlCommand, db);
            }else if (item.equals("Stores")) {
               stores s = new stores();

                String sqlCommand = "CREATE TABLE IF NOT EXISTS " + item + "(";
                String _types = " ";

                for (mapping data : s.Map()) {
                    _types = _types + data.name + data.type;
                }

                sqlCommand = sqlCommand + _types + ")";

                CreateTable(sqlCommand, db);
            }else if (item.equals("Groups")) {
                groups g = new groups();

                String sqlCommand = "CREATE TABLE IF NOT EXISTS " + item + "(";
                String _types = " ";

                for (mapping data : g.Map()) {
                    _types = _types + data.name + data.type;
                }

                sqlCommand = sqlCommand + _types + ")";

                CreateTable(sqlCommand, db);
            }

        }
    }

    private void CreateTable(String sqlCommand, SQLiteDatabase db) {
        //Log.d("scriptssssss",sqlCommand);

        try {
            db.execSQL(sqlCommand);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
