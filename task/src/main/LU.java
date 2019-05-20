package main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LU {
    private int n;
    private double[][] a;
    private double[] b;
    private double[][] l;
    private double[][] u;
    private ExecutorService service;

    public LU(double[][] a, double[] b, int threadCount) {
        service = Executors.newFixedThreadPool(threadCount);
        this.a = a;
        this.b = b;
        n = b.length;
        l = new double[n][n];
        u = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) l[i][j] = 1;
                else if (i > j)
                    l[i][j] = -1;
                else if (i < j)
                    u[i][j] = -1;
            }
        }
    }

    public double[] getX() {
        double[] x = new double[n];
        double[] y = new double[n];
        for (int i = 0; i < n; i++) {
            int finalI = i;
            service.submit(() -> {
                for (int j = 0; j < n; j++) {
                    double sum;
                    if (finalI <= j) {
                        sum = 0;
                        for (int k = 0; k < finalI; k++) {
                            sum += l[finalI][k] * u[k][j];
                        }
                        u[finalI][j] = a[finalI][j] - sum;
                    } else {
                        sum = 0;
                        for (int k = 0; k < j; k++) {
                            sum += l[finalI][k] * u[k][j];
                        }
                        l[finalI][j] = 1 / u[j][j] * (a[finalI][j] - sum);
                    }
                }
            });
        }
        print(l);
        print(u);
//        double sum;
//        for (int i = 0; i < n; i++) {
//            sum = 0;
//            for (int k = 0; k < i; k++) {
//                sum += l[i][k] * y[k];
//            }
//            y[i] = b[i] - sum;
//        }
//        for (int i = n - 1; i > -1; i--) {
//            sum = 0;
//            for (int k = i + 1; k < n; k++) {
//                sum += u[i][k] * x[k];
//            }
//            x[i] = 1 / u[i][i] * (y[i] - sum);
//        }
        return x;
    }

    public void print(double[][] a) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(a[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static double[][] getMatrix(double number, int n) {
        double[][] a = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = 1;
            }
            a[i][i] = number;
        }
        return a;
    }

    private static double[] getVector(double number, int n) {
        double[] b = new double[n];
        for (int i = 0; i < n; i++) {
            b[i] = number;
        }
        return b;
    }

    public static void main(String[] args) {
        int threadCount = 2;
        int n = 4;
        double[][] a = getMatrix(2, n);
        double[] b = getVector(2, n);
        LU obj = new LU(a, b, threadCount);
        double[] x = obj.getX();
    }
}