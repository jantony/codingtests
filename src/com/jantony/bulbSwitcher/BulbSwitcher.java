package com.jantony.bulbSwitcher;


public class BulbSwitcher {

    public int bulbSwitch(int n) {
        if (n == 0) return 0;
        if (n == 1) return 1;

        //find num of perfect squares
        int count = 1;
        for (int i = 2; i * i <= n; i++) {
            if (i * i <= n) count++;
        }
        return count;
    }

    public int bulbSwitch2(int n) {
        return (int) Math.sqrt(n);
    }

    public static void main(String[] args) {
        System.out.println("\nNumber on : " + new BulbSwitcher().bulbSwitch2(8));
    }
}
