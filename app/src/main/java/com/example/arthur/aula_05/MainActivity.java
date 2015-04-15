package com.example.arthur.aula_05;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.InputStream;


public class MainActivity extends Activity {

    ListView listView ;
    final Context context = this;
    public static NodeList capitulos;
    Node nNode;
    NodeList livros;
    String[] arrayLivros = new String[66];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            AssetManager assetManager = getAssets();
            InputStream is = assetManager.open("biblia.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();
            livros = doc.getElementsByTagName("b");
            for (int temp = 0; temp < livros.getLength(); temp++) {
                nNode = livros.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    //System.out.println("Livro: " + eElement.getAttribute("n") + " ("+ temp +")");
                    arrayLivros[temp] = eElement.getAttribute("n");
                    capitulos = eElement.getChildNodes();
                    for (int i = 0; i < capitulos.getLength(); i++) {
                        Node nNode2 = capitulos.item(i);
                        if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement2 = (Element) nNode2;
                            //System.out.println("    Capítulo " + eElement2.getAttribute("n"));
                            NodeList versiculos = eElement2.getChildNodes();
                            for (int j = 0; j < versiculos.getLength(); j++) {
                                Node nNode3 = versiculos.item(j);
                                if (nNode3.getNodeType() == Node.ELEMENT_NODE) {
                                    Element eElement3 = (Element) nNode3;
                                    //System.out.println("        " + eElement3.getAttribute("n") + ": " + eElement2.getElementsByTagName("v").item(Integer.parseInt(eElement3.getAttribute("n"))-1).getTextContent());
                                }
                            }
                        }
                    }
                }
            }

            // Get ListView object from xml
            listView = (ListView) findViewById(R.id.list);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, arrayLivros);


            // Assign adapter to ListView
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView <?> parent, View view,
                                        int position, long id) {
                    // ListView Clicked item index
                    int itemPosition     = position;

                    // ListView Clicked item value
                    String  itemValue    = (String) listView.getItemAtPosition(position);

                    // Show Alert
                   // Toast.makeText(getApplicationContext(),
                     //       "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG)
                       //     .show();
                    int qntCapitulos = 0;
                    for (int temp = 0; temp < livros.getLength(); temp++) {
                        Node nNode = livros.item(temp);
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;
                            if (eElement.getAttribute("n").equalsIgnoreCase(itemValue)) {
                                NodeList capitulos = eElement.getChildNodes();
                                for (int i = 0; i < capitulos.getLength(); i++) {
                                    Node nNode2 = capitulos.item(i);
                                    if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                                        Element eElement2 = (Element) nNode2;
                                        qntCapitulos++;
                                        //arrayCapitulos[i] = "Capítulo " + eElement2.getAttribute("n");
                                        //System.out.println("    Capítulo " + eElement2.getAttribute("n"));

                                    }
                                }
                                break;
                            }
                        }
                    }


                    //Abrir capitulos
                    Intent intent = new Intent(context, Capitulos.class);
                    Bundle params = new Bundle();
                    //params.putString("livro", itemValue);
                    System.out.println(qntCapitulos);
                    params.putString("livro", itemValue);
                    params.putInt("capitulos", qntCapitulos);
                    intent.putExtras(params);

                    startActivity(intent);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        setContentView(R.layout.activity_main);

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
