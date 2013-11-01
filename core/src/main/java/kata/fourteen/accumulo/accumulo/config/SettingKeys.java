package kata.fourteen.accumulo.accumulo.config;

/**
 * Houses binding keys for application settings
 */
public class SettingKeys {
  public static final String ZOOKEEPER_CONNECTION = "kata-fourteen.zookeeper.connection";

  public static final String CONNECTION_TYPE = "kata-fourteen.accumulo.connector.type";
  public static final String ACCUMULO_INSTANCE = "kata-fourteen.accumulo.instance";
  public static final String ACCUMULO_USERNAME = "kata-fourteen.accumulo.username";
  public static final String ACCUMULO_PASSWORD = "kata-fourteen.accumulo.password";

  public static final String NGRAM_TABLE_NAME = "kata-fourteen.ngram.table.name";
  public static final String NGRAM_SIZE = "kata-fourteen.ngram.size";
}
