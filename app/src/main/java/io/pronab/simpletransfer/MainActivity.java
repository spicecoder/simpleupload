package io.pronab.simpletransfer;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static io.pronab.simpletransfer.LittTransferUtil.uploadtos3;

public class MainActivity extends AppCompatActivity {

    Context littcontext ;

/* end */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  Read a video , audio and picture file and store the locations
      // costruct the Asuccess and AFailure call back in each case , finally invoke the
      //upload

        littcontext = getApplicationContext();
        //TYpes are :video , audio, image, profile,

        AssetManager am = getAssets();
        InputStream inputStream = null;
        File photofile, videofile, soundfile, docfile;

        try {
            inputStream = am.open("up.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        docfile = createFileFromInputStream(inputStream);

        try {
            inputStream = am.open("up.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }

        soundfile = createFileFromInputStream(inputStream);


        try {
            inputStream = am.open("up.png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        photofile = createFileFromInputStream(inputStream);

        try {
            inputStream = am.open("up.mp4");
        } catch (IOException e) {
            e.printStackTrace();
        }

        videofile = createFileFromInputStream(inputStream);

// 1 . call backs from docfile
         ASuccess docSuccess = new ASuccess() {
             @Override
             public void successExecute() {
                 Toast.makeText(littcontext,"Doc File Success",Toast.LENGTH_LONG);
             }
         };
        AFailure docFailure = new AFailure() {
            @Override
            public void failureExecute() {
                Toast.makeText(littcontext,"Doc File Failed upload",Toast.LENGTH_LONG);
            }

        };
        AError docError = new AError() {
            @Override
            public void errorExecute() {
                Toast.makeText(littcontext,"Doc File error Occurred",Toast.LENGTH_LONG);
            }

        };



        if (docfile  != null ) {
         //   public static   void uploadtos3(final File file, String utype,
          //          String uuid , final ASuccess asuccess,
          //  AFailure afailure)


            uploadtos3(   docfile,"provider",getUUID(),docSuccess,docFailure,docError); }
        else
            Toast.makeText(this,"No file",Toast.LENGTH_LONG);
    }


//create file


        private File createFileFromInputStream(InputStream inputStream) {


            File afile = new File("data/data/io.pronab.simpletransfer/test.txt");

             if (!afile.exists()) {
                try {
                    afile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            FileOutputStream fos = null;

                    try {
                       fos = new FileOutputStream(afile);
                        final int SIZE_1GB = 1073741824;
                        byte[] data = new byte[SIZE_1GB];
                        int nbread = 0;


                        while (true) {

                            if (!((nbread = inputStream.read(data)) > -1)) break;

                            fos.write(data, 0, nbread);

                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }





            return afile;
        }



    public void startService(View view){
        Intent intent=new Intent(this,UploadService.class);
        startService(intent);
    }

    public void stopService(View view){
        Intent intent=new Intent(this,UploadService.class);
        stopService(intent);
    }
    public String  getUUID() {  return UUID.randomUUID().toString(); }


}

