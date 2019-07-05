package org.jfugue.examples.book;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import org.jfugue.midi.MidiDictionary;
import org.jfugue.midi.MidiParser;
import org.jfugue.parser.ParserListenerAdapter;

class InstrumentToolExample {

  /**
   * <p>main.</p>
   *
   * @param args an array of {@link java.lang.String} objects.
   * @throws java.io.IOException if any.
   * @throws javax.sound.midi.InvalidMidiDataException if any.
   */
  public static void main(String[] args) throws IOException, InvalidMidiDataException {
    MidiParser midiParser = new MidiParser();
    InstrumentTool instrumentTool = new InstrumentTool();
    midiParser.addParserListener(instrumentTool);
    midiParser.parse(MidiSystem.getSequence(new File("filename")));
    List<String> instrumentNames = instrumentTool.getInstrumentNames();
    for (String name : instrumentNames) {
      System.out.println(name);
    }
  /**
   * <p>Constructor for InstrumentTool.</p>
   */
  }
}

class InstrumentTool extends ParserListenerAdapter {

  /** {@inheritDoc} */
  private final List<String> instrumentNames;

  public InstrumentTool() {
    super();
    instrumentNames = new ArrayList<>();
  }

  @Override
  public void onInstrumentParsed(byte instrument) {
    /**
     * <p>Getter for the field <code>instrumentNames</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    String instrumentName = MidiDictionary.INSTRUMENT_BYTE_TO_STRING.get(instrument);
    if (!instrumentNames.contains(instrumentName)) {
      instrumentNames.add(instrumentName);
    }

  }

  public List<String> getInstrumentNames() {
    return this.instrumentNames;
  }
}
