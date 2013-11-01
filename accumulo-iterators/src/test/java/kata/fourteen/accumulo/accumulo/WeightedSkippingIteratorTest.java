package kata.fourteen.accumulo.accumulo;

import org.apache.accumulo.core.client.BatchWriter;
import org.apache.accumulo.core.client.BatchWriterConfig;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Instance;
import org.apache.accumulo.core.client.IteratorSetting;
import org.apache.accumulo.core.client.Scanner;
import org.apache.accumulo.core.client.mock.MockInstance;
import org.apache.accumulo.core.client.security.tokens.PasswordToken;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Mutation;
import org.apache.accumulo.core.data.Value;
import org.apache.accumulo.core.iterators.LongCombiner;
import org.apache.accumulo.core.security.Authorizations;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class WeightedSkippingIteratorTest {

  @Parameterized.Parameters(name = "target({0})={1}")
  public static Iterable<Object[]> data() {
    return Arrays.asList(new Object[][] { { 0, "[0-5)" }, { 1, "[0-5)" }, { 2, "[0-5)" }, { 3, "[0-5)" },
        { 4, "[0-5)" }, { 5, "[5-6)" }, { 6, "[6-8)" }, { 7, "[6-8)" }, { 8, "[8-12)" }, { 9, "[8-12)" },
        { 10, "[8-12)" }, { 11, "[8-12)" } });
  }

  private final long inputTarget;
  private final String expectedColumnFamily;

  public WeightedSkippingIteratorTest(int inputTarget, String expectedColumnFamily) {
    this.inputTarget = inputTarget;
    this.expectedColumnFamily = expectedColumnFamily;
  }

  @Test
  public void skipWithKnownRanges() throws Exception {
    Instance instance = new MockInstance();
    Connector connector = instance.getConnector("root", new PasswordToken(""));

    final String tableName = "weightedSkippingIteratorTest";
    connector.tableOperations().create(tableName);

    BatchWriter writer = connector.createBatchWriter(tableName, new BatchWriterConfig());

    // range [0-5)
    Mutation mutation0 = new Mutation(new byte[] { 0, 0 });
    mutation0.put("[0-5)", "", "5");
    writer.addMutation(mutation0);

    // range [5-6)
    Mutation mutation1 = new Mutation(new byte[] { 0, 1 });
    mutation1.put("[5-6)", "", "1");
    writer.addMutation(mutation1);

    // range [6-8)
    Mutation mutation2 = new Mutation(new byte[] { 1, 0 });
    mutation2.put("[6-8)", "", "2");
    writer.addMutation(mutation2);

    // range [8-12)
    Mutation mutation3 = new Mutation(new byte[] { 1, 1 });
    mutation3.put("[8-12)", "", "4");
    writer.addMutation(mutation3);

    writer.close();

    Scanner scanner = connector.createScanner(tableName, new Authorizations());

    IteratorSetting iteratorSettings = new IteratorSetting(1, WeightedSkippingIterator.class);
    WeightedSkippingIterator.setTarget(iteratorSettings, inputTarget);
    WeightedSkippingIterator.setEncodingType(iteratorSettings, LongCombiner.StringEncoder.class.getName());
    scanner.addScanIterator(iteratorSettings);

    Iterator<Map.Entry<Key, Value>> iterator = scanner.iterator();
    String actualColumnFamily = iterator.next().getKey().getColumnFamily().toString();
    assertThat(actualColumnFamily, is(expectedColumnFamily));
    //only one row should be returned
    assertThat(iterator.hasNext(), is(false));
  }
}
