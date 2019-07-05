package org.jfugue.examples.book;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfugue.realtime.RealtimePlayer;
import org.jfugue.theory.Chord;
import org.jfugue.theory.Intervals;
import org.jfugue.theory.Note;

class VirtualInstrument extends JPanel implements MouseListener {

  private static final int WIDTH = 800;
  private static final int HEIGHT = 600;
  private static final int NUM_ZONES = 100;
  private static final int MIN_RECT_WIDTH = 50;
  private static final int MAX_RECT_WIDTH = 200;
  private static final int MIN_RECT_HEIGHT = 50;
  private static final int MAX_RECT_HEIGHT = 200;
  private final List<InstrumentZone> instrumentZones;
  private RealtimePlayer player;
  private Note[] notes;

  private VirtualInstrument() throws MidiUnavailableException {
    super();
    this.instrumentZones = new ArrayList<>();
    this.addMouseListener(this);
    this.player = new RealtimePlayer();
    this.player.play("I[Crystal]");
    this.notes = new Chord(new Note("C"), new Intervals("1 3 #4 5 7")).getNotes();
    createInstrumentZones();
  }

  /**
   * <p>main.</p>
   *
   * @param args an array of {@link java.lang.String} objects.
   * @throws javax.sound.midi.MidiUnavailableException if any.
   */
  public static void main(String[] args) throws MidiUnavailableException {
    JFrame frame = new JFrame("JFugue - Virtual Instrument Demo");
    frame.setSize(VirtualInstrument.WIDTH, VirtualInstrument.HEIGHT);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(new VirtualInstrument(), BorderLayout.CENTER);
    frame.setVisible(true);
  }

  private void createInstrumentZones() {
    Random rnd = new Random();
    for (int i = 0; i < VirtualInstrument.NUM_ZONES; i++) {
      int x = rnd.nextInt(VirtualInstrument.WIDTH - VirtualInstrument.MAX_RECT_WIDTH);
      int y = rnd.nextInt(VirtualInstrument.HEIGHT - VirtualInstrument.MAX_RECT_HEIGHT);
      int w = VirtualInstrument.MIN_RECT_WIDTH + rnd
        .nextInt(VirtualInstrument.MAX_RECT_WIDTH - VirtualInstrument.MIN_RECT_WIDTH);
      int h = VirtualInstrument.MIN_RECT_HEIGHT + rnd
        .nextInt(VirtualInstrument.MAX_RECT_HEIGHT - VirtualInstrument.MIN_RECT_HEIGHT);
      Color color = new Color(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat(), 0.8f);
      Note note = new Note(notes[rnd.nextInt(notes.length)].toString() + (2 + (rnd.nextInt(4))));
      instrumentZones.add(new InstrumentZone(x, y, w, h, color, player, note));
    }
  }

  /** {@inheritDoc} */
  @Override
  public void paint(Graphics g) {
    super.paint(g);

    Graphics2D g2 = (Graphics2D) g;
    g2.setPaint(
      new GradientPaint(new Point2D.Double(VirtualInstrument.WIDTH / 2.0, 0.0), Color.LIGHT_GRAY,
        new Point2D.Double(VirtualInstrument.WIDTH / 2.0, VirtualInstrument.HEIGHT), Color.BLACK));
    g2.fillRect(0, 0, VirtualInstrument.WIDTH, VirtualInstrument.HEIGHT);
    for (InstrumentZone izone : instrumentZones) {
      izone.paint(g2);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void mousePressed(MouseEvent event) {
    for (InstrumentZone izone : instrumentZones) {
      izone.mousePressed(event.getPoint());
    }
  }

  /** {@inheritDoc} */
  @Override
  public void mouseReleased(MouseEvent event) {
    for (InstrumentZone izone : instrumentZones) {
      izone.mouseReleased(event.getPoint());
    }
  }

  /** {@inheritDoc} */
  @Override
  public void mouseClicked(MouseEvent e) {
  }

  /** {@inheritDoc} */
  @Override
  public void mouseEntered(MouseEvent e) {
  }

  /** {@inheritDoc} */
  @Override
  public void mouseExited(MouseEvent e) {
  }
/**
 * <p>Constructor for InstrumentZone.</p>
 *
 * @param x a int.
 * @param y a int.
 * @param width a int.
 * @param height a int.
 * @param paint a {@link java.awt.Paint} object.
 * @param player a {@link org.jfugue.realtime.RealtimePlayer} object.
 * @param note a {@link org.jfugue.theory.Note} object.
 */
}

class InstrumentZone {

  private final int x;
  private final int y;
  private final int width;
  private final int height;
  private final Paint paint;
  private final RealtimePlayer realtimePlayer;
  private final Note note;
/**
 * <p>paint.</p>
 *
 * @param g2 a {@link java.awt.Graphics2D} object.
 */

  public InstrumentZone(int x, int y, int width, int height, Paint paint, RealtimePlayer player,
    Note note) {
    this.x = x;
    this.y = y;
    /**
     * <p>mousePressed.</p>
     *
     * @param point a {@link java.awt.geom.Point2D} object.
     */
    this.width = width;
    this.height = height;
    this.paint = paint;
    this.realtimePlayer = player;
    this.note = note;
  }

  public void paint(Graphics2D g2) {
    /**
     * <p>mouseReleased.</p>
     *
     * @param point a {@link java.awt.geom.Point2D} object.
     */
    g2.setPaint(this.paint);
    g2.fillRect(x, y, width, height);
  }

  public void mousePressed(Point2D point) {
    if (((point.getX() >= x) && (point.getX() <= x + width)) && ((point.getY() >= y) && (
      point.getY() <= y + height))) {
      realtimePlayer.startNote(note);
      System.out.println("Play " + note);
    }
  }

  public void mouseReleased(Point2D point) {
    if (((point.getX() >= x) && (point.getX() <= x + width)) && ((point.getY() >= y) && (
      point.getY() <= y + height))) {
      realtimePlayer.stopNote(note);
      System.out.println("Stop " + note);
    }
  }
}
