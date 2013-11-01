package kata.fourteen.accumulo.accumulo.bootstrap;

import javax.inject.Inject;
import javax.inject.Named;

import kata.fourteen.accumulo.accumulo.NGramEntry;
import kata.fourteen.accumulo.accumulo.config.SettingKeys;

import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.IteratorSetting;
import org.apache.accumulo.core.client.admin.TableOperations;
import org.apache.accumulo.core.iterators.user.SummingCombiner;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.recipes.locks.InterProcessMutex;

/**
 * Class for bootstrapping the NGRAM accumulo table
 */
public class NGramTableBootstrap {
  private final String NGRAM_TABLE_CREATION_LOCK_PATH = "/ngramtable/create";

  private final Connector connector;
  private final String ngramTable;
  private final CuratorFramework curatorFramework;

  @Inject
  public NGramTableBootstrap(Connector connector, @Named(SettingKeys.NGRAM_TABLE_NAME) String ngramTable,
      CuratorFramework curatorFramework) {
    this.connector = connector;
    this.ngramTable = ngramTable;
    this.curatorFramework = curatorFramework;
  }

  public void bootstrap() throws Exception {
    TableOperations tableOperations = connector.tableOperations();

    // double-check lock for creating the ngram table
    if (!tableOperations.exists(ngramTable)) {
      InterProcessMutex mutex = new InterProcessMutex(curatorFramework, NGRAM_TABLE_CREATION_LOCK_PATH);
      try {
        mutex.acquire();
        if (!tableOperations.exists(ngramTable)) {
          // keep all versions; crucial to data model outlined in NGramTable javadoc
          tableOperations.create(ngramTable, false);

          // each key should be combined to increase performance
          IteratorSetting ngramSummationSettings = new IteratorSetting(1, SummingCombiner.class);
          String valueEncoder = new NGramEntry.NGramEntryTypo().getEncoders().getValueEncoder().getClass().getName();
          SummingCombiner.setEncodingType(ngramSummationSettings, valueEncoder);
          SummingCombiner.setCombineAllColumns(ngramSummationSettings, true);
          tableOperations.attachIterator(ngramTable, ngramSummationSettings);
        }
      } finally {
        mutex.release();
      }
    }
  }
}
