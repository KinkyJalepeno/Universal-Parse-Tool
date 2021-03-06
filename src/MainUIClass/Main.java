package MainUIClass;

import Interfaces.QueryTableInterface;
import QueryDbTable.QueryHyperTable;
import Interfaces.ReadFileInterface;
import QueryDbTable.QueryQuescomTable;
import QueryDbTable.QueryTwonTable;
import ReadFileIntoDb.ReadHyperDataIntoDb;
import ReadFileIntoDb.ReadQuescomDataIntoDb;
import ReadFileIntoDb.ReadTwonDataIntoDb;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends Application {

    private static String filePath;
    private Boolean isPathValid = false;
    private Label recordsParsedValue;
    private Label smsSentValue;
    private Label minsUsedValue;

    private TextArea textArea;
    private final static String url = "jdbc:sqlite:C://sqlite/cdrStore.db";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dave's Universal CDR Parser");


        Button browseButton = new Button("Browse");
        Button parseButton = new Button("Parse-it");

        Label smsSent = new Label("SMS Sent:");
        smsSentValue = new Label("0");
        Label minsUsed = new Label("Total Mins Used: ");
        minsUsedValue = new Label("0");
        Label recordsParsed = new Label("Records Parsed: ");
        recordsParsedValue = new Label("0");

        smsSent.setFont(Font.font(smsSent.getFont().toString(), FontWeight.BOLD, 12));
        minsUsed.setFont(Font.font(minsUsed.getFont().toString(), FontWeight.BOLD, 12));
        recordsParsed.setFont(Font.font(minsUsed.getFont().toString(), FontWeight.BOLD, 12));


        textArea = new TextArea();
        textArea.setPrefHeight(300);

        TextField filePathField = new TextField();
        filePathField.setPrefSize(310, 28);

        smsSentValue.setMinWidth(40);


        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setVgap(10);

        //allow textarea and textfield to expand if window re-sized
        textArea.prefHeightProperty().bind(gridPane.heightProperty());
        textArea.prefWidthProperty().bind(gridPane.widthProperty());
        filePathField.prefWidthProperty().bind(gridPane.widthProperty());

        gridPane.add(browseButton, 0, 0, 1, 1);
        gridPane.add(parseButton, 0, 1, 1, 1);

        gridPane.add(recordsParsed, 0, 2, 1, 1);
        gridPane.add(recordsParsedValue, 1, 2, 1, 1);

        gridPane.add(textArea, 0, 3, 5, 1);
        gridPane.add(filePathField, 1, 0, 4, 1);

        gridPane.add(smsSent, 1, 1, 1, 1);
        gridPane.add(smsSentValue, 2, 1, 1, 1);

        gridPane.add(minsUsed, 3, 1, 1, 1);
        gridPane.add(minsUsedValue, 4, 1, 1, 1);

        Scene scene = new Scene(gridPane, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setMaxWidth(410);
        primaryStage.show();

        browseButton.setOnAction((event -> chooseFile(filePathField)));

        parseButton.setOnAction((event -> checkFilePath()));

        connectToDatabase();

    }

    private void connectToDatabase() {

        try {

            Connection conn = DriverManager.getConnection(url);
            textArea.setText("Connection to DB established \n");
            System.out.println("conn = " + conn);
            conn.close();

        } catch (SQLException e) {
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

            startHyperProcess();

        } else if (filePath.contains(".txt")) {

            startTwoNProcess();

        } else if (filePath.contains(".dat") || (filePath.contains(".tmp"))) {

            startQuescomProcess();
            //System.out.println("filePath = " + filePath);

        } else {

            System.out.println("I don't recognise that file type");
        }

    }

    private void startHyperProcess() {

        try {
            ReadFileInterface operation = new ReadHyperDataIntoDb(url);
            System.out.println(operation.initDatabase() + "\n");

            operation.getData(filePath);
            recordsParsedValue.setText(String.valueOf(ReadHyperDataIntoDb.getCount()));

            triggerHyperDataQuery();
        } catch (SQLException e) {
            System.out.println("Something went wrong..\n" + e.getMessage());

        }

    }

    private void triggerHyperDataQuery() throws SQLException {

        QueryTableInterface query = new QueryHyperTable(url);
        query.getDataFromDb(textArea);

        smsSentValue.setText(query.getTotalUsed());
        minsUsedValue.setText("0");
    }

    private void startTwoNProcess() {

        try{

            ReadFileInterface operation = new ReadTwonDataIntoDb(url);
            System.out.println(operation.initDatabase() + "\n");

            operation.getData(filePath);

            recordsParsedValue.setText(String.valueOf(ReadTwonDataIntoDb.getCount()));

            minsUsedValue.setText(String.valueOf(ReadTwonDataIntoDb.getGrandTotal()));

            triggerTwonDataQuery();

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private void triggerTwonDataQuery() throws SQLException {

        QueryTableInterface query = new QueryTwonTable(url);
        query.getDataFromDb(textArea);

        smsSentValue.setText("0");


    }

    private void startQuescomProcess() {

        try {

            ReadFileInterface operation = new ReadQuescomDataIntoDb(url);
            textArea.setText((operation.initDatabase()));

            operation.getData(filePath);

            smsSentValue.setText("0");
            triggerQuescomDataQuery();

            recordsParsedValue.setText(String.valueOf(((ReadQuescomDataIntoDb) operation).getCount()));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void triggerQuescomDataQuery() throws SQLException {

        smsSentValue.setText("0");

        QueryQuescomTable query = new QueryQuescomTable(url);
        query.getDataFromDb(textArea);

        minsUsedValue.setText(String.valueOf(query.getTotalMins()));


    }

    public static void main(String[] args) {
        launch(args);
    }
}
