package info.chain.chaini.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "APP_BEAN".
*/
public class AppBeanDao extends AbstractDao<AppBean, Long> {

    public static final String TABLENAME = "APP_BEAN";

    /**
     * Properties of entity AppBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property App_name = new Property(1, String.class, "app_name", false, "APP_NAME");
        public final static Property App_info = new Property(2, String.class, "app_info", false, "APP_INFO");
        public final static Property Master_account = new Property(3, Long.class, "master_account", false, "MASTER_ACCOUNT");
        public final static Property Chain_id = new Property(4, Long.class, "chain_id", false, "CHAIN_ID");
        public final static Property User_id = new Property(5, Long.class, "user_id", false, "USER_ID");
    }


    public AppBeanDao(DaoConfig config) {
        super(config);
    }
    
    public AppBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"APP_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"APP_NAME\" TEXT," + // 1: app_name
                "\"APP_INFO\" TEXT," + // 2: app_info
                "\"MASTER_ACCOUNT\" INTEGER," + // 3: master_account
                "\"CHAIN_ID\" INTEGER," + // 4: chain_id
                "\"USER_ID\" INTEGER);"); // 5: user_id
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"APP_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, AppBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String app_name = entity.getApp_name();
        if (app_name != null) {
            stmt.bindString(2, app_name);
        }
 
        String app_info = entity.getApp_info();
        if (app_info != null) {
            stmt.bindString(3, app_info);
        }
 
        Long master_account = entity.getMaster_account();
        if (master_account != null) {
            stmt.bindLong(4, master_account);
        }
 
        Long chain_id = entity.getChain_id();
        if (chain_id != null) {
            stmt.bindLong(5, chain_id);
        }
 
        Long user_id = entity.getUser_id();
        if (user_id != null) {
            stmt.bindLong(6, user_id);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, AppBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String app_name = entity.getApp_name();
        if (app_name != null) {
            stmt.bindString(2, app_name);
        }
 
        String app_info = entity.getApp_info();
        if (app_info != null) {
            stmt.bindString(3, app_info);
        }
 
        Long master_account = entity.getMaster_account();
        if (master_account != null) {
            stmt.bindLong(4, master_account);
        }
 
        Long chain_id = entity.getChain_id();
        if (chain_id != null) {
            stmt.bindLong(5, chain_id);
        }
 
        Long user_id = entity.getUser_id();
        if (user_id != null) {
            stmt.bindLong(6, user_id);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public AppBean readEntity(Cursor cursor, int offset) {
        AppBean entity = new AppBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // app_name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // app_info
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // master_account
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4), // chain_id
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5) // user_id
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, AppBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setApp_name(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setApp_info(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setMaster_account(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setChain_id(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
        entity.setUser_id(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(AppBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(AppBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(AppBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
