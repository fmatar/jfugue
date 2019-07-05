/*
 * JFugue, an Application Programming Interface (API) for Music Programming
 * http://www.jfugue.org
 *
 * Copyright (C) 2003-2014 David Koelle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jfugue.realtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfugue.midi.MidiDefaults;
import org.jfugue.midi.TrackTimeManager;
import org.jfugue.parser.ParserListener;
import org.jfugue.theory.Chord;
import org.jfugue.theory.Note;

/**
 * The callbacks in RealtimeMidiParserListener are only called when a user sends a Pattern to the
 * RealtimePlayer. Otherwise, individual events like "note on" or "change instrument" are handled by
 * RealtimePlayer itself. When this listener receives an event from the parser, it schedules the
 * event with a command that will execute directly on the RealtimePlayer.
 *
 * @author fmatar
 * @version $Id: $Id
 */
public class RealtimeMidiParserListener extends TrackTimeManager implements ParserListener {

  private final long originalClockTimeInMillis;
  private final Map<Long, List<Command>> millisToScheduledCommands;
  private final Map<Long, List<ScheduledEvent>> millisToScheduledEvents;
  private final List<RealtimeInterpolator> interpolators;
  private final RealtimePlayer realtimePlayer;
  private boolean endDaemon;
  private int bpm = MidiDefaults.DEFAULT_TEMPO_BEATS_PER_MINUTE;
  private long activeTimeInMillis;

  /**
   * <p>Constructor for RealtimeMidiParserListener.</p>
   *
   * @param player a {@link org.jfugue.realtime.RealtimePlayer} object.
   */
  public RealtimeMidiParserListener(RealtimePlayer player) {
    super();
    this.realtimePlayer = player;
    this.millisToScheduledCommands = new HashMap<>();
    this.millisToScheduledEvents = new HashMap<>();
    this.interpolators = new ArrayList<>();
    this.originalClockTimeInMillis = System.currentTimeMillis();
    startDaemon();
  }

  private long getDeltaClockTimeInMillis() {
    return System.currentTimeMillis() - this.originalClockTimeInMillis;
  }

  /**
   * <p>getCurrentTime.</p>
   *
   * @return a long.
   */
  public long getCurrentTime() {
    return getDeltaClockTimeInMillis();
  }

  private void startDaemon() {
    Runnable daemon = new Runnable() {
      private long lastMillis = 0L;

      public void run() {
        while (!endDaemon) {
          setAllTrackBeatTime(getDeltaClockTimeInMillis());

          long deltaMillis = getDeltaClockTimeInMillis() - lastMillis;
          if (deltaMillis > 0) {
            for (long time = lastMillis; time < lastMillis + deltaMillis; time++) {
              setActiveTimeInMillis(time);
              executeScheduledCommands(time);
              executeScheduledEvents(time);
              updateInterpolators(time);
            }
          }
          this.lastMillis = this.lastMillis + deltaMillis;
        }
      }
    };

    Thread t = new Thread(daemon);
    t.start();
  }

  // Process any scheduled commands that are internal to this parser
  private void executeScheduledCommands(long time) {
    if (millisToScheduledCommands.containsKey(time)) {
      List<Command> commands = millisToScheduledCommands.get(time);
      for (Command command : commands) {
        command.execute();
      }
    }
  }

  // Process any scheduled events requested by the user
  private void executeScheduledEvents(long time) {
    if (millisToScheduledEvents.containsKey(time)) {
      List<ScheduledEvent> scheduledEvents = millisToScheduledEvents.get(time);
      for (ScheduledEvent event : scheduledEvents) {
        event.execute(realtimePlayer, time);
      }
    }
  }

  // Process any active interpolators
  private void updateInterpolators(long time) {
    for (RealtimeInterpolator interpolator : interpolators) {
      if (!interpolator.isStarted()) {
        interpolator.start(time);
      }
      if (interpolator.isActive()) {
        long elapsedTime = time - interpolator.getStartTime();
        double percentComplete = elapsedTime / interpolator.getDurationInMillis();
        interpolator.update(realtimePlayer, elapsedTime, percentComplete);
        if (elapsedTime == interpolator.getDurationInMillis()) {
          interpolator.end();
        }
      }
    }
  }

