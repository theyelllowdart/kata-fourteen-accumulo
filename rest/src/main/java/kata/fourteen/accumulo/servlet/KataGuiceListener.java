package kata.fourteen.accumulo.servlet;

import java.util.List;

import javax.servlet.ServletContext;

import kata.fourteen.accumulo.accumulo.bootstrap.NGramTableBootstrap;
import kata.fourteen.accumulo.accumulo.config.AccumuloConnectionType;
import kata.fourteen.accumulo.accumulo.config.CoreModule;
import kata.fourteen.accumulo.accumulo.config.InMemoryAccumuloModule;
import kata.fourteen.accumulo.accumulo.config.SettingKeys;
import kata.fourteen.accumulo.accumulo.config.ZookeeperAccumuloModule;
import kata.fourteen.accumulo.config.RestModule;

import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;

import com.google.common.base.Objects;
import com.google.inject.Injector;
import com.google.inject.Module;

public class KataGuiceListener extends GuiceResteasyBootstrapServletContextListener {
  @Override
  protected List<Module> getModules(ServletContext context) {
    List<Module> modules = super.getModules(context);
    String accumuloConnection = context.getInitParameter(SettingKeys.CONNECTION_TYPE);
    switch (AccumuloConnectionType.valueOf(accumuloConnection)) {
      case ZOOKEEPER:
        modules.add(new ZookeeperAccumuloModule(context.getInitParameter(SettingKeys.ACCUMULO_INSTANCE), context
            .getInitParameter(SettingKeys.ACCUMULO_USERNAME), Objects.firstNonNull(
            context.getInitParameter(SettingKeys.ACCUMULO_PASSWORD), "")));
        break;
      case MOCK:
        modules.add(new InMemoryAccumuloModule());
        break;
    }

    modules.add(new CoreModule(context.getInitParameter(SettingKeys.ZOOKEEPER_CONNECTION), Integer.parseInt(context
        .getInitParameter(SettingKeys.NGRAM_SIZE)), context.getInitParameter(SettingKeys.NGRAM_TABLE_NAME)));

    modules.add(new RestModule());

    return modules;
  }

  @Override
  protected void withInjector(Injector injector) {
    super.withInjector(injector);
    try {
      injector.getInstance(NGramTableBootstrap.class).bootstrap();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
