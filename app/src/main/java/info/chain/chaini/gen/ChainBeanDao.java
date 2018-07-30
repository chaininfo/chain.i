package info.chain.chaini.gen;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CHAIN_BEAN".
*/
public class ChainBeanDao extends AbstractDao<ChainBean, Long> {

    public static final String TABLENAME = "CHAIN_BEAN";

    /**
     * Properties of entity ChainBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Chain_name = new Property(1, String.class, "chain_name", false, "CHAIN_NAME");
        public final static Property Chain_type = new Property(2, String.class, "chain_type", false, "CHAIN_TYPE");
        public final static Property Chain_info = new Property(3, String.class, "chain_info", false, "CHAIN_INFO");
        public final static Property Chain_icon = new Property(4, String.class, "chain_icon", false, "CHAIN_ICON");
        public final static Property Chain_icon_id = new Property(5, Long.class, "chain_icon_id", false, "CHAIN_ICON_ID");
        public final static Property User_id = new Property(6, Long.class, "user_id", false, "USER_ID");
        public final static Property Master = new Property(7, Long.class, "master", false, "MASTER");
    }

    private DaoSession daoSession;

    private Query<ChainBean> userBean_ChainsQuery;

    public ChainBeanDao(DaoConfig config) {
        super(config);
    }
    
    public ChainBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CHAIN_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"CHAIN_NAME\" TEXT," + // 1: chain_name
                "\"CHAIN_TYPE\" TEXT," + // 2: chain_type
                "\"CHAIN_INFO\" TEXT," + // 3: chain_info
                "\"CHAIN_ICON\" TEXT," + // 4: chain_icon
                "\"CHAIN_ICON_ID\" INTEGER," + // 5: chain_icon_id
                "\"USER_ID\" INTEGER," + // 6: user_id
                "\"MASTER\" INTEGER);"); // 7: master
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CHAIN_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ChainBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String chain_name = entity.getChain_name();
        if (chain_name != null) {
            stmt.bindString(2, chain_name);
        }
 
        String chain_type = entity.getChain_type();
        if (chain_type != null) {
            stmt.bindString(3, chain_type);
        }
 
        String chain_info = entity.getChain_info();
        if (chain_info != null) {
            stmt.bindString(4, chain_info);
        }
 
        String chain_icon = entity.getChain_icon();
        if (chain_icon != null) {
            stmt.bindString(5, chain_icon);
        }
 
        Long chain_icon_id = entity.getChain_icon_id();
        if (chain_icon_id != null) {
            stmt.bindLong(6, chain_icon_id);
        }
 
        Long user_id = entity.getUser_id();
        if (user_id != null) {
            stmt.bindLong(7, user_id);
        }
 
        Long master = entity.getMaster();
        if (master != null) {
            stmt.bindLong(8, master);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ChainBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String chain_name = entity.getChain_name();
        if (chain_name != null) {
            stmt.bindString(2, chain_name);
        }
 
        String chain_type = entity.getChain_type();
        if (chain_type != null) {
            stmt.bindString(3, chain_type);
        }
 
        String chain_info = entity.getChain_info();
        if (chain_info != null) {
            stmt.bindString(4, chain_info);
        }
 
        String chain_icon = entity.getChain_icon();
        if (chain_icon != null) {
            stmt.bindString(5, chain_icon);
        }
 
        Long chain_icon_id = entity.getChain_icon_id();
        if (chain_icon_id != null) {
            stmt.bindLong(6, chain_icon_id);
        }
 
        Long user_id = entity.getUser_id();
        if (user_id != null) {
            stmt.bindLong(7, user_id);
        }
 
        Long master = entity.getMaster();
        if (master != null) {
            stmt.bindLong(8, master);
        }
    }

    @Override
    protected final void attachEntity(ChainBean entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public ChainBean readEntity(Cursor cursor, int offset) {
        ChainBean entity = new ChainBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // chain_name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // chain_type
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // chain_info
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // chain_icon
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5), // chain_icon_id
            cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6), // user_id
            cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7) // master
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ChainBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setChain_name(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setChain_type(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setChain_info(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setChain_icon(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setChain_icon_id(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
        entity.setUser_id(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
        entity.setMaster(cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ChainBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ChainBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ChainBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "chains" to-many relationship of UserBean. */
    public List<ChainBean> _queryUserBean_Chains(Long user_id) {
        synchronized (this) {
            if (userBean_ChainsQuery == null) {
                QueryBuilder<ChainBean> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.User_id.eq(null));
                userBean_ChainsQuery = queryBuilder.build();
            }
        }
        Query<ChainBean> query = userBean_ChainsQuery.forCurrentThread();
        query.setParameter(0, user_id);
        return query.list();
    }

}
