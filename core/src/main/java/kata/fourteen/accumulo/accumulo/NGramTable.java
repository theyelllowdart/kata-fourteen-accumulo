package kata.fourteen.accumulo.accumulo;

import org.apache.hadoop.io.Text;

public class NGramTable {
  /**
   * NGramTable Column Families
   */
  public static class CFs {

    public static class Next {
      public static final Text text = new Text("next");
      public static final String name = text.toString();
    }

    public static class Meta {
      public static final Text text = new Text("meta");
      public static final String name = text.toString();

      public static class CQs {
        public static class Total{
          public static final Text text = new Text("total");
          public static final String name = text.toString();
        }

      }
    }
  }
}
