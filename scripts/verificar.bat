@echo off
echo === VERIFICACION POST-REPARACION ===
echo.

echo 1. Verificando archivos reparados...
if exist "app\src\main\java\com\juana\app\MainActivity.java" (
    echo   ✓ MainActivity.java existe
) else (
    echo   ✗ ERROR: MainActivity.java no existe
)

if exist "app\src\main\AndroidManifest.xml" (
    echo   ✓ AndroidManifest.xml existe
) else (
    echo   ✗ ERROR: AndroidManifest.xml no existe
)

if exist "app\src\main\java\com\juana\app\AudioRecorder.java" (
    echo   ✓ AudioRecorder.java existe
) else (
    echo   ✗ ERROR: AudioRecorder.java no existe
)

echo.
echo 2. Mostrando tamaño de archivos:
for %%f in (
    "app\src\main\java\com\juana\app\MainActivity.java"
    "app\src\main\AndroidManifest.xml" 
    "app\src\main\java\com\juana\app\AudioRecorder.java"
) do (
    if exist %%f (
        for %%i in (%%f) do echo   %%~nxi: %%~zi bytes
    )
)

echo.
echo === INSTRUCCIONES ===
echo 1. Abre Android Studio
echo 2. Abre la carpeta JUANA_APP
echo 3. Espera a que sincronice
echo 4. Ve a Build > Make Project
echo.
pause