@echo off
echo === REPARACION JUANA APP ===
echo.

echo 1. Reparando MainActivity.java...
(
echo package com.juana.app;
echo.
echo import androidx.appcompat.app.AppCompatActivity;
echo import androidx.core.app.ActivityCompat;
echo import androidx.core.content.ContextCompat;
echo import android.content.Intent;
echo import android.content.pm.PackageManager;
echo import android.os.Bundle;
echo import android.Manifest;
echo.
echo public class MainActivity extends AppCompatActivity {
echo.
echo     private static final int PERMISSION_REQUEST_CODE = 100;
echo.
echo     @Override
echo     protected void onCreate(Bundle savedInstanceState) {
echo         super.onCreate(savedInstanceState);
echo         setContentView(R.layout.activity_main);
echo.
echo         // Solicitar permisos primero
echo         checkPermissions();
echo.
echo         // Iniciar el servicio después de permisos
echo         Intent serviceIntent = new Intent(this, JuanaService.class);
echo         startService(serviceIntent);
echo     }
echo.
echo     private void checkPermissions() {
echo         String[] permissions = {
echo             Manifest.permission.RECORD_AUDIO,
echo             Manifest.permission.WRITE_EXTERNAL_STORAGE,
echo             Manifest.permission.READ_EXTERNAL_STORAGE
echo         };
echo.
echo         boolean allPermissionsGranted = true;
echo         for (String permission : permissions) {
echo             if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
echo                 allPermissionsGranted = false;
echo                 break;
echo             }
echo         }
echo.
echo         if (!allPermissionsGranted) {
echo             ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
echo         }
echo     }
echo.
echo     @Override
echo     public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
echo         super.onRequestPermissionsResult(requestCode, permissions, grantResults);
echo         if (requestCode == PERMISSION_REQUEST_CODE) {
echo             // Permisos concedidos, podemos continuar
echo         }
echo     }
echo }
) > "app\src\main\java\com\juana\app\MainActivity.java"

echo   ✓ MainActivity.java reparado

echo.
echo 2. Actualizando AndroidManifest.xml...
(
echo ^<?xml version="1.0" encoding="utf-8"?^>
echo ^<manifest xmlns:android="http://schemas.android.com/apk/res/android"
echo     xmlns:tools="http://schemas.android.com/tools"
echo     package="com.juana.app"^>
echo.
echo     ^<uses-permission android:name="android.permission.RECORD_AUDIO" /^>
echo     ^<uses-permission android:name="android.permission.WAKE_LOCK" /^>
echo     ^<uses-permission android:name="android.permission.FOREGROUND_SERVICE" /^>
echo     ^<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" /^>
echo     ^<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" /^>
echo     ^<uses-permission android:name="android.permission.FOREGROUND_SERVICE_MICROPHONE" /^>
echo.
echo     ^<application
echo         android:allowBackup="true"
echo         android:icon="@mipmap/ic_launcher"
echo         android:label="@string/app_name"
echo         android:theme="@style/Theme.AppCompat.Light"
echo         tools:ignore="UnusedAttribute"^>
echo.
echo         ^<service
echo             android:name=".JuanaService"
echo             android:enabled="true"
echo             android:exported="false"
echo             android:foregroundServiceType="microphone" /^>
echo.
echo         ^<activity
echo             android:name=".MainActivity"
echo             android:exported="true"
echo             android:label="@string/app_name"^>
echo             ^<intent-filter^>
echo                 ^<action android:name="android.intent.action.MAIN" /^>
echo                 ^<category android:name="android.intent.category.LAUNCHER" /^>
echo             ^</intent-filter^>
echo         ^</activity^>
echo.
echo     ^</application^>
echo ^</manifest^>
) > "app\src\main\AndroidManifest.xml"

echo   ✓ AndroidManifest.xml actualizado

echo.
echo 3. Creando AudioRecorder.java...
(
echo package com.juana.app;
echo.
echo import android.media.AudioRecord;
echo import android.media.MediaRecorder;
echo import android.util.Log;
echo import java.io.File;
echo import java.io.FileOutputStream;
echo import java.io.IOException;
echo.
echo public class AudioRecorder {
echo     private static final String TAG = "AudioRecorder";
echo     private static final int SAMPLE_RATE = 16000;
echo     private static final int CHANNEL_CONFIG = android.media.AudioFormat.CHANNEL_IN_MONO;
echo     private static final int AUDIO_FORMAT = android.media.AudioFormat.ENCODING_PCM_16BIT;
echo.
echo     private AudioRecord audioRecord;
echo     private boolean isRecording = false;
echo.
echo     public File recordForDuration(int durationMs) {
echo         Log.d(TAG, "Iniciando grabación de " + durationMs + " ms");
echo.
echo         try {
echo             int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
echo             Log.d(TAG, "Buffer size: " + bufferSize);
echo.
echo             audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, 
echo                                          CHANNEL_CONFIG, AUDIO_FORMAT, bufferSize);
echo.
echo             if (audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
echo                 Log.e(TAG, "AudioRecord no se pudo inicializar");
echo                 return null;
echo             }
echo.
echo             // Crear archivo temporal
echo             File audioFile = File.createTempFile("juana_audio", ".pcm");
echo             FileOutputStream outputStream = new FileOutputStream(audioFile);
echo             byte[] buffer = new byte[bufferSize];
echo.
echo             audioRecord.startRecording();
echo             isRecording = true;
echo             Log.d(TAG, "Grabación iniciada");
echo.
echo             long startTime = System.currentTimeMillis();
echo             while (isRecording && (System.currentTimeMillis() - startTime) < durationMs) {
echo                 int bytesRead = audioRecord.read(buffer, 0, buffer.length);
echo                 if (bytesRead > 0) {
echo                     outputStream.write(buffer, 0, bytesRead);
echo                 }
echo             }
echo.
echo             stopRecording();
echo             outputStream.close();
echo.
echo             Log.d(TAG, "Grabación completada. Tamaño: " + audioFile.length() + " bytes");
echo             return audioFile;
echo.
echo         } catch (IOException e) {
echo             Log.e(TAG, "Error de IO: " + e.getMessage());
echo             return null;
echo         } catch (Exception e) {
echo             Log.e(TAG, "Error inesperado: " + e.getMessage());
echo             return null;
echo         }
echo     }
echo.
echo     public void stopRecording() {
echo         isRecording = false;
echo         if (audioRecord != null) {
echo             try {
echo                 audioRecord.stop();
echo                 audioRecord.release();
echo                 audioRecord = null;
echo                 Log.d(TAG, "Grabador detenido y liberado");
echo             } catch (Exception e) {
echo                 Log.e(TAG, "Error al detener grabación: " + e.getMessage());
echo             }
echo         }
echo     }
echo }
) > "app\src\main\java\com\juana\app\AudioRecorder.java"

echo   ✓ AudioRecorder.java creado

echo.
echo === REPARACION COMPLETADA ===
echo Ahora abre el proyecto en Android Studio y haz: Build > Make Project
pause