package com.erolakgul.stockhaus.core.db.sqlite;

import android.util.Log;

import com.erolakgul.stockhaus.models.app.mapping;

import java.util.ArrayList;
import java.util.List;

public class stores extends basemodel {

    private String warehouse;
    private String stockPlace;
    private String description;

    // JUST FOR USE LISTING
    private List<mapping> mlist;

    public stores() {
        mlist = new ArrayList<mapping>();
    }

    public stores(String warehouse, String stockPlace, String desc, List<mapping> mlist) {
        this.warehouse = warehouse;
        this.stockPlace = stockPlace;
        this.description = desc;
        this.mlist = mlist;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getStockPlace() {
        return stockPlace;
    }

    public void setStockPlace(String stockPlace) {
        this.stockPlace = stockPlace;
    }

    public String getDesc() {
        return description;
    }

    public void setDesc(String desc) {
        this.description = desc;
    }

    //https://www.tutorialspoint.com/sqlite/sqlite_data_types.htm
    public List<mapping> Map() {

        for (int i = 0; i <= 12; i++) {
            mapping m = new mapping();

            if (i == 0) {
                m.name = "id";
                m.type = " INTEGER PRIMARY KEY,";
                mlist.add(m);
            }else if(i == 1) {
                m.name = "warehouse";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 2) {
                m.name = "stockPlace";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 3) {
                m.name = "description";
                m.type = " TEXT,";
                mlist.add(m);
            } else if(i == 4) {
                m.name = "company";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 5) {
                m.name = "isActive";
                m.type = " INTEGER,";
                mlist.add(m);
            }
            else if(i == 6) {
                m.name = "ipAddress";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 7) {
                m.name = "validFrom";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 8) {
                m.name = "validUntil";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 9) {
                m.name = "createdBy";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 10) {
                m.name = "createDate";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 11) {
                m.name = "changedBy";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 12) {
                m.name = "changedDate";
                m.type = " TEXT";
                mlist.add(m);
            }
        }
        return mlist;
    }

}
