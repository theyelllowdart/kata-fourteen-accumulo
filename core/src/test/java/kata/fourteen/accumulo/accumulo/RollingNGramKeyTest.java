package kata.fourteen.accumulo.accumulo;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.beust.jcommander.internal.Lists;
import org.junit.Test;

public class RollingNGramKeyTest {
  @Test
  public void isComplete() throws Exception {
    RollingNGram rollingNGramKey = new RollingNGram(2);
    assertThat(rollingNGramKey.isFilled(), is(false));
    rollingNGramKey.push("hello");
    assertThat(rollingNGramKey.isFilled(), is(false));
    rollingNGramKey.push("world");
    assertThat(rollingNGramKey.isFilled(), is(true));
    rollingNGramKey.push("galaxy");
    assertThat(rollingNGramKey.isFilled(), is(true));
  }

  @Test
  public void rolls(){
    RollingNGram rollingNGramKey = new RollingNGram(2);
    rollingNGramKey.push("hello");
    rollingNGramKey.push("world");
    rollingNGramKey.push("galaxy");
    assertThat(rollingNGramKey.getTokens(), is(Lists.newArrayList("world", "galaxy")));
  }


}
