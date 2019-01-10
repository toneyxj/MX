package soft.com.dwonloadinstallprogress;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    private ProgressView progess;
    private boolean isRun = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        isRun = true;
        new Thread(new ProgressThread()).start();

    }

    private void initView(){
        progess = (ProgressView) findViewById(R.id.progess);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final int currentMsg = msg.what;
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progess.setMaxNumber(100);
                    progess.setCurNumber(currentMsg);
                }
            });
        }
    };

    class ProgressThread extends Thread{

        int msgWgat = 0;

        @Override
        public void run() {

            try{
                while(isRun){
                    Thread.sleep(3000);
                    if (msgWgat == 100){
                        msgWgat = 0;
                    }
                    msgWgat = msgWgat + 10;
                    handler.sendEmptyMessage(msgWgat);
                }
            }catch (Exception e){

            }

            super.run();
        }
    }

    @Override
    protected void onResume() {
        isRun = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isRun = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        isRun = false;
        super.onDestroy();
    }
}
