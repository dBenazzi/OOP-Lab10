package it.unibo.oop.lab.reactivegui03;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ConcurrentGUI {

    private static final long serialVersionUID = 1L;
    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;
    private final JFrame frame = new JFrame();
    private final JPanel panel = new JPanel();
    private final JLabel display = new JLabel();
    protected final JButton up = new JButton("up");
    protected final JButton down = new JButton("down");
    protected final JButton stop = new JButton("stop");
    protected final Agent agent = new Agent();

    public ConcurrentGUI() {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int) (screenSize.getWidth() * WIDTH_PERC), (int) (screenSize.getHeight() * HEIGHT_PERC));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.add(display);
        panel.add(up);
        panel.add(down);
        panel.add(stop);
        frame.getContentPane().add(panel);

        frame.setVisible(true);

        new Thread(agent).start();

        stop.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                agent.stopCounting();
            }
        });
        up.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                agent.setIncrease();
            }
        });
        down.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                agent.setDecrease();
            }
        });
    }

    class Agent implements Runnable {
        private volatile int counter;
        private volatile boolean stop;
        private volatile boolean inc = true;

        @Override
        public void run() {
            while (!this.stop) {
                try {
                    SwingUtilities.invokeAndWait(() -> ConcurrentGUI.this.display.setText(Integer.toString(Agent.this.counter)));
                    if (inc) {
                        this.counter++;
                    } else {
                        this.counter--;
                    }
                    Thread.sleep(100L);
                } catch (InvocationTargetException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
 
        public void stopCounting() {
            this.stop = true;
        }

        public void setIncrease() {
            this.inc = true;
        }

        public void setDecrease() {
            this.inc = false;
        }
    }
}
