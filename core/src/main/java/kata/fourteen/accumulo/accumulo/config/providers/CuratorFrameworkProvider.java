package kata.fourteen.accumulo.accumulo.config.providers;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import com.netflix.curator.RetryPolicy;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.retry.ExponentialBackoffRetry;
import kata.fourteen.accumulo.accumulo.config.SettingKeys;

public class CuratorFrameworkProvider implements Provider<CuratorFramework> {
  private final String zkConnectionString;

  @Inject
  public CuratorFrameworkProvider(@Named(SettingKeys.ZOOKEEPER_CONNECTION) String zkConnectionString) {
    this.zkConnectionString = zkConnectionString;
  }

  @Override
  public CuratorFramework get() {
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(zkConnectionString, retryPolicy);
    curatorFramework.start();
    return curatorFramework;
  }
}
