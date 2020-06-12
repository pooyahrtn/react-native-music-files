package com.pooyaharatian.musicfiles;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;
import android.os.Build;
import androidx.annotation.Nullable;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.graphics.Bitmap;
import android.util.Log;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.pooyaharatian.musicfiles.ReactNativeFileManager;

public class MusicFilesModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private String[] projection = { MediaStore.Audio.Media._ID, MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM };
    private int version = Build.VERSION.SDK_INT;

    private int minimumSongDuration = 0;

    public MusicFilesModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "MusicFiles";
    }

    @ReactMethod
    public void getAll(ReadableMap options, final Callback successCallback, final Callback errorCallback) {

        if (options.hasKey("minimumSongDuration") && options.getInt("minimumSongDuration") > 0) {
            minimumSongDuration = options.getInt("minimumSongDuration");
        } else {
            minimumSongDuration = 0;
        }
        getSongs(successCallback, errorCallback);
    }

    private void getSongs(final Callback successCallback, final Callback errorCallback) {
        ContentResolver musicResolver = getCurrentActivity().getContentResolver();

        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";

        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";

        if (minimumSongDuration > 0) {
            selection += " AND " + MediaStore.Audio.Media.DURATION + " >= " + minimumSongDuration;
        }

        Cursor cursor = musicResolver.query(musicUri, projection, selection, null, null);

        if (!(cursor != null && cursor.moveToFirst())) {
            errorCallback.invoke("Something get wrong with musicCursor");
        }
        if (cursor.getCount() == 0) {
            errorCallback.invoke("There is no song in device");
        }

        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            ReactNativeFileManager fcm = new ReactNativeFileManager();

            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
            int durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
            int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
            int displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
            int pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            int albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColumn);
                String artist = cursor.getString(artistColumn);
                long duration = cursor.getLong(durationColumn);
                String title = cursor.getString(titleColumn);
                String album = cursor.getString(albumColumn);
                String displayName = cursor.getString(displayNameColumn);
                String path = cursor.getString(pathColumn);
                mmr.setDataSource(path);

                if (path != null && path != "" && duration > minimumSongDuration) {

                    WritableMap trackMap = new WritableNativeMap();
                    trackMap.putString("id", String.valueOf(id));
                    trackMap.putString("path", path);
                    trackMap.putString("artist", artist);
                    trackMap.putString("title", title);
                    trackMap.putString("displayName", displayName);
                    trackMap.putString("album", album);
                    trackMap.putString("duration", String.valueOf(duration));

                    try {
                        byte[] albumImageData = mmr.getEmbeddedPicture();
                        if (albumImageData != null) {
                            Bitmap songImage = BitmapFactory.decodeByteArray(albumImageData, 0, albumImageData.length);
                            String encoded = "";

                            String pathToImg = Environment.getExternalStorageDirectory() + "/" + id + ".jpg";
                            encoded = fcm.saveImageToStorageAndGetPath(pathToImg, songImage);
                            trackMap.putString("cover", "file://" + encoded);

                        }
                    } catch (Exception e) {
                        Log.e("embedImage", e.getMessage());
                    }
                    sendEvent(reactContext, "onSongReceived", trackMap);
                }
            }
        } catch (Exception e) {
            errorCallback.invoke(e.getMessage());
        }
    }

    private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }
}
