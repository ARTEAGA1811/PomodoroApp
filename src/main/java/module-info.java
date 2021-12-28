module ec.edu.epn.pomodoroapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;


    opens ec.edu.epn.pomodoroapp to javafx.fxml;
    exports ec.edu.epn.pomodoroapp;
}