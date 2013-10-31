package kata.fourteen.accumulo.accumulo;

import java.util.ArrayList;
import java.util.List;

import org.apache.accumulo.typo.Typo;
import org.apache.accumulo.typo.encoders.ListLexicoder;
import org.apache.accumulo.typo.encoders.LongLexicoder;
import org.apache.accumulo.typo.encoders.StringLexicoder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class NGramEntry {
  private List<String> key;
  private String next;

  public NGramEntry(List<String> key, String next) {
    this.key = new ArrayList<>(key);
    this.next = next;
  }

  public List<String> getKey() {
    return key;
  }

  public void setKey(List<String> previousTokens) {
    this.key = previousTokens;
  }

  public String getNext() {
    return next;
  }

  public void setNext(String nextToken) {
    this.next = nextToken;
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
