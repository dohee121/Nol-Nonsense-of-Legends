package com.example.nol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class A5_Owl extends AppCompatActivity {
    int brightness;
    ImageView owl1, owl2, correct;
    Button prevBtn, listBtn, hintBtn;
    List<String> gameList = new ArrayList<String>();
    WindowManager.LayoutParams params;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5_owl);

        owl1 = (ImageView) findViewById(R.id.owl1);
        owl2 = (ImageView) findViewById(R.id.owl2);
        correct = (ImageView) findViewById(R.id.correct);

        // 이전 단계
        prevBtn = (Button) findViewById(R.id.owl_Prev);
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MySoundPlayer.play(MySoundPlayer.BUTTON_SOUND);
                Intent intent = new Intent(getApplicationContext(), A0_Enter.class);
                startActivity(intent);
                finish();
            }
        });

        // 게임 목록에 아이템 추가
        for(int i = 1; i <= 5; i++)
            gameList.add(i + "단계");

        // 게임 목록
        listBtn = (Button) findViewById(R.id.owl_List);
        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MySoundPlayer.play(MySoundPlayer.BUTTON_SOUND);
                gameListDialog();
            }
        });

        // 힌트 보기
        hintBtn = (Button) findViewById(R.id.owl_Hint);
        hintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MySoundPlayer.play(MySoundPlayer.BUTTON_SOUND);
                hintDialog("부엉이는 야행성입니다.");
            }
        });

        Thread thread = new Thread(new detectThread());
        thread.start();

    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            correct.setVisibility(View.VISIBLE);
        }
    };

    class detectThread implements Runnable {
        @Override
        public void run() {
            try {
               while(true){
                   Thread.sleep(3000);
                   brightness = Settings.System.getInt(getContentResolver(),
                           Settings.System.SCREEN_BRIGHTNESS);
                   System.out.println(brightness);

                   if(brightness <= 30)
                   {
                       owl1.setVisibility(View.INVISIBLE);
                       break;
                   }
               }
            } catch (Settings.SettingNotFoundException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void hintDialog(String text) {
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        Dialog dialog = new Dialog(this);

        // 액티비티의 타이틀바를 숨긴다.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dialog.setContentView(R.layout.custom_dialog_hint);

        // 커스텀 다이얼로그를 노출한다.
        dialog.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        TextView hintText = (TextView) dialog.findViewById(R.id.hintText);
        TextView hintOk = (TextView) dialog.findViewById(R.id.hintOk);

        hintText.setText(text);
        hintOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void gameListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog_list, null);
        builder.setView(view);

        ListView listview = (ListView)view.findViewById(R.id.gameList);
        AlertDialog dialog = builder.create();

        GameListAdapter adapter = new GameListAdapter(this, R.layout.listitem_game);

        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
            }
        });

        // 닫기 버튼
        TextView listClose = (TextView)view.findViewById(R.id.gameListClose);
        listClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public class GameListAdapter extends BaseAdapter {
        Context context;
        int layout;
        LayoutInflater inflater;

        public GameListAdapter(Context context, int layout){
            this.context = context;
            this.layout = layout;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return gameList.size();
        }

        @Override
        public Object getItem(int i) {
            return gameList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null)
                view = inflater.inflate(layout, null);

            TextView listStep = (TextView)view.findViewById(R.id.gameListStep);
            listStep.setText(gameList.get(i));
            return view;
        }
    }
}