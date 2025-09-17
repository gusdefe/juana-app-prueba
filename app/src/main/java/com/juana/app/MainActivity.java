package com.juana.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest;
import android.util.Log;
import android.widget.Toast;
import android.widget.Button;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final String TAG = "MainActivity";
    private static final String[] REQUIRED_PERMISSIONS = {
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.FOREGROUND_SERVICE
    };

    private Button btnIniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Configurar botón
        btnIniciar = findViewById(R.id.btnIniciar);
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "🎯 Botón presionado - Verificando permisos");
                checkPermissions();
            }
        });
        
        Log.d(TAG, "🚀 App iniciada - Esperando botón");
        Toast.makeText(this, "👋 Presiona el botón para comenzar", Toast.LENGTH_SHORT).show();
    }

    private void checkPermissions() {
        Log.d(TAG, "📋 Verificando permisos...");
        List<String> missingPermissions = new ArrayList<>();
        
        for (String permission : REQUIRED_PERMISSIONS) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            Log.d(TAG, "🔍 Permiso " + permission + ": " + 
                (result == PackageManager.PERMISSION_GRANTED ? "CONCEDIDO" : "FALTANTE"));
            
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }

        if (missingPermissions.isEmpty()) {
            Log.d(TAG, "✅ Todos los permisos ya concedidos");
            startJuanaService();
        } else {
            Log.d(TAG, "🎯 Solicitando permisos: " + missingPermissions);
            ActivityCompat.requestPermissions(this, 
                missingPermissions.toArray(new String[0]), 
                PERMISSION_REQUEST_CODE);
        }
    }

    private void startJuanaService() {
        try {
            Log.d(TAG, "🔊 Intentando iniciar JuanaService...");
            Intent serviceIntent = new Intent(this, JuanaService.class);
            startService(serviceIntent);
            Toast.makeText(this, "🎤 Juana escuchando... Habla ahora!", Toast.LENGTH_LONG).show();
            Log.d(TAG, "✅ Servicio iniciado");
            
            // OPCIONAL: Cerrar la app después de iniciar el servicio
            // finish();
            
        } catch (Exception e) {
            Log.e(TAG, "❌ Error iniciando servicio: " + e.getMessage());
            Toast.makeText(this, "❌ Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        Log.d(TAG, "📋 Resultado de permisos - Código: " + requestCode);
        
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;
            for (int i = 0; i < grantResults.length; i++) {
                String status = grantResults[i] == PackageManager.PERMISSION_GRANTED ? "✅" : "❌";
                Log.d(TAG, status + " " + permissions[i] + ": " + 
                    (grantResults[i] == PackageManager.PERMISSION_GRANTED ? "CONCEDIDO" : "DENEGADO"));
                
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                }
            }
            
            if (allGranted) {
                Log.d(TAG, "🎉 TODOS los permisos concedidos - iniciando servicio");
                startJuanaService();
            } else {
                Log.d(TAG, "💥 Algunos permisos fueron denegados");
                Toast.makeText(this, "❌ Permisos denegados - No puedo escucharte", Toast.LENGTH_LONG).show();
            }
        }
    }
}