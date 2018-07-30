package info.chain.chaini.app;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import javax.inject.Inject;

import info.chain.chaini.eosapi.EosDataManager;
import info.chain.chaini.eosapi.wallet.EosWalletManager;
import info.chain.chaini.gen.AccountBean;
import info.chain.chaini.gen.AccountTokenBean;
import info.chain.chaini.gen.AppBean;
import info.chain.chaini.gen.ChainBean;
import info.chain.chaini.gen.DaoMaster;
import info.chain.chaini.gen.DaoSession;
import info.chain.chaini.gen.TokenBean;
import info.chain.chaini.gen.UserBean;
import info.chain.chaini.gen.WalletBean;
import info.chain.chaini.gen.WalletBeanDao;
import info.chain.chaini.utils.Utils;
import okhttp3.OkHttpClient;

/**
 * Created by pocketEos on 2017/11/23.
 */

public class MyApplication extends MultiDexApplication {
    private static MyApplication mInstance;
    private static DaoSession daoSession;
    //
    private UserBean userBean;
    private WalletBean walletBean;
    private List<AppBean> appBeans;

    //
    private static String WALLET_NAME = "default";
    private static String PREF_WALLET_DIR_NAME= "wallets";

    @Inject
    EosDataManager eosapi;
    private ActivityComponent mActivityComponent;


    public static MyApplication getInstance() {
        return mInstance;
    }
    public static DaoSession getDaoInstant() {return mInstance.daoSession;}

    /*
    user
     */
    public static UserBean getUserBean() {
        return mInstance.userBean;
    }
    public static void setUserBean(UserBean userBean) {
        mInstance.userBean = userBean;
    }
    public static void updateUserBean(UserBean userBean) {
        setUserBean(userBean);
        daoSession.getUserBeanDao().update(userBean);
    }


    /*
    eos api
     */
    public static EosDataManager getEosApi() {return mInstance.eosapi;}
    public static EosWalletManager getEosWallet() {return mInstance.eosapi.getWalletManager();}

    /*
    wallet
     */
    public static WalletBean getWallet() {return mInstance.walletBean;}
    public static void updateWallet(WalletBean walletBean) {
        mInstance.walletBean = walletBean;
        daoSession.getWalletBeanDao().update(walletBean);
    }

    /*
    app
     */
    public static List<AppBean> getApps() { return mInstance.appBeans;}
    public static void addApp(AppBean appBean) {
        if(getApp(appBean.getApp_name()) != null)
            return;
        mInstance.appBeans.add(appBean);
        daoSession.getAppBeanDao().insert(appBean);
    }
    public static AppBean getApp(Long id) {
        for(AppBean appBean : mInstance.appBeans) {
            if(appBean.getId().equals(id))
                return appBean;
        }
        return null;
    }
    public static AppBean getApp(String name) {
        for(AppBean appBean : mInstance.appBeans) {
            if(appBean.getApp_name().equals(name))
                return appBean;
        }
        return null;
    }
    public static int indexApp(Long id) {
        int index = 0;
        for(AppBean tmpappBean : mInstance.appBeans) {
            if(tmpappBean.getId().equals(id)) {
                return index;
            }
            index ++;
        }
        return -1;
    }
    public static int indexApp(String name) {
        int index = 0;
        for(AppBean tmpappBean : mInstance.appBeans) {
            if(tmpappBean.getApp_name().equals(name)) {
                return index;
            }
            index ++;
        }
        return -1;
    }
    public static void updateApp(AppBean appBean) {
        int index = indexApp(appBean.getId());
        if(index == -1)
            return;
        mInstance.appBeans.set(index, appBean);
        daoSession.getAppBeanDao().update(appBean);
    }

