package kata.fourteen.accumulo.accumulo;

import com.google.common.io.Closeables;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.netflix.curator.test.TestingServer;
import kata.fourteen.accumulo.accumulo.bootstrap.NGramTableBootstrap;
import kata.fourteen.accumulo.accumulo.config.CoreModule;
import kata.fourteen.accumulo.accumulo.config.InMemoryAccumuloModule;
import org.junit.After;
import org.junit.Before;

public abstract class KataFourteenIntegrationTest {
  private TestingServer testingServer;
  public static final String ngramTable = "ngram";

  @Before
  public void setup() throws Exception {
    testingServer = new TestingServer();
    Injector injector = Guice.createInjector(new InMemoryAccumuloModule(),
        new CoreModule(testingServer.getConnectString(), 2, ngramTable));

    NGramTableBootstrap ngramTableBootstrap = injector.getInstance(NGramTableBootstrap.class);
    ngramTableBootstrap.bootstrap();

    injector.injectMembers(this);
  }

  @After
  public void shutdown() {
    Closeables.closeQuietly(testingServer);
  }
}
