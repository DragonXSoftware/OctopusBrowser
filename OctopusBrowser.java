import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.File;

public class OctopusBrowser extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a WebView and get its engine
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();

        // Create navigation controls
        Button backBtn = new Button("←");
        backBtn.setOnAction(e -> engine.executeScript("history.back()"));

        Button forwardBtn = new Button("→");
        forwardBtn.setOnAction(e -> engine.executeScript("history.forward()"));

        Button reloadBtn = new Button("⟳");
        reloadBtn.setOnAction(e -> engine.reload());

        // Address bar for manual URL entry
        TextField urlField = new TextField();
        urlField.setPrefWidth(600);
        urlField.setOnAction(e -> {
            String url = urlField.getText();
            // Ensure the URL starts with http:// or https://
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
            }
            engine.load(url);
        });

        // Create a toolbar and add the navigation controls
        ToolBar toolBar = new ToolBar(backBtn, forwardBtn, reloadBtn, urlField);

        // Update the address bar as the location changes
        engine.locationProperty().addListener((obs, oldLoc, newLoc) -> urlField.setText(newLoc));

        // Layout: toolbar at the top and web view in the center
        BorderPane root = new BorderPane();
        root.setTop(toolBar);
        root.setCenter(webView);

        Scene scene = new Scene(root, 1280, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Octopus Browser");
        primaryStage.show();

        // Load the local homepage (search.html) as the default page.
        File searchPage = new File("search.html");
        engine.load(searchPage.toURI().toString());
    }

    public static void main(String[] args) {
        launch(args);
    }
}