    /*
    chain
     */
    public static List<ChainBean> getChains() {
        return mInstance.userBean.getChains();}
    public static void addChain(ChainBean chainBean) {
        if(getChain(chainBean.getChain_name()) != null)
            return;
        chainBean.setUser_id(mInstance.userBean.getId());
        getChains().add(chainBean);
        daoSession.getChainBeanDao().insert(chainBean);
    }
    public static ChainBean getChain(Long id) {
        List<ChainBean> chainBeans = getChains();
        for(ChainBean chainBean : chainBeans) {
            if(chainBean.getId().equals(id))
                return chainBean;
        }
        return null;
    }
    public static ChainBean getChain(String name) {
        List<ChainBean> chainBeans = getChains();
        for(ChainBean chainBean : chainBeans) {
            if(chainBean.getChain_name().equals(name))
                return chainBean;
        }
        return null;
    }
    public static int indexChain(Long id) {
        int index = 0;
        List<ChainBean> chainBeans = getChains();
        for(ChainBean tempchainBean : chainBeans) {
            if(tempchainBean.getId().equals(id)) {
                return index;
            }
        }
        return -1;
    }
    public static int indexChain(String name) {
        int index = 0;
        List<ChainBean> chainBeans = getChains();
        for(ChainBean tempchainBean : chainBeans) {
            if(tempchainBean.getChain_name().equals(name)) {
                return index;
            }
        }
        return -1;
    }
    public static void updateChain(ChainBean chainBean) {
        int index = indexChain(chainBean.getId());
        if(index == -1)
            return;
        getChains().set(index, chainBean);
        daoSession.getChainBeanDao().update(chainBean);
    }
    public static ChainBean getChainMaster() {
        List<ChainBean> chainBeans = getChains();
        for(ChainBean chainBean : chainBeans) {
            if(chainBean.getMaster().equals(1))
                return chainBean;
        }
        return null;
    }
    public static void setChainMaster(Long id) {
        ChainBean chainBean = getChain(id);
        if(chainBean == null)
            return;
        chainBean.setMaster(new Long(1));
        updateChain(chainBean);
    }
    public static ChainBean getChain() {
        //
        Long chainId = Utils.getSpUtils().getLong("chain_used");
        ChainBean chainBean = getChain(chainId);
        if(chainBean != null)
            return chainBean;

        chainBean = getChainMaster();
        if(chainBean != null)
            return chainBean;

        List<ChainBean> chainBeans = getChains();
        if(chainBeans.size() <= 0)
            return null;

        chainBean = chainBeans.get(0);
        return chainBean;
    }

    /*
    account
     */
    public static List<AccountBean> getAccounts(Long chainId) { return getChain(chainId).getAccounts();}
    public static void addAccount(Long chainId, AccountBean accountBean) {
        if(getAccount(chainId, accountBean.getAccount_name()) != null)
            return;
        List<AccountBean> accounts = getAccounts(chainId);
        accountBean.setUser_id(mInstance.userBean.getId());
        accountBean.setChain_id(chainId);
        accounts.add(accountBean);
        daoSession.getAccountBeanDao().insert(accountBean);
    }
    public static AccountBean getAccount(Long chainId, Long id) {
        List<AccountBean> accounts = getAccounts(chainId);
        for(AccountBean accountBean : accounts) {
            if(accountBean.getId().equals(id))
                return accountBean;
        }
        return null;
    }
    public static AccountBean getAccount(Long chainId, String name) {
        List<AccountBean> accounts = getAccounts(chainId);
        for(AccountBean accountBean : accounts) {
            if(accountBean.getAccount_name().equals(name))
                return accountBean;
        }
        return null;
    }
    public static int indexAccount(Long chainId, Long id) {
        int index = 0;
        List<AccountBean> accounts = getAccounts(chainId);
        for(AccountBean tempaccountBean : accounts) {
            if(tempaccountBean.getId().equals(id)) {
                return index;
            }
        }
        return -1;
    }
    public static int indexAccount(Long chainId, String name) {
        int index = 0;
        List<AccountBean> accounts = getAccounts(chainId);
        for(AccountBean tempaccountBean : accounts) {
            if(tempaccountBean.getAccount_name().equals(name)) {
                return index;
            }
        }
        return -1;
    }
    public static void updateAccount(Long chainId, AccountBean accountBean) {
        int index = indexAccount(chainId, accountBean.getId());
        if(index == -1)
            return;
        getAccounts(chainId).set(index, accountBean);
        daoSession.getAccountBeanDao().update(accountBean);
    }
    public static AccountBean getAccountMaster(Long chainId) {
        List<AccountBean> accounts = getAccounts(chainId);
        for(AccountBean accountBean : accounts) {
            if(accountBean.getMaster().equals(1))
                return accountBean;
        }
        return null;
    }
    public static void setAccountMaster(Long chainId, Long id) {
        AccountBean account = getAccount(chainId, id);
        if(account == null)
            return;
        account.setMaster(new Long(1));
        updateAccount(chainId, account);
    }
    public static void clearAccountMaster(Long chainId, Long id) {
        AccountBean account = getAccount(chainId, id);
        if(account == null)
            return;
        account.setMaster(new Long(0));
        updateAccount(chainId, account);
    }
    public static List<AccountBean> getAccounts() {
        List<ChainBean> chainBeans = getChains();
        List<AccountBean> accountBeans = new ArrayList<>();
        for(ChainBean chainBean : chainBeans) {
            accountBeans.addAll(getAccounts(chainBean.getId()));
        }
        return accountBeans;
    }
    public static AccountBean getAccount() {
        ChainBean chainBean = getChain();
        if(chainBean == null)
            return null;

        //
        Long accountId = Utils.getSpUtils().getLong("account_used");
        AccountBean accountBean = getAccount(chainBean.getId(), accountId);
        if(accountBean != null)
            return accountBean;

        accountBean = getAccountMaster(chainBean.getId());
        if(accountBean != null)
            return accountBean;

        List<AccountBean> accountBeans = getAccounts(chainBean.getId());
        if(accountBeans.size() <= 0)
            return null;

        accountBean = accountBeans.get(0);
        return accountBean;
    }
    public static void SelectAccount(Long accountId) {
        List<ChainBean> chainBeans = getChains();
        for(ChainBean chainBean : chainBeans) {
            List<AccountBean> accountBeans = getAccounts(chainBean.getId());
            for(AccountBean accountBean : accountBeans) {
                if(accountBean.getMaster().equals(1) && !accountBean.getId().equals(accountId)) {
                    clearAccountMaster(chainBean.getId(), accountBean.getId());
                    continue;
                }

                if(accountBean.getMaster().equals(0) && accountBean.getId().equals(accountId)) {
                    setAccountMaster(chainBean.getId(), accountBean.getId());
                    continue;
                }
            }
        }
    }

