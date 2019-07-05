package org.jfugue.pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This utility takes replacement strings with dollar signs, like "$0q $1h $2w", and replaces each $
 * index with a value from the array of candidates. $_ is replaced with the underscoreReplacement.
 * Returns the resulting Pattern. Special replacements, like $R or $-B, can be specified in the
 * specialReplacement map. Current known users include ChordProgression, Intervals, and
 * BrokenChordPreprocessor.
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class ReplacementFormatUtil {

  /**
   * <p>replaceDollarsWithCandidates.</p>
   *
   * @param sequence a {@link java.lang.String} object.
   * @param candidates an array of {@link org.jfugue.pattern.PatternProducer} objects.
   * @param underscoreReplacement a {@link org.jfugue.pattern.PatternProducer} object.
   * @return a {@link org.jfugue.pattern.Pattern} object.
   */
  public static Pattern replaceDollarsWithCandidates(String sequence, PatternProducer[] candidates,
    PatternProducer underscoreReplacement) {
    return replaceDollarsWithCandidates(sequence, candidates, underscoreReplacement,
      null, " ", " ", null);
  }

  /**
   * <p>replaceDollarsWithCandidates.</p>
   *
   * @param sequence a {@link java.lang.String} object.
   * @param candidates an array of {@link org.jfugue.pattern.PatternProducer} objects.
   * @param underscoreReplacement a {@link org.jfugue.pattern.PatternProducer} object.
   * @param specialReplacers a {@link java.util.Map} object.
   * @param inputSeparator a {@link java.lang.String} object.
   * @param outputSeparator a {@link java.lang.String} object.
   * @param finalThingToAppend a {@link java.lang.String} object.
   * @return a {@link org.jfugue.pattern.Pattern} object.
   */
  public static Pattern replaceDollarsWithCandidates(String sequence, PatternProducer[] candidates,
    PatternProducer underscoreReplacement, Map<String, PatternProducer> specialReplacers,
    String inputSeparator, String outputSeparator, String finalThingToAppend) {
    StringBuilder buddy = new StringBuilder();

    // Understand the contents of the special replacer map.
    // Specifically, find the longest-length item in the map.
    int maxSpecialReplacerKeyLength = 0;
    if (specialReplacers != null) {
      for (String replacerKey : specialReplacers.keySet()) {
        if (replacerKey.length() > maxSpecialReplacerKeyLength) {
          maxSpecialReplacerKeyLength = replacerKey.length();
        }
      }
    }

    // Split the input into individual elements and parse each one.
    for (ElementWithSeparator elementWithSeparator : splitElementsWithSeparators(sequence)) {
      String element = elementWithSeparator.element;
      String innerSeparator = String.valueOf(elementWithSeparator.separator);
      String distributionNeeded = null;
      String appender = "";
      boolean replacementFound = false;
      if (element.startsWith("$")) {
        int indexForAppender = element.length();
        for (int len = Math.min(maxSpecialReplacerKeyLength + 1, element.length());
          len > 1 && !replacementFound; len--) {
          String selectionString = element.substring(1, len);
          if (specialReplacers.containsKey(selectionString)) {
            distributionNeeded = specialReplacers.get(selectionString).toString();
            indexForAppender = len;
            replacementFound = true;
          }
        }
        if (!replacementFound) {
          if ((element.charAt(1) >= '0') && (element.charAt(1) <= '9')) {
            int index = Integer.parseInt(element.substring(1, 2));
            distributionNeeded = candidates[index].toString();
            indexForAppender = 2;
            replacementFound = true;
          } else if (element.charAt(1) == '!') {
            distributionNeeded = underscoreReplacement.toString();
            indexForAppender = 2;
            replacementFound = true;
          }
        }
        if (indexForAppender < element.length()) {
          appender = element.substring(indexForAppender);
          replacementFound = true;
        }
      } else {
        distributionNeeded = element;
        replacementFound = true;
      }

      if (replacementFound) {
        if (finalThingToAppend != null) {
          appender = appender + finalThingToAppend;
        }
        distribute(buddy, distributionNeeded, appender);
      }

      buddy.append(innerSeparator.equals(",") ? " " : innerSeparator);
    }

    // Remove the last output separator
    buddy.deleteCharAt(buddy.length() - 1);

    return new Pattern(buddy.toString());
  }

  private static void distribute(StringBuilder buddy, String elements, String appender) {
    for (String element : elements.split(" ")) {
      buddy.append(element);
      buddy.append(appender);
      buddy.append(" ");
    }
    buddy.deleteCharAt(buddy.length() - 1);
  }

  private static List<ElementWithSeparator> splitElementsWithSeparators(String sequence) {
    char[] separators = new char[]{' ', '+', '_', ','};
    List<ElementWithSeparator> retVal = new ArrayList<>();
    int startingPos = 0;
    int cursor = 0;
    int which = -1;
    while (cursor < sequence.length()) {
      cursor++;
      if (cursor < sequence.length()) {
        if ((which = inWhich(sequence.charAt(cursor), separators)) != -1) {
          String element = sequence.substring(startingPos, cursor);
          char separator = separators[which];
          retVal.add(new ElementWithSeparator(element, separator));
          startingPos = cursor + 1;
        }
      } else if (startingPos < sequence.length()) {
        String element = sequence.substring(startingPos);
        /**
         * <p>Constructor for ElementWithSeparator.</p>
         *
         * @param element a {@link java.lang.String} object.
         * @param separator a char.
         */
        char separator = ' ';
        retVal.add(new ElementWithSeparator(element, separator));
      }
    }
    return retVal;
  }

  private static int inWhich(char ch, char[] chars) {
    for (int i = 0; i < chars.length; i++) {
      if (ch == chars[i]) {
        return i;
      }
    }
    return -1;
  }

}

class ElementWithSeparator {

  public final String element;
  public final char separator;

  public ElementWithSeparator(String element, char separator) {
    this.element = element;
    this.separator = separator;
  }
}
