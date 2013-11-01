package kata.fourteen.accumulo.accumulo.ingest;

import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.process.PTBTokenizer;

import java.io.Reader;
import java.util.Iterator;

/**
 * Parses a text source into token
 */
public class Tokenizer {

  /**
   * Tokenizes english text using Stanford's NLP tokenizer
   * 
   * @param reader
   *          english text
   * @return a list of tokens
   */
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
