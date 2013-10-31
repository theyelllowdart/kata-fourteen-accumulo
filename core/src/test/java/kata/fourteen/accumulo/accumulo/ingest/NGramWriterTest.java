package kata.fourteen.accumulo.accumulo.ingest;

import mockit.Cascading;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;

import org.apache.accumulo.core.client.BatchWriter;
import org.apache.accumulo.core.client.Connector;
import org.junit.Test;

public class NGramWriterTest {
  @Tested
  private NGramWriter writer;

  @Injectable
  private String ngramTable = "ngram";

  @Test
  public void close_closesAccumuloWriter(@Injectable @Cascading Connector connector, @Mocked final BatchWriter batchWriter) throws Exception {
    writer.close();
    new Verifications() {
      {
        batchWriter.close();
      }
    };
  }
}
