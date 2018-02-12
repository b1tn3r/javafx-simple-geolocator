package sample;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Geocoding {

    public String[] getData(String url) {

        List<String> list = new ArrayList<String>();
        String latTxt = "";
        String lngTxt = "";
        try {
            URL geo = new URL(url);
            URLConnection gc = geo.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            gc.getInputStream()));
            String inputLine;

            while((inputLine = in.readLine()) != null) {
                list.add(inputLine);
            }
            in.close();

        } catch (MalformedURLException e) {
            System.out.println("URL Error");
        } catch (IOException e) {
            System.out.println("IO Error");
        }

        try {
            File file = new File("output.xml");
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter out = new BufferedWriter(fw);

            if(file.exists()) {
                file.delete();
            }
            file.createNewFile();

            for (String item : list) {
                out.write(item);
                out.newLine();
            }
            out.close();
        } catch (IOException e) {}



        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse("output.xml");
            NodeList locationList = doc.getElementsByTagName("location");
            for(int temp = 0; temp < locationList.getLength(); temp++) {
                Node loc = locationList.item(temp);
                if(loc.getNodeType() == Node.ELEMENT_NODE) {
                    Element location = (Element) loc;

                    latTxt = location.getElementsByTagName("lat").item(0).getTextContent();
                    lngTxt = location.getElementsByTagName("lng").item(0).getTextContent();
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch(SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] array = new String[2];
        array[0] = latTxt;
        array[1] = lngTxt;

        return array;
    }
}
