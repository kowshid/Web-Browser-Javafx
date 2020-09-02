/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import org.w3c.dom.Document;

/**
 *
 * @author Kowshid
 */
public class webbrowser extends Application {

    Button goBtn;
    Button bookMarkBtn;
    Button historyBtn;
    Button reload;
    Button searchBtn;
    Button addBookmark;
    Button backBtn;
    Button forwardBtn;
    Button newTabBtn;
    Button doc;
    WebView webView = new WebView();
    WebEngine webEngine = webView.getEngine();
    String address;
    String look;
    TextField url = new TextField();
    TextField search = new TextField();
    //int counter;
    WebHistory history = webEngine.getHistory();
    Document document;
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Okay Browser");

        url.setPromptText("Enter Address");
        search.setPromptText("Search in Google");

        GridPane layout = new GridPane();
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setMinSize(800, 600);
        layout.setVgap(1);
        layout.setHgap(1);

//        TextField field = new TextField("Enter Address");
//        String address = null;
        goBtn = new Button("GO");
        bookMarkBtn = new Button("BOOKMARKS");
        historyBtn = new Button("HISTORY");
        searchBtn = new Button("SEARCH");
        reload = new Button("RELOAD");
        backBtn = new Button("BACK");
        forwardBtn = new Button("FORWARD");
        addBookmark = new Button("Add to Bookmark");
//        newTabBtn = new Button("NEW TAB");
        doc = new Button("PAGE");
//        layout.add(url, 0, 0);
//        layout.add(goBtn, 0, 5);
//        layout.add(bookMarkBtn, 0, 10);
//        layout.add(historyBtn, 0, 15);
//        layout.add(webView, 0, 20);
        HBox hBox1 = new HBox();
        hBox1.getChildren().setAll(url, goBtn, search, searchBtn);
        HBox.setHgrow(url, Priority.ALWAYS);

        HBox hBox2 = new HBox();
        hBox2.getChildren().setAll(backBtn, forwardBtn, reload, historyBtn, bookMarkBtn, addBookmark);
        
        VBox vBox=new VBox();
        vBox.getChildren().add(webView);
//        webView.setFontScale(.9);
        webView.maxHeight(600);
        webView.maxWidth(800);
//        webView.prefHeight(600);
//        webView.prefWidth(800);
//        webView.resize(800, 600);

        layout.add(hBox1, 0, 0);
        layout.add(hBox2, 0, 1);
        layout.add(vBox, 0, 2);

        File fHistory = new File(System.getProperty("user.dir") + "\\History.txt");
        File bookmark = new File(System.getProperty("user.dir") + "\\Bookmark.txt");

        if (!fHistory.exists()) {
            try {
                fHistory.createNewFile();
//                counter = 0;
            } catch (IOException ex) {
                Logger.getLogger(webbrowser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (!bookmark.exists()) {
            try {
                bookmark.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(webbrowser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        goBtn.setOnAction((ActionEvent e) -> {
            //webEngine.load(address.startsWith("http://"));
            address = url.getText().startsWith("http://") ? url.getText() : "http://" + url.getText();
            webEngine.load(address);
            try {
                addEntry(fHistory);
            } catch (IOException ex) {
                Logger.getLogger(webbrowser.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        searchBtn.setOnAction(e -> {
            look = search.getText();
            webEngine.load("https://www.google.com.bd/search?q=" + look);

            try {
                addEntry(fHistory);
            } catch (IOException ex) {
                Logger.getLogger(webbrowser.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
//        entryHistory(address, fHistory);
//        entryHistory(look, fHistory);
        
        
        reload.setOnAction(e -> {
            webEngine.reload();
        });

        backBtn.setOnAction(e -> {
            webEngine.executeScript("history.back()");
            try {
                addEntry(fHistory);
            } catch (IOException ex) {
                Logger.getLogger(webbrowser.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        forwardBtn.setOnAction(e -> {
            webEngine.executeScript("history.forward()");
            try {
                addEntry(fHistory);
            } catch (IOException ex) {
                Logger.getLogger(webbrowser.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        addBookmark.setOnAction(e -> {
            try {
                //File file = new File(System.getProperty("user.dir") + "\\Bookmark.txt");
                addEntry(bookmark);
            } catch (IOException ex) {
                Logger.getLogger(webbrowser.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        historyBtn.setOnAction(e -> {
//            Stage stage = new Stage();
//            BorderPane borderPane = new BorderPane();
//            ListView<URL> listView = new ListView<>();
//            URL myURL;
//            String line;
//
//            try {
//                BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(System.getProperty("user.dir") + "\\History.txt")));
//                while ((line = bufferedReader.readLine()) != null) {
//                    myURL = new URL(line);
//                    listView.getItems().add(myURL);
//                }
//
//                borderPane.setCenter(listView);
//                Scene scene = new Scene(borderPane, 600, 600);
//                stage.setScene(scene);
//                stage.show();
//            } catch (FileNotFoundException ex) {
//                Logger.getLogger(webbrowser.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (IOException ex) {
//                Logger.getLogger(webbrowser.class.getName()).log(Level.SEVERE, null, ex);
//            }
            getEntry(fHistory);
        });

        bookMarkBtn.setOnAction(e -> {
            getEntry(bookmark);
        });
        
//        doc.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent e) {
//                document.createDocumentFragment();
//                document = webEngine.getDocument();
//            }
//        });

        webEngine.getLoadWorker().stateProperty().addListener(
                new ChangeListener<State>() {
            public void changed(ObservableValue ov, State oldState, State newState) {
                if (newState == State.SUCCEEDED) {
                    primaryStage.setTitle(webEngine.getTitle());
                    url.setText(webEngine.getLocation());
                    try {
                        addEntry(fHistory);
                    } catch (IOException ex) {
                        Logger.getLogger(webbrowser.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        
//        webEngine.setOnResized(
//        new EventHandler<WebEvent<Rectangle2D>>() {
//            public void handle(WebEvent<Rectangle2D> ev) {
//                Rectangle2D r = ev.getData();
//                primaryStage.setWidth(800);
//                primaryStage.setHeight(600);
//            }
//        });

//        if(!address.matches(webEngine.getLocation())) {
//            addEntry(fHistory);
//        }

        Scene scene;
        scene = new Scene(layout, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    void addEntry(File file) throws IOException {
        FileWriter fileWriter = new FileWriter(file, true);
        //FileWriter fileWriter = new FileWriter;
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        PrintWriter printWriter = new PrintWriter(bufferedWriter);
        String add;
        add = webEngine.getLocation();
        printWriter.println(add);
        printWriter.close();
    }
    
    
    
//    void entryHistory(String string, File file) throws IOException {
//        if (!string.matches(webEngine.getLocation())) {
//            addEntry(file);
//        }
//    }

    void getEntry(File file) {
        Stage stage = new Stage();
        BorderPane borderPane = new BorderPane();
        ListView<String> listView = new ListView<>();
        String line;
        
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            while ((line = bufferedReader.readLine()) != null) {
                listView.getItems().add(line);
            }

            borderPane.setCenter(listView);
            Scene scene = new Scene(borderPane, 800, 600);
            stage.setScene(scene);
            stage.show();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(webbrowser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(webbrowser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
