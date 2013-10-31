package kata.fourteen.accumulo.accumulo.generation;

import org.apache.accumulo.core.client.TableNotFoundException;

import java.util.Iterator;

public interface TextGenerator {
  public Iterator<String> generate();
}
