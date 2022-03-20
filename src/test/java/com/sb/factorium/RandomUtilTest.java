package com.sb.factorium;

import com.github.javafaker.Faker;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class RandomUtilTest {

    private static final Faker faker = new Faker();

    /*
    Very large enum used for testing the randomness of selecting a random element in an enum.
    It has many elements to reduce the risk of accidental collisions.
     */
    private enum VeryLargeEnum {
        A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, Y, Z,
        a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, y, z,
        A1, B1, C1, D1, E1, F1, G1, H1, I1, J1, K1, L1, M1, N1, O1, P1, Q1, R1, S1, T1, U1, V1, W1, Y1, Z1,
        a1, b1, c1, d1, e1, f1, g1, h1, i1, j1, k1, l1, m1, n1, o1, p1, q1, r1, s1, t1, u1, v1, w1, y1, z1,
        A2, B2, C2, D2, E2, F2, G2, H2, I2, J2, K2, L2, M2, N2, O2, P2, Q2, R2, S2, T2, U2, V2, W2, Y2, Z2,
        a2, b2, c2, d2, e2, f2, g2, h2, i2, j2, k2, l2, m2, n2, o2, p2, q2, r2, s2, t2, u2, v2, w2, y2, z2,
        A3, B3, C3, D3, E3, F3, G3, H3, I3, J3, K3, L3, M3, N3, O3, P3, Q3, R3, S3, T3, U3, V3, W3, Y3, Z3,
        a3, b3, c3, d3, e3, f3, g3, h3, i3, j3, k3, l3, m3, n3, o3, p3, q3, r3, s3, t3, u3, v3, w3, y3, z3,
        A4, B4, C4, D4, E4, F4, G4, H4, I4, J4, K4, L4, M4, N4, O4, P4, Q4, R4, S4, T4, U4, V4, W4, Y4, Z4,
        a4, b4, c4, d4, e4, f4, g4, h4, i4, j4, k4, l4, m4, n4, o4, p4, q4, r4, s4, t4, u4, v4, w4, y4, z4,
        A5, B5, C5, D5, E5, F5, G5, H5, I5, J5, K5, L5, M5, N5, O5, P5, Q5, R5, S5, T5, U5, V5, W5, Y5, Z5,
        a5, b5, c5, d5, e5, f5, g5, h5, i5, j5, k5, l5, m5, n5, o5, p5, q5, r5, s5, t5, u5, v5, w5, y5, z5,
    }

    @Test
    public void testRandomEnum() {
        VeryLargeEnum first = RandomUtil.randomEnum(VeryLargeEnum.class);
        final double MARGIN_OF_ERROR = 0.00001;
        final int N_POSSIBILITIES = VeryLargeEnum.values().length;
        double currentMarginOfError = 1;
        while (first.equals(RandomUtil.randomEnum(VeryLargeEnum.class))) {
            currentMarginOfError /= N_POSSIBILITIES;
            if (currentMarginOfError < MARGIN_OF_ERROR)
                fail();
        }
        assertTrue(true);
    }

    @Test
    public void testRandomElement_byteArray() {
        byte[] array = new byte[127];
        for (byte i = 0; i < array.length; i++)
            array[i] = i;

        final double MARGIN_OF_ERROR = 0.00001;
        final int N_POSSIBILITIES = array.length;
        double currentMarginOfError = 1;

        byte first = RandomUtil.randomElement(array);
        while (first == RandomUtil.randomElement(array)) {
            currentMarginOfError /= N_POSSIBILITIES;
            if (currentMarginOfError < MARGIN_OF_ERROR)
                fail();
        }
        assertTrue(true);
    }

    @Test
    public void testRandomElement_charArray() {
        char[] array = new char[127];
        for (char i = 0; i < array.length; i++)
            array[i] = i;

        final double MARGIN_OF_ERROR = 0.00001;
        final int N_POSSIBILITIES = array.length;
        double currentMarginOfError = 1;

        char first = RandomUtil.randomElement(array);
        while (first == RandomUtil.randomElement(array)) {
            currentMarginOfError /= N_POSSIBILITIES;
            if (currentMarginOfError < MARGIN_OF_ERROR)
                fail();
        }
        assertTrue(true);
    }

    @Test
    public void testRandomElement_shortArray() {
        short[] array = new short[500];
        for (short i = 0; i < array.length; i++)
            array[i] = i;

        final double MARGIN_OF_ERROR = 0.00001;
        final int N_POSSIBILITIES = array.length;
        double currentMarginOfError = 1;

        short first = RandomUtil.randomElement(array);
        while (first == RandomUtil.randomElement(array)) {
            currentMarginOfError /= N_POSSIBILITIES;
            if (currentMarginOfError < MARGIN_OF_ERROR)
                fail();
        }
        assertTrue(true);
    }

    @Test
    public void testRandomElement_intArray() {
        int[] array = new int[500];
        for (int i = 0; i < array.length; i++)
            array[i] = i;

        final double MARGIN_OF_ERROR = 0.00001;
        final int N_POSSIBILITIES = array.length;
        double currentMarginOfError = 1;

        int first = RandomUtil.randomElement(array);
        while (first == RandomUtil.randomElement(array)) {
            currentMarginOfError /= N_POSSIBILITIES;
            if (currentMarginOfError < MARGIN_OF_ERROR)
                fail();
        }
        assertTrue(true);
    }

    @Test
    public void testRandomElement_longArray() {
        long[] array = new long[500];
        for (int i = 0; i < array.length; i++)
            array[i] = i;

        final double MARGIN_OF_ERROR = 0.00001;
        final int N_POSSIBILITIES = array.length;
        double currentMarginOfError = 1;

        long first = RandomUtil.randomElement(array);
        while (first == RandomUtil.randomElement(array)) {
            currentMarginOfError /= N_POSSIBILITIES;
            if (currentMarginOfError < MARGIN_OF_ERROR)
                fail();
        }
        assertTrue(true);
    }

    @Test
    public void testRandomElement_floatArray() {
        float[] array = new float[500];
        for (int i = 0; i < array.length; i++)
            array[i] = i;

        final double MARGIN_OF_ERROR = 0.00001;
        final int N_POSSIBILITIES = array.length;
        double currentMarginOfError = 1;

        float first = RandomUtil.randomElement(array);
        while (first == RandomUtil.randomElement(array)) {
            currentMarginOfError /= N_POSSIBILITIES;
            if (currentMarginOfError < MARGIN_OF_ERROR)
                fail();
        }
        assertTrue(true);
    }

    @Test
    public void testRandomElement_doubleArray() {
        double[] array = new double[500];
        for (int i = 0; i < array.length; i++)
            array[i] = i;

        final double MARGIN_OF_ERROR = 0.00001;
        final int N_POSSIBILITIES = array.length;
        double currentMarginOfError = 1;

        double first = RandomUtil.randomElement(array);
        while (first == RandomUtil.randomElement(array)) {
            currentMarginOfError /= N_POSSIBILITIES;
            if (currentMarginOfError < MARGIN_OF_ERROR)
                fail();
        }
        assertTrue(true);
    }

    private static String[] randomStringArray() {
        String[] array = new String[500];
        for (int i = 0; i < array.length; i++) {
            array[i] = faker.letterify("???????????");
        }
        return array;
    }

    @Test
    public void testRandomElement_stringArray() {
        String[] array = randomStringArray();

        final double MARGIN_OF_ERROR = 0.00001;
        final int N_POSSIBILITIES = array.length;
        double currentMarginOfError = 1;

        String first = RandomUtil.randomElement(array);
        while (first.equals(RandomUtil.randomElement(array))) {
            currentMarginOfError /= N_POSSIBILITIES;
            if (currentMarginOfError < MARGIN_OF_ERROR)
                fail();
        }
        assertTrue(true);
    }

    @Test
    public void testRandomElement_stringList() {
        List<String> list = Arrays.asList(randomStringArray());

        final double MARGIN_OF_ERROR = 0.00001;
        final int N_POSSIBILITIES = list.size();
        double currentMarginOfError = 1;

        String first = RandomUtil.randomElement(list);
        while (first.equals(RandomUtil.randomElement(list))) {
            currentMarginOfError /= N_POSSIBILITIES;
            if (currentMarginOfError < MARGIN_OF_ERROR)
                fail();
        }
        assertTrue(true);
    }

    @Test
    public void testRandomElement_stringSet() {
        String[] array = randomStringArray();
        Set<String> set = new HashSet<>(array.length);
        Collections.addAll(set, array);

        final double MARGIN_OF_ERROR = 0.00001;
        final int N_POSSIBILITIES = array.length;
        double currentMarginOfError = 1;

        String first = RandomUtil.randomElement(set);
        while (first.equals(RandomUtil.randomElement(set))) {
            currentMarginOfError /= N_POSSIBILITIES;
            if (currentMarginOfError < MARGIN_OF_ERROR)
                fail();
        }
        assertTrue(true);
    }

}