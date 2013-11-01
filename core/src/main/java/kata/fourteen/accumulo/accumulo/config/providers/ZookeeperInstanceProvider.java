package kata.fourteen.accumulo.accumulo.config.providers;

import kata.fourteen.accumulo.accumulo.config.SettingKeys;
import org.apache.accumulo.core.client.Instance;
import org.apache.accumulo.core.client.ZooKeeperInstance;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

public class ZookeeperInstanceProvider implements Provider<Instance> {
  private final String zkConnectionString;
  private final String instanceName;

  @Inject
  public ZookeeperInstanceProvider(@Named(SettingKeys.ZOOKEEPER_CONNECTION) String zkConnectionString,
      @Named(SettingKeys.ACCUMULO_INSTANCE) String instanceName) {
    this.zkConnectionString = zkConnectionString;
    this.instanceName = instanceName;
  }

  @Override
  public Instance get() {
    return new ZooKeeperInstance(instanceName, zkConnectionString);
  }
}
