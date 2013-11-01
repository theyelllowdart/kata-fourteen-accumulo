package kata.fourteen.accumulo.accumulo.config.providers;

import kata.fourteen.accumulo.accumulo.config.SettingKeys;
import org.apache.accumulo.core.client.AccumuloException;
import org.apache.accumulo.core.client.AccumuloSecurityException;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.Instance;
import org.apache.accumulo.core.client.security.tokens.PasswordToken;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

public class ConnectorProvider implements Provider<Connector> {
  private final Instance instance;
  private final String username;
  private final String password;

  @Inject
  public ConnectorProvider(Instance instance, @Named(SettingKeys.ACCUMULO_USERNAME) String username,
      @Named(SettingKeys.ACCUMULO_PASSWORD) String password) {
    this.instance = instance;
    this.username = username;
    this.password = password;
  }

  @Override
  public Connector get() {
    try {
      return instance.getConnector(username, new PasswordToken(password));
    } catch (AccumuloException | AccumuloSecurityException e) {
      throw new RuntimeException(e);
    }
  }
}
