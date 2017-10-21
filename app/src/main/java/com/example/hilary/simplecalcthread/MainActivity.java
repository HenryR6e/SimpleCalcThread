package com.example.hilary.simplecalcthread;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.os.Message;

public class MainActivity extends AppCompatActivity {

    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message message) {
            TextView txtTitl = (TextView) findViewById(R.id.txtTitle);
            txtTitl.setText("This has a thread");
        }
    };
    TextView txtVwResult, txtDisp;
    EditText txtEnPerc, txtEnNum;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Magembe and Henry", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button btnCal = (Button) findViewById(R.id.btnCalc);
        txtVwResult = (TextView) findViewById(R.id.txtResult);
        txtEnPerc = (EditText) findViewById(R.id.txtPerc);
        txtEnNum = (EditText) findViewById(R.id.txtNum);

        btnCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        long fTime = System.currentTimeMillis() + 5000;
                        while (System.currentTimeMillis() < fTime) {
                            synchronized (this) {
                                try {
                                    wait(fTime - System.currentTimeMillis());
                                } catch (Exception e) {
                                }
                            }
                        }
                        handler1.sendEmptyMessage(0); //Calls the handler1
                    }
                };

                Thread thread = new Thread(runnable);
                thread.start();

                int percentage = Integer.parseInt(txtEnPerc.getText().toString());  //(Float.parseFloat(txtEnPerc.getText().toString())) / 100;
                int number = Integer.parseInt(txtEnNum.getText().toString());
                int result = percentage * number;
                txtVwResult.setText(String.valueOf(result));
//                txtVwResult.setText(Float.toString(result));

                Toast myToast = Toast.makeText(getApplicationContext(),
                        "Calculated",
                        Toast.LENGTH_LONG
                );
                myToast.show();

            }
        });

    }

    int rs;
    public void change(View view) {
        rs = Integer.parseInt(String.valueOf(txtVwResult));
        MyTask myTask = new MyTask();
        myTask.execute(rs);
    }

    public class  MyTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... integers) {

            int i = 0;
            while (i < integers[0]){
                try {
                    Thread.sleep(integers[1]);

                    int p = i*2;
                    publishProgress(p);
                } catch (InterruptedException e) {
//                log.wth("MainActivity", e.getMessage());
                }
                i = i*2;
            }
            return "Done" + integers[0] +" ding dong Async tasks";
        }

        @Override
        protected void onPreExecute() {
            //Display messsage before we start
            txtDisp.setText("Starting Async");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //                    display an update while processing
            txtDisp.setText((values[0]) + "ding dong.");
        }

        @Override
        protected void onPostExecute(String msg) {
//            display a message once we are done
            txtDisp.setText(msg);
        }
    }

}

