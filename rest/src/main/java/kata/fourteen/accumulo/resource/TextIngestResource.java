package kata.fourteen.accumulo.resource;

import java.io.Reader;

import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import kata.fourteen.accumulo.accumulo.ingest.TextIngester;

import org.apache.accumulo.core.client.MutationsRejectedException;
import org.apache.accumulo.core.client.TableNotFoundException;

@Path("/ingest")
public class TextIngestResource {
  private final TextIngester textIngester;

  @Inject
  public TextIngestResource(TextIngester textIngester) {
    this.textIngester = textIngester;
  }

  @PUT
  public void ingest(Reader reader) throws MutationsRejectedException, TableNotFoundException {
    textIngester.ingest(reader);
  }
}
