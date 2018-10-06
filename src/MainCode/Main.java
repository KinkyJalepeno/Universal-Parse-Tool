package MainCode;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends Application {

    private String filePath;
    private Boolean isPathValid = false;

    private TextArea textArea;
    private final static String url = "jdbc:sqlite:cdrStore.db";
    private static Connection conn = null;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dave's Grid Experiment");


        Button browseButton = new Button("Browse");
        Button parseButton = new Button("Parse-it");

        textArea = new TextArea();
        textArea.setPrefHeight(300);

        TextField filePathField = new TextField();
        filePathField.setPrefSize(300, 28);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(15);
        gridPane.setVgap(20);

        //allow textarea and textfield to expand if window re-sized
        textArea.prefHeightProperty().bind(gridPane.heightProperty());
        textArea.prefWidthProperty().bind(gridPane.widthProperty());
        filePathField.prefWidthProperty().bind(gridPane.widthProperty());

        gridPane.add(browseButton, 0, 0, 1, 1);
        gridPane.add(filePathField, 1, 0, 4, 1);
        gridPane.add(parseButton, 0, 1, 1, 1);
        gridPane.add(textArea, 0, 2, 5, 1);

        Scene scene = new Scene(gridPane, 390, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        browseButton.setOnAction((event -> chooseFile(filePathField)));

        parseButton.setOnAction((event -> checkFilePath()));

        connectToDatabase();

    }

    private void connectToDatabase() {

        try{

            conn = DriverManager.getConnection(url);
            textArea.setText("Connection to DB established");

        }catch(SQLException e){
            textArea.setText("Unable to connect to DB");
            System.out.println(e.getMessage());
        }
    }

    private void chooseFile(TextField filePathField) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CDR Files", "*.log",
                "*.txt", "*.tmp", "*.dat"));

        fileChooser.setTitle("Select CDR File:");
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {

            filePath = file.getAbsolutePath();
            filePathField.setText(filePath);


            isPathValid = true;
        }
    }

    private void checkFilePath() {

        if (!isPathValid) {
            textArea.appendText("You must select a file to parse ! \n");

        } else {
            getCdrType(filePath);
        }

    }

    private void getCdrType(String filePath) {

        if (filePath.contains(".log")) {
            System.out.println("This is a Hypermedia CDR");
        } else if (filePath.contains(".txt")) {
            System.out.println("This is a 2N CDR");
        } else if (filePath.contains("*.tmp") || filePath.contains("*.dat")) {
            System.out.println("This is a Quescom CDR");
        } else {
            System.out.println("I don't recognise that file type");
        }

    }


    public static void main(String[] args) {
        launch(args);
    }
}
