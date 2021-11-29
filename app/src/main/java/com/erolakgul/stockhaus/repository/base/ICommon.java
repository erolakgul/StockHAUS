package com.erolakgul.stockhaus.repository.base;

import android.content.ContentValues;
import android.content.Context;

import com.erolakgul.stockhaus.core.db.sqlite.basemodel;

public interface ICommon<T extends basemodel> {
    boolean Rep_Remove(String table,T entity,Context context);
    boolean Rep_Delete(String table,T entity,Context context);
    long Rep_Save(String table, ContentValues values, int type, T entity, Context context);
}
