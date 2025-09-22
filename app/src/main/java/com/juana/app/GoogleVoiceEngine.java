package com.juana.app;

import android.content.Context;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.content.Intent;
import java.util.ArrayList;

public class GoogleVoiceEngine implements IVoiceEngine {
    private SpeechRecognizer speechRecognizer;
    private Context context;
    private VoiceCommandListener commandListener;

    public GoogleVoiceEngine(Context context) {
        this.context = context;
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
    }

    @Override
    public void start() {
        Intent intent = new Intent(android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matches != null && !matches.isEmpty() && commandListener != null) {
                    commandListener.onCommandRecognized(matches.get(0));
                }
            }
            @Override public void onError(int error) {
                if (commandListener != null) {
                    commandListener.onError(new Exception(\"Error de reconocimiento: \" + error));
                }
            }
            @Override public void onReadyForSpeech(Bundle params) {}
            @Override public void onBeginningOfSpeech() {}
            @Override public void onRmsChanged(float rmsdB) {}
            @Override public void onBufferReceived(byte[] buffer) {}
            @Override public void onEndOfSpeech() {}
            @Override public void onPartialResults(Bundle partialResults) {}
            @Override public void onEvent(int eventType, Bundle params) {}
        });

        speechRecognizer.startListening(intent);
    }

    @Override
    public void stop() {
        speechRecognizer.stopListening();
    }

    // Conectar el listener
    public void setCommandListener(VoiceCommandListener listener) {
        this.commandListener = listener;
    }
}
