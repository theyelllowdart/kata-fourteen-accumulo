package kata.fourteen.accumulo.accumulo;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import kata.fourteen.accumulo.resource.TextIngestResource;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Scanner;
import org.apache.accumulo.core.security.Authorizations;
import org.apache.accumulo.typo.Typo;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

public class IngestIT extends KataFourteenIntegrationTest {
  @Inject
  private TextIngestResource ingest;

  @Inject
  private Connector connector;

  @Test
  public void sampleIngest() throws Exception {
    Reader input = new StringReader("I wish I may I wish I might");
    ingest.ingest(input);

    Scanner scanner = connector.createScanner(ngramTable, new Authorizations());
    NGramEntry.NGramEntryTypo typo = new NGramEntry.NGramEntryTypo();
    NGramEntry.NGramEntryTypo.Scanner typoScanner = typo.newScanner(scanner);
    ArrayList<Map.Entry<Typo<List<String>, String, String, Long>.Key, Long>> entries = Lists.newArrayList(typoScanner);

    checkEntry(entries.get(0), Lists.newArrayList("I", "may"), NGramTable.CFs.Meta.name,
        NGramTable.CFs.Meta.CQs.Total.name, 1L);
    checkEntry(entries.get(1), Lists.newArrayList("I", "may"), NGramTable.CFs.Next.name, "I", 1L);

    checkEntry(entries.get(2), Lists.newArrayList("I", "wish"), NGramTable.CFs.Meta.name,
        NGramTable.CFs.Meta.CQs.Total.name, 2L);
    checkEntry(entries.get(3), Lists.newArrayList("I", "wish"), NGramTable.CFs.Next.name, "I", 2L);

    checkEntry(entries.get(4), Lists.newArrayList("may", "I"), NGramTable.CFs.Meta.name,
        NGramTable.CFs.Meta.CQs.Total.name, 1L);
    checkEntry(entries.get(5), Lists.newArrayList("may", "I"), NGramTable.CFs.Next.name, "wish", 1L);

    checkEntry(entries.get(6), Lists.newArrayList("wish", "I"), NGramTable.CFs.Meta.name,
        NGramTable.CFs.Meta.CQs.Total.name, 2L);
    checkEntry(entries.get(7), Lists.newArrayList("wish", "I"), NGramTable.CFs.Next.name, "may", 1L);
    checkEntry(entries.get(8), Lists.newArrayList("wish", "I"), NGramTable.CFs.Next.name, "might", 1L);
  }

  private static void checkEntry(Map.Entry<Typo<List<String>, String, String, Long>.Key, Long> entry, List<String> row, String columnFamily, String columnQualifier, Long value) {
    assertThat(entry.getKey().getRow(), is(row));
    assertThat(entry.getKey().getColumnFamily(), is(columnFamily));
    assertThat(entry.getKey().getColumnQualifier(), is(columnQualifier));
    assertThat(entry.getValue(), is(value));
  }

  @Test
  public void bookIngest() throws Exception {
    Reader input = new InputStreamReader(this.getClass().getResourceAsStream("/huck finn.txt"));
    ingest.ingest(input);
    input = new InputStreamReader(this.getClass().getResourceAsStream("/sherlock.txt"));
    ingest.ingest(input);
    input = new InputStreamReader(this.getClass().getResourceAsStream("/moby dick.txt"));
    ingest.ingest(input);

    Scanner scanner = connector.createScanner(ngramTable, new Authorizations());
    NGramEntry.NGramEntryTypo typo = new NGramEntry.NGramEntryTypo();
    NGramEntry.NGramEntryTypo.Scanner typoScanner = typo.newScanner(scanner);
    ArrayList<Map.Entry<Typo<List<String>, String, String, Long>.Key, Long>> entries = Lists.newArrayList(typoScanner);

    //rough estimate from book word counts, there should be at least 500,000 entries
    Assert.assertThat(entries.size(), greaterThan(500000));
  }
}
