package com.example.nol;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.nol.A0_Enter.mediaPlayer;

public class A2_Egg extends AppCompatActivity {
    private ImageView imageView, correct;
    Button prevBtn, listBtn, hintBtn;
    int i = 0;
    int flag;
    Drawable drawable; // 대리자를 선언합니다
    List<String> gameList = new ArrayList<String>();
    Activity activity = A2_Egg.this;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_egg);
        correct = (ImageView) findViewById(R.id.eggCorrect);
        MySoundPlayer.initSounds(getApplicationContext());

        Intent intent = getIntent();
        flag = intent.getExtras().getInt("flag");

        // 타이머 시작
        TextView time = (TextView) findViewById(R.id.eggTimer);
        timer = new Timer(this,this, time);
        timer.startTimer();

        // 힌트 보기
        hintBtn = (Button) findViewById(R.id.eggHint);
        hintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySoundPlayer.play(MySoundPlayer.BUTTON_SOUND);
                new CustomDialog(A2_Egg.this).hintDialog("계란을 자극시켜보세요.");
            }
        });

        // 이전 단계
        prevBtn = (Button) findViewById(R.id.eggPrev);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySoundPlayer.play(MySoundPlayer.BUTTON_SOUND);
                Intent intent = new Intent(getApplicationContext(), A1_Rabbit.class);
                intent.putExtra("flag", flag); // 이전 단계로 가도 현재 단계까지 깼음을 알 수 있음
                startActivity(intent);
                timer.countDownTimer.cancel();
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        // 게임 목록
        listBtn = (Button) findViewById(R.id.eggList);
        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MySoundPlayer.play(MySoundPlayer.BUTTON_SOUND);
                LayoutInflater inflater = getLayoutInflater();
                new CustomDialog(A2_Egg.this, activity, inflater, flag, 2, timer).gameListDialog();
            }
        });


        imageView = (ImageView) findViewById(R.id.egg); // ID 설정을 합니다
        final Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE); // 진동

        // 그림 터치시에, 이벤트를 발생하게 해주는 함수입니다
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                i++;
                vibrator.vibrate(50); // 계란 터치 시 0.05초 진동

                if (i == 10) {
                    MySoundPlayer.play(MySoundPlayer.CRACK);
                    drawable = getResources().getDrawable(R.drawable.egg_cracked_1);
                    imageView.setImageDrawable(drawable); // 이미지를 적용합니다
                } else if (i == 20) {
                    MySoundPlayer.play(MySoundPlayer.CRACK);
                    drawable = getResources().getDrawable(R.drawable.egg_cracked_2);
                    imageView.setImageDrawable(drawable); // 이미지를 적용합니다
                } else if (i == 30) {
                    MySoundPlayer.play(MySoundPlayer.CRACK);
                    drawable = getResources().getDrawable(R.drawable.egg_cracked_3);
                    imageView.setImageDrawable(drawable); // 이미지를 적용합니다

                } else if (i == 40) {
                    MySoundPlayer.play(MySoundPlayer.BROKEN);
                    MySoundPlayer.play(MySoundPlayer.CHICKEN);
                    drawable = getResources().getDrawable(R.drawable.chicken);
                    imageView.setImageDrawable(drawable); // 이미지를 적용합니다
                    timer.countDownTimer.cancel();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MySoundPlayer.stop(MySoundPlayer.CHICKEN);
                            MySoundPlayer.stop(MySoundPlayer.BROKEN);
                            MySoundPlayer.play(MySoundPlayer.CORRECT);
                            correct.setVisibility(View.VISIBLE); //딜레이 후 시작할 코드 작성

                            ObjectAnimator anim1 = ObjectAnimator.ofFloat(correct, "rotation", -2f, 2f);
                            anim1.setRepeatMode(ValueAnimator.REVERSE);
                            anim1.setRepeatCount(9999);
                            anim1.setDuration(300);
                            anim1.start();
                        }
                    }, 2000);// 2초 정도 딜레이를 준 후 시작

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), AX_StageClear.class);
                            intent.putExtra("text",
                                    "귀여운 병아리예요!\n" +
                                            "이건 할만했죠?");
                            if(flag == 2) ++flag;
                            intent.putExtra("flag", flag);
                            intent.putExtra("key", "birthday");
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        }
                    }, 4000);
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
    }
}