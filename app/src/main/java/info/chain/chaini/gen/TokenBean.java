package info.chain.chaini.gen;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by pocketEos on 2018/1/27.
 */

@Entity
public class TokenBean {

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
    public TokenBean(Long id, String token_name, String token_info, String token_icon, Long token_icon_id, Double cny_rate, Double usd_rate, Long master, Long chain_id) {
        this.id = id;
        this.token_name = token_name;
        this.token_info = token_info;
        this.token_icon = token_icon;
        this.token_icon_id = token_icon_id;
        this.cny_rate = cny_rate;
        this.usd_rate = usd_rate;
        this.master = master;
        this.chain_id = chain_id;
    }

    public TokenBean() {

    }

    @Id(autoincrement = true)
    private Long id;
    private String token_name;
    private String token_info;
    private String token_icon;
    private Long token_icon_id;
    private Double cny_rate;
    private Double usd_rate;
    private Long master;
    private Long chain_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken_name() {
        return token_name == null ? "" : token_name;
    }

    public void setToken_name(String token_name) {
        this.token_name = token_name;
    }

    public String getToken_info() {
        return token_info == null ? "" : token_info;
    }

    public void setToken_info(String token_info) {
        this.token_info = token_info;
    }

    public String getToken_icon() {
        return token_icon == null ? "" : token_icon;
    }

    public void setToken_icon(String token_icon) {
        this.token_icon = token_icon;
    }

    public Long getToken_icon_id() {
        return token_icon_id;
    }

    public void setToken_icon_id(Long token_icon_id) {
        this.token_icon_id = token_icon_id;
    }

    public Double getCny_rate() {
        return cny_rate;
    }

    public void setCny_rate(Double cny_rate) {
        this.cny_rate = cny_rate;
    }

    public Double getUsd_rate() {
        return usd_rate;
    }

    public void setUsd_rate(Double usd_rate) {
        this.usd_rate = usd_rate;
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

    public String toString() {
        return "{\n" +//
                "\tid=" + id + "\n" +//
                "\ttoken_name=" + token_name + "\n" +//
                "\ttoken_info=" + token_info + "\n" +//
                "\ttoken_icon=" + token_icon + "\n" +//
                "\ttoken_icon_id=" + token_icon_id + "\n" +//
                "\tcny_rate=" + cny_rate + "\n" +//
                "\tusd_rate=" + usd_rate + "\n" +//
                "\tmaster=" + master + "\n" +//
                "\tchain_id=" + chain_id + "\n" +//
                '}';
    }
}