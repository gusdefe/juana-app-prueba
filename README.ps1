# Crear el README.md con el contenido completo
$readmeContent = @'
# 🎤 Juana App - Asistente de Voz Inteligente

[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com)
[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://java.com)
[![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/gusdefe/juana-app)

Asistente de voz inteligente para Android que escucha y responde en tiempo real usando reconocimiento de voz continuo.

## ✨ Características

- 🎤 **Reconocimiento de voz continuo** - Escucha siempre activa
- 🗣️ **Respuestas inteligentes** - Procesamiento de comandos de voz
- 🔔 **Notificaciones interactivas** - Respuestas visibles
- 📱 **Servicio en segundo plano** - Funciona 24/7
- 🎨 **Interfaz minimalista** - Botón de inicio manual

## 🚀 Demo

*(Agrega un video demo cuando tengas)*

## 📦 Instalación

### Requisitos
- Android SDK 34+
- Java JDK 11+
- Dispositivo Android o emulador

### Compilación
```bash
# Clonar repositorio
git clone https://github.com/gusdefe/juana-app.git
cd juana-app

# Compilar e instalar
./gradlew installDebug

# O compilar APK
./gradlew assembleDebug