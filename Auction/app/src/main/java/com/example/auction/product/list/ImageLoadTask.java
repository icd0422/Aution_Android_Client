package com.example.auction.product.list;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.net.URL;
import java.util.HashMap;

public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

    private String urlStr ;
    private ImageView imageView ;

    private static HashMap<String, Bitmap> bitmapHash = new HashMap<String, Bitmap>();

    public ImageLoadTask(String urlStr, ImageView imageView)
    {
        this.urlStr = urlStr;
        this.imageView = imageView ;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap bitmap = null ;
        try{
            if(bitmapHash.containsKey(urlStr))
            {
                return bitmapHash.get(urlStr);
            }

            URL url = new URL(urlStr);
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            bitmapHash.put(urlStr, bitmap);
        }catch (Exception e){
            e.printStackTrace();
        }

        return bitmap;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        imageView.setImageBitmap(bitmap);
        imageView.invalidate();//다시 그려주는 메서드인데 UI객체는 일부로 할 필요는 없지만 혹시나 해서 해줌
    }
}