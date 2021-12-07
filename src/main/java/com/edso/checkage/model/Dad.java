package com.edso.checkage.model;

import com.edso.checkage.service.CheckService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class Dad extends Thread{

    private final ReentrantLock lock;
    public Dad(ReentrantLock lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        System.out.println("d start");
        lock.lock();
        try {
            sleep(1000);
            if (checkAge(readFile())) {
                CheckService.count++;
                if (CheckService.count >= 2) {
                    System.out.println("d count" + CheckService.count);
                    CheckService.executor.shutdownNow();
                    CheckService.executor = Executors.newFixedThreadPool(3);
                    System.out.println("d shutdown");
                }
            }
            CheckService.countT++;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        System.out.println("d stop");
    }

    private int readFile() throws IOException {
        int age = 0;
        FileReader frd = null;
        BufferedReader bufR = null;

        try {
            frd = new FileReader("src/main/resources/file/dad.txt");
            bufR = new BufferedReader(frd);
            String line;
            while ((line = bufR.readLine()) != null)
            {
                age = Integer.parseInt(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(bufR != null ) {
                    bufR.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return age;
    }

    private boolean checkAge(int age) {
        return age == 21;
    }
}

