package com.sb.factorium;

import com.sb.factorium.iterators.IteratorUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * Implement the Modifier interface by modifying a field directly.
 * <p>
 * Supports nested targets by separating each field name with a dot.
 * <p>
 * e.g.: (on an Address) "city.nCitizens"
 */
public class FieldMod implements Modifier {
    public static final char ALL_SYMBOL = '*';
    private static final int ALL_CHILDREN = -1;

    /**
     * Describe the type of operation to be done on an indexed path segment.
     */
    private enum OperationType {REPLACE, SET_SUBCOMPONENT}

    private final String[] nestedFields;
    private final Object value;

    public FieldMod(String field, Object value) {
        this.value = value;
        this.nestedFields = field.split("\\.");
    }

    private boolean isIndexed(String pathSegment) {
        return pathSegment.endsWith("]");
    }

    /**
     * Extract the index that must be extracted from this field.
     * Example : list[4] -> 4
     * Example 2: list[0] -> 0
     * Example 3: list[*] -> -1 (apply to all children)
     *
     * @param indexedField name of the field with "[number]" or "[*]"
     * @return the index at which to continue, or -1 if for all children
     */
    private int extractIndex(String indexedField) {
        String indexer = indexedField.substring(indexedField.indexOf('[') + 1, indexedField.length() - 1);
        if (indexer.length() == 0) {
            throw new IllegalArgumentException("Empty indexer in " + indexedField);
        }
        if (indexer.length() == 1 && indexer.charAt(0) == ALL_SYMBOL)
            return ALL_CHILDREN;
        return Integer.parseInt(indexer);
    }

    private void recursiveApply(Object target, int startAt) throws NoSuchFieldException, IllegalAccessException {
        String field = nestedFields[startAt];
        if (startAt != nestedFields.length - 1) {
                /*
                - If it is a field name, load it and keep going
                - If it is a simple index, load it and keep going
                - Else:
                    - If there are more path segments:  call recursiveApply with the rest of the array on each item of the current collection
                    - Else: Set all elements of the iterable to value
                */
            for (int i = startAt; i < nestedFields.length - 1; i++) {
                field = nestedFields[i];
                if (!isIndexed(field)) {
                    target = getTarget(target, field);
                } else {
                    target = indexedOperation(target, field, i);
                    if (target == null)
                        return;
                }
            }
            field = nestedFields[nestedFields.length - 1];
        }
        if (isIndexed(field))
            indexedOperation(target, field, startAt);
        else
            replace(target, field);
    }

    /**
     * Apply the indexed operations on the given target.
     * @param target on which to operate the index
     * @param field the name of the current field (with the index suffix)
     * @param indexOfField the index of the field within the nestedFields array
     * @return the next target, null if all operations are completed and there is nothing left to apply
     */
    private Object indexedOperation(Object target, String field, int indexOfField) throws NoSuchFieldException, IllegalAccessException {
        target = getTarget(target, field.substring(0, field.indexOf('[')));
        int targetIndex = extractIndex(field);
        OperationType opType = operationType(indexOfField);
        if (opType == OperationType.REPLACE) {
            indexedReplace(target, targetIndex);
        } else {
            Iterator iterator = IteratorUtil.toIterator(target);
            if (targetIndex == ALL_CHILDREN) {
                while (iterator.hasNext())
                    recursiveApply(iterator.next(), indexOfField + 1);
                return null;
            } else {
                for (int j = 0; j < targetIndex; j++)
                    iterator.next();
                target = iterator.next();
            }
        }
        return target;
    }

    private void replace(Object target, String field) throws NoSuchFieldException, IllegalAccessException {
        Field toChange = target.getClass().getDeclaredField(field);
        boolean originalAccess = toChange.isAccessible();
        toChange.setAccessible(true);
        toChange.set(target, value);
        toChange.setAccessible(originalAccess);
    }

    private void indexedReplace(Object target, int index) {
        ListIterator listIterator = IteratorUtil.toListIterator(target);
        if (index == ALL_CHILDREN) {
            while (listIterator.hasNext()) {
                listIterator.next();
                listIterator.set(value);
            }
        } else {
            while (listIterator.previousIndex() != index) listIterator.next();
            listIterator.set(value);
        }
    }

    private Object getTarget(Object target, String field) throws NoSuchFieldException, IllegalAccessException {
        Field targetField = target.getClass().getDeclaredField(field);
        boolean originalAccess = targetField.isAccessible();
        targetField.setAccessible(true);
        target = targetField.get(target);
        targetField.setAccessible(originalAccess);
        return target;
    }

    @Override
    public void apply(Object target) {
        try {
            recursiveApply(target, 0);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Type " + target.getClass().getName() + " does not contain the field: " + e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Return the path to the target field.
     *
     * @return the path to the target field.
     */
    public String fullTarget() {
        return StringUtils.joinWith(".", (Object[]) nestedFields);
    }

    public Object getValue() {
        return value;
    }

    private OperationType operationType(int index) {
        return index == nestedFields.length - 1 ? OperationType.REPLACE : OperationType.SET_SUBCOMPONENT;
    }
}
