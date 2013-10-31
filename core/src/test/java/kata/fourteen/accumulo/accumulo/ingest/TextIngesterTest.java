package kata.fourteen.accumulo.accumulo.ingest;

import java.io.Reader;

import kata.fourteen.accumulo.accumulo.NGramEntry;
import mockit.Expectations;
import mockit.FullVerifications;
import mockit.Injectable;
import mockit.Tested;

import org.junit.Test;

import com.beust.jcommander.internal.Lists;

public class TextIngesterTest {

  @Tested
  private TextIngester textIngester;

  @Injectable
  private TokenParser tokenParser;

  @Injectable
  private NGramWriter ngramWriter;

  @Injectable
  private int ngramSize = 2;

  @Test
  public void ingest_matchesSample(@Injectable final Reader reader) throws Exception {
    new Expectations() {
      {
        tokenParser.parse(withSameInstance(reader));
        returns("I", "wish", "I", "may", "I", "wish", "I", "might");
      }
    };

    textIngester.ingest(reader);

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
