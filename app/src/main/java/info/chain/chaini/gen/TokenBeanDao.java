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
 * DAO for table "TOKEN_BEAN".
*/
public class TokenBeanDao extends AbstractDao<TokenBean, Long> {

    public static final String TABLENAME = "TOKEN_BEAN";

    /**
     * Properties of entity TokenBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Token_name = new Property(1, String.class, "token_name", false, "TOKEN_NAME");
        public final static Property Token_info = new Property(2, String.class, "token_info", false, "TOKEN_INFO");
        public final static Property Token_icon = new Property(3, String.class, "token_icon", false, "TOKEN_ICON");
        public final static Property Token_icon_id = new Property(4, Long.class, "token_icon_id", false, "TOKEN_ICON_ID");
        public final static Property Cny_rate = new Property(5, Double.class, "cny_rate", false, "CNY_RATE");
        public final static Property Usd_rate = new Property(6, Double.class, "usd_rate", false, "USD_RATE");
        public final static Property Master = new Property(7, Long.class, "master", false, "MASTER");
        public final static Property Chain_id = new Property(8, Long.class, "chain_id", false, "CHAIN_ID");
    }

    private Query<TokenBean> chainBean_TokensQuery;

    public TokenBeanDao(DaoConfig config) {
        super(config);
    }
    
    public TokenBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TOKEN_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TOKEN_NAME\" TEXT," + // 1: token_name
                "\"TOKEN_INFO\" TEXT," + // 2: token_info
                "\"TOKEN_ICON\" TEXT," + // 3: token_icon
                "\"TOKEN_ICON_ID\" INTEGER," + // 4: token_icon_id
                "\"CNY_RATE\" REAL," + // 5: cny_rate
                "\"USD_RATE\" REAL," + // 6: usd_rate
                "\"MASTER\" INTEGER," + // 7: master
                "\"CHAIN_ID\" INTEGER);"); // 8: chain_id
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TOKEN_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, TokenBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String token_name = entity.getToken_name();
        if (token_name != null) {
            stmt.bindString(2, token_name);
        }
 
        String token_info = entity.getToken_info();
        if (token_info != null) {
            stmt.bindString(3, token_info);
        }
 
        String token_icon = entity.getToken_icon();
        if (token_icon != null) {
            stmt.bindString(4, token_icon);
        }
 
        Long token_icon_id = entity.getToken_icon_id();
        if (token_icon_id != null) {
            stmt.bindLong(5, token_icon_id);
        }
 
        Double cny_rate = entity.getCny_rate();
        if (cny_rate != null) {
            stmt.bindDouble(6, cny_rate);
        }
 
        Double usd_rate = entity.getUsd_rate();
        if (usd_rate != null) {
            stmt.bindDouble(7, usd_rate);
        }
 
        Long master = entity.getMaster();
        if (master != null) {
            stmt.bindLong(8, master);
        }
 
        Long chain_id = entity.getChain_id();
        if (chain_id != null) {
            stmt.bindLong(9, chain_id);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, TokenBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String token_name = entity.getToken_name();
        if (token_name != null) {
            stmt.bindString(2, token_name);
        }
 
        String token_info = entity.getToken_info();
        if (token_info != null) {
            stmt.bindString(3, token_info);
        }
 
        String token_icon = entity.getToken_icon();
        if (token_icon != null) {
            stmt.bindString(4, token_icon);
        }
 
        Long token_icon_id = entity.getToken_icon_id();
        if (token_icon_id != null) {
            stmt.bindLong(5, token_icon_id);
        }
 
        Double cny_rate = entity.getCny_rate();
        if (cny_rate != null) {
            stmt.bindDouble(6, cny_rate);
        }
 
        Double usd_rate = entity.getUsd_rate();
        if (usd_rate != null) {
            stmt.bindDouble(7, usd_rate);
        }
 
        Long master = entity.getMaster();
        if (master != null) {
            stmt.bindLong(8, master);
        }
 
        Long chain_id = entity.getChain_id();
        if (chain_id != null) {
            stmt.bindLong(9, chain_id);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public TokenBean readEntity(Cursor cursor, int offset) {
        TokenBean entity = new TokenBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // token_name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // token_info
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // token_icon
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4), // token_icon_id
            cursor.isNull(offset + 5) ? null : cursor.getDouble(offset + 5), // cny_rate
            cursor.isNull(offset + 6) ? null : cursor.getDouble(offset + 6), // usd_rate
            cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7), // master
            cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8) // chain_id
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, TokenBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setToken_name(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setToken_info(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setToken_icon(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setToken_icon_id(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
        entity.setCny_rate(cursor.isNull(offset + 5) ? null : cursor.getDouble(offset + 5));
        entity.setUsd_rate(cursor.isNull(offset + 6) ? null : cursor.getDouble(offset + 6));
        entity.setMaster(cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7));
        entity.setChain_id(cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(TokenBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(TokenBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(TokenBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "tokens" to-many relationship of ChainBean. */
    public List<TokenBean> _queryChainBean_Tokens(Long chain_id) {
        synchronized (this) {
            if (chainBean_TokensQuery == null) {
                QueryBuilder<TokenBean> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Chain_id.eq(null));
                chainBean_TokensQuery = queryBuilder.build();
            }
        }
        Query<TokenBean> query = chainBean_TokensQuery.forCurrentThread();
        query.setParameter(0, chain_id);
        return query.list();
    }

}
