package com.erolakgul.stockhaus.repository.base;

import android.content.ContentValues;
import android.content.Context;

import com.erolakgul.stockhaus.core.db.sqlite.basemodel;

import java.util.ArrayList;
import java.util.HashMap;

public interface IRepository<T extends basemodel>  {

    // select query
    HashMap<String, String> Rep_Get_Id(int id,Context context);
    ArrayList<HashMap<String, String>> Rep_GetAll(Context context);

    // CRUD Operation
    boolean Rep_Create(T entity,Context context);
    boolean Rep_Update(T entity,Context context);

    //ICommon a taşındı her tabloda ortak hareket edilebiliyor
    //boolean Rep_Remove(T entity,Context context);
    //boolean Rep_Delete(T entity,Context context);
    //long Rep_Save(String table, ContentValues values, int type, T entity, Context context);
}
