package com.example.jobscheduler;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by indianic on 04/04/15.
 */
public class SyncJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        new JobTask(this, getBaseContext()).execute(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private static class JobTask extends AsyncTask<JobParameters, Void, JobParameters> {
        private final JobService jobService;
        private Context mContext;
        Thread thread;
        public JobTask(JobService jobService, Context c) {
            this.mContext = c;
            this.jobService = jobService;
        }

        @Override
        protected JobParameters doInBackground(JobParameters... params) {

            //Your code goes here
            String imageUrl = "http://www.hdpaperz.com/wallpaper/original/3D-Beach-Wallpaper-HD-Download.jpg";
            // Download image
            Bitmap bitmap = downloadImage(imageUrl);

            createNotification(bitmap);

            return params[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            Log.e("Service", "Service Finished");
            jobService.jobFinished(jobParameters, false);
        }

        protected Bitmap downloadImage(String address) {
            // Convert string to URL
            URL url = getUrlFromString(address);
            // Get input stream
            InputStream in = getInputStream(url);
            // Decode bitmap
            Bitmap bitmap = decodeBitmap(in);
            // Return bitmap result
            return bitmap;
        }

        // Construct compatible notification
        private void createNotification(Bitmap bmp) {
            // Resize bitmap
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bmp, bmp.getWidth() / 5, bmp.getHeight() / 5, false);
            // Construct pending intent to serve as action for notification item
            Intent intent = new Intent(mContext, ImagePreviewActivity.class);
            intent.putExtra("bitmap", resizedBitmap);
            PendingIntent pIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
            // Create notification
            Notification noti = new Notification.Builder(mContext).setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Image Download Complete!").setContentText("Image download from IntentService has completed! Click to view!")
                    .setStyle(new Notification.BigPictureStyle().bigPicture(bmp))
                    .setContentIntent(pIntent).build();

            // Hide the notification after its selected
            noti.flags |= Notification.FLAG_AUTO_CANCEL;

            NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on.
            mNotificationManager.notify(12, noti);

            Log.e("Service", "noti called");
            // Method to stop the service

        }

        private URL getUrlFromString(String address) {
            URL url;
            try {
                url = new URL(address);
            } catch (MalformedURLException e1) {
                url = null;
            }
            return url;
        }

        private InputStream getInputStream(URL url) {
            InputStream in;
            // Open connection
            URLConnection conn;
            try {
                conn = url.openConnection();
                conn.connect();
                in = conn.getInputStream();
            } catch (IOException e) {
                in = null;
            }
            return in;
        }

        private Bitmap decodeBitmap(InputStream in) {
            Bitmap bitmap;
            try {
                // Turn response into Bitmap
                bitmap = BitmapFactory.decodeStream(in);
                // Close the input stream
                in.close();
            } catch (IOException e) {
                in = null;
                bitmap = null;
            }
            return bitmap;
        }

        private void sleep(long millis) {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}