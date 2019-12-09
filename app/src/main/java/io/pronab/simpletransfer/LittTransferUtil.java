/*
 * Copyright 2015-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package io.pronab.simpletransfer;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;

import java.io.File;

// The region in which identity pool exists
 // let identityRegion:AWSRegionType = .APSoutheast1

          // The identity pool id for this provider
 //         let identityPoolId = "ap-southeast-1:5ead1671-a36b-4089-a62a-071a03d6133d"


public class LittTransferUtil {
    public static final String TAG = Util.class.getSimpleName();
    String  identityPoolId = "ap-southeast-1:5ead1671-a36b-4089-a62a-071a03d6133d";
    String regionDefault = "ap-southeast-2";
    String regionAPSoutheast1 = "ap-southeast-1";
    String  regionAPSoutheast2 = "ap-southeast-2";
    String  regionUSWest1 = "us-west-1";
    String jpgContentType = "image/jpg";
    String  mp4ContentType = "video/mpeg4";

    String  jpgExtension = ".jpg";
    String mp4Extension = ".mp4";

   String  profileFolder = "profile";

    public AmazonS3Client getsS3Client() {
        return sS3Client;
    }

    String  imageFolder = "image";
   String videoFolder = "video";
    String  audioFolder = "audio";


    private AmazonS3Client sS3Client;
    private AWSCredentialsProvider sMobileClient;

    private static  Context context ;
    private static String pubkey  ="722c709b-806e-4404-83ce-a3d70cd55574" ;

    static AWSCredentialsProvider   credentialsProvider = Util.getCredProvider(context.getApplicationContext());
    //public LittTransferUtil(Context cc){     context  = cc ;}

    public static void setContext(Context cr ) {context = cr; }

  public static   void uploadtos3(final File file, String utype,
                                  String uuid , final ASuccess asuccess,
                                  AFailure afailure,AError aError)
{

        if(file !=null){


             String filelocation  = pubkey + "/" + utype + "/"  +uuid ;
            AmazonS3 s3 = new AmazonS3Client(credentialsProvider);
            // var s3Client = new AmazonS3Client(credentials,region);

            TransferUtility transferUtility = new TransferUtility(s3, context);

            final TransferObserver observer = transferUtility.upload(
                    "litt-ap-southeast-2-dev",  //this is the bucket name on S3
                    filelocation,
                    file,
                    CannedAccessControlList.PublicRead //to make the file public
            );
            observer.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    Log.d("S3tran0-1:", state.name()+":" + id);
                    if (state.equals(TransferState.COMPLETED)) {
                        asuccess.successExecute();
                        Toast.makeText(context,"Success -!! upload",Toast.LENGTH_LONG).show();
                    } else if (state.equals(TransferState.FAILED)) {
                        Toast.makeText(context,"Failed -- upload",Toast.LENGTH_LONG).show();

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





}
