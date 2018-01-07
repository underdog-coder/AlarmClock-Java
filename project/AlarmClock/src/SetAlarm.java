import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

public class SetAlarm {
    private Timer timer;
    private long seconds;
    private String str;

    public String getStr(){
        return str;
    }
    public SetAlarm(long seconds,String str) {
        timer = new Timer();
        timer.schedule(new RemindTask(), seconds*1000);
        this.seconds = seconds;
        this.str=str;
    }

    public long getSeconds() {
        return seconds;
    }

    public void cancel() {
        timer.cancel();
    }

    class RemindTask extends TimerTask {

        @Override
        public void run() {
            AlarmWindow alarmWindow = null;
                alarmWindow = new AlarmWindow();
            alarmWindow.setVisible(true);
            alarmWindow.setBounds(515, 220, 350, 322);
            try {
                Thread.sleep(60000);
                if (alarmWindow.getSnooze() == true) {
                    new SetAlarm(240,str);
                }
            }catch(Exception e){}
            timer.cancel();
        }
    }
}