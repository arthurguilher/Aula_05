package com.example.arthur.aula_05;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.InputStream;
import java.util.ArrayList;


public class Versiculos extends Activity {

    private int capitulo;
    private String livro;
    private int contadorVersiculos;
    private String[] arrayVersiculos;
    private ListView listView;
    private ArrayList<String> listaVersiculos = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_versiculos);

        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        livro = params.getString("livro");
        capitulo = params.getInt("capitulo");
        System.out.println(livro + " " + capitulo);

        try {
            contadorVersiculos = 0;
            AssetManager assetManager = getAssets();
            InputStream is = assetManager.open("biblia.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();
            NodeList livros = doc.getElementsByTagName("b");
            System.out.println("Capítulo: ");
            for (int temp = 0; temp < livros.getLength(); temp++) {
                Node nNode = livros.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if (eElement.getAttribute("n").equalsIgnoreCase(livro)) {
                        NodeList capitulos = eElement.getChildNodes();
                        for (int i = 0; i < capitulos.getLength(); i++) {
                            Node nNode2 = capitulos.item(i);
                            if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                                Element eElement2 = (Element) nNode2;
                                //System.out.println("    Capítulo " + eElement2.getAttribute("n"));
                                NodeList versiculos = eElement2.getChildNodes();
                                if (Integer.parseInt(eElement2.getAttribute("n")) == capitulo) {
                                    for (int j = 0; j < versiculos.getLength(); j++) {
                                        Node nNode3 = versiculos.item(j);
                                        if (nNode3.getNodeType() == Node.ELEMENT_NODE) {
                                            Element eElement3 = (Element) nNode3;
                                            //arrayVersiculos[j] = eElement2.getElementsByTagName("v").item(Integer.parseInt(eElement3.getAttribute("n")) - 1).getTextContent();
                                            listaVersiculos.add(eElement2.getElementsByTagName("v").item(Integer.parseInt(eElement3.getAttribute("n")) - 1).getTextContent());
                                            contadorVersiculos++;
                                            //System.out.println("        " + eElement3.getAttribute("n") + ": " + eElement2.getElementsByTagName("v").item(Integer.parseInt(eElement3.getAttribute("n")) - 1).getTextContent());
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }

            listView = (ListView) findViewById(R.id.list);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, listaVersiculos);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_versiculos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
