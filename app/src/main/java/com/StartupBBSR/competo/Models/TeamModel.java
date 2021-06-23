package com.StartupBBSR.competo.Models;

import java.io.Serializable;
import java.util.List;

public class TeamModel implements Serializable {
    String teamName, teamImage, teamID, creatorID;
    List<String> teamMembers;

    public TeamModel(String teamName, String teamImage, String teamID, String creatorID, List<String> teamMembers) {
        this.teamName = teamName;
        this.teamImage = teamImage;
        this.teamID = teamID;
        this.creatorID = creatorID;
        this.teamMembers = teamMembers;
    }

    public TeamModel() {
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTeamImage() {
        return teamImage;
    }

    public String getTeamID() {
        return teamID;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public List<String> getTeamMembers() {
        return teamMembers;
    }
}