    /*
    token
     */
    public static List<TokenBean> getTokens(Long chainId) { return getChain(chainId).getTokens();}
    public static void addToken(Long chainId, TokenBean tokenBean) {
        if(getToken(chainId, tokenBean.getToken_name()) != null)
            return;
        List<TokenBean> tokens = getTokens(chainId);
        tokenBean.setChain_id(chainId);
        tokens.add(tokenBean);
        daoSession.getTokenBeanDao().insert(tokenBean);
    }
    public static TokenBean getToken(Long chainId, Long id) {
        List<TokenBean> tokens = getTokens(chainId);
        for(TokenBean tokenBean : tokens) {
            if(tokenBean.getId().equals(id))
                return tokenBean;
        }
        return null;
    }
    public static TokenBean getToken(Long chainId, String name) {
        List<TokenBean> tokens = getTokens(chainId);
        for(TokenBean tokenBean : tokens) {
            if(tokenBean.getToken_name().equals(name))
                return tokenBean;
        }
        return null;
    }
    public static int indexToken(Long chainId, Long id) {
        int index = 0;
        List<TokenBean> tokens = getTokens(chainId);
        for(TokenBean temptokenBean : tokens) {
            if(temptokenBean.getId().equals(id)) {
                return index;
            }
        }
        return -1;
    }
    public static int indexToken(Long chainId, String name) {
        int index = 0;
        List<TokenBean> tokens = getTokens(chainId);
        for(TokenBean temptokenBean : tokens) {
            if(temptokenBean.getToken_name().equals(name)) {
                return index;
            }
        }
        return -1;
    }
    public static void updateToken(Long chainId, TokenBean tokenBean) {
        int index = indexToken(chainId, tokenBean.getId());
        if(index == -1)
            return;
        getTokens(chainId).set(index, tokenBean);
        daoSession.getTokenBeanDao().update(tokenBean);
    }
    public static TokenBean getTokenMaster(Long chainId) {
        List<TokenBean> tokens = getTokens(chainId);
        for(TokenBean tokenBean : tokens) {
            if(tokenBean.getMaster().equals(1))
                return tokenBean;
        }
        return null;
    }
    public static void setTokenMaster(Long chainId, Long id) {
        TokenBean token = getToken(chainId, id);
        if(token == null)
            return;
        token.setMaster(new Long(1));
        updateToken(chainId, token);
    }

