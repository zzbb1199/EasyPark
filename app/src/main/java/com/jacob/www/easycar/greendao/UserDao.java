package com.jacob.www.easycar.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.jacob.www.easycar.data.User;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER".
*/
public class UserDao extends AbstractDao<User, Long> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Icon = new Property(1, String.class, "icon", false, "ICON");
        public final static Property Phone = new Property(2, String.class, "phone", false, "PHONE");
        public final static Property Uid = new Property(3, String.class, "uid", false, "UID");
        public final static Property Username = new Property(4, String.class, "username", false, "USERNAME");
        public final static Property PlateNums = new Property(5, String.class, "plateNums", false, "PLATE_NUMS");
    };


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"ICON\" TEXT," + // 1: icon
                "\"PHONE\" TEXT," + // 2: phone
                "\"UID\" TEXT," + // 3: uid
                "\"USERNAME\" TEXT," + // 4: username
                "\"PLATE_NUMS\" TEXT);"); // 5: plateNums
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String icon = entity.getIcon();
        if (icon != null) {
            stmt.bindString(2, icon);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(3, phone);
        }
 
        String uid = entity.getUid();
        if (uid != null) {
            stmt.bindString(4, uid);
        }
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(5, username);
        }
 
        String plateNums = entity.getPlateNums();
        if (plateNums != null) {
            stmt.bindString(6, plateNums);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String icon = entity.getIcon();
        if (icon != null) {
            stmt.bindString(2, icon);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(3, phone);
        }
 
        String uid = entity.getUid();
        if (uid != null) {
            stmt.bindString(4, uid);
        }
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(5, username);
        }
 
        String plateNums = entity.getPlateNums();
        if (plateNums != null) {
            stmt.bindString(6, plateNums);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // icon
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // phone
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // uid
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // username
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // plateNums
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setIcon(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPhone(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUid(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUsername(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setPlateNums(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(User entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(User entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}