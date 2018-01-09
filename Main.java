
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.effect.Blend;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class Main extends Application {
    @FXML private TextField regionField, yearField;
    @FXML private ChoiceBox chooser;



    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Election Generator");
        primaryStage.setScene(new Scene(root, 300,200));
        primaryStage.show();
    }

    @FXML
    public void draw() throws Exception{
        BlendedAmerica.main(new String[] {regionField.getText(), yearField.getText()});
        System.out.println("tried to draw");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
