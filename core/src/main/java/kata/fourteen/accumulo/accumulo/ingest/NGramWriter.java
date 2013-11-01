package kata.fourteen.accumulo.accumulo.ingest;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import kata.fourteen.accumulo.accumulo.NGramEntry;
import kata.fourteen.accumulo.accumulo.NGramTable;
import kata.fourteen.accumulo.accumulo.config.SettingKeys;

import org.apache.accumulo.core.client.BatchWriter;
import org.apache.accumulo.core.client.BatchWriterConfig;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.MutationsRejectedException;
import org.apache.accumulo.core.client.TableNotFoundException;
import org.apache.accumulo.typo.Typo;

import com.google.common.io.Closeables;

/**
 * Writes ngrams into Accumulo
 */
public class NGramWriter implements AutoCloseable {
  private final BatchWriter batchWriter;

  @Inject
  public NGramWriter(@Named(SettingKeys.NGRAM_TABLE_NAME) String ngramTable, Connector connector) throws TableNotFoundException {
    batchWriter = connector.createBatchWriter(ngramTable, new BatchWriterConfig());
  }

  public void insert(NGramEntry entry) throws TableNotFoundException, MutationsRejectedException {
    NGramEntry.NGramEntryTypo typo = new NGramEntry.NGramEntryTypo();
    Typo<List<String>, String, String, Long>.Mutation mutation = typo.newMutation(entry.getKey());
    mutation.put(NGramTable.CFs.Meta.name, NGramTable.CFs.Meta.CQs.Total.name, 1L);
    mutation.put(NGramTable.CFs.Next.name, entry.getNext(), 1L);
    batchWriter.addMutation(mutation);
  }

  @Override
  public void close() {
    Closeables.closeQuietly(new Closeable() {
      @Override
      public void close() throws IOException {
        try {
          batchWriter.close();
        } catch (MutationsRejectedException e) {
          throw new RuntimeException(e);
        }
      }
    });
  }
}
