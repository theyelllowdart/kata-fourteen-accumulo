package kata.fourteen.accumulo.accumulo.ingest;

import java.io.Reader;
import java.util.Iterator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import kata.fourteen.accumulo.accumulo.NGramEntry;
import kata.fourteen.accumulo.accumulo.RollingNGram;
import kata.fourteen.accumulo.accumulo.config.SettingKeys;

import org.apache.accumulo.core.client.MutationsRejectedException;
import org.apache.accumulo.core.client.TableNotFoundException;

public class TextIngester {
  private final TokenParser tokenParser;
  private final Provider<NGramWriter> writerProvider;
  private final int ngramSize;

  @Inject
  public TextIngester(TokenParser tokenParser, Provider<NGramWriter> writerProvider,
      @Named(SettingKeys.NGRAM_SIZE) int ngramSize) {
    this.tokenParser = tokenParser;
    this.writerProvider = writerProvider;
    this.ngramSize = ngramSize;
  }

  public void ingest(final Reader reader) throws MutationsRejectedException, TableNotFoundException {
    try (NGramWriter writer = writerProvider.get()) {
      Iterator<String> tokens = tokenParser.parse(reader);
      RollingNGram rollingNGram = new RollingNGram(ngramSize);
      // reuse ngramEntry
      while (tokens.hasNext()) {
        String nextToken = tokens.next();
        // skip quotes. simple n-gram text generation performs poorly with quotations
        if (nextToken.equals("\"") || nextToken.equals("\'") || nextToken.equals("''") || nextToken.equals("``")
            || nextToken.equals("_") || nextToken.equals("`")) {
          continue;
        }
        if (rollingNGram.isFilled()) {
          writer.insert(new NGramEntry(rollingNGram.getTokens(), nextToken));
        }
        rollingNGram.push(nextToken);
      }
    }
  }
}
