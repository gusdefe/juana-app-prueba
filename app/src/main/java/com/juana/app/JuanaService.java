package com.juana.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class JuanaService extends Service implements IVoiceEngine.VoiceCommandListener {

    private IVoiceEngine voiceEngine;
    private JuanaBrain juanaBrain;

    @Override
    public void onCreate() {
        super.onCreate();
        juanaBrain = new JuanaBrain();
        voiceEngine = new GoogleVoiceEngine(this);
        if (voiceEngine instanceof GoogleVoiceEngine) {
            ((GoogleVoiceEngine) voiceEngine).setCommandListener(this);
        }
    }

    @Override
    public void onCommandRecognized(String commandText) {
        String respuesta = juanaBrain.processVoiceCommand(commandText);
        Log.d("JuanaService", "Respuesta: " + respuesta);
        // Aquí puedes manejar la respuesta (ejemplo: notificación, UI, etc.)
    }

    @Override
    public void onError(Exception e) {
        Log.e("JuanaService", "Error reconocido: ", e);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        voiceEngine.start();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // Sin binding por ahora
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        voiceEngine.stop();
    }
}
