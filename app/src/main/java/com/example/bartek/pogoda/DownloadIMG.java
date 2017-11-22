package com.example.bartek.pogoda;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by Bartek on 2017-04-12.
 */

public class DownloadIMG extends AsyncTask <String, Void, Bitmap> {
    @Override
    protected Bitmap doInBackground(String... urls) {

        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        Log.d("err", "downIMG");
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result){
        MainActivity.icon.setImageBitmap(result);
        MainActivity.icon.setVisibility(ImageView.VISIBLE);

    }

}
