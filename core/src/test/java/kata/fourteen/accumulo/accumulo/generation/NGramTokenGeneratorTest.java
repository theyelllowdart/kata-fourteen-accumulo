package kata.fourteen.accumulo.accumulo.generation;

import com.google.common.collect.Lists;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.Test;

import java.util.Iterator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class NGramTokenGeneratorTest {
  @Tested
  private NGramTokenGenerator tokenGenerator;

  @Injectable
  private NGramReader reader;

  @Injectable
  private int ngramSize = 2;

  @Test
  public void generate_matchesSample() throws Exception {
    new Expectations() {
      {
        reader.getInitial();
        returns(Lists.newArrayList("I", "wish"));

        reader.getNext(withEqual(Lists.newArrayList("I", "wish")));
        returns("I");

        reader.getNext(withEqual(Lists.newArrayList("wish", "I")));
        returns("may");

        reader.getNext(withEqual(Lists.newArrayList("I", "may")));
        returns("I");

        reader.getNext(withEqual(Lists.newArrayList("may", "I")));
        returns("wish");

        reader.getNext(withEqual(Lists.newArrayList("I", "wish")));
        returns("I");

        reader.getNext(withEqual(Lists.newArrayList("wish", "I")));
        returns("might");

        reader.getNext(withEqual(Lists.newArrayList("I", "might")));
        returns(null);
      }
    };

    Iterator<String> generatedTokens = tokenGenerator.generate();

    assertThat(Lists.newArrayList(generatedTokens),
        is(Lists.newArrayList("I", "wish", "I", "may", "I", "wish", "I", "might")));

  }

}
