package kata.fourteen.accumulo.accumulo.generation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import kata.fourteen.accumulo.accumulo.NGramEntry;
import kata.fourteen.accumulo.accumulo.NGramTable;
import kata.fourteen.accumulo.accumulo.WeightedSkippingIterator;
import kata.fourteen.accumulo.accumulo.config.SettingKeys;

import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.IteratorSetting;
import org.apache.accumulo.core.client.Scanner;
import org.apache.accumulo.core.client.TableNotFoundException;
import org.apache.accumulo.core.data.Range;
import org.apache.accumulo.core.security.Authorizations;
import org.apache.accumulo.typo.Typo;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.math.random.RandomDataImpl;

public class NGramReader {
  private final String ngramTable;
  private final Connector connector;
  private final int ngramSize;

  @Inject
  public NGramReader(@Named(SettingKeys.NGRAM_TABLE_NAME) String ngramTable, Connector connector,
      @Named(SettingKeys.NGRAM_SIZE) int ngramSize) {
    this.ngramTable = ngramTable;
    this.connector = connector;
    this.ngramSize = ngramSize;
  }

  /**
   * Retrieve a random ngram.
   * <p/>
   * Note not all ngram are equally likely to be selected.
   * @throws TableNotFoundException
   */
  public List<String> getInitial() throws TableNotFoundException {
    // try random position
    Scanner scanner = connector.createScanner(ngramTable, new Authorizations());
    NGramEntry.NGramEntryTypo typo = new NGramEntry.NGramEntryTypo();
    // max row key length according to Mary Poppins
    int maxRowKeyLength = "Supercalifragilisticexpialidocious".length() * ngramSize - 1;
    scanner.setRange(new Range(RandomStringUtils.randomAscii(maxRowKeyLength), null));
    NGramEntry.NGramEntryTypo.Scanner typoScanner = typo.newScanner(scanner);
    Iterator<Map.Entry<Typo<List<String>, String, String, Long>.Key, Long>> randomIterator = typoScanner.iterator();
    if (randomIterator.hasNext()) {
      return randomIterator.next().getKey().getRow();
    } else {
      // random element wasn't found; just grab the first entry from the table
      scanner.setRange(new Range());
      Iterator<Map.Entry<Typo<List<String>, String, String, Long>.Key, Long>> infinityIterator = typoScanner.iterator();
      if (infinityIterator.hasNext()) {
        return infinityIterator.next().getKey().getRow();
      } else {
        return null;
      }
    }
  }

  /**
   * Get random next using roulette selection
   * @throws TableNotFoundException
   */
  public String getNext(List<String> key) throws TableNotFoundException {
    //fetch total for "next" entries for key
    Scanner scanner = connector.createScanner(ngramTable, new Authorizations());
    NGramEntry.NGramEntryTypo typo = new NGramEntry.NGramEntryTypo();
    scanner.setRange(typo.newRange(key));
    scanner.fetchColumnFamily(NGramTable.CFs.Meta.text);
    NGramEntry.NGramEntryTypo.Scanner typoScanner = typo.newScanner(scanner);
    Iterator<Map.Entry<Typo<List<String>, String, String, Long>.Key, Long>> iterator = typoScanner.iterator();
    if (iterator.hasNext()) {
      Long totalNextEntries = iterator.next().getValue();

      //fetch a random next using roulette selection via WeightedSkippingIterator
      scanner.clearColumns();
      scanner.fetchColumnFamily(NGramTable.CFs.Next.text);
      IteratorSetting iteratorSettings = new IteratorSetting(1, WeightedSkippingIterator.class);
      Long target = new RandomDataImpl().nextLong(0, totalNextEntries);
      WeightedSkippingIterator.setTarget(iteratorSettings, target);
      WeightedSkippingIterator.setEncodingType(iteratorSettings, typo.getEncoders().getValueEncoder().getClass()
          .getName());
      scanner.addScanIterator(iteratorSettings);

      Iterator<Map.Entry<Typo<List<String>, String, String, Long>.Key, Long>> nextIterator = typoScanner.iterator();
      if (nextIterator.hasNext()) {
        return nextIterator.next().getKey().getColumnQualifier();
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

}
