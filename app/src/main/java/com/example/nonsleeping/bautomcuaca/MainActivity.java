package com.example.nonsleeping.bautomcuaca;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nonsleeping.bautomcuaca.AdapterGridView.CustomGridViewAdapter;
import com.example.nonsleeping.bautomcuaca.ObjectGrid.ObjectGridCustom;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    GridView mGridView;
    CustomGridViewAdapter mAdapter;
    ArrayList<ObjectGridCustom> mArrayList;
    AnimationDrawable cdXiNgau1, cdXiNgau2, cdXiNgau3;
    TextView txtTongTien, tvThoiGian;
    ImageView xg1, xg2, xg3;
    Random randomNg;
    LinearLayout lnXucSac;
    int valueXiNgau, valueXiNgau1, valueXiNgau2, valueXiNgau3;
    int tienthuong = 0;
    Timer timer;
    Handler handler;
    Handler.Callback callback = new Handler.Callback() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public boolean handleMessage(Message msg) {
            RandomXg(xg1, 0, valueXiNgau);
            RandomXg(xg2, 1, valueXiNgau);
            RandomXg(xg3, 2, valueXiNgau);

            tongtiengmoi += totalValue();

            if (tongtiengmoi <= 0) {
                tongtiengmoi = 0;
                txtTongTien.setText("Money!");
                //btnPlay.setClickable(false);
                lnXucSac.setAlpha(0.5f);

                btnPlay.setBackgroundResource(R.drawable.btnhelp);
                btnPlay.setAlpha(0.9f);
                //Toast.makeText(MainActivity.this, "Hãy nạp thẻ đi nào !", Toast.LENGTH_SHORT).show();
            } else {
                lnXucSac.setAlpha(1f);
                btnPlay.setClickable(true);
                btnPlay.setAlpha(1.0f);
                txtTongTien.setText(String.valueOf(tongtiengmoi));
            }

            //Toast.makeText(MainActivity.this, "Checker : " + msg.getData().getString("TEST"), Toast.LENGTH_LONG).show();
            return false;
        }
    };

    public static Integer[] gtDatCuoc = new Integer[6];
    SharedPreferences saveMoney;
    int tongtiencu, tongtiengmoi, idSound;
    SoundPool soundXiNgau = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    MediaPlayer nhacnen = new MediaPlayer();
    CheckBox chkSoud;
    Button btnPlay;
    CountDownTimer demthoigian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xg1 = (ImageView) findViewById(R.id.xingau1);
        xg2 = (ImageView) findViewById(R.id.xingau2);
        xg3 = (ImageView) findViewById(R.id.xingau3);
        txtTongTien = (TextView) findViewById(R.id.txtTongTien);
        lnXucSac = (LinearLayout) findViewById(R.id.lnXucSac);

        mGridView = (GridView) findViewById(R.id.gvBg);
        chkSoud = (CheckBox) findViewById(R.id.chkSound);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnPlay.setAlpha(1.0f);
        tvThoiGian = (TextView) findViewById(R.id.tvThoiGian);

        createData();

        Log.d("abc", "mArrayList =" + mArrayList.size());
        mAdapter = new CustomGridViewAdapter(this, R.layout.custom_banco, mArrayList);
        mGridView.setAdapter(mAdapter);

        saveMoney = getSharedPreferences("luutruthongtin", Context.MODE_PRIVATE);
        tongtiencu = saveMoney.getInt("TongTien", 500);

        idSound = soundXiNgau.load(this, R.raw.diceshake, 1);

        nhacnen = MediaPlayer.create(this, R.raw.bg);
        nhacnen.start();
        chkSoud.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    nhacnen.stop();
                } else {
                    try {
                        nhacnen.prepare();
                        nhacnen.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        demthoigian = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                long gio = TimeUnit.MICROSECONDS.toHours(millis);
                long phut = TimeUnit.MICROSECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MICROSECONDS.toHours(millis));
                long giay = TimeUnit.MICROSECONDS.toSeconds(millis) - TimeUnit.MINUTES.toMinutes(TimeUnit.MICROSECONDS.toMillis(millis));

                String timer = String.format("%02d:%02d:%02d", gio, phut, giay);
                tvThoiGian.setText(timer);

            }

            @Override
            public void onFinish() {
                SharedPreferences.Editor edit = saveMoney.edit();
                tongtiengmoi += 500;
                edit.putInt("TongTien", tongtiengmoi);
                edit.commit();

                txtTongTien.setText(String.valueOf(tongtiengmoi));
                demthoigian.cancel();
                demthoigian.start();
            }
        };

        demthoigian.start();
        handler = new Handler(callback);
    }

    private void saveDataUser() {
        SharedPreferences.Editor edit = saveMoney.edit();
        /*if (tongtiengmoi == 0)
            tongtiengmoi = 500;*/
        edit.putInt("TongTien", tongtiengmoi);
        edit.commit();
        Log.d("abc", "Money = " + tongtiencu +" --" +  tongtiengmoi);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("abc","call it 2");
        tongtiencu = saveMoney.getInt("TongTien", 500);
        tongtiengmoi = tongtiencu;

        if (tongtiencu == 0) {
            lnXucSac.setAlpha(0.5f);
            lnXucSac.setClickable(false);
            txtTongTien.setText("Money!");
            btnPlay.setAlpha(0.9f);
            btnPlay.setBackgroundResource(R.drawable.btnhelp);
        } else {
            lnXucSac.setAlpha(1f);
            lnXucSac.setClickable(true);
            btnPlay.setAlpha(1.0f);
            btnPlay.setBackgroundResource(R.drawable.btnplay);
            txtTongTien.setText(String.valueOf(tongtiencu));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("abc","call it");
        saveDataUser();
    }

    public int totalCuoc() {
        int tong = 0;
        for (int i = 0; i< gtDatCuoc.length; i++) {
            tong += gtDatCuoc[i];
        }
        return tong;
    }

    public int totalValue() {
        for (int i = 0; i< gtDatCuoc.length; i++) {
            if (gtDatCuoc[i] != 0) {
                if (i == valueXiNgau1) {
                    tienthuong += gtDatCuoc[i];
                }
                if (i == valueXiNgau2) {
                    tienthuong += gtDatCuoc[i];
                }
                if (i == valueXiNgau3) {
                    tienthuong += gtDatCuoc[i];
                }

                if (i != valueXiNgau1 && i != valueXiNgau2 && i != valueXiNgau3) {
                    tienthuong -= gtDatCuoc[i];
                }

               /* if (tienthuong < 0)
                    tienthuong = 0;*/
            }
        }
        Log.d("abc","gtDatCuoc = " + tienthuong);
        return  tienthuong;
    }

    public void createData() {
        mArrayList = new ArrayList<ObjectGridCustom>();
        mArrayList.add(new ObjectGridCustom("100", R.drawable.bau));
        mArrayList.add(new ObjectGridCustom("100", R.drawable.tom));
        mArrayList.add(new ObjectGridCustom("100", R.drawable.cua));
        mArrayList.add(new ObjectGridCustom("100", R.drawable.ca));
        mArrayList.add(new ObjectGridCustom("100", R.drawable.ga));
        mArrayList.add(new ObjectGridCustom("100", R.drawable.nai));
    }

    public void loadAnimationImg() {
        xg1.setImageResource(R.drawable.animationxingau);
        xg2.setImageResource(R.drawable.animationxingau);
        xg3.setImageResource(R.drawable.animationxingau);
    }
    public void laxXiNgau(View view) {
        loadAnimationImg();

        cdXiNgau1 = (AnimationDrawable) xg1.getDrawable();
        cdXiNgau2 = (AnimationDrawable) xg2.getDrawable();
        cdXiNgau3 = (AnimationDrawable) xg3.getDrawable();

        if (btnPlay.getAlpha() == 0.9f) {
            tongtiengmoi = 1000;
            txtTongTien.setText(String.valueOf(tongtiengmoi));
            lnXucSac.setAlpha(1.0f);
            btnPlay.setAlpha(1.0f);
            btnPlay.setBackgroundResource(R.drawable.btnplay);
        } else if (btnPlay.getAlpha() == 1.0f) {
            if (totalCuoc() == 0) {
                Toast.makeText(getApplicationContext(), "Bạn vui lòng đặt cược !", Toast.LENGTH_SHORT).show();
            } else {
                if (totalCuoc() > tongtiengmoi) {
                    Toast.makeText(getApplicationContext(), "Bạn không đủ tiền để đặt cược !", Toast.LENGTH_SHORT).show();
                } else {
                    soundXiNgau.play(idSound, 1.0f, 1.0f, 1, 0, 1.0f);
                    cdXiNgau1.start();
                    cdXiNgau2.start();
                    cdXiNgau3.start();
                    tienthuong = 0;

                    timer = new Timer();
                    timer.schedule(new LaxXiNgau(), 1000);
                }
            }
        }

        Bundle bundle = new Bundle();
        bundle.putString("TEST", "Cong hoa xa hoi chu nghia viet nam");
        Message msg = new Message();
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    public void btnHelp(View view) {
        tongtiengmoi = 1000;
        txtTongTien.setText(String.valueOf(tongtiengmoi));
        lnXucSac.setAlpha(1f);
        lnXucSac.setClickable(true);
    }

    class LaxXiNgau extends TimerTask {
        @Override
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }

    private void RandomXg(ImageView xg, int flag, int valueXiNgau) {
        randomNg = new Random();
        switch (randomNg.nextInt(6)) {
            case 0:
                xg.setImageResource(R.drawable.bau);
                valueXiNgau = 0;
                break;
            case 1:
                xg.setImageResource(R.drawable.tom);
                valueXiNgau = 1;
                break;
            case 2:
                xg.setImageResource(R.drawable.cua);
                valueXiNgau = 2;
                break;
            case 3:
                xg.setImageResource(R.drawable.ca);
                valueXiNgau = 3;
                break;
            case 4:
                xg.setImageResource(R.drawable.ga);
                valueXiNgau = 4;
                break;
            case 5:
                xg.setImageResource(R.drawable.nai);
                valueXiNgau = 5;
                break;
        }
        if (flag == 0) {
            valueXiNgau1 = valueXiNgau;
        } else if (flag == 1)
            valueXiNgau2 = valueXiNgau;
        else
            valueXiNgau3 = valueXiNgau;
    }
}
