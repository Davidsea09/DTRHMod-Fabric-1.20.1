package net.emanueljdf09.dtrhmod.util.config;

public class ModConfigData {
    private static boolean biomeLockingEnabled = true;

    public static boolean isBiomeLockingEnabled() {
        return biomeLockingEnabled;
    }

    public static void setBiomeLockingEnabled(boolean enabled) {
        biomeLockingEnabled = enabled;
    }

    public static boolean toggleBiomeLocking() {
        biomeLockingEnabled = !biomeLockingEnabled;
        return biomeLockingEnabled;
    }
}
