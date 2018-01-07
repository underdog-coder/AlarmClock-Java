import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import sun.audio.AudioStream;
import sun.audio.AudioPlayer;

public class AlarmWindow extends JFrame {
    JButton closeButton;
    JButton snoozeButton;
    private ImageIcon image1;
    private JLabel label1;
    private boolean snooze = false;
    private boolean stop = false;
    private AudioStream audioStream;
    AlarmWindow() {
        super("Alarm Ringing!!!!!");
        setLayout(new FlowLayout());
        setResizable(false);
        GridBagConstraints g = new GridBagConstraints();
        image1 = new ImageIcon(getClass().getResource("image2.gif"));
        label1 = new JLabel(image1);
        snoozeButton = new JButton("Snooze for 5 Minutes!!");
        closeButton = new JButton("Stop Alarm");
        snoozeButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                snooze = true;
                AudioPlayer.player.stop(audioStream);
                dispose();
            }
            public void mousePressed(MouseEvent mouseEvent){}
            public void mouseReleased(MouseEvent mouseEvent){}
            public void mouseEntered(MouseEvent mouseEvent){}
            public void mouseExited(MouseEvent mouseEvent){}
        });
        closeButton.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent mouseEvent) {
                AudioPlayer.player.stop(audioStream);
                stop = true;
                dispose();
            }
            public void mousePressed(MouseEvent mouseEvent){}
            public void mouseReleased(MouseEvent mouseEvent){}
            public void mouseEntered(MouseEvent mouseEvent){}
            public void mouseExited(MouseEvent mouseEvent){}
        });
        add(label1);
        add(closeButton);
        add(snoozeButton);
        pack();
        new Thread() {
            public void run() {
                try {
                    audioStream = new AudioStream(getClass().getResourceAsStream("AlarmTone.wav"));
                    AudioPlayer.player.start(audioStream);
                } catch (Exception e) {
                    System.out.println("File Not Found");
                }
                try {
                    Thread.sleep(60000);
                    AudioPlayer.player.stop(audioStream);
                    dispose();
                    if(stop == false && snooze == false) {
                        snooze = true;
                        Toolkit.getDefaultToolkit().beep();
                        JOptionPane.showMessageDialog(null, "Alarm Snoozed for 5 Minutes!!", "Alarm Snoozed!!", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    boolean getSnooze(){
        return snooze;
    }
}