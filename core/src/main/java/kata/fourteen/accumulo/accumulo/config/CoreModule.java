package kata.fourteen.accumulo.accumulo.config;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.netflix.curator.framework.CuratorFramework;
import kata.fourteen.accumulo.accumulo.config.providers.CuratorFrameworkProvider;
import kata.fourteen.accumulo.accumulo.generation.EnglishTextGenerator;
import kata.fourteen.accumulo.accumulo.generation.NGramTokenGenerator;
import kata.fourteen.accumulo.accumulo.generation.TextGenerator;
import kata.fourteen.accumulo.accumulo.generation.TokenGenerator;

public class CoreModule implements Module {
  private final String zookeeperConnection;
  private final Integer ngramSize;
  private final String ngramTable;

  public CoreModule(String zookeeperConnection, Integer ngramSize, String ngramTable) {
    this.zookeeperConnection = zookeeperConnection;
    this.ngramSize = ngramSize;
    this.ngramTable = ngramTable;
  }

  @Override
  public void configure(Binder binder) {
    binder.bindConstant().annotatedWith(Names.named(SettingKeys.ZOOKEEPER_CONNECTION)).to(zookeeperConnection);
    binder.bind(CuratorFramework.class).toProvider(CuratorFrameworkProvider.class);

    binder.bindConstant().annotatedWith(Names.named(SettingKeys.NGRAM_SIZE)).to(ngramSize);
    binder.bindConstant().annotatedWith(Names.named(SettingKeys.NGRAM_TABLE_NAME)).to(ngramTable);

    binder.bind(TextGenerator.class).to(EnglishTextGenerator.class);
    binder.bind(TokenGenerator.class).to(NGramTokenGenerator.class);
  }
}
