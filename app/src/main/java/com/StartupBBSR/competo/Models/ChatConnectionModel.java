package com.StartupBBSR.competo.Models;

import java.io.Serializable;
import java.util.List;

public class ChatConnectionModel implements Serializable {
    List<String> Connections;

    public ChatConnectionModel(List<String> Connections) {
        this.Connections = Connections;
    }

    public ChatConnectionModel() {
    }

    public List<String> getConnections() {
        return Connections;
    }

    public void setConnection(List<String> Connections) {
        this.Connections = Connections;
    }
}

