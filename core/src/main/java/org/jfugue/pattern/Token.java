package org.jfugue.pattern;

/**
 * <p>Token class.</p>
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class Token implements PatternProducer {

  private final String string;
  private final TokenType type;

  /**
   * <p>Constructor for Token.</p>
   *
   * @param string a {@link java.lang.String} object.
   * @param type a {@link org.jfugue.pattern.Token.TokenType} object.
   */
  public Token(String string, TokenType type) {
    this.string = string;
    this.type = type;
  }

  /**
   * Involves the Staccato parsers to figure out what type of token this is
   *
   * @return TokenType
   */
  public TokenType getType() {
    return this.type;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return this.string;
  }

  /** {@inheritDoc} */
  @Override
  public Pattern getPattern() {
    return new Pattern(string);
  }

  public enum TokenType {
    VOICE, LAYER, INSTRUMENT, TEMPO, KEY_SIGNATURE, TIME_SIGNATURE,
    BAR_LINE, TRACK_TIME_BOOKMARK, TRACK_TIME_BOOKMARK_REQUESTED,
    LYRIC, MARKER, FUNCTION, NOTE,
    WHITESPACE, ATOM,
    UNKNOWN_TOKEN
  }

}
