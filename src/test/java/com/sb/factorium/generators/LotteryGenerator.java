package com.sb.factorium.generators;

import com.sb.factorium.FakerGenerator;
import com.sb.factorium.beans.Lottery;

public class LotteryGenerator extends FakerGenerator<Lottery> {
    
    private final int N_DIGITS;

    public LotteryGenerator(int nDigits) {
        N_DIGITS = nDigits;
    }

    @Override
    protected Lottery make() {
        byte[] winningDigits = new byte[N_DIGITS];
        for (int i = 0; i < winningDigits.length; i++) {
            winningDigits[i] = (byte) faker.random().nextInt(Byte.MAX_VALUE);
        }
        return new Lottery(winningDigits);
    }

    public int getNDigit() {
        return N_DIGITS;
    }
}
