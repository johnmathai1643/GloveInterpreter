package com.example.john.gloveinterpreter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

public class CalibrationModule extends Fragment implements TextToSpeech.OnInitListener {
    View rootView;
    private TextView alpha;
    private int i;
    private TextToSpeech tts;
    private static final char[] Alphabets = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
    public CalibrationModule() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.calibration_fragment, container, false);
        alpha = (TextView) rootView.findViewById(R.id.alpha);
        tts = new TextToSpeech(getActivity(), this);
//        mHandler.postDelayed(mUpdateUITimerTask, 3000);
        alpha.setText("0");
        i=0;
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                startRepeatingTask();
                return false;
            }
        });

        return rootView;
    }



    private final Handler mHandler = new Handler();
    private final Runnable mUpdateUITimerTask = new Runnable() {
        public void run() {
                mHandler.postDelayed(this, 2000);
                alpha.setText("" + Alphabets[i]);
                speakOut("" + Alphabets[i]);
                i++;

            if (i == 26){
                stopRepeatingTask();
                speakOut("Calibration complete.");
            }
        }
    };

    void startRepeatingTask() {
        mUpdateUITimerTask.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mUpdateUITimerTask);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        alpha.setText("A");
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt("section_number"));
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                speakOut("Touch to start calibration.");
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void speakOut(String text) {
        tts.setSpeechRate(0.75f);
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}
