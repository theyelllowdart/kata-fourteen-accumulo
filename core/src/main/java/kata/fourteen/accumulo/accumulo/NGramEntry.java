package kata.fourteen.accumulo.accumulo;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import org.apache.accumulo.typo.Typo;
import org.apache.accumulo.typo.encoders.ListLexicoder;
import org.apache.accumulo.typo.encoders.LongLexicoder;
import org.apache.accumulo.typo.encoders.StringLexicoder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Represents an ngram entry according to kata_fourteen instructions: a series (n - 1) of sequential words (key) paired
 * with the following word (next).
 *
 * @see <a href="http://codekata.pragprog.com/2007/01/kata_fourteen_t.html">kata fourteen blog</a>
 */
public class NGramEntry {
  private List<String> key;
  private String next;

  public NGramEntry(Iterable<String> key, String next) {
    this.key = Lists.newArrayList(key);
    this.next = next;
  }

  public List<String> getKey() {
    return key;
  }

  public String getNext() {
    return next;
  }

  @Override
  public boolean equals(Object o) {
    return EqualsBuilder.reflectionEquals(this, o);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  public static class NGramEntryTypo extends Typo<List<String>, String, String, Long> {
    public NGramEntryTypo() {
      super(new ListLexicoder<>(new StringLexicoder()), new StringLexicoder(), new StringLexicoder(),
          new LongLexicoder());
    }
  }
}
