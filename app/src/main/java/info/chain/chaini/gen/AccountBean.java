package info.chain.chaini.gen;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.ToOne;

/**
 * Created by pocketEos on 2018/1/27.
 */

@Entity
public class AccountBean {

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
    public AccountBean(Long id, String account_name, String account_info, String account_icon, Long account_icon_id, Long master, Long chain_id, Long user_id) {
        this.id = id;
        this.account_name = account_name;
        this.account_info = account_info;
        this.account_icon = account_icon;
        this.account_icon_id = account_icon_id;
        this.master = master;
        this.chain_id = chain_id;
        this.user_id = user_id;
    }

    public AccountBean() {

    }

    @Id(autoincrement = true)
    private Long id;
    private String account_name;
    private String account_info;
    private String account_icon;
    private Long account_icon_id;
    private Long master;
    private Long chain_id;
    private Long user_id;

    //
    @ToMany(referencedJoinProperty = "account_id")
    private List<AccountTokenBean> accounttokenBeans;
    //
    @ToOne(joinProperty = "chain_id")
    private ChainBean chain;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 894456595)
    private transient AccountBeanDao myDao;
    @Generated(hash = 1103199144)
    private transient Long chain__resolvedKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount_name() {
        return account_name == null ? "" : account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAccount_info() {
        return account_info == null ? "" : account_info;
    }

    public void setAccount_info(String account_info) {
        this.account_info = account_info;
    }

    public String getAccount_icon() {
        return account_icon == null ? "" : account_icon;
    }

    public void setAccount_icon(String account_icon) {
        this.account_icon = account_icon;
    }

    public Long getAccount_icon_id() {
        return account_icon_id;
    }

    public void setAccount_icon_id(Long account_icon_id) {
        this.account_icon_id = account_icon_id;
    }

    public Long getMaster() {
        return master;
    }

    public void setMaster(Long master) {
        this.master = master;
    }

    public Long getChain_id() {
        return chain_id;
    }

    public void setChain_id(Long chain_id) {
        this.chain_id = chain_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String toString() {
        return "{\n" +//
                "\tid=" + id + "\n" +//
                "\taccount_name=" + account_name + "\n" +//
                "\taccount_info=" + account_info + "\n" +//
                "\taccount_icon=" + account_icon + "\n" +//
                "\taccount_icon_id=" + account_icon_id + "\n" +//
                "\tmaster=" + master + "\n" +//
                "\tchain_id=" + chain_id + "\n" +//
                "\tuser_id=" + user_id + "\n" +//
                '}';
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
    @Generated(hash = 851289874)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAccountBeanDao() : null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1395494135)
    public List<AccountTokenBean> getAccounttokenBeans() {
        if (accounttokenBeans == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AccountTokenBeanDao targetDao = daoSession.getAccountTokenBeanDao();
            List<AccountTokenBean> accounttokenBeansNew = targetDao._queryAccountBean_AccounttokenBeans(id);
            synchronized (this) {
                if (accounttokenBeans == null) {
                    accounttokenBeans = accounttokenBeansNew;
                }
            }
        }
        return accounttokenBeans;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 773557852)
    public synchronized void resetAccounttokenBeans() {
        accounttokenBeans = null;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1789082197)
    public ChainBean getChain() {
        Long __key = this.chain_id;
        if (chain__resolvedKey == null || !chain__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ChainBeanDao targetDao = daoSession.getChainBeanDao();
            ChainBean chainNew = targetDao.load(__key);
            synchronized (this) {
                chain = chainNew;
                chain__resolvedKey = __key;
            }
        }
        return chain;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1320522928)
    public void setChain(ChainBean chain) {
        synchronized (this) {
            this.chain = chain;
            chain_id = chain == null ? null : chain.getId();
            chain__resolvedKey = chain_id;
        }
    }
}