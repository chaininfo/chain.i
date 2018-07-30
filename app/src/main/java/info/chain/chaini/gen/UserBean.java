package info.chain.chaini.gen;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.DaoException;

/**
 * Created by pocketEos on 2017/12/27.
 *
 * @Entity：告诉GreenDao该对象为实体，只有被@Entity注释的Bean类才能被dao类操作
 * @Id：对象的Id，使用Long类型作为EntityId，否则会报错。(autoincrement = true)表示主键会自增，如果false就会使用旧值
 * @Property：可以自定义字段名，注意外键不能使用该属性
 * @NotNull：属性不能为空
 * @Transient：使用该注释的属性不会被存入数据库的字段中
 * @Unique：该属性值必须在数据库中是唯一值
 * @Generated：编译后自动生成的构造函数、方法等的注释，提示构造函数、方法等不能被修改
 */
@Entity
public class UserBean {
    @Id(autoincrement = true)
    private Long id;
    private String user_uid;
    private String user_name;
    private String user_img;
    private String user_phone;
    private Long chain_init;
    private Long account_init;
    private Long account_token_init;
    private Long token_init;
    private Long app_init;
    private Long wallet_init;
    private String user_info;

    //
    @ToMany(referencedJoinProperty = "user_id")
    private List<ChainBean> chains;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 83707551)
    private transient UserBeanDao myDao;

    @Keep
    public UserBean(Long id, String user_uid, String user_name, String user_img, String user_phone,
                    Long chain_init, Long account_init, Long account_token_init, Long token_init, Long app_init, Long wallet_init, String user_info) {
        this.id = id;
        this.user_uid = user_uid;
        this.user_name = user_name;
        this.user_img = user_img;
        this.user_phone = user_phone;
        this.chain_init = chain_init;
        this.account_init = account_init;
        this.account_token_init = account_token_init;
        this.token_init = token_init;
        this.app_init = app_init;
        this.wallet_init = wallet_init;
        this.user_info = user_info;
    }

    public UserBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser_uid() {
        return this.user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getUser_name() {
        return this.user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_img() {
        return this.user_img;
    }

    public void setUser_img(String user_img) {
        this.user_img = user_img;
    }

    public String getUser_phone() {
        return this.user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public Long getChain_init() {
        return this.chain_init;
    }

    public void setChain_init(Long chain_init) {
        this.chain_init = chain_init;
    }

    public Long getAccount_token_init() {
        return this.account_token_init;
    }

    public void setAccount_token_init(Long account_token_init) {
        this.account_token_init = account_token_init;
    }

    public Long getAccount_init() {
        return this.account_init;
    }

    public void setAccount_init(Long account_init) {
        this.account_init = account_init;
    }

    public Long getToken_init() {
        return this.token_init;
    }

    public void setToken_init(Long token_init) {
        this.token_init = token_init;
    }


    public Long getApp_init() {
        return this.app_init;
    }

    public void setApp_init(Long app_init) {
        this.app_init = app_init;
    }

    public Long getWallet_init() {
        return this.wallet_init;
    }

    public void setWallet_init(Long wallet_init) {
        this.wallet_init = wallet_init;
    }

    public String getUser_info() {
        return this.user_info;
    }

    public void setUser_info(String user_info) {
        this.user_info = user_info;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1296850008)
    public List<ChainBean> getChains() {
        if (chains == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ChainBeanDao targetDao = daoSession.getChainBeanDao();
            List<ChainBean> chainsNew = targetDao._queryUserBean_Chains(id);
            synchronized (this) {
                if (chains == null) {
                    chains = chainsNew;
                }
            }
        }
        return chains;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1252340040)
    public synchronized void resetChains() {
        chains = null;
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
    @Generated(hash = 1491512534)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserBeanDao() : null;
    }
}