  /**
   * <p>finish.</p>
   */
  public void finish() {
    this.endDaemon = true;
  }

  private RealtimePlayer getRealtimePlayer() {
    return this.realtimePlayer;
  }

  private void setActiveTimeInMillis(long timeInMillis) {
    this.activeTimeInMillis = timeInMillis;
  }

  private long getNextAvailableTimeInMillis(long timeInMillis) {
    if (timeInMillis <= activeTimeInMillis) {
      timeInMillis += activeTimeInMillis + 1;
    }
    return timeInMillis;
  }

  private void scheduleCommand(long timeInMillis, Command command) {
    timeInMillis = getNextAvailableTimeInMillis(timeInMillis);
    List<Command> commands = millisToScheduledCommands
      .computeIfAbsent(timeInMillis, k -> new ArrayList<>());
    commands.add(command);
  }

  private void scheduleEvent(long timeInMillis, ScheduledEvent event) {
    timeInMillis = getNextAvailableTimeInMillis(timeInMillis);
    List<ScheduledEvent> events = millisToScheduledEvents
      .computeIfAbsent(timeInMillis, k -> new ArrayList<>());
    events.add(event);
  }

  private void unscheduleEvent(long timeInMillis, ScheduledEvent event) {
    List<ScheduledEvent> events = millisToScheduledEvents.get(timeInMillis);
    if (events == null) {
      return;
    }
    events.remove(event);
  }

  /* ParserListener Events */

  /** {@inheritDoc} */
  @Override
  public void beforeParsingStarts() {
  }

  /** {@inheritDoc} */
  @Override
  public void afterParsingFinished() {
  }

  /** {@inheritDoc} */
  @Override
  public void onTrackChanged(final byte track) {
    setCurrentTrack(track);
    scheduleCommand((long) getTrackBeatTime(), () -> getRealtimePlayer().changeTrack(track));
  }

  /** {@inheritDoc} */
  @Override
  public void onLayerChanged(byte layer) {
    setCurrentLayerNumber(layer);
  }

  /** {@inheritDoc} */
  @Override
  public void onInstrumentParsed(final byte instrument) {
    scheduleCommand((long) getTrackBeatTime(),
      () -> getRealtimePlayer().changeInstrument(instrument));
  }

  /** {@inheritDoc} */
  @Override
  public void onTempoChanged(int tempoBPM) {
    this.bpm = tempoBPM;
  }

  /** {@inheritDoc} */
  @Override
  public void onKeySignatureParsed(byte key, byte scale) {
  }

  /** {@inheritDoc} */
  @Override
  public void onTimeSignatureParsed(byte numerator, byte powerOfTwo) {
  }

  /** {@inheritDoc} */
  @Override
  public void onBarLineParsed(long time) {
  }

  /** {@inheritDoc} */
  @Override
  public void onTrackBeatTimeBookmarked(String timeBookmarkID) {
  }

  /** {@inheritDoc} */
  @Override
  public void onTrackBeatTimeBookmarkRequested(String timeBookmarkID) {
  }

  /** {@inheritDoc} */
  @Override
  public void onTrackBeatTimeRequested(double time) {
  }

  /** {@inheritDoc} */
  @Override
  public void onPitchWheelParsed(final byte lsb, final byte msb) {
    scheduleCommand((long) getTrackBeatTime(),
      () -> getRealtimePlayer().setPitchBend(lsb + (msb << 7)));
  }

  /** {@inheritDoc} */
  @Override
  public void onChannelPressureParsed(final byte pressure) {
    scheduleCommand((long) getTrackBeatTime(),
      () -> getRealtimePlayer().changeChannelPressure(pressure));
  }

  /** {@inheritDoc} */
  @Override
  public void onPolyphonicPressureParsed(final byte key, final byte pressure) {
    scheduleCommand((long) getTrackBeatTime(),
      () -> getRealtimePlayer().changePolyphonicPressure(key, pressure));
  }

  /** {@inheritDoc} */
  @Override
  public void onSystemExclusiveParsed(byte... bytes) {
  }

