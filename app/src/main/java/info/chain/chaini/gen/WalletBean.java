package info.chain.chaini.gen;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

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
public class WalletBean {
    @Id(autoincrement = true)
    private Long id;
    private String wallet_uid;
    private String wallet_name;
    private String wallet_type;
    private String wallet_img;
    private String wallet_weixin;
    private String wallet_qq;
    private String wallet_phone;
    private String wallet_shapwd;
    private String password_check;
    private String wallet_otherpwd;

    @Keep
    public WalletBean(Long id, String wallet_uid, String wallet_name, String wallet_type, String wallet_img,
            String wallet_weixin, String wallet_qq, String wallet_phone, String wallet_shapwd,
            String password_check, String wallet_otherpwd) {
        this.id = id;
        this.wallet_uid = wallet_uid;
        this.wallet_name = wallet_name;
        this.wallet_type = wallet_type;
        this.wallet_img = wallet_img;
        this.wallet_weixin = wallet_weixin;
        this.wallet_qq = wallet_qq;
        this.wallet_phone = wallet_phone;
        this.wallet_shapwd = wallet_shapwd;
        this.password_check = password_check;
        this.wallet_otherpwd = wallet_otherpwd;
    }

    public WalletBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWallet_uid() {
        return this.wallet_uid;
    }

    public void setWallet_uid(String wallet_uid) {
        this.wallet_uid = wallet_uid;
    }

    public String getWallet_name() {
        return this.wallet_name;
    }

    public void setWallet_name(String wallet_name) {
        this.wallet_name = wallet_name;
    }

    public String getWallet_type() {
        return this.wallet_type;
    }

    public void setWallet_type(String wallet_type) {
        this.wallet_type = wallet_type;
    }

    public String getWallet_img() {
        return this.wallet_img;
    }

    public void setWallet_img(String wallet_img) {
        this.wallet_img = wallet_img;
    }

    public String getWallet_weixin() {
        return this.wallet_weixin;
    }

    public void setWallet_weixin(String wallet_weixin) {
        this.wallet_weixin = wallet_weixin;
    }

    public String getWallet_qq() {
        return this.wallet_qq;
    }

    public void setWallet_qq(String wallet_qq) {
        this.wallet_qq = wallet_qq;
    }

    public String getWallet_phone() {
        return this.wallet_phone;
    }

    public void setWallet_phone(String wallet_phone) {
        this.wallet_phone = wallet_phone;
    }

    public String getPassword_check() {
        return this.password_check;
    }

    public void setPassword_check(String password_check) {
        this.password_check = password_check;
    }

    public String getWallet_shapwd() {
        return this.wallet_shapwd;
    }

    public void setWallet_shapwd(String wallet_shapwd) {
        this.wallet_shapwd = wallet_shapwd;
    }


    public String getAccount_info() {
        return this.wallet_shapwd;
    }

    public void setAccount_info(String wallet_shapwd) {
        this.wallet_shapwd = wallet_shapwd;
    }

    public String getWallet_otherpwd() {
        return this.wallet_otherpwd;
    }

    public void setWallet_otherpwd(String wallet_otherpwd) {
        this.wallet_otherpwd = wallet_otherpwd;
    }
}
