package info.chain.chaini.gen;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by pocketEos on 2018/1/27.
 */

@Entity
public class AppBean {

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
    public AppBean(Long id, String app_name, String app_info, Long master_account, Long chain_id, Long user_id) {
        this.id = id;
        this.app_name = app_name;
        this.app_info = app_info;
        this.chain_id = chain_id;
        this.user_id = user_id;
    }

    public AppBean() {

    }

    @Id(autoincrement = true)
    private Long id;
    private String app_name;
    private String app_info;
    private Long master_account;
    private Long chain_id;
    private Long user_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApp_name() {
        return app_name == null ? "" : app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    public String getApp_info() {
        return app_info == null ? "" : app_info;
    }

    public void setApp_info(String app_info) {
        this.app_info = app_info;
    }

    public Long getMaster_account() {
        return master_account;
    }

    public void setMaster_account(Long master_account) {
        this.master_account = master_account;
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
                "\taccount_name=" + app_name + "\n" +//
                "\taccount_info=" + app_info + "\n" +//
                "\tmaster_account=" + master_account + "\n" +//
                "\tchain_id=" + chain_id + "\n" +//
                "\tuser_id=" + user_id + "\n" +//
                '}';
    }
}