package com.sb.factorium;

import com.sb.factorium.reflection.ReflectionUtil;

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
    private final ClassDistanceComparator comparator;
    private final Class<?> target;

    public GeneratorComparator(Class<?> target) {
        this.comparator = new ClassDistanceComparator(target, false);
        this.target = target;
    }

    @Override
    public int compare(Generator<?> left, Generator<?> right) {
        GeneratorInfo leftInfo = left.getClass().getAnnotation(GeneratorInfo.class);
        boolean isLeftDefault = leftInfo != null && leftInfo.isDefault() && target.equals(leftInfo.target());
        GeneratorInfo rightInfo = right.getClass().getAnnotation(GeneratorInfo.class);
        boolean isRightDefault = rightInfo != null && rightInfo.isDefault() && target.equals(rightInfo.target());

        if (isLeftDefault == isRightDefault) {
            if (!isLeftDefault) {
                /* If none of them are explicitly the default one for a type, check if one is a nested class within the other */
                int enclosingOne = ReflectionUtil.checkEnclosing(left.getClass(), right.getClass());
                if (enclosingOne != 0)
                    return enclosingOne;
            }

            return comparator.compare(MetaGeneratorUtil.returnType(left), MetaGeneratorUtil.returnType(right));
        } else if (isLeftDefault) {
            return -1;
        } else {
            return 1;
        }
    }
}
