import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Timer;

class Main extends JFrame implements ActionListener  {
    private JPanel left;
    private JPanel right;
    private volatile JButton b1,b2,b3,b4;
    volatile String[] alarmset=new String[100];
    volatile int k=0;
    volatile int i=0;
    private volatile Set<SetAlarm> list;

    final Dimension frameDim = new Dimension(700,300);

    private JLabel l1;
    private JLabel l3;
    volatile private JLabel l2;
    private JTextField f1,f3;
    Timer timer;


    public Main()
    {
        super("Java Alarm Clock");
        list = new HashSet<SetAlarm>();
        this.setResizable(false);
        setSize(frameDim);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.left = createLeftComponent();
        this.right = createRightComponent();

        addComponents();

        b1.addActionListener(this);
        b2.addActionListener(this);
        b3.addActionListener(this);
        b4.addActionListener(this);
        setVisible(true);
    }

    private JPanel createLeftComponent(){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(350, 0));
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
        panel.setBackground(Color.BLACK);
        JPanel upper = new JPanel(new BorderLayout());
        upper.setBackground(Color.BLACK);
        upper.setPreferredSize(new Dimension(0,300));
        upper.setVisible(true);
        Digitalclock dcl = new Digitalclock();
        b2 = new JButton("prev");
        b3=new JButton("next");
        b4 = new JButton("X");
        b2.setVisible(false);
        b4.setVisible(false);
        b3.setVisible(false);
        b2.setBounds(10,120,70,20);
        b3.setBounds(270,120,70,20);
        b4.setBounds(270,150,50,30);
        b4.setHorizontalAlignment(JLabel.CENTER);
        b4.setVerticalAlignment(JLabel.CENTER);
        b3.setHorizontalAlignment(JLabel.CENTER);
        b3.setVerticalAlignment(JLabel.CENTER);
        b2.setHorizontalAlignment(JLabel.CENTER);
        b2.setVerticalAlignment(JLabel.CENTER);
        upper.add(b2,BorderLayout.CENTER);
        upper.add(b3,BorderLayout.CENTER);
        upper.add(b4,BorderLayout.CENTER);
        upper.add(dcl, BorderLayout.PAGE_START);
        l2=new JLabel("");
        l2.setHorizontalAlignment(JLabel.CENTER);
        l2.setVerticalAlignment(JLabel.CENTER);
        l2.setFont(new Font("Serif", Font.BOLD, 15));
        l2.setForeground(Color.decode("#ffffff"));
        upper.add(l2,BorderLayout.CENTER);
        panel.add(BorderLayout.PAGE_START, upper);
        return panel;
    }

    private void addComponents(){
        this.add(BorderLayout.LINE_START, left);
        this.add(BorderLayout.CENTER, right);
    }

    public void actionPerformed(ActionEvent ae){
        if(ae.getSource() == b1){
            long timediff=0, indays=0, inmonth=0, inmin=0, inhour=0, inyear=0,flag=0;
            try {
                String alarmTime = f3.getText();
                String date = f1.getText();
                String d = date.substring(0, 2);
                String m = date.substring(3, 5);
                String y = date.substring(6, 10);
                indays = Integer.parseInt(d);
                inmonth = Integer.parseInt(m);
                inyear = Integer.parseInt(y);

                inhour = Integer.parseInt(alarmTime.substring(0, 2));
                inmin = Integer.parseInt(alarmTime.substring(3, 5));
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
                //System.out.println(inhour + " "+ inmin + " " +inmonth);
                String time = sdf.format(new Date(System.currentTimeMillis()));
                String sd = time.substring(0, 2);
                String sm = time.substring(3, 5);
                String sy = time.substring(6, 10);
                String sh = time.substring(11, 13);
                String smin = time.substring(14, 16);
                String ssec = time.substring(17, 19);
                long syshour = Integer.parseInt(sh);
                long sysmin = Integer.parseInt(smin);
                long syssec = Integer.parseInt(ssec);
                long sysdays = Integer.parseInt(sd);
                long sysmonth = Integer.parseInt(sm);
                long sysyear = Integer.parseInt(sy);
                long finalsystime = (525600 * (sysyear - 2000) + 44600 * sysmonth + 1440 * sysdays + 60 * syshour + sysmin) * 60;
                long finalinptime = (525600 * (inyear - 2000) + 44600 * inmonth + 1440 * indays + 60 * inhour + inmin) * 60;
                timediff = finalinptime - finalsystime;
                timediff = timediff - syssec;
                if (timediff < 0 || inmin > 60 || inhour > 24 || inmin < 0 || inhour < 0 ) {
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Invalid Inputs!!", "Error Message", JOptionPane.WARNING_MESSAGE);
                    flag=1;
                } else {
                    alarmset[k] = ("Alarm set for " + f1.getText() + " on " + alarmTime);
                    for(Iterator<SetAlarm> iterator = list.iterator(); iterator.hasNext();){
                        if(iterator.next().getStr().equals(alarmset[k])){
                            Toolkit.getDefaultToolkit().beep();
                            JOptionPane.showMessageDialog(null, "Alarm for requested time already set", "Error Message", JOptionPane.WARNING_MESSAGE);
                            return;
                        }
                    }
                    SetAlarm temp = new SetAlarm(timediff, alarmset[k]);
                    list.add(temp);
                    long seconds = temp.getSeconds();
                    timer = new Timer();
                    timer.schedule(new RemindTask(temp, alarmset[k]), seconds * 1000);
                    //System.out.println(timediff + " "+ alarmset[k]);
                    b4.setVisible(true);
                    l2.setText(alarmset[k]);
                    i = k;
                    k++;
                    int j;
                    j = i;
                    //System.out.println(timediff + " "+ alarmset[k]);
                    while (j >= 0) {
                        j--;
                        if (j == -1) {
                            b2.setVisible(false);
                            break;
                        } else
                            b2.setVisible(true);
                        if (!alarmset[j].equals(""))
                            break;
                    }
                    j = i;
                    while (j < k) {
                        j++;
                        if (j == k) {
                            b3.setVisible(false);
                            break;
                        } else
                            b3.setVisible(true);
                        if (!alarmset[j].equals(""))
                            break;
                    }
                }
            } catch (Exception e) {
                if(flag == 0) {
                    System.out.println(e.toString());
                    Toolkit.getDefaultToolkit().beep();
                    JOptionPane.showMessageDialog(null, "Invalid Input!!", "Error Message", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        else if(ae.getSource() == b2){
            b3.setVisible(true);
            i--;
            while(alarmset[i].equals(""))
                i--;
            //b2.setVisible(false);
            l2.setText(alarmset[i]);
            b4.setVisible(true);
            int j;
            j=i;
            while(j>=0){
                j--;
                if(j==-1) {
                    b2.setVisible(false);
                    break;
                }
                else
                    b2.setVisible(true);
                if(!alarmset[j].equals(""))
                    break;
            }
        }
        else if(ae.getSource() == b3){
            b2.setVisible(true);
            System.out.println(i);
            i++;
            while(alarmset[i].equals(""))
                i++;
            l2.setText(alarmset[i]);
            int j=i;
            while(j<k){
                j++;
                if(j==k){
                    b3.setVisible(false);
                    break;
                }
                else
                    b3.setVisible(true);
                if(!alarmset[j].equals(""))
                    break;
            }
        } else if (ae.getSource() == b4) {
            String str = l2.getText();
            for(Iterator<SetAlarm> iterator = list.iterator(); iterator.hasNext();){
                SetAlarm it = iterator.next();
                if(it.getStr().equals(l2.getText())){
                    it.cancel();
                    iterator.remove();
                    l2.setText("");
                    b4.setVisible(false);
                }
            }
            int j;
            for(j=0;j < k;j++) {
                if(str.equals(alarmset[j])) {
                    alarmset[j] = "";
                }
            }
            if(!b4.isVisible()) {
                for(j=0;j < k;j++) {
                    if(!alarmset[j].equals("")) {
                        l2.setText(alarmset[j]);
                        break;
                    } else {
                        l2.setText("");
                    }
                }
            }
            i=j;
            if(i==k){
                b2.setVisible(false);
                b3.setVisible(false);
                b4.setVisible(false);
                l2.setText("");
            }
            else {
                b4.setVisible(true);
                while (j < k) {
                    j++;
                    if (j == k) {
                        b3.setVisible(false);
                        break;
                    } else
                        b3.setVisible(true);
                    if (!alarmset[j].equals(""))
                        break;
                }
                j = i;
                while (j >= 0) {
                    j--;
                    if (j == -1) {
                        b2.setVisible(false);
                        break;
                    } else
                        b2.setVisible(true);
                    if (!alarmset[j].equals(""))
                        break;
                }
            }
        }
    }

    class RemindTask extends TimerTask {
        SetAlarm temp;
        String s;

        public RemindTask(SetAlarm temp, String s) {
            this.temp = temp;
            this.s = s;
        }

        @Override
        public void run() {
           for(Iterator<SetAlarm> iterator = list.iterator(); iterator.hasNext();){
               if(iterator.next().getStr().equals(s)){
                   iterator.remove();
                   l2.setText("");
                   b4.setVisible(false);
               }
           }
            int j;
            for(j=0;j < k;j++) {
                if(s.equals(alarmset[j])) {
                    alarmset[j] = "";
                }
            }
            if(!b4.isVisible()) {
                for(j=0;j < k;j++) {
                    if(!alarmset[j].equals("")) {
                        l2.setText(alarmset[j]);
                        b4.setVisible(true);
                        break;
                    } else {
                        l2.setText("");
                    }
                }
            }
            i=j;
            if(i==k){
                b2.setVisible(false);
                b3.setVisible(false);
                b4.setVisible(false);
                l2.setText("");
            }
            else {
                b4.setVisible(true);
                while (j < k) {
                    j++;
                    if (j == k) {
                        b3.setVisible(false);
                        break;
                    } else
                        b3.setVisible(true);
                    if (!alarmset[j].equals(""))
                        break;
                }
                j = i;
                while (j >= 0) {
                    j--;
                    if (j == -1) {
                        b2.setVisible(false);
                        break;
                    } else
                        b2.setVisible(true);
                    if (!alarmset[j].equals(""))
                        break;
                }
            }
            timer.cancel();
        }
    }

    private JPanel createRightComponent(){
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();

        l1 = new JLabel("Alarm Date");
        l1.setFont(new Font("Serif", Font.BOLD, 13));
        l3 = new JLabel("Alarm Time");
        l3.setFont(new Font("Serif", Font.BOLD, 13));
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        f1 = new HintTextField("dd/mm/yyyy");
        f3 = new HintTextField("hh:mm");

        f3.setVisible(true);

        f1.setPreferredSize(new Dimension(120,25));
        f3.setPreferredSize(new Dimension(120,25));

        b1 = new JButton("Set Alarm");

        g.insets = new Insets(5, 5, 5, 5);

        g.gridx = 0;
        g.gridy = 0;
        panel.add(l1, g);

        g.gridx = 1;
        g.gridy = 0;
        panel.add(f1, g);

        g.gridx = 0;
        g.gridy = 1;
        panel.add(l3, g);

        g.gridx = 1;
        g.gridy = 1;
        panel.add(f3, g);

        g.gridx = 0;
        g.gridy = 2;
        panel.add(b1, g);

        return panel;
    }

    public static void main(String[] args) {
        new Main().setVisible(true);
    }
}