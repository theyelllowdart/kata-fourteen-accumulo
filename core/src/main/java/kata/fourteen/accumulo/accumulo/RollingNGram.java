package kata.fourteen.accumulo.accumulo;

import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Preconditions;

public class RollingNGram {
  private final int length;
  private final LinkedList<String> tokens;

  public RollingNGram(int length) {
    this(length, null);
  }

  public RollingNGram(int length, List<String> tokens) {
    Preconditions.checkArgument(length > 0, "length must be a positive integer");
    this.length = length;
    if (tokens == null) {
      this.tokens = new LinkedList<>();
    } else {
      this.tokens = new LinkedList<>(tokens);
    }
  }

  public String push(String token) {
    String poppedToken = null;
    if (tokens.size() == length) {
      poppedToken = tokens.pop();
    }
    tokens.add(token);
    return poppedToken;
  }

  public String pop(){
    return tokens.pop();
  }

  public boolean isFilled() {
    return tokens.size() == length;
  }

  public List<String> getTokens() {
    return tokens;
  }

  public int size(){
    return tokens.size();
  }
}
