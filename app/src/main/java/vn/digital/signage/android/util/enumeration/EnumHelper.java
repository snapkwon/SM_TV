package vn.digital.signage.android.util.enumeration;

/**
 * The type Enum helper.
 */
public final class EnumHelper {

    private EnumHelper() {
        throw new IllegalAccessError("Utility class");
    }

    /**
     * Gets enum type from id.
     *
     * @param <T1>         the type parameter
     * @param <T>          the type parameter
     * @param cls          the cls
     * @param id           the id
     * @param defaultValue the default value
     * @return the enum type from id
     */
    public static <T1, T extends Enum & IEnum<T1>> T getEnumTypeFromId(Class<T> cls, T1 id, T defaultValue) {

        for (T option : cls.getEnumConstants()) {
            if (id instanceof String && ((String) option.getId()).equalsIgnoreCase((String) id)) {
                return option;
            } else if (option.getId() == id)
                return option;
        }

        return defaultValue;
    }
}
