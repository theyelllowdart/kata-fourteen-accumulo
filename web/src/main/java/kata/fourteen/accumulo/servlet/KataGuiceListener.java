package kata.fourteen.accumulo.servlet;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import kata.fourteen.accumulo.accumulo.bootstrap.NGramTableBootstrap;
import kata.fourteen.accumulo.accumulo.config.AccumuloConnectionType;
import kata.fourteen.accumulo.accumulo.config.CoreModule;
import kata.fourteen.accumulo.accumulo.config.InMemoryAccumuloModule;
import kata.fourteen.accumulo.accumulo.config.SettingKeys;
import kata.fourteen.accumulo.accumulo.config.ZookeeperAccumuloModule;
import kata.fourteen.accumulo.config.RestModule;

import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;

import com.google.common.base.Objects;
import com.google.common.io.Closeables;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.netflix.curator.test.TestingServer;

public class KataGuiceListener extends GuiceResteasyBootstrapServletContextListener {
  private TestingServer zkTestingServer;

  @Override
  protected List<Module> getModules(ServletContext context) {
    List<Module> modules = super.getModules(context);

    // configure either mock or zookeeper accumulo
    String accumuloConnection = context.getInitParameter(SettingKeys.CONNECTION_TYPE);
    switch (AccumuloConnectionType.valueOf(accumuloConnection)) {
      case ZOOKEEPER:
        modules.add(new ZookeeperAccumuloModule(context.getInitParameter(SettingKeys.ACCUMULO_INSTANCE), context
            .getInitParameter(SettingKeys.ACCUMULO_USERNAME), Objects.firstNonNull(
            context.getInitParameter(SettingKeys.ACCUMULO_PASSWORD), "")));
        break;
      case MOCK:
        try {
          // start a zookeeper if a connection string wasn't specified
          if (context.getInitParameter(SettingKeys.ZOOKEEPER_CONNECTION) == null) {
            zkTestingServer = new TestingServer();
          }
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
        modules.add(new InMemoryAccumuloModule());
        break;
    }

    // configure core
    modules.add(new CoreModule(zkTestingServer != null ? zkTestingServer.getConnectString() : context
        .getInitParameter(SettingKeys.ZOOKEEPER_CONNECTION), Integer.parseInt(context
        .getInitParameter(SettingKeys.NGRAM_SIZE)), context.getInitParameter(SettingKeys.NGRAM_TABLE_NAME)));

    // configure rest
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

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    super.contextDestroyed(event);
    Closeables.closeQuietly(zkTestingServer);
  }
}
