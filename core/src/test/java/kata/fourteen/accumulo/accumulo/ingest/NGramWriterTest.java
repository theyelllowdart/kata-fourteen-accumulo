package kata.fourteen.accumulo.accumulo.ingest;

import mockit.Cascading;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Verifications;
import org.apache.accumulo.core.client.BatchWriter;
import org.apache.accumulo.core.client.Connector;
import org.junit.Test;

public class NGramWriterTest {
  @Injectable
  @Cascading
  private Connector connector;

  @Test
  public void close_closesAccumuloWriter(@Mocked final BatchWriter batchWriter) throws Exception {
    NGramWriter writer = new NGramWriter("ngram", connector);
    writer.close();
    new Verifications() {
      {
        batchWriter.close();
      }
    };
  }
}
