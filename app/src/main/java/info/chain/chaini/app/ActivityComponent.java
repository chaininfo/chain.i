package info.chain.chaini.app;

import dagger.Component;
import info.chain.chaini.eosapi.EosDataManager;

@Component(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MyApplication app);
    EosDataManager dataManager();
}