package io.pronab.simpletransfer;

import android.app.IntentService;
import android.content.Intent;

public class UploadService extends IntentService {
	/*
	creates an IntentService. Invoked by your subclass's constructor.
	@param name Used to name the worker thread, important only for debugging.
	*/

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UploadService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        try {
            wait(20000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();


        }

    }


    @Override
    public int onStartCommand(Intent intent, int flag, int startId){

        return super.onStartCommand(intent, flag, startId);
    }

    @Override
    public void onDestroy(){}

}