    /*
    account token
     */
    public static List<AccountTokenBean> getAccountTokens(Long chainId, Long accountId) {
            return getAccount(chainId, accountId).getAccounttokenBeans();
        }
    public static void addAccountToken(Long chainId, Long accountId, AccountTokenBean tokenBean) {
       if(getAccountTokenByTokenId(chainId, accountId, tokenBean.getToken_id()) != null)
           return;
        List<AccountTokenBean> tokens = getAccountTokens(chainId, accountId);
        tokenBean.setAccount_id(accountId);
        tokens.add(tokenBean);
        daoSession.getAccountTokenBeanDao().insert(tokenBean);
    }
    public static AccountTokenBean getAccountToken(Long chainId, Long accountId, Long  id) {
        List<AccountTokenBean> tokens = getAccountTokens(chainId, accountId);
        for(AccountTokenBean tokenBean : tokens) {
            if(tokenBean.getId().equals(id))
                return tokenBean;
        }
        return null;
    }
    public static AccountTokenBean getAccountTokenByTokenId(Long chainId, Long accountId, Long  token_id) {
        List<AccountTokenBean> tokens = getAccountTokens(chainId, accountId);
        for(AccountTokenBean tokenBean : tokens) {
            if(tokenBean.getToken_id().equals(token_id))
                return tokenBean;
        }
        return null;
    }
    public static int indexAccountToken(Long chainId, Long accountId, Long id) {
        int index = 0;
        List<AccountTokenBean> tokens = getAccountTokens(chainId, accountId);
        for(AccountTokenBean temptokenBean : tokens) {
            if(temptokenBean.getId().equals(id)) {
                return index;
            }
        }
        return -1;
    }
    public static void updateAccountToken(Long chainId, Long accountId, AccountTokenBean tokenBean) {
        int index = indexAccountToken(chainId, accountId, tokenBean.getId());
        if(index == -1)
            return;
        getAccountTokens(chainId, accountId).set(index, tokenBean);
        daoSession.getAccountTokenBeanDao().update(tokenBean);
    }
    public static AccountTokenBean getAccountTokenMaster(Long chainId, Long accountId) {
        List<AccountTokenBean> tokens = getAccountTokens(chainId, accountId);
        for(AccountTokenBean tokenBean : tokens) {
            if(tokenBean.getMaster().equals(1))
                return tokenBean;
        }
        return null;
    }
    public static void setAccountTokenMaster(Long chainId, Long accountId, Long id) {
       AccountTokenBean tokenBean = getAccountToken(chainId, accountId, id);
       if(tokenBean == null)
           return;
       tokenBean.setMaster(new Long(1));
        updateAccountToken(chainId, accountId, tokenBean);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        //保存系统选择语言
        //LocalManageUtil.saveSystemCurrentLanguage(base);
        //super.attachBaseContext(LocalManageUtil.setLocal(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //保存系统选择语言
        //LocalManageUtil.onConfigurationChanged(getApplicationContext());
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        Utils.init(getApplicationContext());


        try {
            initOkGo();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //AutoLayoutConifg.getInstance().useDeviceSize();

        //配置数据库
        setupDatabase();

        //LocalManageUtil.setApplicationLanguage( getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mInstance != null) {
            mInstance = null;
        }
    }

    public void initOkGo() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        if (new SPCookieStore(this).getAllCookie().size() != 0) {
            headers.put("cookie", String.valueOf(new SPCookieStore(this).getAllCookie().get(0)));
        }
        headers.put("version", "3.0");
        //headers.put("uid", MyApplication.getInstance().getUserBean().getWallet_uid());

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //log相关
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkHttp");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);                               //log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor);                                 //添加OkGo默认debug日志
        //超时时间设置
        builder.readTimeout(10000, TimeUnit.MILLISECONDS);      //全局的读取超时时间
        builder.writeTimeout(10000, TimeUnit.MILLISECONDS);     //全局的写入超时时间
        builder.connectTimeout(10000, TimeUnit.MILLISECONDS);   //全局的连接超时时间
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));            //使用sp保持cookie，如果cookie不过期，则一直有效


        //HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(getAssets().open("server.cer"));
        //builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
