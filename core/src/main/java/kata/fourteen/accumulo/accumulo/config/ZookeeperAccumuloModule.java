package kata.fourteen.accumulo.accumulo.config;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.name.Names;
import kata.fourteen.accumulo.accumulo.config.providers.ConnectorProvider;
import kata.fourteen.accumulo.accumulo.config.providers.MockInstanceProvider;
import kata.fourteen.accumulo.accumulo.config.providers.ZookeeperInstanceProvider;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Instance;

import javax.inject.Singleton;

public class ZookeeperAccumuloModule implements Module {
  private final String instanceName;
  private final String accumuloUser;
  private final String accumuloPassword;

  public ZookeeperAccumuloModule(String instanceName, String accumuloUser, String accumuloPassword) {
    this.instanceName = instanceName;
    this.accumuloUser = accumuloUser;
    this.accumuloPassword = accumuloPassword;
  }

  @Override
  public void configure(Binder binder) {
    binder.bindConstant().annotatedWith(Names.named(SettingKeys.ACCUMULO_INSTANCE)).to(instanceName);
    binder.bindConstant().annotatedWith(Names.named(SettingKeys.ACCUMULO_USERNAME)).to(accumuloUser);
    binder.bindConstant().annotatedWith(Names.named(SettingKeys.ACCUMULO_PASSWORD)).to(accumuloPassword);

    binder.bind(Connector.class).toProvider(ConnectorProvider.class).in(Singleton.class);
    binder.bind(Instance.class).toProvider(ZookeeperInstanceProvider.class).in(Singleton.class);
  }
}