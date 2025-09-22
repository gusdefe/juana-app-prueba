package com.juana.app;

import android.speech.SpeechRecognizer;

public class GoogleVoiceEngine implements IVoiceEngine {

    private SpeechRecognizer speechRecognizer;

    public GoogleVoiceEngine() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
    }

    @Override
    public void startListening() {
        // Implementar la lógica para iniciar el reconocimiento de voz
    }

    @Override
    public void stopListening() {
        // Implementar la lógica para detener el reconocimiento de voz
    }

    // Otros métodos de la interfaz IVoiceEngine
}