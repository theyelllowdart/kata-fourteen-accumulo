package kata.fourteen.accumulo.accumulo;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;

import org.apache.accumulo.core.client.IteratorSetting;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Value;
import org.apache.accumulo.core.iterators.IteratorEnvironment;
import org.apache.accumulo.core.iterators.SkippingIterator;
import org.apache.accumulo.core.iterators.SortedKeyValueIterator;
import org.apache.accumulo.core.iterators.TypedValueCombiner;
import org.apache.accumulo.start.classloader.vfs.AccumuloVFSClassLoader;

public class WeightedSkippingIterator extends SkippingIterator {
  private static final String ENCODER_CLASS_NAME = "encoder";
  private static final String TARGET = "target";

  private long target;
  private long current = 0L;
  private Map.Entry<Key, Value> last;

  private TypedValueCombiner.Encoder<Long> encoder;

  @Override
  public void init(SortedKeyValueIterator<Key, Value> source, Map<String, String> options, IteratorEnvironment env) throws IOException {
    super.init(source, options, env);
    target = Long.parseLong(options.get(TARGET));
    setEncoder(options.get(ENCODER_CLASS_NAME));
  }

  @Override
  protected void consume() throws IOException {
    while (super.hasTop() && current <= target) {
      last = new AbstractMap.SimpleImmutableEntry<>(super.getTopKey(), super.getTopValue());
      current += encoder.decode(getTopValue().get());
      getSource().next();
    }
  }

  @Override
  public boolean hasTop() {
    return last != null;
  }

  @Override
  public Key getTopKey() {
    return last.getKey();
  }

  @Override
  public Value getTopValue() {
    return last.getValue();
  }

  @Override
  public void next() throws IOException {
    last = null;
  }

  public static void setTarget(IteratorSetting cfg, Long target) {
    cfg.addOption(TARGET, target.toString());
  }

  protected void setEncoder(String encoderClass) {
    try {
      @SuppressWarnings("unchecked")
      Class<? extends TypedValueCombiner.Encoder<Long>> clazz = (Class<? extends TypedValueCombiner.Encoder<Long>>) AccumuloVFSClassLoader
          .loadClass(encoderClass, TypedValueCombiner.Encoder.class);
      encoder = clazz.newInstance();
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static void setEncodingType(IteratorSetting is, String encoderClassName) {
    is.addOption(ENCODER_CLASS_NAME, encoderClassName);
  }
}
