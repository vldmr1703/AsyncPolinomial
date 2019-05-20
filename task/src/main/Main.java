package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        int r = 4;
        int n;
        double x = 1;
        int s = 1;
        double[] xr = new double[r];
        for (int i = 0; i < r; i++) {
            s *= 2;
        }
        n = r * s;

        //a
        xr[0] = x;
        for (int i = 1; i < r; i++) {
            xr[i] = xr[i - 1] * xr[0];
        }

        //b
        double[] a = new double[n + 1];
        for (int i = 0; i < n + 1; i++) {
            a[i] = 1;
        }
        double[] q = new double[s + 1];
        ExecutorService service = Executors.newFixedThreadPool(s);
        for (int i = 1; i < s + 1; i++) {
            int index = i;
            service.execute(() -> {
                double sum = 0;
                int k = (index - 1) * r + 1;
                for (int j = 0; j < r; j++) {
                    sum += a[k + j] * xr[j];
                }
                q[index] = sum;
            });
        }
        try {
            if (!service.awaitTermination(1, TimeUnit.SECONDS)) {
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //c
        double[] xrs = new double[s - 1];
        xrs[0] = xr[r - 1];
        for (int i = 1; i < s - 1; i++) {
            xrs[i] = xrs[i - 1] * xrs[0];
        }

        //d
        service = Executors.newFixedThreadPool(s - 1);
        for (int i = 2; i < s + 1; i++) {
            int index = i;
            service.execute(() -> q[index] *= xrs[index - 2]);
        }
        try {
            if (!service.awaitTermination(1, TimeUnit.SECONDS)) {
                service.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        q[0] = a[0];
        for (int i = 0; i < s + 1; i++) {
            System.out.println(q[i]);
        }

        //e
        List<Double> list = Arrays.stream(q)
                .boxed()
                .collect(Collectors.toList());
        System.out.println("Sum: " + algorithm(list));
    }

    private static double algorithm(List<Double> list) {
        if (list.size() == 1) {
            return list.get(0);
        }
        List<Double> newList = new ArrayList<>();
        for (int i = 0; i < list.size(); i += 2) {
            double sum = list.get(i);
            if (list.size() != i + 1) {
                sum += list.get(i + 1);
            }
            newList.add(sum);
        }
        return algorithm(newList);
    }
}
