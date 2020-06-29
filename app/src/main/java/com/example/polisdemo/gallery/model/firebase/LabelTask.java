package com.example.polisdemo.gallery.model.firebase;

import com.example.polisdemo.gallery.model.db.FireBaseDBService;
import com.example.polisdemo.gallery.model.db.LabelEntity;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LabelTask {
    private final FireBaseLabeler labeler;
    private final FireBaseDBService dbService;
    private int count;
    private int all;
    private final int notifyAfter;
    private final Lock lock = new ReentrantLock();
    private final Runnable notifyThatLabeled;

    public LabelTask(FireBaseLabeler labeler, FireBaseDBService dbService, int notifyAfter, Runnable notifyThatLabeled) {
        this.labeler = labeler;
        this.dbService = dbService;
        this.notifyAfter = notifyAfter;
        this.notifyThatLabeled = notifyThatLabeled;
        this.count = 0;
        this.all = 0;
    }


    public void execute() {
        CompletableFuture.runAsync(() -> {
            System.out.println("Execute");

            List<CompletableFuture<List<LabelEntity>>> future = labeler.getLabelV3();
            future.forEach(f -> {
                f.thenAccept(l -> {
                    lock.lock();
                    try {
                        all++;
                        System.out.println(all);
                        dbService.putAllEntities(l);
                        count += l.size();
                        if (count > notifyAfter) {
                            notifyThatLabeled.run();
                            count = 0;
                        }
                    } finally {
                        lock.unlock();
                    }
                    if (all == future.size()) {
                        notifyThatLabeled.run();
                    }
                });
            });

            System.out.println("Execute end");
        });

    }
}
