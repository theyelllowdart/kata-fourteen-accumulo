package kata.fourteen.accumulo.accumulo;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class RollingQueueTest {
  @Test
  public void isComplete() throws Exception {
    RollingQueue<String> rollingQueueKey = new RollingQueue<>(2);
    assertThat(rollingQueueKey.isFilled(), is(false));
    rollingQueueKey.push("hello");
    assertThat(rollingQueueKey.isFilled(), is(false));
    rollingQueueKey.push("world");
    assertThat(rollingQueueKey.isFilled(), is(true));
    rollingQueueKey.push("galaxy");
    assertThat(rollingQueueKey.isFilled(), is(true));
  }

  @Test
  public void rolls() {
    RollingQueue<String> rollingQueueKey = new RollingQueue<>(2);
    rollingQueueKey.push("hello");
    rollingQueueKey.push("world");
    rollingQueueKey.push("galaxy");
    // assertThat(rollingQueueKey.iterator(), is(Lists.newArrayList("world", "galaxy").iterator()));
    assertThat(rollingQueueKey, hasItems("galaxy", "world"));
  }

}
