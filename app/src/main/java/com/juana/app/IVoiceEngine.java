package com.juana.app;

public interface IVoiceEngine {
    void start();
    void stop();

    interface VoiceCommandListener {
        void onCommandRecognized(String commandText);
        void onError(Exception e);
    }
}
