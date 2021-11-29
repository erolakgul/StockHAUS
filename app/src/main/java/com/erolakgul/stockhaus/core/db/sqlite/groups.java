package com.erolakgul.stockhaus.core.db.sqlite;

import com.erolakgul.stockhaus.models.app.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;

public class groups extends basemodel {

    private String teamNo;
    private String groupNo;

    private String name;
    private Integer mission; //1 uygulamayı kullanır 2 sayım yapar web arayüzünde

    private Integer userId;  // Foreign Key
    private Integer storeStartId;
    private Integer storeFinishId;

    // JUST FOR USE LISTING
    private List<mapping> mlist;

    public groups(String teamNo, String groupNo, String name, Integer mission, Integer userId, Integer storeStartId, Integer storeFinishId) {
        this.teamNo = teamNo;
        this.groupNo = groupNo;
        this.name = name;
        this.mission = mission;
        this.userId = userId;
        this.storeStartId = storeStartId;
        this.storeFinishId = storeFinishId;
    }

    public String getTeamNo() {
        return teamNo;
    }

    public void setTeamNo(String teamNo) {
        this.teamNo = teamNo;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMission() {
        return mission;
    }

    public void setMission(Integer mission) {
        this.mission = mission;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStoreStartId() {
        return storeStartId;
    }

    public void setStoreStartId(Integer storeStartId) {
        this.storeStartId = storeStartId;
    }

    public Integer getStoreFinishId() {
        return storeFinishId;
    }

    public void setStoreFinishId(Integer storeFinishId) {
        this.storeFinishId = storeFinishId;
    }

    public groups() {
        mlist = new ArrayList<mapping>();
    }

    //https://www.tutorialspoint.com/sqlite/sqlite_data_types.htm
    public List<mapping> Map() {

        for (int i = 0; i <= 16; i++) {
            mapping m = new mapping();

            if (i == 0) {
                m.name = "id";
                m.type = " INTEGER PRIMARY KEY,";
                mlist.add(m);
            }else if(i == 1) {
                m.name = "teamNo";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 2) {
                m.name = "groupNo";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 3) {
                m.name = "name";
                m.type = " TEXT,";
                mlist.add(m);
            } else if(i == 4) {
                m.name = "mission";
                m.type = " INTEGER,";
                mlist.add(m);
            } else if(i == 5) {
                m.name = "userId";
                m.type = " INTEGER,";
                mlist.add(m);
            } else if(i == 6) {
                m.name = "storeStartId";
                m.type = " INTEGER,";
                mlist.add(m);
            }
            else if(i == 7) {
                m.name = "storeFinishId";
                m.type = " INTEGER,";
                mlist.add(m);
            }
            else if(i == 8) {
                m.name = "isActive";
                m.type = " INTEGER,";
                mlist.add(m);
            }
            else if(i == 9) {
                m.name = "ipAddress";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 10) {
                m.name = "validFrom";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 11) {
                m.name = "validUntil";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 12) {
                m.name = "createdBy";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 13) {
                m.name = "createDate";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 14) {
                m.name = "changedBy";
                m.type = " TEXT,";
                mlist.add(m);
            }
            else if(i == 15) {
                m.name = "changedDate";
                m.type = " TEXT,";
                mlist.add(m);
            }else if(i == 16) {
                m.name = "company";
                m.type = " TEXT";
                mlist.add(m);
            }
        }
        return mlist;
    }
}
