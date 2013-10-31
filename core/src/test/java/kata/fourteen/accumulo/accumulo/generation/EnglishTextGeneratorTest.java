package kata.fourteen.accumulo.accumulo.generation;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import mockit.Injectable;
import mockit.NonStrictExpectations;
import mockit.Tested;
import org.junit.Test;

import java.util.Iterator;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EnglishTextGeneratorTest {
  @Tested
  private EnglishTextGenerator englishTextGenerator;

  @Injectable
  private TokenGenerator tokenGenerator;

  @Test
  public void generate_capitalizeFirstWord() throws Exception {
    new NonStrictExpectations() {
      {
        tokenGenerator.generate();
        returns(Lists.newArrayList("test").iterator());
      }
    };

    Iterator<String> englishText = englishTextGenerator.generate();
    assertThat(Joiner.on("").join(englishText), is("Test"));
  }

  @Test
  public void generate_combineContractions() throws Exception {
    new NonStrictExpectations() {
      {
        tokenGenerator.generate();
        returns(Lists.newArrayList("Is", "n't").iterator());
      }
    };

    Iterator<String> englishText = englishTextGenerator.generate();
    assertThat(Joiner.on("").join(englishText), is("Isn't"));
  }

  @Test
  public void generate_insertWhiteSpaceBetweenWords() throws Exception {
    new NonStrictExpectations() {
      {
        tokenGenerator.generate();
        returns(Lists.newArrayList("Testing", "is", "fun").iterator());
      }
    };

    Iterator<String> englishText = englishTextGenerator.generate();
    assertThat(Joiner.on("").join(englishText), is("Testing is fun"));
  }

  @Test
  public void generate_punctuationAppendedToWord() throws Exception {
    new NonStrictExpectations() {
      {
        tokenGenerator.generate();
        returns(Lists.newArrayList("Testing", ",").iterator());
      }
    };

    Iterator<String> englishText = englishTextGenerator.generate();
    assertThat(Joiner.on("").join(englishText), is("Testing,"));
  }
}
