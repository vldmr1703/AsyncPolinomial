package main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelMultiplying {
    private ExecutorService service;

    public ParallelMultiplying(int threadCount) {
        service = Executors.newFixedThreadPool(threadCount);
    }

    public double[][] scalarMultiplying(double[][] a, double[][] b) {
        int n = a.length;
        double[][] c = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int finalI = i;
                int finalJ = j;
                c[finalI][finalJ] = 0;
                for (int k = 0; k < n; k++) {
                    int finalK = k;
                    service.submit(() -> {
                        c[finalI][finalJ] = c[finalI][finalJ] + a[finalI][finalK] * b[finalK][finalJ];
                    });
                }
            }
        }
        try {
            if (!service.awaitTermination(1, TimeUnit.SECONDS)) {
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return c;
    }

    public double[][] rowMultiplying(double[][] a, double[][] b) {
        int n = a.length;
        double[][] c = new double[n][n];
        for (int i = 0; i < n; i++) {
            int finalI = i;
            service.submit(() -> {
                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < n; k++) {
                        c[finalI][j] = c[finalI][j] + a[finalI][k] * b[k][j];
                    }
                }
            });
        }
        try {
            if (!service.awaitTermination(1, TimeUnit.SECONDS)) {
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return c;
    }

    public double[][] columnMultiplying(double[][] a, double[][] b) {
        int n = a.length;
        double[][] c = new double[n][n];
        for (int i = 0; i < n; i++) {
            int finalI = i;
            for (int j = 0; j < n; j++) {
                int finalJ = j;
                service.submit(() -> {
                    for (int k = 0; k < n; k++) {
                        c[finalI][finalJ] = c[finalI][finalJ] + a[finalI][k] * b[k][finalJ];
                    }
                });
            }
        }
        try {
            if (!service.awaitTermination(1, TimeUnit.SECONDS)) {
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return c;
    }


    private double[][] getMatrix(double number, int n) {
        double[][] a = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = number;
            }
        }
        return a;
    }

    public static void main(String[] args) {
        int threadCount = 2;
        ParallelMultiplying obj = new ParallelMultiplying(threadCount);
        int n = 20;
        double[][] a = obj.getMatrix(2, n);
        double[][] b = obj.getMatrix(2, n);
        double[][] c = obj.scalarMultiplying(a, b);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(c[i][j] + " ");
            }
            System.out.println();
        }

    }
}
