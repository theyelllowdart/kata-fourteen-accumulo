package kata.fourteen.accumulo.accumulo;

public class EmptyTableException extends IllegalStateException {
  public EmptyTableException(String s) {
    super(s);
  }
}
