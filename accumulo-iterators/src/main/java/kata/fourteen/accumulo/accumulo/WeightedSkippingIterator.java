package kata.fourteen.accumulo.accumulo;

import org.apache.accumulo.core.client.IteratorSetting;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Value;
import org.apache.accumulo.core.iterators.IteratorEnvironment;
import org.apache.accumulo.core.iterators.SkippingIterator;
import org.apache.accumulo.core.iterators.SortedKeyValueIterator;
import org.apache.accumulo.core.iterators.TypedValueCombiner;
import org.apache.accumulo.start.classloader.vfs.AccumuloVFSClassLoader;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;

/**
 * Iterator for skipping entries with weights (assumes each entry has value long which represent its weight). The
 * iterator will sum each entry until it reaches the target sum.
 */
public class WeightedSkippingIterator extends SkippingIterator {
  private static final String ENCODER_CLASS_NAME = "encoder";
  private static final String TARGET_SUM = "targetSum";

  private long targetSum;
  private long currentSum = 0L;
  private Map.Entry<Key, Value> targetEntry;
  private TypedValueCombiner.Encoder<Long> encoder;

  @Override
  public void init(SortedKeyValueIterator<Key, Value> source, Map<String, String> options, IteratorEnvironment env) throws IOException {
    super.init(source, options, env);
    targetSum = Long.parseLong(options.get(TARGET_SUM));
    setEncoder(options.get(ENCODER_CLASS_NAME));
  }

  /**
   * Sums each entry in range until <code>targetSum</code> is reached or there are no more entries left.
   * 
   * @throws IOException
   */
  @Override
  protected void consume() throws IOException {
    while (super.hasTop() && currentSum <= targetSum) {
      targetEntry = new AbstractMap.SimpleImmutableEntry<>(super.getTopKey(), super.getTopValue());
      currentSum += encoder.decode(getTopValue().get());
      getSource().next();
    }
  }

  @Override
  public boolean hasTop() {
    return targetEntry != null;
  }

  @Override
  public Key getTopKey() {
    return targetEntry.getKey();
  }

  @Override
  public Value getTopValue() {
    return targetEntry.getValue();
  }

  @Override
  public void next() throws IOException {
    // ensure targetEntry is only returned once
    targetEntry = null;
  }

  public static void setTarget(IteratorSetting cfg, Long target) {
    cfg.addOption(TARGET_SUM, target.toString());
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

  /**
   * @see org.apache.accumulo.core.iterators.LongCombiner#setEncodingType(IteratorSetting, String)
   */
  public static void setEncodingType(IteratorSetting is, String encoderClassName) {
    is.addOption(ENCODER_CLASS_NAME, encoderClassName);
  }
}
