package ec.edu.epn.pomodoroapp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Principal_control implements Initializable {
    //Attributes FXML
    @FXML
    private Label lblCurrentTime;
    @FXML
    private Label lblCurrentCycle;
    @FXML
    private Label lblCurrentActivity;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnStop;

    //My Attributes
    private int minute = 0;
    private int second = 0;
    private int pomodoroDuration = 25;
    private int shortBreakDuration = 5;
    private int longBreakDuration = 15;
    private int cycles = 4;
    Timer myTime;
    private int currentCycle = 1;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myTime = new Timer(1000, runMyTime);
        //Define the time of the timer based on the settings
        this.minute = pomodoroDuration;
        updateMyTime();
        updateCycleView();

        //Disable the stop button
        btnStop.setDisable(true);

    }

    enum ActivitiesList {
        POMODORO,
        SHORT_BREAK,
        LONG_BREAK
    }
    ActivitiesList currentActivity = ActivitiesList.POMODORO;


    //Tag Methods
    public void btnStart__OnAction() {
        if(myTime.isRunning()){
            myTime.stop();
            btnStart.setText("Continue");
        }else{
            myTime.start();
            btnStart.setText("Pause");
        }

        btnStop.setDisable(false);
        updateCycleView();

    }

    public void btnStop__OnAction() {
        myTime.stop();
        minute = pomodoroDuration;
        second = 0;
        updateMyTime();

        btnStart.setText("Start");
        btnStop.setDisable(true);
        currentCycle = 1;
        updateCycleView();
        currentActivity = ActivitiesList.POMODORO;
        lblCurrentActivity.setText("Work Time!!");
    }


    public void btnSettings__OnAction() throws IOException{
        if(!myTime.isRunning() && btnStart.getText().equals("Start")){
            //load the settings window
            FXMLLoader loaderSettings = new FXMLLoader(getClass().getResource("GuiSettings.fxml"));
            Parent rootSettings = loaderSettings.load();
            //Get controller of GuiSettings.fxml
            Settings_control settings_control = loaderSettings.getController();
            //Show GuiSettings.fxml
            Stage stgSettings = new Stage();
            stgSettings.setScene(new Scene(rootSettings));
            stgSettings.setTitle("Settings");
            stgSettings.setResizable(false);

            //SEt this class to the controller of GuiSettings.fxml
            settings_control.setPrincipal_control(this);
            settings_control.getInitialValues(pomodoroDuration, shortBreakDuration, longBreakDuration, cycles);

            //Create this gui as a modal window
            stgSettings.initOwner(null);
            stgSettings.initModality(Modality.APPLICATION_MODAL);
            stgSettings.showAndWait();

        }else{
            //Show a message to the user, I use other thread because the principal one is being used by the timer.
            Thread t = new Thread(() -> {
                try {
                    JOptionPane.showMessageDialog(null, "You can't change the settings while the timer is running", "Tomato message", JOptionPane.ERROR_MESSAGE);
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
            t.start();
        }

    }



    //My Methods
    public void updateMyTime(){
        //the only thing that changes is the format of the time, just add a zero in front of single digit numbers
        String finalTextTime = ((minute < 10) ? ("0" + minute) : minute) + ":" + ((second < 10) ? ("0" + second) : second);
        //This form is used because the Thread is being used by the timer, and I can change the label only with the JavaFX thread.
        Platform.runLater(() -> lblCurrentTime.setText(finalTextTime) );

    }

    public void updateCycleView(){
        //this method updates the cycle view
        //This form is used because the Thread is being used by the timer, and I can change the label only with the JavaFX thread.
        Platform.runLater(() -> lblCurrentCycle.setText(currentCycle + "/" + cycles));

    }

    private final ActionListener runMyTime = new ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (second == 0 && minute == 0){
                myTime.stop();
                Platform.runLater(()-> btnStart.setText("Start"));
                Platform.runLater(()-> btnStop.setDisable(true));


                // here I have to define the times for the next cycle
                // I just finished a study time and the current cycle is not the last one
                if (currentActivity == ActivitiesList.POMODORO && currentCycle < cycles){
                    currentActivity = ActivitiesList.SHORT_BREAK;
                    minute = shortBreakDuration;
                    second = 0;

                    //JOptionPane.showMessageDialog(null, "Short Break Time!!!", "Information", JOptionPane.INFORMATION_MESSAGE);
                    try {
                        WindowsNotification.displayTray("Short Break Time!!!", "Now you can rest for a while");
                    } catch (AWTException ex) {
                        ex.printStackTrace();
                    }
                    Platform.runLater(()-> lblCurrentActivity.setText("Short Break Time!!"));

                    // I just finished a study time and the current cycle is the last one
                }else if (currentActivity == ActivitiesList.POMODORO && currentCycle == cycles){
                    currentActivity = ActivitiesList.LONG_BREAK;
                    minute = longBreakDuration;
                    second = 0;
                    //JOptionPane.showMessageDialog(null, "Long Break Time!!!", "Information", JOptionPane.INFORMATION_MESSAGE);
                    try {
                        WindowsNotification.displayTray("Long Break Time!!!", "You deserve a long break");
                    } catch (AWTException ex) {
                        ex.printStackTrace();
                    }
                    Platform.runLater(()-> lblCurrentActivity.setText("Long Break Time!!"));

                    // I just finished a short break time and the current cycle is not the last one
                }else if (currentActivity == ActivitiesList.SHORT_BREAK){
                    currentActivity = ActivitiesList.POMODORO;
                    minute = pomodoroDuration;
                    second = 0;
                    //JOptionPane.showMessageDialog(null, "Work Time!!!", "Information", JOptionPane.INFORMATION_MESSAGE);
                    try {
                        WindowsNotification.displayTray("Work Time!!!", "It's time to start working");
                    } catch (AWTException ex) {
                        ex.printStackTrace();
                    }
                    Platform.runLater(()-> lblCurrentActivity.setText("Work Time!!"));
                    currentCycle++;


                    // I just finished a long break time so the current cycle is the last one, I have to reset the cycle
                }else if (currentActivity == ActivitiesList.LONG_BREAK){
                    currentActivity = ActivitiesList.POMODORO;
                    minute = pomodoroDuration;
                    second = 0;
                    //JOptionPane.showMessageDialog(null, "Work Time!!", "Information", JOptionPane.INFORMATION_MESSAGE);
                    try {
                        WindowsNotification.displayTray("Work Time!!", "It's time to start working");
                    } catch (AWTException ex) {
                        ex.printStackTrace();
                    }
                    Platform.runLater(()-> lblCurrentActivity.setText("Work Time!!"));
                    currentCycle = 1;

                }
                //update the cycle view and the time
                updateMyTime();
                updateCycleView();

            }else{ //here is the normal flow of the program
                second--;
                if (second == -1) {
                    minute--;
                    second = 59;
                }

                updateMyTime();

            }
        }
    };


    //Getter and Setters
    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setPomodoroDuration(int pomodoroDuration) {
        this.pomodoroDuration = pomodoroDuration;
    }

    public void setShortBreakDuration(int shortBreakDuration) {
        this.shortBreakDuration = shortBreakDuration;
    }

    public void setLongBreakDuration(int longBreakDuration) {
        this.longBreakDuration = longBreakDuration;
    }

    public void setCycles(int cycles) {
        this.cycles = cycles;
    }
}