  /** {@inheritDoc} */
  @Override
  public void onControllerEventParsed(final byte controller, final byte value) {
    scheduleCommand((long) getTrackBeatTime(),
      () -> getRealtimePlayer().changeController(controller, value));
  }

  /** {@inheritDoc} */
  @Override
  public void onLyricParsed(String lyric) {
  }

  /** {@inheritDoc} */
  @Override
  public void onMarkerParsed(String marker) {
  }

  /** {@inheritDoc} */
  @Override
  public void onFunctionParsed(String id, Object message) {
  }

  /** {@inheritDoc} */
  @Override
  public void onNotePressed(Note note) {
  }

  /** {@inheritDoc} */
  @Override
  public void onNoteReleased(Note note) {
  }

  /** {@inheritDoc} */
  @Override
  public void onNoteParsed(final Note note) {
    if (note.getDuration() == 0.0) {
      note.useDefaultDuration();
    }

    // If this is the first note in a sequence of harmonic or melodic notes, remember what time it is.
    if (note.isFirstNote()) {
      setInitialNoteBeatTimeForHarmonicNotes(getTrackBeatTime());
    }

    // If we're going to the next sequence in a parallel note situation, roll back the time to the beginning of the first note.
    // A note will never be a parallel note if a first note has not happened first.
    if (note.isHarmonicNote()) {
      setTrackBeatTime(getInitialNoteBeatTimeForHarmonicNotes());
    }

    // If the note is a rest, simply advance the track time and get outta here
    if (note.isRest()) {
      advanceTrackBeatTime(convertBeatsToMillis(note.getDuration()));
      return;
    }

    // Add a NOTE_ON event.
    // If the note is continuing a tie, it is already sounding, and there is not need to turn the note on
    if (!note.isEndOfTie()) {
      scheduleCommand((long) getTrackBeatTime(), () -> getRealtimePlayer().startNote(note));
    }

    // Advance the track timer
    advanceTrackBeatTime(convertBeatsToMillis(note.getDuration()));

    // Add a NOTE_OFF event.
    // If this note is the start of a tie, the note will continue to sound, so we don't want to turn it off.
    if (!note.isStartOfTie()) {
      scheduleCommand((long) getTrackBeatTime(), () -> getRealtimePlayer().stopNote(note));
    }
  }

  /** {@inheritDoc} */
  @Override
  public void onChordParsed(Chord chord) {
    for (Note note : chord.getNotes()) {
      this.onNoteParsed(note);
    }
  }

  //
  // Scheduled Events
  //

  /**
   * <p>onEventScheduled.</p>
   *
   * @param timeInMillis a long.
   * @param event a {@link org.jfugue.realtime.ScheduledEvent} object.
   */
  public void onEventScheduled(long timeInMillis, ScheduledEvent event) {
    scheduleEvent(timeInMillis, event);
  }

  /**
   * <p>onEventUnscheduled.</p>
   *
   * @param timeInMillis a long.
   * @param event a {@link org.jfugue.realtime.ScheduledEvent} object.
   */
  public void onEventUnscheduled(long timeInMillis, ScheduledEvent event) {
    unscheduleEvent(timeInMillis, event);
  }

  /**
   * <p>onInterpolatorStarted.</p>
   *
   * @param interpolator a {@link org.jfugue.realtime.RealtimeInterpolator} object.
   * @param durationInMillis a long.
   */
  public void onInterpolatorStarted(RealtimeInterpolator interpolator, long durationInMillis) {
    interpolator.setDurationInMillis(durationInMillis);
    interpolators.add(interpolator);
  }

  /**
   * <p>onInterpolatorStopping.</p>
   *
   * @param interpolator a {@link org.jfugue.realtime.RealtimeInterpolator} object.
   */
  public void onInterpolatorStopping(RealtimeInterpolator interpolator) {
    interpolators.remove(interpolator);
  }

  private long convertBeatsToMillis(double beats) {
    return (long) ((beats / bpm) * MidiDefaults.MS_PER_MIN * 4);
  }

  interface Command {

    void execute();
  }
}


