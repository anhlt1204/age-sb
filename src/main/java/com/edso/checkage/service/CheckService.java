package com.edso.checkage.service;

import com.edso.checkage.model.Dad;
import com.edso.checkage.model.Mom;
import com.edso.checkage.model.Ubnd;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class CheckService {
    private static final ReentrantLock lock = new ReentrantLock();
    public static int count = 0;
    public static int countT = 0;
    public static ExecutorService executor = Executors.newFixedThreadPool(3);

    public boolean runCheck() {
        Dad dad = new Dad(lock);
        Mom mom = new Mom(lock);
        Ubnd ubnd = new Ubnd(lock);

        executor.execute(dad);
        executor.execute(mom);
        executor.execute(ubnd);

        executor.shutdown();

//        try {
//            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        System.out.println("==>" + count);
        boolean checkAge = false;
        while (countT < 3) {
            if (count >= 2) {
                checkAge = true;
            }
        }
        countT = 0;
        count = 0;

        write(checkAge);
        log.info("Check success");
        return checkAge;
    }

    public static void write(boolean check) {
        try {
            FileWriter fw = new FileWriter("result.txt");
            fw.write(String.valueOf(check));
            fw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Success...");
    }
}
