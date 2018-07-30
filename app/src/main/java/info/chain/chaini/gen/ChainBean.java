package info.chain.chaini.gen;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.DaoException;

/**
 * Created by pocketEos on 2018/1/27.
 */

@Entity
public class ChainBean {
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
    @Keep
    public ChainBean(Long id, String chain_name, String chain_type, String chain_info, String chain_icon, Long chain_icon_id, Long user_id, Long master) {
        this.id = id;
        this.chain_name = chain_name;
        this.chain_type = chain_type;
        this.chain_info = chain_info;
        this.chain_icon = chain_icon;
        this.chain_icon_id = chain_icon_id;
        this.user_id = user_id;
        this.master = master;
    }

    public ChainBean() {

    }

    @Id(autoincrement = true)
    private Long id;
    private String chain_name;
    private String chain_type;
    private String chain_info;
    private String chain_icon;
    private Long chain_icon_id;
    private Long user_id;
    private Long master;

    //
    @ToMany(referencedJoinProperty = "chain_id")
    private List<AccountBean> accounts;
    //
    @ToMany(referencedJoinProperty = "chain_id")
    private List<TokenBean> tokens;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1093128647)
    private transient ChainBeanDao myDao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChain_name() {
        return chain_name == null ? "" : chain_name;
    }

    public void setChain_name(String chain_name) {
        this.chain_name = chain_name;
    }

    public String getChain_type() {
        return chain_type == null ? "" : chain_type;
    }

    public void setChain_type(String chain_type) {
        this.chain_type = chain_type;
    }

    public String getChain_info() {
        return chain_info == null ? "" : chain_info;
    }

    public void setChain_info(String chain_info) {
        this.chain_info = chain_info;
    }

    public String getChain_icon() {
        return chain_icon == null ? "" : chain_icon;
    }

    public void setChain_icon(String chain_icon) {
        this.chain_icon = chain_icon;
    }

    public Long getChain_icon_id() {
        return chain_icon_id;
    }

    public void setChain_icon_id(Long chain_icon_id) {
        this.chain_icon_id = chain_icon_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
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
                "\tchain_name=" + chain_name + "\n" +//
                "\tchain_type=" + chain_type + "\n" +//
                "\tchain_info=" + chain_info + "\n" +//
                "\tchain_icon=" + chain_icon + "\n" +//
                "\tchain_icon_id=" + chain_icon_id + "\n" +//
                "\tuser_id=" + user_id + "\n" +//
                "\tmaster=" + master + "\n" +//
                '}';
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1268400748)
    public List<AccountBean> getAccounts() {
        if (accounts == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AccountBeanDao targetDao = daoSession.getAccountBeanDao();
            List<AccountBean> accountsNew = targetDao._queryChainBean_Accounts(id);
            synchronized (this) {
                if (accounts == null) {
                    accounts = accountsNew;
                }
            }
        }
        return accounts;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 121514453)
    public synchronized void resetAccounts() {
        accounts = null;
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
    @Generated(hash = 580666594)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getChainBeanDao() : null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 163584401)
    public List<TokenBean> getTokens() {
        if (tokens == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TokenBeanDao targetDao = daoSession.getTokenBeanDao();
            List<TokenBean> tokensNew = targetDao._queryChainBean_Tokens(id);
            synchronized (this) {
                if (tokens == null) {
                    tokens = tokensNew;
                }
            }
        }
        return tokens;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1493810148)
    public synchronized void resetTokens() {
        tokens = null;
    }
}