package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.ZoomEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public TextField addressTextField;
    public TextField cityTextField;
    public TextField stateTextField;
    public TextField zipCodeTextField;
    public Button findLocationButton;
    public ComboBox<String> mapViewComboBox;
    public WebView webView;
    public Slider slider;
    public long zoom;

    public void findLocation() {
        String address = addressTextField.getText();
        String addressF = address.replace(" ", "+");
        String city = cityTextField.getText();
        String cityF = city.replace(" ", "+");
        String state = stateTextField.getText();
        String stateF = state.replace(" ", "+");
        String zip = zipCodeTextField.getText();
        String zipF = zip.replace(" ", "+");

        String url = "https://maps.googleapis.com/maps/api/geocode/xml?address=" + addressF + ",+" + cityF + ",+" + stateF + ",+" + zipF;

        Geocoding g = new Geocoding();
        String[] array = g.getData(url);
        String latTxt = array[0];
        String lngTxt = array[1];

        String apiKey = "AIzaSyC4dfPGyp2NzZP7xWdy4Yol36zbc5XU7Lc";
        zoom = Math.round(slider.getValue());
        long width = Math.round(webView.getWidth());
        long height = Math.round(webView.getHeight());

        String mapsUrl = "https://maps.googleapis.com/maps/api/staticmap?center=" + latTxt + "," + lngTxt + "&zoom=" + zoom + "" + "&size=" + width + "x" + height + "&maptype=" + mapViewComboBox.getValue() +
                         "&markers=color:red%7Clabel:A%7C" + latTxt + "," + lngTxt;
        //String iframe = "<iframe width=\"300\" height=\"300\" src=\"mapsUrl\" frameborder=\"0\" allowfullscreen><iframe>";

        WebEngine webEngine = webView.getEngine();
        webEngine.load(mapsUrl);
    }
    public void changeMapType() {
        if(!addressTextField.getText().matches("") && !cityTextField.getText().matches("") && !stateTextField.getText().matches("") && !zipCodeTextField.getText().matches("")) {
            findLocation();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mapViewComboBox.getItems().addAll("roadmap", "satellite", "hybrid", "terrain");
        slider.setShowTickMarks(true);
        slider.setMax(20);
        slider.setValue(13);
        slider.setShowTickLabels(true);

        slider.valueProperty().addListener( (v, oldValue, newValue) -> findLocation() );

    }
}
