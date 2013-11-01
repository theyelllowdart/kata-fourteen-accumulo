package kata.fourteen.accumulo.accumulo.ingest;

import java.io.Reader;
import java.util.Iterator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import kata.fourteen.accumulo.accumulo.NGramEntry;
import kata.fourteen.accumulo.accumulo.RollingQueue;
import kata.fourteen.accumulo.accumulo.config.SettingKeys;

import org.apache.accumulo.core.client.MutationsRejectedException;
import org.apache.accumulo.core.client.TableNotFoundException;

/**
 *
 */
public class TextIngester {
  private final Tokenizer tokenizer;
  private final Provider<NGramWriter> writerProvider;
  private final int ngramSize;

  @Inject
  public TextIngester(Tokenizer tokenizer, Provider<NGramWriter> writerProvider,
      @Named(SettingKeys.NGRAM_SIZE) int ngramSize) {
    this.tokenizer = tokenizer;
    this.writerProvider = writerProvider;
    this.ngramSize = ngramSize;
  }

  /**
   * Converts text into a sequence of {@link NGramEntry} and writes them using {@link NGramWriter}
   * 
   * @param reader
   *          english text
   * @throws MutationsRejectedException
   * @throws TableNotFoundException
   * @return number of entries ingested
   */
  public long ingest(final Reader reader) throws MutationsRejectedException, TableNotFoundException {
    long entriesWritten = 0;
    try (NGramWriter writer = writerProvider.get()) {
      Iterator<String> tokens = tokenizer.parse(reader);
      RollingQueue<String> rollingQueue = new RollingQueue<>(ngramSize);
      while (tokens.hasNext()) {
        String nextToken = tokens.next();
        // skip quotes. simple n-gram text generation performs poorly with quotations
        if (nextToken.equals("\"") || nextToken.equals("\'") || nextToken.equals("''") || nextToken.equals("``")
            || nextToken.equals("_") || nextToken.equals("`")) {
          continue;
        }
        if (rollingQueue.isFilled()) {
          writer.insert(new NGramEntry(rollingQueue, nextToken));
          entriesWritten++;
        }
        // push next token into rolling queue
        rollingQueue.push(nextToken);
      }
    }
    return entriesWritten;
  }
}
