package com.google.refine.extension.database;

import java.util.List;

public class SavedConnectionContainer {
    
    public List<DatabaseConfiguration> getSavedConnections() {
        return savedConnections;
    }

    
    public void setSavedConnections(List<DatabaseConfiguration> savedConnections) {
        this.savedConnections = savedConnections;
    }

    List<DatabaseConfiguration> savedConnections;

    public SavedConnectionContainer(List<DatabaseConfiguration> savedConnections) {
        super();
        this.savedConnections = savedConnections;
    }


    public SavedConnectionContainer() {
    
    }
    

}
