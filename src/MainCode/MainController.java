package MainCode;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import java.io.File;


public class MainController {

    @FXML
    private TextField filePathField;
    @FXML
    private TextArea textArea;

    private String filePath;
    private Boolean isPathValid = false;


    @FXML
    void browseButtonPressed() {

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

    @FXML
    void parseButtonPressed() {

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


}
