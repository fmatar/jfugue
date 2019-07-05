package org.jfugue.realtime;


interface ScheduledEvent {

  /**
   * <p>execute.</p>
   *
   * @param player a {@link org.jfugue.realtime.RealtimePlayer} object.
   * @param timeInMillis a long.
   */
  void execute(RealtimePlayer player, long timeInMillis);
}
