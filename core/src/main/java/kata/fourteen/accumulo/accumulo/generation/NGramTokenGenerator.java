package kata.fourteen.accumulo.accumulo.generation;

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import kata.fourteen.accumulo.accumulo.EmptyTableException;
import kata.fourteen.accumulo.accumulo.RollingQueue;
import kata.fourteen.accumulo.accumulo.config.SettingKeys;

import org.apache.accumulo.core.client.TableNotFoundException;

import com.google.common.collect.Lists;

public class NGramTokenGenerator implements TokenGenerator {
  private final NGramReader reader;
  private final int ngramSize;

  @Inject
  public NGramTokenGenerator(NGramReader reader, @Named(SettingKeys.NGRAM_SIZE) int ngramSize) {
    this.reader = reader;
    this.ngramSize = ngramSize;
  }

  public Iterator<String> generate() {
    List<String> initial;
    try {
      initial = reader.getInitial();
    } catch (TableNotFoundException e) {
      throw new RuntimeException(e);
    }
    if (initial == null) {
      throw new EmptyTableException("No entries exist in the NGram table.");
    }
    final RollingQueue<String> rollingQueue = new RollingQueue<>(ngramSize, initial);
    return new Iterator<String>() {
      @Override
      public boolean hasNext() {
        return rollingQueue.size() > 0;
      }

      @Override
      public String next() {
        if (!rollingQueue.isFilled()) {
          return rollingQueue.pop();
        }
        try {
          String nextToken = reader.getNext(Lists.newArrayList(rollingQueue));
          if (nextToken != null) {
            return rollingQueue.push(nextToken);
          } else {
            return rollingQueue.pop();
          }
        } catch (TableNotFoundException e) {
          throw new RuntimeException(e);
        }
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }
}
