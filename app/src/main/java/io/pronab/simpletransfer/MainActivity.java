package io.pronab.simpletransfer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    /*    AmazonS3 s3Client = new AmazonS3Client(new BasicSessionCredentials(awsAccessKey, awsSecretKey, sessionToken));
        s3Client.setRegion(Region.getRegion(Regions.US_WEST_2));
        TransferUtility transferUtility = new TransferUtility(s3Client, context);
        TransferObserver transferObserver = transferUtility.upload(bucketName, pathToStore, file, CannedAccessControlList.PublicRead);
 */
    }

    public static void uploadtos3 (final Context context, final File file) {

        if(file !=null){
            CognitoCachingCredentialsProvider credentialsProvider;
            credentialsProvider = new CognitoCachingCredentialsProvider(
                    context,
                    "your identity pool id", // Identity Pool ID
                    Regions.AP_SOUTH_1 // Region
            );

            AmazonS3 s3 = new AmazonS3Client(credentialsProvider);


            TransferUtility transferUtility = new TransferUtility(s3, context);
            final TransferObserver observer = transferUtility.upload(
                    "bucket name",  //this is the bucket name on S3
                    file.getName(),
                    file,
                    CannedAccessControlList.PublicRead //to make the file public
            );
            observer.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    if (state.equals(TransferState.COMPLETED)) {
                    } else if (state.equals(TransferState.FAILED)) {
                        Toast.makeText(context,"Failed to upload",Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

                }

                @Override
                public void onError(int id, Exception ex) {

                }
            });
        }
    }


}
