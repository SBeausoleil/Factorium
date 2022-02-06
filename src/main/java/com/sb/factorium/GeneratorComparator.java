package com.sb.factorium;

import java.util.Comparator;

/**
 * Comparator for generators.
 *
 * First checks if one of the generators declares itself as the default comparator for the target class.
 * If none of them (or if, somehow, both) are the default generator, use a class distance comparison.
 * The default generator will always be the first one in a sorted set of generators, meaning that if you compare two
 * generators using this comparator, the one you should use for default is the "lesser ordered" of the two generators.
 */
public class GeneratorComparator implements Comparator<Generator<?>> {
    private ClassDistanceComparator comparator;

    public GeneratorComparator(ClassDistanceComparator comparator) {
        this.comparator = comparator;
    }

    @Override
    public int compare(Generator<?> left, Generator<?> right) {
        GeneratorInfo leftInfo = left.getClass().getAnnotation(GeneratorInfo.class);
        boolean isLeftDefault = leftInfo != null && leftInfo.isDefault();
        GeneratorInfo rightInfo = right.getClass().getAnnotation(GeneratorInfo.class);
        boolean isRightDefault = rightInfo != null && rightInfo.isDefault();

        if (isLeftDefault == isRightDefault) {
            return comparator.compare(MetaGeneratorUtil.returnType(left), MetaGeneratorUtil.returnType(right));
        } else if (isLeftDefault) {
            return -1;
        } else {
            return 1;
        }
    }
}
