package com.example.camerainlistview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
//来自https://github.com/sanjogshrestha/ListViewCamera
    CustomImageAdapter customImageAdapter;
    ArrayList<GetSet> getSets;
    ListView listView;

    // Temp save listItem position
    int position;

    int imageCount;
    String imageTempName;
    String[] imageFor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.captureList);
        getSets = new ArrayList<GetSet>();
        imageFor = getResources().getStringArray(R.array.imageFor);
        for (int i = 0; i < 3; i++) {

            GetSet inflate = new GetSet();
            // Global Values
            inflate.setUid(String.valueOf(i));

            inflate.setLabel("Image");
            inflate.setHaveImage(false);
            inflate.setSubtext(imageFor[i]);
            inflate.setStatus(true);

            getSets.add(inflate);
        }
        customImageAdapter = new CustomImageAdapter(getSets, MainActivity.this);
        listView.setAdapter(customImageAdapter);
    }


    /**
     * Capture Image and save into database
     */

    public void captureImage(int pos, String imageName) {
        position = pos;
        imageTempName = imageName;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 100);
    }

    /**
     * Set capture image to database and set to image preview
     *
     * @param data
     */
    private void onCaptureImageResult(Intent data) {

        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");

        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
        Uri tempUri = getImageUri(getApplicationContext(), imageBitmap, imageTempName);
        String picturePath = getRealPathFromURI(tempUri);
        customImageAdapter.setImageInItem(position, imageBitmap, picturePath);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == 100) {
                onCaptureImageResult(data);
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage, String imageName) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, imageName, null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public Bitmap convertSrcToBitmap(String imageSrc) {
        Bitmap myBitmap = null;
        File imgFile = new File(imageSrc);
        if (imgFile.exists()) {
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        return myBitmap;
    }
}
