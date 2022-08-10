package edu.eci.arsw.blacklistvalidator;

public class SearchThread extends Thread {
    public void run() {
        System.out.println("thread is running...");
    }

    public static void main(String[] args) {
        SearchThread obj = new SearchThread();
        obj.start();
    }
}
