package info.chain.chaini.app;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import info.chain.chaini.eosapi.remote.HostInterceptor;
import info.chain.chaini.eosapi.remote.NodeosApi;
import info.chain.chaini.eosapi.util.GsonEosTypeAdapterFactory;
import info.chain.chaini.eosapi.wallet.EosWalletManager;
import info.chain.chaini.gen.ChainBean;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ActivityModule {
    @Provides
    HostInterceptor providesHostInterceptor() {
        return new HostInterceptor();
    }

    @Provides
    OkHttpClient providesOkHttpClient(HostInterceptor interceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }

    @Provides
    Gson providesGson() {
        return new GsonBuilder()
                .registerTypeAdapterFactory(new GsonEosTypeAdapterFactory())
                .serializeNulls()
                .excludeFieldsWithoutExposeAnnotation().create();
    }

    @Provides
    NodeosApi providesEosService(Gson gson, OkHttpClient okHttpClient/*, PreferencesHelper preferencesHelper*/) {
        //
        ChainBean chainBean = MyApplication.getChain("EOS");
        String eosAddress = null;
        if(chainBean == null) {
            eosAddress = "118.126.97.57:8888";
        } else {
            eosAddress = chainBean.getChain_info();
        }
        //
        //int index = eosAddress.lastIndexOf(" ");
        //String strPort = eosAddress.substring(index);
        //int port = new Integer(strPort);
        //String strAddr = eosAddress.substring(0, index);

        //
       // RefValue<Integer> portRef = new RefValue<>(port);
        String url =  "http://" + eosAddress;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( url )
                .addConverterFactory( GsonConverterFactory.create( gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // retrofit 용 rxjava2 adapter
                .client( okHttpClient )
                .build();

        return retrofit.create( NodeosApi.class);
    }

    @Provides
    EosWalletManager providesWalletManager() {
        return new EosWalletManager();
    }
}