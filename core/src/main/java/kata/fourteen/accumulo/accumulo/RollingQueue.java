package kata.fourteen.accumulo.accumulo;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Preconditions;

/**
 *
 */
public class RollingQueue<T> implements Iterable<T>{
  private final int length;
  private final LinkedList<T> tokens;

  public RollingQueue(int length) {
    this(length, null);
  }

  public RollingQueue(int length, List<T> tokens) {
    Preconditions.checkArgument(length > 0, "length must be a positive integer");
    this.length = length;
    if (tokens == null) {
      this.tokens = new LinkedList<>();
    } else {
      this.tokens = new LinkedList<>(tokens);
    }
  }

  public T push(T token) {
    T poppedToken = null;
    if (tokens.size() == length) {
      poppedToken = tokens.pop();
    }
    tokens.add(token);
    return poppedToken;
  }

  public T pop() {
    return tokens.pop();
  }

  public boolean isFilled() {
    return tokens.size() == length;
  }

  public int size() {
    return tokens.size();
  }

  @Override
  public Iterator<T> iterator() {
    return tokens.iterator();
  }
}
