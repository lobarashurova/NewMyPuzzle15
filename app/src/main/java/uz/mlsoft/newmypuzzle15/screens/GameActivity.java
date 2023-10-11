package uz.mlsoft.newmypuzzle15.screens;


import static java.lang.Math.abs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import uz.mlsoft.newmypuzzle15.R;
import uz.mlsoft.newmypuzzle15.data.Pairs;

public class GameActivity extends AppCompatActivity {
    static final int N = 4;
    private TextView[][] views;
    private ArrayList<Integer> numbers;

    private final int[][] checkArray = new int[4][4];
    private Pairs pairs;
    private int count;
    private ViewGroup group;

    private TextView textScore;
    private AppCompatImageView restart;
    private AppCompatImageView finish;
    private final Boolean isSave = true;

    MediaPlayer buttomSound;
    MediaPlayer winSound;
    private Chronometer chronometer;
    private AppCompatImageView volue;
    private boolean isSound = true;

    private MediaPlayer media;

    private SharedPreferences pref;
    private SharedPreferences.Editor shape;
    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Log.d("TTT", "onCreate: CHECK1");


        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Glide.with(this).load(R.drawable.game_back2).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        findViewById(R.id.layout_game).setBackground(resource);
                    }
                }
            });
        } else {

            Glide.with(this).load(R.drawable.game_land).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        findViewById(R.id.layout_game).setBackground(resource);
                    }
                }
            });
        }
        pref = this.getSharedPreferences("puzzle", Context.MODE_PRIVATE);

        shape = pref.edit();


        Window window = this.getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        init();
        loadViews();
        initNumbers();

        if (getIntent().getIntExtra("data", -1) == 1) {
            String save = pref.getString("save", " ");
            String[] temp = save.split("#");
            Log.d("TTT", Arrays.toString(temp) + "");
            ArrayList<String> list = new ArrayList<>(Arrays.asList(temp));
            if (temp.length == 1 || temp.length == 0) {
                loadNUmbersToViews();
            } else {
                loadSavedNumbers(list);
            }
            long del = pref.getLong("time", -1);
            chronometer.setBase(SystemClock.elapsedRealtime() + del);
            isSound = pref.getBoolean("isSound", true);
            chronometer.start();
            count = pref.getInt("step", 0);
            textScore.setText(String.valueOf(count));
        } else {
            loadNUmbersToViews();
            Log.d("TTT", "yangi o'yin");
        }
        setButtomSoundIcon();
        menu();


    }

    @SuppressLint("CommitPrefEdits")
    public void save() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append(views[i / 4][i % 4].getText().toString()).append("#");
            Log.d("TTT", sb.toString());
        }

        shape.putString("save", String.valueOf(sb)).apply();
        shape.putBoolean("isSound", isSound);
        chronometer.stop();
        long delTime = chronometer.getBase() - SystemClock.elapsedRealtime();
        shape.putLong("time", delTime);
        shape.putInt("step", count);

    }


    @Override
    protected void onStop() {
        super.onStop();
        save();
        Log.d("TTT", "onStop: CHECK");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        save();
        startActivity(new Intent(this, StartActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        save();
        Log.d("TTT", "onPause:CHECK ");
    }


    private void menu() {
        restart.setOnClickListener(view -> {


            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_restart);
            dialog.setCancelable(false);

            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            dialog.show();
            dialog.findViewById(R.id.yes_btn).setOnClickListener(v -> {
                dialog.dismiss();
                loadNUmbersToViews();
            });
            dialog.findViewById(R.id.no_btn).setOnClickListener(v -> {
                dialog.dismiss();
            });
        });

        finish.setOnClickListener(v -> {
            save();
            startActivity(new Intent(this, StartActivity.class));
            finish();
        });
    }

    private void setButtomSoundIcon() {
        volue.setOnClickListener(view -> {
            if (isSound) {
                volue.setImageResource(R.drawable.mute);
                isSound = false;
            } else {
                volue.setImageResource(R.drawable.unmute);
                isSound = true;
            }
        });
    }

    private void init() {
        restart = findViewById(R.id.restart_btn);
        finish = findViewById(R.id.home_btn);
        textScore = findViewById(R.id.count_score);
        chronometer = findViewById(R.id.chronometer);
        buttomSound = MediaPlayer.create(GameActivity.this, R.raw.click_sound);
        winSound = MediaPlayer.create(GameActivity.this, R.raw.click_sound);
        media = MediaPlayer.create(GameActivity.this, R.raw.u_click);
        volue = findViewById(R.id.image_mute);

    }

    private void loadViews() {
        group = findViewById(R.id.relative_container);
        int count = group.getChildCount();
        int size = (int) Math.sqrt(count);
        views = new TextView[size][size];

        for (int i = 0; i < count; i++) {
            View view = group.getChildAt(i);
            TextView view1 = (TextView) view;
            view1.setOnClickListener(this::onViewCLick);
            int y = i / size;
            int x = i % size;
            view1.setTag(new Pairs(x, y));
            views[y][x] = view1;
        }
    }

    private void initNumbers() {
        numbers = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {

            numbers.add(i);
        }
    }

    public void loadNUmbersToViews() {
        Collections.shuffle(numbers);
        for (int i = 0; i < views.length; i++) {
            for (int j = 0; j < views.length; j++) {
                int index = i * 4 + j;
                if (index < 15) {
                    views[i][j].setText(String.valueOf(numbers.get(index)));
                    checkArray[i][j] = numbers.get(index);
                }
            }
        }

        if (!isSolvable(checkArray)) loadNUmbersToViews();
        views[3][3].setText("");
        pairs = new Pairs(3, 3);


        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
        count = 0;
        textScore.setText(String.valueOf(count));
    }


    private void onViewCLick(View view) {
        TextView view1 = (TextView) view;
        Pairs c = (Pairs) view1.getTag();
        int eX = pairs.getI();
        int eY = pairs.getJ();
        int dX = abs(c.getI() - eX);
        int dY = abs(c.getJ() - eY);
        if (dX + dY == 1) {
            setButtomSound();
            count++;
            textScore.setText(String.valueOf(count));
            views[eY][eX].setText(view1.getText());
            view1.setText("");
            pairs = c;
            if (isWin()) {
                if (isSound) {
                    winSound.start();
                    shape.clear();
                } else winSound.pause();

            }
        }
    }

    private void setButtomSound() {
        if (isSound) {
            buttomSound.start();
        }
    }

    @SuppressLint("CommitPrefEdits")
    private boolean isWin() {
        if (!(pairs.getI() == 3 && pairs.getJ() == 3)) return false;
        for (int i = 0; i < 15; i++) {
            String s = views[i / 4][i % 4].getText().toString();
            if (!s.equals(String.valueOf(i + 1))) return false;
        }

        showDialogWin();
        chronometer.stop();
        return true;
    }

    static int getInvCount(int[] arr) {
        int inv_count = 0;
        for (int i = 0; i < N * N - 1; i++) {
            for (int j = i + 1; j < N * N; j++) {
                if (arr[j] != 0 && arr[i] != 0
                        && arr[i] > arr[j])
                    inv_count++;
            }
        }
        return inv_count;
    }

    static int findXPosition(int[][] puzzle) {
        for (int i = N - 1; i >= 0; i--)
            for (int j = N - 1; j >= 0; j--)
                if (puzzle[i][j] == 0)
                    return N - i;
        return -1;
    }

    static boolean isSolvable(int[][] puzzle) {
        int[] arr = new int[N * N];
        int k = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                arr[k++] = puzzle[i][j];

        int invCount = getInvCount(arr);
        int pos = findXPosition(puzzle);
        if (pos % 2 == 1)
            return invCount % 2 == 0;
        else
            return invCount % 2 == 1;
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        group = findViewById(R.id.relative_container);
        outState.putInt("COUNT", count);
        outState.putBoolean("sound", isSound);
        long delTime = chronometer.getBase() - SystemClock.elapsedRealtime();
        outState.putLong("time", delTime);
        super.onSaveInstanceState(outState);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < group.getChildCount(); i++) {
            TextView textView = (TextView) group.getChildAt(i);
            list.add(textView.getText().toString());
            outState.putStringArrayList("buttons", list);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        count = savedInstanceState.getInt("COUNT", 0);
        long delTime = savedInstanceState.getLong("time", 0);
        chronometer.setBase(SystemClock.elapsedRealtime() + delTime);
        textScore.setText(String.valueOf(count));
        isSound = savedInstanceState.getBoolean("sound");
        ArrayList<String> list = savedInstanceState.getStringArrayList("buttons");
        loadSavedNumbers(list);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        long time = pref.getLong("time", 0L);
        chronometer.setBase(SystemClock.elapsedRealtime() + time);
        chronometer.start();
        if (isSound) {
            volue.setImageResource(R.drawable.unmute);
        } else {
            volue.setImageResource(R.drawable.mute);
        }
        setButtomSoundIcon();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("TTT", "onResume: ");

        if (isSound) {
            volue.setImageResource(R.drawable.unmute);
        } else {
            volue.setImageResource(R.drawable.mute);
        }
        setButtomSoundIcon();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TTT", "ONSTART:  ");
        if (isSound) {
            volue.setImageResource(R.drawable.unmute);
        } else {
            volue.setImageResource(R.drawable.mute);
        }
        setButtomSoundIcon();
    }

    private void loadSavedNumbers(List<String> numbers) {
        textScore.setText(String.valueOf(count));
        for (int i = 0; i < numbers.size(); i++) {
            if (numbers.get(i).equals("")) {
                pairs = new Pairs(i % 4, i / 4);
            }
            views[i / 4][i % 4].setText(numbers.get(i));
        }
    }

    private void showDialogWin() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_win);
        dialog.setCancelable(false);

        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        long delTime = chronometer.getBase() - SystemClock.elapsedRealtime();
        chronometer.stop();
        ((AppCompatTextView) dialog.findViewById(R.id.move_dialog)).setText(String.valueOf(count));
        ((Chronometer) dialog.findViewById(R.id.chronometer_text)).setBase(SystemClock.elapsedRealtime() + delTime);

        dialog.findViewById(R.id.menu_btn).setOnClickListener(v -> {
            loadNUmbersToViews();
            dialog.dismiss();
        });
        dialog.show();
    }

}