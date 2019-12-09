package io.pronab.simpletransfer;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static io.pronab.simpletransfer.LittTransferUtil.getURI;
import static io.pronab.simpletransfer.LittTransferUtil.getUUID;
import static io.pronab.simpletransfer.LittTransferUtil.uploadtos3;

public class MainActivity extends AppCompatActivity {

    Context littcontext ;
    Button btnUp;
    Button btnDown;
    Button gUUID;
    EditText mtype;
    TextView surl;
    String  aguid ;
    String  aURI ;
    String  aType;
    File imagefile, videofile, soundfile, profilefile;
    InputStream inputStream = null;


/* end */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnUp= (Button) findViewById(R.id.toS3);
        btnDown= (Button) findViewById(R.id.fromS3);
        gUUID = (Button) findViewById(R.id.genID);
        mtype =(EditText) findViewById(R.id.Mtype); // P , I , V , A
        surl = (TextView) findViewById(R.id.S3URL);
//
        //  Read a profile,  video , audio and image file and store the locations
        // costruct the Asuccess and AFailure call back in each case , finally invoke the
        //upload

        littcontext = getApplicationContext();
        LittTransferUtil.filelocation ="" ;

// 1 . call backs from docfile
        final ASuccess docSuccess = new ASuccess() {
            @Override
            public void successExecute() {
                Toast.makeText(littcontext,"Doc File Success",Toast.LENGTH_LONG);
            }
        };
        final AFailure docFailure = new AFailure() {
            @Override
            public void failureExecute() {
                Toast.makeText(littcontext,"Doc File Failed upload",Toast.LENGTH_LONG);
            }

        };
        final AError docError = new AError() {
            @Override
            public void errorExecute() {
                Toast.makeText(littcontext,"Doc File error Occurred",Toast.LENGTH_LONG);
            }

        };
        //TYpes are :video , audio, image, profile,

        AssetManager am = getAssets();



        try {
            inputStream = am.open("up.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }

        profilefile = createFileFromInputStream(inputStream);

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

        imagefile = createFileFromInputStream(inputStream);

        try {
            inputStream = am.open("up.mp4");
        } catch (IOException e) {
            e.printStackTrace();
        }

        videofile = createFileFromInputStream(inputStream);







        gUUID.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        aguid =  getUUID();
                                        aType =  mtype.getText().toString();
                                        aURI =   getURI(aType);
                                        LittTransferUtil.filelocation = aURI;

                                    }
                                }) ;
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if (aType.equalsIgnoreCase("P")) {

                LittTransferUtil.uploadtos3(profilefile,"P",docSuccess,docFailure,docError);
            }
        } });
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }) ;



/


        if (docfile  != null ) {
         //   public static   void uploadtos3(final File file, String utype,
          //          String uuid , final ASuccess asuccess,
          //  AFailure afailure)


            uploadtos3(   docfile,"provider", getUUID(),docSuccess,docFailure,docError); }
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


}

