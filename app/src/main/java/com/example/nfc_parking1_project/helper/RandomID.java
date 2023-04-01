package com.example.nfc_parking1_project.helper;


import androidx.core.util.Supplier;

public final class RandomID implements Supplier<String> {

    private int code;

    public static void main(String... args) {
        Supplier<String> generator = new RandomID();
        for (int i = 0; i < 10; i++)
            System.out.println(generator.get());
    }

    @Override
    public synchronized String get() {
        return String.format("%05d", code++);
    }
}