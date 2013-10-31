package kata.fourteen.accumulo.accumulo.config.providers;

import org.apache.accumulo.core.client.Instance;
import org.apache.accumulo.core.client.mock.MockInstance;

import javax.inject.Provider;

public class MockInstanceProvider implements Provider<Instance> {
  @Override
  public Instance get() {
    return new MockInstance();
  }
}
