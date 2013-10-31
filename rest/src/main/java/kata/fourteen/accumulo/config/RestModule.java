package kata.fourteen.accumulo.config;

import com.google.inject.Binder;
import com.google.inject.Module;
import kata.fourteen.accumulo.resource.TextGeneratorResource;
import kata.fourteen.accumulo.resource.TextIngestResource;

public class RestModule implements Module {
  @Override
  public void configure(Binder binder) {
    binder.bind(TextIngestResource.class);
    binder.bind(TextGeneratorResource.class);
  }
}
