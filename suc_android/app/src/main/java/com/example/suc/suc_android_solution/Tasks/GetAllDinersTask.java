package com.example.suc.suc_android_solution.Tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.suc.suc_android_solution.Models.Diners;
import com.example.suc.suc_android_solution.Services.DinerService;

/**
 * Created by efridman on 19/11/17.
 */

public class GetAllDinersTask extends AsyncTask<String, Void, Diners> {

    DinerService dinerService;
    Context mContext;

    TaskListener taskListener;

    public GetAllDinersTask(Context context) {
        mContext = context;
        dinerService = new DinerService(context);
    }

    public void setTaskListener(TaskListener taskListenerImpl) {
        taskListener = taskListenerImpl;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Diners doInBackground(String... params) {

        try {
            Diners response = dinerService.getAllDiners(Integer.parseInt(params[0]), params[1]);
            /*try{
                URL dinerPhotoUrl = new URL(response.photos.get(0).url);
                Bitmap image = BitmapFactory.decodeStream(dinerPhotoUrl.openConnection().getInputStream());
                response.mainPhoto = image;
            }catch (Exception ex){
                //not implemented
            }*/

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Diners diners) {
        taskListener.onComplete(diners);
    }
}