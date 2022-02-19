package com.example.javafxhelpapllication;

import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;

public class ProgressBarChanger implements Runnable{

    ProgressBar progressBar;
    ScrollPane scrollPane;

    public ProgressBarChanger(ProgressBar progressBar, ScrollPane scrollPane){
        this.progressBar = progressBar;
        this.scrollPane = scrollPane;
    }

    @Override
    public void run() {
        while(true){
            progressBar.setProgress(scrollPane.getVvalue());
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
