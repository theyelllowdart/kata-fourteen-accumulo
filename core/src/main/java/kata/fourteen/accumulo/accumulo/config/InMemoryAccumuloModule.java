package kata.fourteen.accumulo.accumulo.config;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.name.Names;
import kata.fourteen.accumulo.accumulo.config.providers.ConnectorProvider;
import kata.fourteen.accumulo.accumulo.config.providers.MockInstanceProvider;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Instance;

import javax.inject.Singleton;

public class InMemoryAccumuloModule implements Module {
  @Override
  public void configure(Binder binder) {
    // default username and password in accumulo 1.5
    binder.bindConstant().annotatedWith(Names.named(SettingKeys.ACCUMULO_USERNAME)).to("root");
    binder.bindConstant().annotatedWith(Names.named(SettingKeys.ACCUMULO_PASSWORD)).to("");

    binder.bind(Connector.class).toProvider(ConnectorProvider.class).in(Singleton.class);
    binder.bind(Instance.class).toProvider(MockInstanceProvider.class).in(Singleton.class);
  }
}
