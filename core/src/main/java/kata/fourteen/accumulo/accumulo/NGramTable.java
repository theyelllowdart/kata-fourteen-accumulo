package kata.fourteen.accumulo.accumulo;

import org.apache.hadoop.io.Text;

/**
 * <pre>
 * |-----------------------------------------------------------------------------------------------------------------\
 * | Row ID                       | Column Fam | Column Qualifier              | Value                                |
 * |==============================|============|===============================|======================================|
 * | sequential words (n-1)       |            | next word in sequence (n)     |                                      |
 * | ["word1", "word2"]           |   "next    | "word3"                       | 1 (will be combined by LongCombiner) |
 * -------------------------------------------------------------------------------------------------------------------|
 * | sequential words (n-1)       |            |count of next entries for RowID|                                      |
 * | ["word1", "word2"]           |   "meta"   | "total"                       | 1 (will be combined by LongCombiner) |
 * -------------------------------------------------------------------------------------------------------------------
 * </pre>
 */
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
        public static class Total {
          public static final Text text = new Text("total");
          public static final String name = text.toString();
        }

      }
    }
  }
}
