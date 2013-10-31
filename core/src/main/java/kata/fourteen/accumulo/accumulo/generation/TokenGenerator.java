package kata.fourteen.accumulo.accumulo.generation;

import java.util.Iterator;

public interface TokenGenerator {
  public Iterator<String> generate();
}
