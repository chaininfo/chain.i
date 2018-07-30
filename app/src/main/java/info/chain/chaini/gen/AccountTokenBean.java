package info.chain.chaini.gen;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;
import org.greenrobot.greendao.DaoException;

/**
 * Created by pocketEos on 2018/1/27.
 */

@Entity
public class AccountTokenBean {

    /**
     * server_version : 2cc40a4e
     * head_block_num : 307
     * last_irreversible_block_num : 289
     * head_block_id : 00000133b3261702a51f6d10a254dd682ef0def232442ee3b3d80be967dcd4ae
     * head_block_time : 2018-01-22T02:36:58
     * head_block_producer : inith
     * recent_slots : 1111111111111111111111111111111111111111111111111111111111111111
     * participation_rate : 1.00000000000000000
     */
    public AccountTokenBean() {

    }

    @Generated(hash = 1488669856)
    public AccountTokenBean(Long id, Long account_id, Long token_id, Double token_balance, String token_info, Long master) {
        this.id = id;
        this.account_id = account_id;
        this.token_id = token_id;
        this.token_balance = token_balance;
        this.token_info = token_info;
        this.master = master;
    }

    @Id(autoincrement = true)
    private Long id;
    private Long account_id;
    private Long token_id;
    private Double token_balance;
    private String token_info;
    private Long master;

    //
    @ToOne(joinProperty = "token_id")
    private TokenBean token;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 485203933)
    private transient AccountTokenBeanDao myDao;
    @Generated(hash = 2059307683)
    private transient Long token__resolvedKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Long account_id) {
        this.account_id = account_id;
    }

    public Long getToken_id() {
        return token_id;
    }

    public void setToken_id(Long token_id) {
        this.token_id = token_id;
    }

    public Double getToken_balance() {
        return token_balance;
    }

    public void setToken_balance(Double token_balance) {
        this.token_balance = token_balance;
    }

    public String getToken_info() {
        return token_info == null ? "" : token_info;
    }

    public void setToken_info(String token_info) {
        this.token_info = token_info;
    }

    public Long getMaster() {
        return master;
    }

    public void setMaster(Long master) {
        this.master = master;
    }

    public String toString() {
        return "{\n" +//
                "\tid=" + id + "\n" +//
                "\taccount_id=" + account_id + "\n" +//
                "\ttoken_id=" + token_id + "\n" +//
                "\ttoken_balance=" + token_balance + "\n" +//
                "\ttoken_info=" + token_info + "\n" +//
                "\tmaster=" + master + "\n" +//
                '}';
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2066766303)
    public TokenBean getToken() {
        Long __key = this.token_id;
        if (token__resolvedKey == null || !token__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TokenBeanDao targetDao = daoSession.getTokenBeanDao();
            TokenBean tokenNew = targetDao.load(__key);
            synchronized (this) {
                token = tokenNew;
                token__resolvedKey = __key;
            }
        }
        return token;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1619119115)
    public void setToken(TokenBean token) {
        synchronized (this) {
            this.token = token;
            token_id = token == null ? null : token.getId();
            token__resolvedKey = token_id;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1673562193)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAccountTokenBeanDao() : null;
    }
}