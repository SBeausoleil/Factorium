package com.sb.factorium;


import java.util.ArrayList;
import java.util.List;

/**
 * Represent a mix and match of FieldMods and MethodMods with a simple creation syntax.
 *
 * <pre>{@code
 * MixedMultiMod mod = new MixedMultiMod(
 *                 "postalCode", "H4",
 *                 "setCity()", new City("Ottawa", 1_408_000L),
 *                 "setCity(1)", new City("Quebec", 832_000L),
 *                 "setNumberAndStreet(2)", 123, "Street Name",
 *                 "city.nCitizens", 1234);
 * }</pre>
 */
public class MixedMultiMod implements Modifier {
    private static final int NOT_A_METHOD = -1;

    protected Modifier[] modifiers;

    public MixedMultiMod(Object... args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("A MixedMultiMod must have at least two arguments (a key and a value)");
        }
        init(args);
    }

    private void init(Object[] args) {
        ArrayList<Modifier> accumulator = new ArrayList<>();
        int i = 0;
        while (i < args.length) {
            if (args[i] instanceof String) {
                i = analyzeArgument(args, i, accumulator);
                i++;
            } else {
                throw new IllegalArgumentException("Argument #" + i + " should be a string indicating a field or a method!");
            }
        }
        this.modifiers = accumulator.toArray(new Modifier[0]);
    }

    private int analyzeArgument(Object[] args, int i, List<Modifier> accumulator) {
        String name = (String) args[i];
        int nArguments = countArguments(name);
        if (nArguments == NOT_A_METHOD) {
            if (i + 1 >= args.length) {
                throw new IndexOutOfBoundsException("Argument #" + i + " is a field reference, but it is the last argument!");
            }
            accumulator.add(new FieldMod(name, args[++i]));
        } else {
            name = name.substring(0, name.lastIndexOf("("));
            final int STOP_AT = i + nArguments;
            if (STOP_AT >= args.length) {
                throw new IndexOutOfBoundsException("Argument #" + i + " references a method that would take " + nArguments + " arguments, but not enough arguments were provided!");
            }
            ArrayList<Object> arguments = new ArrayList<>(nArguments);
            while (i < STOP_AT) {
                arguments.add(args[++i]);
            }
            accumulator.add(new MethodMod(name, arguments.toArray()));
        }
        return i;
    }

    /**
     * Check if a string represents a method
     * @param str a string that may content a method representation
     * @return the number of arguments that the method takes. -1 (MixedMultiMod.NOT_A_METHOD) if it is not a method.
     */
    protected int countArguments(String str) {
        if (str.endsWith(")")) {
            String nArguments = str.substring(str.lastIndexOf("(") + 1, str.length() - 1);
            return nArguments.length() == 0 ? 1 : Integer.parseInt(nArguments);
        }
        return NOT_A_METHOD;
    }

    @Override
    public void apply(Object target) {
        for (Modifier modifier : modifiers) {
            modifier.apply(target);
        }
    }
}
