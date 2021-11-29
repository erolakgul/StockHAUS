package com.erolakgul.stockhaus.core.db.sqlite;

import android.util.Log;
import com.erolakgul.stockhaus.models.app.mapping;
import java.util.ArrayList;
import java.util.List;

public class users extends basemodel {

    private String name;
    private String surname;
    private String mail;
    private String password;
    private String role;

    // Boolean flag2 = (intValue == 1)? true : false;
    private boolean isOnline;
    private boolean isSignable;

    private String phone_number;
    private String age;

    // JUST FOR USE LISTING
    private List<mapping> mlist;

    // getter setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean isSignable() {
        return isSignable;
    }

    public void setSignable(boolean signable) {
        isSignable = signable;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    // general getter and setter constructor method
    public users(String name, String surname, String mail, String password, String role, boolean isOnline, boolean isSignable, String company, String phone_number, String age, List<mapping> mlist) {
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.password = password;
        this.role = role;
        this.isOnline = isOnline;
        this.isSignable = isSignable;
        this.phone_number = phone_number;
        this.age = age;
        this.mlist = mlist;
    }

    public users() {
        mlist = new ArrayList<mapping>();
    }

    // attribute count for class
    private int getColumnCount() {
        return getClass().getDeclaredFields().length;
    }


    //https://www.tutorialspoint.com/sqlite/sqlite_data_types.htm
    public List<mapping> Map() {

        Log.d("vvvvvvvvvvv sayısı", String.valueOf(getColumnCount()));

      for (int i = 0; i <= 18; i++) {
            mapping m = new mapping();

            if (i == 0) {
                m.name = "id";
                m.type = " INTEGER PRIMARY KEY,";
                mlist.add(m);
            }else if(i == 1) {
                m.name = "name";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 2) {
                m.name = "surname";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 3) {
                m.name = "mail";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 4) {
                m.name = "password";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 5) {
                m.name = "role";
                m.type = " TEXT,";
                mlist.add(m);

            }
            else if(i == 6) {
                m.name = "isOnline";
                m.type = " INTEGER,";
                mlist.add(m);
            }
            else if(i == 7) {
                m.name = "isSignable";
                m.type = " INTEGER,";
                mlist.add(m);
            } else if(i == 8) {
                m.name = "company";
                m.type = " TEXT,";
                mlist.add(m);
            }
             else if(i == 9) {
                m.name = "phone_number";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 10) {
                m.name = "age";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 11) {
                m.name = "isActive";
                m.type = " INTEGER,";
                mlist.add(m);
            }
            else if(i == 12) {
                m.name = "ipAddress";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 13) {
                m.name = "validFrom";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 14) {
                m.name = "validUntil";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 15) {
                m.name = "createdBy";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 16) {
                m.name = "createDate";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 17) {
                m.name = "changedBy";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 18) {
                m.name = "changedDate";
                m.type = " TEXT";
                mlist.add(m);
            }
        }
        return mlist;
    }

}
