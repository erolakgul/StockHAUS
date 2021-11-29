package com.erolakgul.stockhaus.core.db.sqlite;

import com.erolakgul.stockhaus.models.app.mapping;

import java.util.ArrayList;
import java.util.List;

public class inventories extends basemodel {
    private String code;
    private String batchnum;
    private String specialStock;
    private String serialNumber;
    private String name;
    private String section;
    private String stockPlace;
    private String units;
    private String quantity;

    // JUST FOR USE LISTING
    private List<mapping> mlist;

    public inventories(String code, String batchnum, String specialStock, String serialNumber, String name, String section, String stockPlace, String units, String quantity, List<mapping> mlist) {
        this.code = code;
        this.batchnum = batchnum;
        this.specialStock = specialStock;
        this.serialNumber = serialNumber;
        this.name = name;
        this.section = section;
        this.stockPlace = stockPlace;
        this.units = units;
        this.quantity = quantity;
        this.mlist = mlist;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBatchnum() {
        return batchnum;
    }

    public void setBatchnum(String batchnum) {
        this.batchnum = batchnum;
    }

    public String getSpecialStock() {
        return specialStock;
    }

    public void setSpecialStock(String specialStock) {
        this.specialStock = specialStock;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getStockPlace() {
        return stockPlace;
    }

    public void setStockPlace(String stockPlace) {
        this.stockPlace = stockPlace;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public List<mapping> getMlist() {
        return mlist;
    }

    public void setMlist(List<mapping> mlist) {
        this.mlist = mlist;
    }

    public inventories() {
        mlist = new ArrayList<mapping>();
    }

    //https://www.tutorialspoint.com/sqlite/sqlite_data_types.htm
    public List<mapping> Map() {

        for (int i = 0; i <= 18; i++) {
            mapping m = new mapping();

            if (i == 0) {
                m.name = "id";
                m.type = " INTEGER PRIMARY KEY,";
                mlist.add(m);
            }else if (i == 1) {
                m.name = "code";
                m.type = " TEXT,";
                mlist.add(m);
            }else if (i == 2) {
                m.name = "batchnum";
                m.type = " TEXT,";
                mlist.add(m);
            } else if (i == 3) {
                m.name = "specialStock";
                m.type = " TEXT,";
                mlist.add(m);
            }else if (i == 4) {
                m.name = "serialNumber";
                m.type = " TEXT,";
                mlist.add(m);
            }else if (i == 5) {
                m.name = "name";
                m.type = " TEXT,";
                mlist.add(m);
            }else if (i == 6) {
                m.name = "section";
                m.type = " TEXT,";
                mlist.add(m);
            }else if (i == 7) {
                m.name = "stockPlace";
                m.type = " TEXT,";
                mlist.add(m);
            }else if (i == 8) {
                m.name = "units";
                m.type = " TEXT,";
                mlist.add(m);
            }else if (i == 9) {
                m.name = "quantity";
                m.type = " NUMERIC,";
                mlist.add(m);
            } else if (i == 10) {
                m.name = "isActive";
                m.type = " INTEGER,";
                mlist.add(m);
            } else if (i == 11) {
                m.name = "ipAddress";
                m.type = " TEXT,";
                mlist.add(m);
            } else if (i == 12) {
                m.name = "validFrom";
                m.type = " TEXT,";
                mlist.add(m);
            } else if (i == 13) {
                m.name = "validUntil";
                m.type = " TEXT,";
                mlist.add(m);
            } else if (i == 14) {
                m.name = "createdBy";
                m.type = " TEXT,";
                mlist.add(m);
            } else if (i == 15) {
                m.name = "createDate";
                m.type = " TEXT,";
                mlist.add(m);
            } else if (i == 16) {
                m.name = "changedBy";
                m.type = " TEXT,";
                mlist.add(m);
            } else if (i == 17) {
                m.name = "changedDate";
                m.type = " TEXT,";
                mlist.add(m);
            } else if (i == 18) {
                m.name = "company";
                m.type = " TEXT";
                mlist.add(m);
            }
        }

        return mlist;
    }
}
