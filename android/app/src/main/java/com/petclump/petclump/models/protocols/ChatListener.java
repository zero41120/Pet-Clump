package com.petclump.petclump.models.protocols;

public interface ChatListener {
    public void startListening();
    public void didCompleteSend();
    public void didCompleteReceive();
}
