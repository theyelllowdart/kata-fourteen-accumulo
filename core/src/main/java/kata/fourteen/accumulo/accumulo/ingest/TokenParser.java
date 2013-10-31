package kata.fourteen.accumulo.accumulo.ingest;

import java.io.Reader;
import java.util.Iterator;

import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.PTBTokenizer;

public class TokenParser {
  public Iterator<String> parse(final Reader reader) {
    final PTBTokenizer<Word> tokenizer = PTBTokenizer.newPTBTokenizer(reader);
    return new Iterator<String>() {
      @Override
      public boolean hasNext() {
        return tokenizer.hasNext();
      }

      @Override
      public String next() {
        return tokenizer.next().toString();
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }
}
