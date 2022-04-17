package com.sb.factorium.beans;

public class Lottery {
    private byte[] winningDigits;

    public Lottery(byte[] winningDigits) {
        this.winningDigits = winningDigits;
    }

    public byte[] getWinningDigits() {
        return winningDigits;
    }

    public void setWinningDigits(byte[] winningDigits) {
        this.winningDigits = winningDigits;
    }
}
