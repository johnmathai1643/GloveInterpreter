package com.example.john.gloveinterpreter;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

public class SpeechModule extends Fragment implements TextToSpeech.OnInitListener {
    View rootView;
    private TextToSpeech tts;
    private String text;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.speech_fragment, container, false);
        Bundle bundle = this.getArguments();
        text = bundle.getString("text", "empty");

        tts = new TextToSpeech(getActivity(), this);
        return rootView;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                speakOut();
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void speakOut() {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}
