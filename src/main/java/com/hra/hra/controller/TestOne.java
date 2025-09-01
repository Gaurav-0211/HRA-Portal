package com.hra.hra.controller;

public class TestOne {
    public static void rotateByK(int[] arr, int k){
        int num = arr.length;
        int[] ans = new int[num];
        k = k % num;
        for(int i = 0; i<num; i++){
            if(i < k){
                ans[i] = arr[num+i-k];
            }else{
                ans[i] = arr[i-k];
            }
        }
        for(int x : ans){
            System.out.print(x+" ");
        }

    }
    public static void main(String[] args) {
        int[] arr = {12, 34, 53, 17, 81, 55, 67};
        rotateByK(arr, 3);
    }
}
