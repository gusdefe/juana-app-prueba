package com.juana.app;

public class JuanaBrain {
    public String processVoiceCommand(String commandText) {
        // Ejemplo simple
        if ("saludar".equalsIgnoreCase(commandText)) {
            return "¡Hola! ¿En qué puedo ayudarte?";
        }
        return "Comando no reconocido: " + commandText;
    }
}
