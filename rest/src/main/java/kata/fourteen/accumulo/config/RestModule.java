package kata.fourteen.accumulo.config;

import kata.fourteen.accumulo.resource.TextGeneratorResource;
import kata.fourteen.accumulo.resource.TextIngestResource;

import com.google.inject.Binder;
import com.google.inject.Module;

public class RestModule implements Module {
  @Override
  public void configure(Binder binder) {
    binder.bind(TextIngestResource.class);
    binder.bind(TextGeneratorResource.class);
  }
}
