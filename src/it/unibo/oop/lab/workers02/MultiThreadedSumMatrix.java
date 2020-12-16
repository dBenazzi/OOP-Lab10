package it.unibo.oop.lab.workers02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiThreadedSumMatrix implements SumMatrix {

    private final int threads;

    public MultiThreadedSumMatrix(final int numberOfThreads) {
        this.threads = numberOfThreads;
    }

    private static final class Adder extends Thread {

        private final int beginning, finish;
        private final List<Double> list;
        private double sum;

        private Adder(final List<Double> list, final int start, final int finish) {
            this.list = list;
            this.beginning = start;
            this.finish = finish;
        }

        @Override
        public void run() {
            System.out.println("new thread from " + beginning + " to " + (finish > this.list.size() ? this.list.size() : finish));
            for (int i = beginning; i < finish && i < list.size(); i++) {
                sum += list.get(i);
            }
        }

        private double getSum() {
            return sum;
        }
    }

    @Override
    public double sum(final double[][] matrix) {
        final int matrixLenght = matrix.length * matrix[0].length; 
        final int elementsNumber = matrixLenght % this.threads + matrixLenght / this.threads;
        final List<Double> list = new ArrayList<>(matrixLenght);
        final List<Adder> threadList = new ArrayList<>(this.threads);
        double sum = 0;

        for (int i = 0; i < matrix.length ; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                list.add(matrix[i][j]);
            }
        }
        for (int i = 0; i < matrixLenght; i += elementsNumber) {
            threadList.add(new Adder(list, i, i + elementsNumber));
        }
        for (final Thread elem: threadList) {
            elem.start();
        }
        for (final Adder elem: threadList) {
            try {
                elem.join();
            } catch (InterruptedException e) {
                System.err.println("thread");
                e.printStackTrace();
            }
            sum += elem.getSum();
        }
        return sum;
    }

}
