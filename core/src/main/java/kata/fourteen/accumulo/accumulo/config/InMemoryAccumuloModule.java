package kata.fourteen.accumulo.accumulo.config;

import javax.inject.Singleton;

import kata.fourteen.accumulo.accumulo.config.providers.ConnectorProvider;
import kata.fourteen.accumulo.accumulo.config.providers.MockInstanceProvider;

import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Instance;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.name.Names;

public class InMemoryAccumuloModule implements Module {
  @Override
  public void configure(Binder binder) {
    binder.bindConstant().annotatedWith(Names.named(SettingKeys.ACCUMULO_USERNAME)).to("root");
    binder.bindConstant().annotatedWith(Names.named(SettingKeys.ACCUMULO_PASSWORD)).to("");

    binder.bind(Connector.class).toProvider(ConnectorProvider.class).in(Singleton.class);
    binder.bind(Instance.class).toProvider(MockInstanceProvider.class).in(Singleton.class);
  }
}
