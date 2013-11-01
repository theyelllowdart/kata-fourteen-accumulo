package kata.fourteen.accumulo.accumulo.ingest;

import java.io.Reader;

import kata.fourteen.accumulo.accumulo.NGramEntry;
import mockit.Expectations;
import mockit.FullVerifications;
import mockit.Injectable;
import mockit.Tested;

import org.junit.Test;

import com.beust.jcommander.internal.Lists;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TextIngesterTest {

  @Tested
  private TextIngester textIngester;

  @Injectable
  private Tokenizer tokenizer;

  @Injectable
  private NGramWriter ngramWriter;

  @Injectable
  private int ngramSize = 2;

  @Test
  public void ingest_matchesSample(@Injectable final Reader reader) throws Exception {
    new Expectations() {
      {
        tokenizer.parse(withSameInstance(reader));
        returns("I", "wish", "I", "may", "I", "wish", "I", "might");
      }
    };

    long entriesWritten = textIngester.ingest(reader);
    assertThat(entriesWritten, is(6l));

    new FullVerifications() {
      {
        ngramWriter.insert(withEqual(new NGramEntry(Lists.newArrayList("I", "wish"), "I")));
        ngramWriter.insert(withEqual(new NGramEntry(Lists.newArrayList("wish", "I"), "may")));
        ngramWriter.insert(withEqual(new NGramEntry(Lists.newArrayList("I", "may"), "I")));
        ngramWriter.insert(withEqual(new NGramEntry(Lists.newArrayList("may", "I"), "wish")));
        ngramWriter.insert(withEqual(new NGramEntry(Lists.newArrayList("I", "wish"), "I")));
        ngramWriter.insert(withEqual(new NGramEntry(Lists.newArrayList("wish", "I"), "might")));
        ngramWriter.close();
      }
    };

  }
}
