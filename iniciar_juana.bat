@echo off
chcp 65001 >nul
echo.
echo ================================
echo    INICIADOR JUANA APP
echo ================================
echo.

REM Verificar si ADB está disponible
where adb >nul 2>&1
if errorlevel 1 (
    echo ❌ ADB no encontrado. Asegúrate de tener Android SDK instalado.
    pause
    exit /b 1
)

REM Navegar al directorio del proyecto
cd /d "C:\Users\Impresos\Desktop\Juana_App"

echo 🔄 Compilando aplicación...
call gradlew clean installDebug

if errorlevel 1 (
    echo.
    echo ❌ Error en la compilación. Revisa los mensajes arriba.
    pause
    exit /b 1
)

echo.
echo ✅ Compilación exitosa!
echo.

echo 🚀 Forzando solicitud de permisos...
echo.
echo 📝 INSTRUCCIONES:
echo 1. CERRAR completamente la app Juana si está abierta
echo 2. ACEPTAR todos los permisos cuando aparezcan
echo 3. La app iniciará automáticamente después
echo.

REM Limpiar datos para forzar solicitud de permisos
adb shell pm clear com.juana.app

echo ⏳ Esperando 3 segundos...
timeout /t 3 /nobreak >nul

echo 🎯 Iniciando aplicación (debe pedir permisos)...
adb shell am start -n com.juana.app/.MainActivity

echo.
echo 📋 Verificando que el servicio esté ejecutándose...
timeout /t 5 /nobreak >nul
adb shell dumpsys activity services | findstr "JuanaService"

echo.
echo 🔊 Monitoreo de logs (Ctrl+C para detener)...
echo    - Buscando: JuanaService, MainActivity, permisos
echo.
adb logcat -s "JuanaService:V" "MainActivity:V" | findstr "permission\|Permission\|audio\|record"