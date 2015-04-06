package com.example.jobscheduler;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class JobSchActivity extends Activity {

    public static final int NOTIF_ID = 82;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_sch);
        JobInfo jobBuilder = new JobInfo.Builder(12, new ComponentName(this, SyncJobService.class)) // JobInfo needs an integer id and the job service component
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY) // Set some description of the kind of network type your job needs to have.
                .setRequiresCharging(false) // Specify that to run this job, the device needs to be plugged in.
                .setOverrideDeadline(1000) // Set deadline which is the maximum scheduling latency, is also set, allowing your job to run even if conditions are not met.
                .build();
        final JobScheduler jobScheduler =
                (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobBuilder);
   }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_job_sch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
