package ec.edu.epn.pomodoroapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class Settings_control implements Initializable {

    //Attributes
    @FXML
    private Spinner<Integer> spnPomDuration;
    @FXML
    private Spinner<Integer> spnShortBreak;
    @FXML
    private Spinner<Integer> spnLongBreak;
    @FXML
    private Spinner<Integer> spnCycles;
    @FXML
    private Button btnSaveChanges;

    //My Attributes
    Principal_control principal_control;

    SpinnerValueFactory<Integer> valFacPomDuration;
    SpinnerValueFactory<Integer> valFacShortBreak;
    SpinnerValueFactory<Integer> valFacLongBreak;
    SpinnerValueFactory<Integer> valFacCycles;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Initialize the Spinner
        valFacPomDuration = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 60);
        valFacShortBreak = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 60);
        valFacLongBreak = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 60);
        valFacCycles = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10);

    }

    // Tag methods
    public void btnDefault__OnAction() {

        //Set default values
        valFacPomDuration.setValue(25);
        valFacShortBreak.setValue(5);
        valFacLongBreak.setValue(15);
        valFacCycles.setValue(4);

        //Set the Spinner
        spnPomDuration.setValueFactory(valFacPomDuration);
        spnShortBreak.setValueFactory(valFacShortBreak);
        spnLongBreak.setValueFactory(valFacLongBreak);
        spnCycles.setValueFactory(valFacCycles);
    }

    public void btnSaveChanges__OnAction(){
        //Save the changes
        principal_control.setPomodoroDuration(spnPomDuration.getValue());
        principal_control.setShortBreakDuration(spnShortBreak.getValue());
        principal_control.setLongBreakDuration(spnLongBreak.getValue());
        principal_control.setCycles(spnCycles.getValue());

        principal_control.setMinute(spnPomDuration.getValue());
        principal_control.updateMyTime();
        principal_control.updateCycleView();

        //Close this window
        Stage stageClose = (Stage) btnSaveChanges.getScene().getWindow();
        stageClose.close();

    }

    public void getInitialValues(int pomodoroDuration, int shortBreakDuration, int longBreakDuration, int cycles) {

        //Set default values
        valFacPomDuration.setValue(pomodoroDuration);
        valFacShortBreak.setValue(shortBreakDuration);
        valFacLongBreak.setValue(longBreakDuration);
        valFacCycles.setValue(cycles);

        //Set the Spinner
        spnPomDuration.setValueFactory(valFacPomDuration);
        spnShortBreak.setValueFactory(valFacShortBreak);
        spnLongBreak.setValueFactory(valFacLongBreak);
        spnCycles.setValueFactory(valFacCycles);
    }

    //Getters and setters

    // principal_control setter
    public void setPrincipal_control(Principal_control principal_control) {
        this.principal_control = principal_control;
    }
}
