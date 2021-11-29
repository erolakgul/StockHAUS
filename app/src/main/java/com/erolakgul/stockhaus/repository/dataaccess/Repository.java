package com.erolakgul.stockhaus.repository.dataaccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.erolakgul.stockhaus.core.context.sqlite.databaseHandler;
import com.erolakgul.stockhaus.core.db.sqlite.basemodel;
import com.erolakgul.stockhaus.repository.base.ICommon;

public class Repository <T extends basemodel>
        implements ICommon<basemodel> {

    private static Repository singleton;


    // gives a instance to servicepoints
    public static Repository getInstance() {
        /*if (singleton == null) {
            singleton = new inventoriesRepository(_context);
        }
        // boş ise yeni instance ı gönderir dolu ise zaten dolu olan instance ı gönderir
        return singleton;*/
        return singleton = (singleton == null) ? new Repository() : singleton;
    }

    @Override
    public boolean Rep_Remove(String table, basemodel entity, Context context) {
        ContentValues values = new ContentValues();
        values.put("isActive",String.valueOf(0));  // isactive sıfırlanacak remove larda

        if (Rep_Save(table, values, 3, entity, context) > 0) return true;

        return false;
    }

    @Override
    public boolean Rep_Delete(String table, basemodel entity, Context context) {
        // entity contains id number
        if (Rep_Save(table, null, 2, entity, context) > 0) return true;

        return false;
    }


    @Override
    public long Rep_Save(String table, ContentValues values, int type, basemodel entity, Context context) {
        databaseHandler database = new databaseHandler(context);
        SQLiteDatabase db = database.getWritableDatabase();

        long id = 0;

        try {
            if (type == 1) {
                id = db.insert(table, null, values);
            } else if (type == 2) {
                id = db.delete(table, "id" + " = ?",
                        new String[]{String.valueOf(entity.getId())});
            }else if (type == 3){
                id = db.update(table, values, "id" + " = ?",
                        new String[]{
                                String.valueOf(entity.getId())
                        });
            }

        } catch (Exception e) {
            e.printStackTrace();
            id = 0;
        }
        ;
        db.close();
        return id;
    }
}
