package org.jfugue.realtime;

/**
 * <p>Abstract RealtimeInterpolator class.</p>
 *
 * @author fmatar
 * @version $Id: $Id
 */
public abstract class RealtimeInterpolator {

  private boolean started;
  private boolean active;
  private boolean ended;
  private long startTime;
  private long durationInMillis;

  /**
   * <p>Constructor for RealtimeInterpolator.</p>
   */
  public RealtimeInterpolator() {
    this.started = false;
    this.active = false;
    this.ended = false;
  }

  /**
   * <p>start.</p>
   *
   * @param startTime a long.
   */
  public void start(long startTime) {
    this.startTime = startTime;
    this.started = true;
    this.active = true;
    this.ended = false;
  }

  /**
   * <p>end.</p>
   */
  public void end() {
    this.started = true;
    this.active = false;
    this.ended = true;
  }

  /**
   * <p>isStarted.</p>
   *
   * @return a boolean.
   */
  public boolean isStarted() {
    return this.started;
  }

  /**
   * <p>isActive.</p>
   *
   * @return a boolean.
   */
  public boolean isActive() {
    return this.active;
  }

  /**
   * <p>isEnded.</p>
   *
   * @return a boolean.
   */
  public boolean isEnded() {
    return this.ended;
  }

  /**
   * <p>Getter for the field <code>startTime</code>.</p>
   *
   * @return a long.
   */
  public long getStartTime() {
    return this.startTime;
  }

  /**
   * <p>Getter for the field <code>durationInMillis</code>.</p>
   *
   * @return a long.
   */
  public long getDurationInMillis() {
    return this.durationInMillis;
  }

  /**
   * <p>Setter for the field <code>durationInMillis</code>.</p>
   *
   * @param durationInMillis a long.
   */
  public void setDurationInMillis(long durationInMillis) {
    this.durationInMillis = durationInMillis;
  }

  /**
   * <p>update.</p>
   *
   * @param realtimePlayer a {@link org.jfugue.realtime.RealtimePlayer} object.
   * @param elapsedTime a long.
   * @param percentComplete a double.
   */
  public abstract void update(RealtimePlayer realtimePlayer, long elapsedTime,
    double percentComplete);
}