//        //配置https的域名匹配规则，使用不当会导致https握手失败
        builder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);

        // 其他统一的配置
        OkGo.getInstance().init(this)                           //必须调用初始化
                .setOkHttpClient(builder.build())               //必须设置OkHttpClient
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3)          //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(headers) ;              //全局公共头
//                .addCommonParams(httpParams);                       //全局公共参数

    }

    /**
     * 配置数据库
     */
    private void setupDatabase() {
        //创建数据库
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "pocketEos.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao对象管理者
        daoSession = daoMaster.newSession();
    }

    private void setupEos() {
        //
        mActivityComponent = DaggerActivityComponent
            .builder()
            .build();

        mActivityComponent.inject(this);
        eosapi = mActivityComponent.dataManager();
    }

    private void setupWallet()  {
        if(userBean.getWallet_init().longValue() == 0) {
            try {
                //
                File mWalletDirFile = new File( this.getFilesDir(), PREF_WALLET_DIR_NAME);
                mWalletDirFile.mkdirs();
                getEosWallet().setDir(mWalletDirFile);

                //
                String password = getEosWallet().create(WALLET_NAME);
                walletBean.setWallet_otherpwd(password);
                daoSession.getWalletBeanDao().update(walletBean);
                getEosWallet().saveFile(WALLET_NAME);
            } catch (IOException e) {
                e.printStackTrace();
            }

            userBean.setWallet_init(new Long(1));
            daoSession.getUserBeanDao().update(userBean);
        }

        File mWalletDirFile = new File( this.getFilesDir(), PREF_WALLET_DIR_NAME);
        getEosWallet().setDir(mWalletDirFile);
        getEosWallet().unlock(WALLET_NAME, walletBean.getWallet_otherpwd());
    }

    private void initChain() {
        if(userBean.getChain_init().longValue() == 0) {
            ChainBean chainBean = new ChainBean();
            chainBean.setChain_name("EOS");
            chainBean.setChain_type("EOS");
            chainBean.setMaster(new Long(1));
            chainBean.setChain_info("118.126.97.57:8888");
            chainBean.setChain_icon("eos_icon.png");
            chainBean.setChain_icon_id(new Long(0));
            chainBean.setUser_id(userBean.getId());
            daoSession.getChainBeanDao().insert(chainBean);

            userBean.setChain_init(new Long(1));
            daoSession.getUserBeanDao().update(userBean);
        }
    }

    private void initAccount() {
        if(userBean.getAccount_init().longValue() == 0) {

            userBean.setAccount_init(new Long(1));
            daoSession.getUserBeanDao().update(userBean);
        }
    }

    private void initAccountToken() {
        if(userBean.getAccount_token_init().longValue() == 0) {

            userBean.setAccount_token_init(new Long(1));
            daoSession.getUserBeanDao().update(userBean);
        }
    }

    private void initToken() {

        if(userBean.getToken_init().longValue() == 0) {

            ChainBean chainBean = getChain();

            String filename = "token/token.json";
            StringBuilder string_build = new StringBuilder();
            AssetManager asset_manager = this.getApplicationContext().getAssets();
            try {
                BufferedReader buffer_reader = new BufferedReader(new InputStreamReader(asset_manager.open(filename), "utf-8"));
                String line;
                while((line = buffer_reader.readLine()) != null) {
                    string_build.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            String token_json = string_build.toString();
            Gson gson = new Gson();
            List<TokenBean> tokens = gson.fromJson(token_json, new TypeToken<List<TokenBean>>(){}.getType());

            //
            for(TokenBean token : tokens) {
                token.setChain_id(chainBean.getId());
                token.setMaster(new Long(0));
                daoSession.getTokenBeanDao().insert(token);
            }

            /*

            {
                TokenBean tokenBean = new TokenBean();
                tokenBean.setToken_name("SYS");
                tokenBean.setToken_info("SYS");
                tokenBean.setChain_id(chainBean.getId());
                tokenBean.setMaster(new Long(0));
                tokenBean.setToken_icon("eos_icon.png");
                tokenBean.setToken_icon_id(new Long(0));
                tokenBean.setCny_rate(new Double(59.53));
                tokenBean.setUsd_rate(new Double(0));
                daoSession.getTokenBeanDao().insert(tokenBean);
            }
            {
                TokenBean tokenBean = new TokenBean();
                tokenBean.setToken_name("OCT");
                tokenBean.setToken_info("OCT");
                tokenBean.setChain_id(chainBean.getId());
                tokenBean.setMaster(new Long(0));
                tokenBean.setToken_icon("eos_icon.png");
                tokenBean.setToken_icon_id(new Long(0));
                tokenBean.setCny_rate(new Double(0.9));
                tokenBean.setUsd_rate(new Double(0));
                daoSession.getTokenBeanDao().insert(tokenBean);
            }

            {
                TokenBean tokenBean = new TokenBean();
                tokenBean.setToken_name("EOS");
                tokenBean.setToken_info("EOS");
                tokenBean.setChain_id(chainBean.getId());
                tokenBean.setMaster(new Long(0));
                tokenBean.setToken_icon("eos_icon.png");
                tokenBean.setToken_icon_id(new Long(0));
                tokenBean.setCny_rate(new Double(5));
                tokenBean.setUsd_rate(new Double(0));
                daoSession.getTokenBeanDao().insert(tokenBean);
            }

            {
                TokenBean tokenBean = new TokenBean();
                tokenBean.setToken_name("BTC");
                tokenBean.setToken_info("BTC");
                tokenBean.setChain_id(chainBean.getId());
                tokenBean.setMaster(new Long(0));
                tokenBean.setToken_icon("eos_icon.png");
                tokenBean.setToken_icon_id(new Long(0));
                tokenBean.setCny_rate(new Double(49750.49));
                tokenBean.setUsd_rate(new Double(0));
                daoSession.getTokenBeanDao().insert(tokenBean);
            }

            {
                TokenBean tokenBean = new TokenBean();
                tokenBean.setToken_name("ETH");
                tokenBean.setToken_info("ETH");
                tokenBean.setChain_id(chainBean.getId());
                tokenBean.setMaster(new Long(0));
                tokenBean.setToken_icon("eos_icon.png");
                tokenBean.setToken_icon_id(new Long(0));
                tokenBean.setCny_rate(new Double(3352.04));
                tokenBean.setUsd_rate(new Double(0));
                daoSession.getTokenBeanDao().insert(tokenBean);
            }

            {
                TokenBean tokenBean = new TokenBean();
                tokenBean.setToken_name("TE1");
                tokenBean.setToken_info("TE1");
                tokenBean.setChain_id(chainBean.getId());
                tokenBean.setMaster(new Long(0));
                tokenBean.setToken_icon("eos_icon.png");
                tokenBean.setToken_icon_id(new Long(0));
                tokenBean.setCny_rate(new Double(0));
                tokenBean.setUsd_rate(new Double(0));
                daoSession.getTokenBeanDao().insert(tokenBean);
            }

            {
                TokenBean tokenBean = new TokenBean();
                tokenBean.setToken_name("TE2");
                tokenBean.setToken_info("TE2");
                tokenBean.setChain_id(chainBean.getId());
                tokenBean.setMaster(new Long(0));
                tokenBean.setToken_icon("eos_icon.png");
                tokenBean.setToken_icon_id(new Long(0));
                tokenBean.setCny_rate(new Double(0));
                tokenBean.setUsd_rate(new Double(0));
                daoSession.getTokenBeanDao().insert(tokenBean);
            }

            {
                TokenBean tokenBean = new TokenBean();
                tokenBean.setToken_name("TE3");
                tokenBean.setToken_info("TE3");
                tokenBean.setChain_id(chainBean.getId());
                tokenBean.setMaster(new Long(0));
                tokenBean.setToken_icon("eos_icon.png");
                tokenBean.setToken_icon_id(new Long(0));
                tokenBean.setCny_rate(new Double(0));
                tokenBean.setUsd_rate(new Double(0));
                daoSession.getTokenBeanDao().insert(tokenBean);
            }

            {
                TokenBean tokenBean = new TokenBean();
                tokenBean.setToken_name("TE4");
                tokenBean.setToken_info("TE4");
                tokenBean.setChain_id(chainBean.getId());
                tokenBean.setMaster(new Long(0));
                tokenBean.setToken_icon("eos_icon.png");
                tokenBean.setToken_icon_id(new Long(0));
                tokenBean.setCny_rate(new Double(0));
                tokenBean.setUsd_rate(new Double(0));
                daoSession.getTokenBeanDao().insert(tokenBean);
            }

            {
                TokenBean tokenBean = new TokenBean();
                tokenBean.setToken_name("TE5");
                tokenBean.setToken_info("TE5");
                tokenBean.setChain_id(chainBean.getId());
                tokenBean.setMaster(new Long(0));
                tokenBean.setToken_icon("eos_icon.png");
                tokenBean.setToken_icon_id(new Long(0));
                tokenBean.setCny_rate(new Double(0));
                tokenBean.setUsd_rate(new Double(0));
                daoSession.getTokenBeanDao().insert(tokenBean);
            }

            {
                TokenBean tokenBean = new TokenBean();
                tokenBean.setToken_name("TE6");
                tokenBean.setToken_info("TE6");
                tokenBean.setChain_id(chainBean.getId());
                tokenBean.setMaster(new Long(0));
                tokenBean.setToken_icon("eos_icon.png");
                tokenBean.setToken_icon_id(new Long(0));
                tokenBean.setCny_rate(new Double(0));
                tokenBean.setUsd_rate(new Double(0));
                daoSession.getTokenBeanDao().insert(tokenBean);
            }

            {
                TokenBean tokenBean = new TokenBean();
                tokenBean.setToken_name("TE7");
                tokenBean.setToken_info("TE7");
                tokenBean.setChain_id(chainBean.getId());
                tokenBean.setMaster(new Long(0));
                tokenBean.setToken_icon("eos_icon.png");
                tokenBean.setToken_icon_id(new Long(0));
                tokenBean.setCny_rate(new Double(0));
                tokenBean.setUsd_rate(new Double(0));
                daoSession.getTokenBeanDao().insert(tokenBean);
            }

            {
                TokenBean tokenBean = new TokenBean();
                tokenBean.setToken_name("TE8");
                tokenBean.setToken_info("TE8");
                tokenBean.setChain_id(chainBean.getId());
                tokenBean.setMaster(new Long(0));
                tokenBean.setToken_icon("eos_icon.png");
                tokenBean.setToken_icon_id(new Long(0));
                tokenBean.setCny_rate(new Double(0));
                tokenBean.setUsd_rate(new Double(0));
                daoSession.getTokenBeanDao().insert(tokenBean);
            }

            {
                TokenBean tokenBean = new TokenBean();
                tokenBean.setToken_name("TE9");
                tokenBean.setToken_info("TE9");
                tokenBean.setChain_id(chainBean.getId());
                tokenBean.setMaster(new Long(0));
                tokenBean.setToken_icon("eos_icon.png");
                tokenBean.setToken_icon_id(new Long(0));
                tokenBean.setCny_rate(new Double(0));
                tokenBean.setUsd_rate(new Double(0));
                daoSession.getTokenBeanDao().insert(tokenBean);
            }
            */


            userBean.setToken_init(new Long(1));
            daoSession.getUserBeanDao().update(userBean);
        }
    }

    private void initApp() {
        if(userBean.getApp_init().longValue() == 0) {
            ChainBean chainBean = getChain();
            for(int i = 0;i < 40;i ++) {
                AppBean appBean = new AppBean();
                appBean.setUser_id(userBean.getId());
                appBean.setChain_id(chainBean.getId());
                appBean.setApp_name("eos transfer " + String.valueOf(i));
                appBean.setApp_info("dapp.html");
                appBean.setMaster_account(new Long(0));
                MyApplication.addApp(appBean);
            }

            userBean.setApp_init(new Long(1));
            daoSession.getUserBeanDao().update(userBean);
        }

        appBeans = daoSession.getAppBeanDao().queryBuilder().build().list();
    }

    /*
    public void showImage(String url, final ImageView image) {
        Glide.with(getApplicationContext()).load(url).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                .into(new SimpleTarget<GlideDrawable>() { // 加上这段代码 可以解决
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                        image.setImageDrawable(resource); //显示图片
                    }
                });
    }

    public void showCirImage(String url, final ImageView image) {
        if (url == null || url.isEmpty() || "".equals(url)) {
            image.setImageResource(R.mipmap.defeat_person_img);
            return;
        }
        Glide.with(getApplicationContext())
                .load(url)
                .error(R.mipmap.ic_launcher_round)
                .into(new SimpleTarget<GlideDrawable>() { // 加上这段代码 可以解决
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                        image.setImageDrawable(resource); //显示图片
                    }
                });
    }
    */

    public void onLogin() {
        //
        walletBean = daoSession.getWalletBeanDao().queryBuilder().where(WalletBeanDao.Properties.Wallet_phone.eq(userBean.getUser_name())).build().unique();
        appBeans = daoSession.getAppBeanDao().queryBuilder().build().list();

        //
        initChain();

        //
        initAccount();

        //
        initAccountToken();

        //
        initToken();

        //
        initApp();

        //
        setupEos();

        //
        setupWallet();
    }
}

