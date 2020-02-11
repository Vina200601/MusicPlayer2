 package com.example.musicplayer2;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

 public class MainActivity extends AppCompatActivity {

     ListView Mymusic;
     String[] items;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

         Mymusic = (ListView) findViewById(R.id.mymusic);


         rutaimePermision();

     }


     public void rutaimePermision() {

         Dexter.withActivity(this)
                 .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                 .withListener(new PermissionListener() {
                     @Override
                     public void onPermissionGranted(PermissionGrantedResponse response) {

                         display();

                     }

                     @Override
                     public void onPermissionDenied(PermissionDeniedResponse response) {

                     }

                     @Override
                     public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                         token.cancelPermissionRequest();

                     }
                 }).check();
     }

     public ArrayList<File> findsong (File file){


         ArrayList<File> arrayList= new ArrayList<>();

         File[] files= file.listFiles();

         for (File singlefile: files){

             if(singlefile.isDirectory() && !singlefile.isHidden()){

                 arrayList.addAll(findsong(singlefile));
             }
             else{
                 if(singlefile.getName().endsWith(".mp3") ||
                 singlefile.getName().endsWith(".wav")) {


                     arrayList.add(singlefile);

                 }
             }


         }

         return arrayList;
     }



     void display(){

         final ArrayList<File> mysongs =findsong(Environment.getExternalStorageDirectory());


         items=new  String[mysongs.size()];

         for (int i=0;i<mysongs.size();i++){


             items[i]=mysongs.get(i).getName().toString().replace(".mp3","").replace(".vaw","");


         }

         ArrayAdapter<String> myAdafter =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
         Mymusic.setAdapter(myAdafter);



         Mymusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



                 String songName =Mymusic.getItemAtPosition(i).toString();

                 startActivity(new Intent(getApplicationContext(),PlayerActivity.class)
                 .putExtra("songs",mysongs).putExtra("songname",songName)
                 .putExtra("pos",i));



             }
         });

     }

 }


