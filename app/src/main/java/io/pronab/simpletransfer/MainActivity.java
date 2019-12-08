package io.pronab.simpletransfer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.internal.Constants;
import com.amazonaws.services.s3.model.CannedAccessControlList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
/*class constants */
// We only need one instance of the clients and credentials provider
private static AmazonS3Client sS3Client;
    private static CognitoCachingCredentialsProvider sCredProvider;
    private static TransferUtility sTransferUtility;
    AWSCredentialsProvider credentialsProvider;

/* end */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    /*    AmazonS3 s3Client = new AmazonS3Client(new BasicSessionCredentials(awsAccessKey, awsSecretKey, sessionToken));
        s3Client.setRegion(Region.getRegion(Regions.US_WEST_2));
        TransferUtility transferUtility = new TransferUtility(s3Client, context);
        TransferObserver transferObserver = transferUtility.upload(bucketName, pathToStore, file, CannedAccessControlList.PublicRead);
 */
        credentialsProvider = Util.getCredProvider(getApplicationContext());



        AssetManager am = getAssets();
        InputStream inputStream = null;
        try {
            inputStream = am.open("up.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        File wf = createFileFromInputStream(inputStream);
      if (wf  != null ) {
     uploadtos3(this,   wf);}
      else Toast.makeText(this,"No file",Toast.LENGTH_LONG);
    }
        private File createFileFromInputStream(InputStream inputStream) {
           /////

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

                        byte[] data = new byte[2048];
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

    public   void uploadtos3(final Context context, final File file) {

        if(file !=null){



            AmazonS3 s3 = new AmazonS3Client(credentialsProvider);
           // var s3Client = new AmazonS3Client(credentials,region);

            TransferUtility transferUtility = new TransferUtility(s3, context);

            final TransferObserver observer = transferUtility.upload(
                    "litt-ap-southeast-2-dev",  //this is the bucket name on S3
                    file.getName(),
                    file,
                    CannedAccessControlList.PublicRead //to make the file public
            );
            observer.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    Log.d("S3tran0-1:", state.name()+":" + id);
                    if (state.equals(TransferState.COMPLETED)) {
                        Toast.makeText(context,"Success !! upload",Toast.LENGTH_LONG).show();
                    } else if (state.equals(TransferState.FAILED)) {
                        Toast.makeText(context,"Failed to upload",Toast.LENGTH_LONG).show();

                    }

                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

                }

                @Override
                public void onError(int id, Exception ex) {
                    Toast.makeText(context,"Failed " + ex.getMessage(),Toast.LENGTH_LONG).show();
                    Log.d("S3tran0-3:",  ex.getMessage());

                }
            });
        }
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

