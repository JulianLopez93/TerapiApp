package com.juanlopez.apimarvel;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.juanlopez.apimarvel.Model.Data;
import com.juanlopez.apimarvel.Model.Result;
import com.juanlopez.apimarvel.Model.Root;
import com.juanlopez.apimarvel.Model.Thumbnail;

import java.net.MalformedURLException;
import java.net.URL;

import cafsoft.foundation.HTTPURLResponse;
import cafsoft.foundation.URLRequest;
import cafsoft.foundation.URLSession;

public class MainActivity extends AppCompatActivity {

    final String PUBLIC_API_KEY = "7ed7c34da13b9baf08733c597d174094";
    final String PRIVATE_API_KEY = "884ab3bdff679b201896ccd88a9aa1be66a8ec0d";
    final String TS = "1111";
    final String HASH = "714c564fba719b72b04bb0cda4286f82"; //ts+clave privada + clave publica 
    final String HOSTNAME = "http://gateway.marvel.com/";
    final String SERVICE = "v1/public/characters?";
 //https://gateway.marvel.com/v1/public/characters?apikey=7ed7c34da13b9baf08733c597d174094&ts=1111&hash=714c564fba719b72b04bb0cda4286f82&name=thor
    /**
     * Inicializacion de las variables para la interfaz
     */
    private ImageView img = null;
    EditText superHeroe;
    TextView descriptionH;
    Button showInfoH = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        initViews();
        initEvents();
    }

    /**
     * Inicializacion de las variables parte grafica
     */
    public void initViews(){
        img = findViewById(R.id.imageView);
        superHeroe = findViewById(R.id.name);
        descriptionH = findViewById(R.id.description);
        showInfoH = findViewById(R.id.button);

    }

    /**
     * inicializacion de los eventos
     */
    public void initEvents(){
        showInfoH.setOnClickListener(m ->{
            getInfo();});
    }



    public void getInfo(){
        System.out.println("entra el clase");
        URL url = null;
        final String name = superHeroe.getText().toString(); //thor
        System.out.println("TTTTTTTTTTTT"+name);

        String urlSearch = HOSTNAME + SERVICE  + "apikey=" + PUBLIC_API_KEY + "&ts=" + TS  + "&hash=" + HASH + "&name=" + name;
        urlSearch = urlSearch.replace("http:","https:");

        System.out.println(urlSearch);
        try {
            url = new URL(urlSearch);
            downloadData(url);
        }catch(MalformedURLException e){
        }

    }

    public void downloadData(URL url){
        URLRequest request = new URLRequest(url);
        URLSession.getShared().dataTask(request, (data,response,error)->{
            if (error == null){
                HTTPURLResponse resp = (HTTPURLResponse) response;
                if(resp.getStatusCode() == 200){
                    String text = data.toText();
                    System.out.println(text);
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson json = gsonBuilder.create();
                    Root root = json.fromJson(text, Root.class);

                    if(root.getData().getResults().size() > 0){
                        showInfo(root.getData().getResults().get(0));
                    }
                }
            }
        }).resume();
    }

    public void showInfo(Result result){
        runOnUiThread(() -> {
            Thumbnail thumbnail = null;
            String description = "";
            String urlImage = "";

            description = result.getDescription();
            thumbnail = result.getThumbnail();
            urlImage = thumbnail.getPath() + "." +thumbnail.getExtension();
            descriptionH.setText(String.valueOf(description));
            System.out.println(descriptionH);
            getImage(urlImage);
        });
    }



    public void getImage(String urlImage){
        URL url = null;
        urlImage = urlImage.replace("http:","https:");

        try{
            url = new URL(urlImage);
            downloadImage(url);
        }catch(MalformedURLException e){}
    }

    public void downloadImage(URL url){
        URLRequest request = new URLRequest(url);
        URLSession.getShared().dataTask(request, (data, response, error)->{
            if (error == null){
                HTTPURLResponse resp = (HTTPURLResponse) response;
                if(resp.getStatusCode() == 200){
                    //Bitmap bitmap = BitmapFactory.decodeByteArray(data.toBytes() , 0, data.length());
                    showImage(dataToImage(data), img);
                }
            }
        }).resume();
    }

    public Bitmap dataToImage(cafsoft.foundation.Data data) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data.toBytes(), 0, data.length());
        return bitmap;
    }

    public void showImage(Bitmap image, ImageView imageView){
        runOnUiThread(() -> imageView.setImageBitmap(image));
    }

}