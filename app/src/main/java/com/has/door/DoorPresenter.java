package com.has.door;

import android.os.AsyncTask;
import android.util.Log;

import com.has.data.ClientSocket;

/**
 * Created by YoungWon on 2016-02-01.
 */
public class DoorPresenter {
    String sResult;
    DoorView view;
    DoorPresenter(DoorView view){
        this.view = view;
    }
    public void btnClickedWithCommand(final String command){
        new AsyncTask<Integer, Integer, Integer>() {
            @Override
            protected Integer doInBackground(Integer... params) {
                sResult = ClientSocket.getInstance().getServerRequest(command);
                if(sResult!=null)
                    Log.i("TAG", sResult);
                return null;
            }
            @Override
            protected void onPostExecute(Integer result) {
                view.displayToastMessage("Send Complete");
            }
        }.execute();

    }
}
