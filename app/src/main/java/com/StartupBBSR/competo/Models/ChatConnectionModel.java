package com.StartupBBSR.competo.Models;

import java.io.Serializable;
import java.util.List;

public class ChatConnectionModel implements Serializable {
    List<String> Connections;
    List<String> TeamConnection;

    public ChatConnectionModel(List<String> Connections, List<String> TeamConnection) {
        this.Connections = Connections;
        this.TeamConnection = TeamConnection;
    }

    public ChatConnectionModel() {
    }

    public List<String> getConnections() {
        return Connections;
    }

    public void setConnection(List<String> Connections) {
        this.Connections = Connections;
    }

    public List<String> getTeamConnection() {
        return TeamConnection;
    }

    public void setTeamConnection(List<String> TeamConnection) {
        this.TeamConnection = TeamConnection;
    }
}

