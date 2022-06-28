package com.example.pheasant;

import android.net.Uri;
import android.os.Debug;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.*;


public class StorageManager {
    private FirebaseStorage storage;
    private StorageReference masterRef;
    private StorageReference ImageRef;
    final static String FILE_NAME = "map.pgm";
    StorageManager(){
        storage = FirebaseStorage.getInstance();
        masterRef = storage.getReference();
    }

    public void getImage(){
        masterRef.child("images/2fa942d1d424d8f97f79e9cfb85ad6f8.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("JB",true+"");
            }
        });
    }

    public StorageReference getMasterRef(){
        return masterRef;
    }
    public StorageReference getImageRef(){
        return ImageRef;
    }
}
