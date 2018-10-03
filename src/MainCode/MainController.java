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

            filePathField.setText(file.getAbsolutePath());

            filePath = file.getAbsolutePath();
            isPathValid = true;
        }
    }

    @FXML
    void parseButtonPressed() {

        if (!isPathValid) {
            textArea.appendText("Oh dear gimp \n");

        } else {
            textArea.appendText(filePath + " is a valid file");
        }

    }
}
