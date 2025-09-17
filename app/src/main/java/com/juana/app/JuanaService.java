package com.juana.app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

// NUEVAS IMPORTACIONES PARA RECONOCIMIENTO DE VOZ
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.os.Bundle;
import java.util.ArrayList;
import java.io.IOException;

public class JuanaService extends Service {
    
    private MediaRecorder mediaRecorder;
    private static final String TAG = "JuanaService";
    
    // NUEVAS VARIABLES PARA RECONOCIMIENTO DE VOZ
    private SpeechRecognizer speechRecognizer;
    private Intent speechIntent;
    
    @Override
    public void onCreate() {
        super.onCreate();
        setupForegroundService();
        setupSpeechRecognizer(); // ✅ NUEVO: Configurar reconocimiento de voz
        startAudioRecording();
    }
    
    private void setupForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                "juana_channel", 
                "Juana Service", 
                NotificationManager.IMPORTANCE_HIGH
            );
            
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        
        Notification notification = new NotificationCompat.Builder(this, "juana_channel")
            .setContentTitle("🎤 Juana Activada")
            .setContentText("Escuchando... habla ahora!")
            .setSmallIcon(R.drawable.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build();
            
        startForeground(1, notification);
    }
    
    // ✅ NUEVO MÉTODO: Configurar reconocimiento de voz
    private void setupSpeechRecognizer() {
        try {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            speechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
            speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES"); // Español
            
            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                    Log.d(TAG, "🎤 Listo para escuchar voz");
                }

                @Override
                public void onBeginningOfSpeech() {
                    Log.d(TAG, "🎤 Comenzó a hablar");
                }

                @Override
                public void onRmsChanged(float rmsdB) {
                    // Nivel de audio en tiempo real
                }

                @Override
                public void onBufferReceived(byte[] buffer) {}

                @Override
                public void onEndOfSpeech() {
                    Log.d(TAG, "🎤 Dejó de hablar");
                }

                @Override
                public void onError(int error) {
                    Log.e(TAG, "❌ Error reconocimiento: " + error);
                    restartSpeechRecognition();
                }

                @Override
                public void onResults(Bundle results) {
                    // CORRECCIÓN: Usar la constante correcta
                    ArrayList<String> matches = results.getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
                    if (matches != null && !matches.isEmpty()) {
                        String spokenText = matches.get(0);
                        Log.d(TAG, "🗣️ Reconocido: " + spokenText);
                        processVoiceCommand(spokenText);
                    }
                    restartSpeechRecognition();
                }

                @Override
                public void onPartialResults(Bundle partialResults) {
                    // CORRECCIÓN: Usar la constante correcta
                    ArrayList<String> partial = partialResults.getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
                    if (partial != null && !partial.isEmpty()) {
                        Log.d(TAG, "🔊 Parcial: " + partial.get(0));
                    }
                }

                @Override
                public void onEvent(int eventType, Bundle params) {}
            });

            // Iniciar reconocimiento
            restartSpeechRecognition();
            
        } catch (Exception e) {
            Log.e(TAG, "❌ Error inicializando reconocimiento: " + e.getMessage());
        }
    }
    
    // ✅ NUEVO MÉTODO: Reiniciar reconocimiento de voz
    private void restartSpeechRecognition() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    speechRecognizer.startListening(speechIntent);
                    Log.d(TAG, "🔄 Reiniciando reconocimiento de voz");
                } catch (Exception e) {
                    Log.e(TAG, "❌ Error reiniciando reconocimiento: " + e.getMessage());
                }
            }
        }, 1000);
    }
    
    // ✅ NUEVO MÉTODO: Procesar comandos de voz
    private void processVoiceCommand(String command) {
        Log.d(TAG, "🎯 Procesando comando: " + command);
        
        // Respuestas según lo que digas
        String response;
        if (command.toLowerCase().contains("hola")) {
            response = "¡Hola! ¿Cómo estás?";
        } else if (command.toLowerCase().contains("cómo estás")) {
            response = "¡Estoy muy bien, gracias por preguntar!";
        } else if (command.toLowerCase().contains("qué puedes hacer")) {
            response = "Puedo escucharte y responder. ¡Pruébame!";
        } else if (command.toLowerCase().contains("gracias")) {
            response = "¡De nada! Estoy aquí para ayudarte";
        } else {
            response = "Te escuché decir: " + command;
        }
        
        simulateResponse(response);
    }
    
    private void startAudioRecording() {
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mediaRecorder.setOutputFile("/dev/null");
            
            mediaRecorder.prepare();
            mediaRecorder.start();
            
            Log.d(TAG, "✅ Grabación de audio iniciada");
            
        } catch (IOException e) {
            Log.e(TAG, "❌ Error iniciando grabación: " + e.getMessage());
            tryAlternativeRecording();
        }
    }
    
    private void tryAlternativeRecording() {
        try {
            if (mediaRecorder != null) {
                mediaRecorder.release();
            }
            
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile("/dev/null");
            
            mediaRecorder.prepare();
            mediaRecorder.start();
            
            Log.d(TAG, "✅ Grabación alternativa iniciada (VOICE_RECOGNITION)");
            
        } catch (Exception e) {
            Log.e(TAG, "❌ Error en grabación alternativa: " + e.getMessage());
        }
    }
    
    private void simulateResponse(String message) {
        Log.d(TAG, "🗣️ RESPUESTA: " + message);
        
        Notification notification = new NotificationCompat.Builder(this, "juana_channel")
            .setContentTitle("💬 Juana Te Responde")
            .setContentText("👉 " + message)
            .setSmallIcon(R.drawable.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .build();
            
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.notify(1, notification);
        
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(JuanaService.this, "🎯 Juana: " + message, Toast.LENGTH_LONG).show();
            }
        });
        
        Log.d(TAG, "✅ Notificación y Toast mostrados");
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "🚀 Servicio iniciado");
        return START_STICKY;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
            speechRecognizer = null;
        }
        Log.d(TAG, "⏹️ Servicio detenido");
    }
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}