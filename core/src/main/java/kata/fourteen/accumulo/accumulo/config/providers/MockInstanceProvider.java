package kata.fourteen.accumulo.accumulo.config.providers;

import javax.inject.Provider;

import org.apache.accumulo.core.client.Instance;
import org.apache.accumulo.core.client.mock.MockInstance;

public class MockInstanceProvider implements Provider<Instance> {
  @Override
  public Instance get() {
    return new MockInstance();
  }
}
