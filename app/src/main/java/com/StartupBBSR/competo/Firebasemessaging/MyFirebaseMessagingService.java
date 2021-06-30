package com.StartupBBSR.competo.Firebasemessaging;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.StartupBBSR.competo.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull @NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        getfirebasemessage(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());

    }

    public void getfirebasemessage(String title, String body)
    {

        NotificationManager notificationmanager99 = (NotificationManager)getSystemService(NotificationManager.class);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel99 = new NotificationChannel("firebasemessage","firebasenotification", NotificationManager.IMPORTANCE_HIGH);
            channel99.setDescription("this is firebase cloud messaging channel");

            notificationmanager99.createNotificationChannel(channel99);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"firebasemessage")
                .setSmallIcon(R.drawable.ic_settings)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true);

        notificationmanager99.notify(1201,builder.build());
    }
}
