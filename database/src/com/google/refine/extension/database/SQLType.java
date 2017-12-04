
package com.google.refine.extension.database;



import java.util.HashMap;
import java.util.Map;


public final class SQLType {

    private static final Map<DriverContainer, SQLType> jdbcDriverRegistry = new HashMap<DriverContainer, SQLType>();
    private final DriverContainer driverContainer;

    private SQLType(DriverContainer container) {
        this.driverContainer = container;
    }

    public static SQLType forName(String name) {
        for (SQLType sqlType : jdbcDriverRegistry.values()) {
            if (sqlType.getIdentifier().equalsIgnoreCase(name)) {
                return sqlType;
            }
        }
        return null;
    }

    public static SQLType registerSQLDriver(String identifier, String classpath) {
        return registerSQLDriver(identifier, classpath, true);
    }

    public static SQLType registerSQLDriver(String identifier, String classpath, boolean useJDBCManager) {
        DriverContainer driverContainer = new DriverContainer(identifier, classpath, useJDBCManager);
        if (!jdbcDriverRegistry.containsKey(driverContainer)) {
            SQLType newType = new SQLType(driverContainer);
            jdbcDriverRegistry.put(driverContainer, newType);
            return newType;
        }
        return null;
    }
    

    public String getClassPath() {
        return this.driverContainer.classpath;
    }

    public String getIdentifier() {
        return this.driverContainer.identifier;
    }

    public boolean usesJDBCManager() {
        return this.driverContainer.useJDBCManager;
    }

    
    private static class DriverContainer {

        public final String classpath;
        public final String identifier;
        public final boolean useJDBCManager;

        private DriverContainer(String identifier, String classpath, boolean useJDBCManager) {
            this.classpath = classpath;
            this.identifier = identifier;
            this.useJDBCManager = useJDBCManager;
        }

        public final boolean equals(Object obj) {
            return obj instanceof DriverContainer && ((DriverContainer) obj).classpath.equals(this.classpath)
                    && ((DriverContainer) obj).identifier.equals(this.identifier)
                    && ((DriverContainer) obj).useJDBCManager == this.useJDBCManager;
        }
    }
}
