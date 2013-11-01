package kata.fourteen.accumulo.resource;

import kata.fourteen.accumulo.accumulo.ingest.TextIngester;
import org.apache.accumulo.core.client.MutationsRejectedException;
import org.apache.accumulo.core.client.TableNotFoundException;

import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.Reader;

@Path("/ingest")
public class TextIngestResource {
  private final TextIngester textIngester;

  @Inject
  public TextIngestResource(TextIngester textIngester) {
    this.textIngester = textIngester;
  }

  @PUT
  // we should probably return a model for xml or json but lets keep it simple
  @Produces(MediaType.TEXT_PLAIN)
  public long ingest(Reader reader) throws MutationsRejectedException, TableNotFoundException {
    return textIngester.ingest(reader);
  }
}
