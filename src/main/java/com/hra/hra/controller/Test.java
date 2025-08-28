package com.hra.hra.controller;

public class Test {

    private static int closestNumber(int n, int m) {
        int q = n/m;

        // floor value
        int n1 = q * m;

        // ceil value:
        int n2 = (n*m)>0? (q+1)*m : (q-1)*m;

        if (Math.abs(n - n1) < Math.abs(n - n2)) {
            return n1;
        } else if (Math.abs(n - n2) < Math.abs(n - n1)) {
            return n2;
        } else {
            return Math.abs(n1) < Math.abs(n2) ? n2 : n1;
        }

    }
    public static void main(String[] args) {
        int n = -16;
        int m = 6;
        int result = closestNumber(n, m);
        System.out.println(result);
    }
}
