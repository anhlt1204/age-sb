package com.edso.checkage.model;

import com.edso.checkage.service.CheckService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class Ubnd extends Thread{

    private final ReentrantLock lock;

    public Ubnd(ReentrantLock lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        System.out.println("u start");
        lock.lock();
        try {
            sleep(1);
            if (checkAge(readFile())) {
                CheckService.count++;
                if (CheckService.count >= 2) {
                    System.out.println("u count" + CheckService.count);
                    CheckService.executor.shutdownNow();
                    CheckService.executor = Executors.newFixedThreadPool(3);
                    System.out.println("u shutdown");
                }
            }
            CheckService.countT++;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        System.out.println("u stop");
    }

    private Date readFile() throws IOException {
        Date birthday = new Date();
        FileReader frd;
        BufferedReader bufR = null;

        try {
            frd = new FileReader("src/main/resources/file/ubnd.txt");
            bufR = new BufferedReader(frd);
            String line;
            while ((line = bufR.readLine()) != null)
            {
                birthday =new SimpleDateFormat("dd/MM/yyyy").parse(line);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                if(bufR != null ) {
                    bufR.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return birthday;

    }

    private boolean checkAge(Date birthday) {
        Date date = new Date();
        long diff = date.getTime() - birthday.getTime();
        long age = diff / (1000 * 60 * 60 * 24)/ 365;
        return age == 21;
    }
}

