package kata.fourteen.accumulo.accumulo.generation;

import com.google.common.collect.Sets;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import org.apache.commons.lang.WordUtils;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

/**
 * Convert tokens into pretty text with proper spacing and capitalization
 */
public class EnglishTextGenerator implements TextGenerator {

  private final PennTreebankLanguagePack languagePack;
  private final TokenGenerator tokenGenerator;

  @Inject
  public EnglishTextGenerator(TokenGenerator tokenGenerator) {
    this.languagePack = new PennTreebankLanguagePack();
    this.tokenGenerator = tokenGenerator;
  }

  @Override
  public Iterator<String> generate() {
    return new EnglishTextGeneratorIterator(tokenGenerator.generate(), languagePack);
  }

  private static class EnglishTextGeneratorIterator implements Iterator<String> {
    private final HashSet<String> contractions = Sets.newHashSet("n't", "'m", "'s", "'re", "'ve", "'d", "'ll");

    private final Iterator<String> tokenIterator;
    private final PennTreebankLanguagePack languagePack;
    private final HashSet<String> contractiblePunctuationWords;
    private final HashMap<Integer, Double> paragraphProbByFinalPunctuationCount = new HashMap<>();
    private final Random random;

    private String lastToken = null;
    private int finalPunctuationCount;

    private EnglishTextGeneratorIterator(Iterator<String> tokenIterator, PennTreebankLanguagePack languagePack) {
      this.tokenIterator = tokenIterator;
      this.languagePack = languagePack;

      contractiblePunctuationWords = Sets.newHashSet(languagePack.punctuationWords());
      contractiblePunctuationWords.remove("--");

      paragraphProbByFinalPunctuationCount.put(0, 0.0);
      paragraphProbByFinalPunctuationCount.put(1, .1);
      paragraphProbByFinalPunctuationCount.put(2, .2);
      paragraphProbByFinalPunctuationCount.put(3, .4);
      paragraphProbByFinalPunctuationCount.put(4, .75);
      paragraphProbByFinalPunctuationCount.put(5, .85);
      paragraphProbByFinalPunctuationCount.put(6, .90);
      paragraphProbByFinalPunctuationCount.put(7, .95);
      paragraphProbByFinalPunctuationCount.put(8, .100);

      random = new Random();
    }

    @Override
    public boolean hasNext() {
      return tokenIterator.hasNext();
    }

    @Override
    public String next() {
      String returnText;
      String nextToken = tokenIterator.next();

      // if text start, capitalize token
      if (lastToken == null) {
        returnText = WordUtils.capitalize(nextToken);
      }
      // if final punctuation, increment counter and probabilistically break paragraph
      else if (languagePack.isSentenceFinalPunctuationTag(nextToken)) {
        finalPunctuationCount++;
        Double paragraphProb = paragraphProbByFinalPunctuationCount.get(finalPunctuationCount);
        if (random.nextDouble() <= paragraphProb) {
          // reset final punctuation counter
          finalPunctuationCount = 0;
          returnText = nextToken + System.lineSeparator() + System.lineSeparator();
        } else {
          returnText = nextToken;
        }
      }
      // if the last token was final punctuation, prepend a space and capitalize current token
      else if (languagePack.isSentenceFinalPunctuationTag(lastToken)) {
        returnText = " " + WordUtils.capitalize(nextToken);
      }
      // if next token is a contraction or a punctuation symbol not requiring a space then don't prepend a space
      else if (contractiblePunctuationWords.contains(nextToken) || contractions.contains(nextToken)) {
        returnText = nextToken;
      }
      // default: prepend a space
      else {
        returnText = " " + nextToken;
      }
      lastToken = nextToken;
      return returnText;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
}
