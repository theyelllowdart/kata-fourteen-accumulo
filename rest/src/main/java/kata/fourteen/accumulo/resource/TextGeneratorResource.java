package kata.fourteen.accumulo.resource;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;

import kata.fourteen.accumulo.accumulo.generation.TextGenerator;
import org.apache.accumulo.core.client.TableNotFoundException;

@Path("/generator")
public class TextGeneratorResource {
  private final TextGenerator textGenerator;

  @Inject
  public TextGeneratorResource(TextGenerator textGenerator) {
    this.textGenerator = textGenerator;
  }

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public StreamingOutput generate(@QueryParam("maxLength") final long maxLength) throws TableNotFoundException {
    final Iterator<String> generate = textGenerator.generate();
    return new StreamingOutput() {
      @Override
      public void write(OutputStream output) throws IOException, WebApplicationException {
        long length = 0;
        while (generate.hasNext() && length < maxLength) {
          output.write(generate.next().getBytes());
          length++;
        }
        output.flush();
      }
    };
  }
}
