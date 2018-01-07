import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

class Digitalclock extends JPanel {


    JLabel label,dLabel;
    SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat d = new SimpleDateFormat("dd/MM/yyyy");

    public Digitalclock() {
        this.setPreferredSize(new Dimension(0,100));
        label = new JLabel();
        dLabel = new JLabel();
        this.add(dLabel);
        this.add(label);
        this.setBackground(Color.BLACK);

        new Thread(){
            public void run(){
                while (true) {
                    dLabel.setText("       "+String.valueOf(d.format(new Date()))+"       ");
                    dLabel.setFont(new Font("Serif", Font.BOLD, 25));
                    label.setText(String.valueOf(f.format(new Date())));
                    label.setFont(new Font("Serif", Font.BOLD, 30));
                    label.setForeground(Color.decode("#57d132"));
                    dLabel.setForeground(Color.decode("#57d132"));
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {

                    }
                }
            }
        }.start();
    }
}