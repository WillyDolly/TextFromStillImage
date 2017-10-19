package com.popland.pop.textfromstillimage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class MainActivity extends AppCompatActivity {
Button btn;
    TextView tv;
    ImageView iv;
    TextRecognizer teReco;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView)findViewById(R.id.iv);
        tv = (TextView)findViewById(R.id.tv);
        btn = (Button)findViewById(R.id.btn);

         teReco = new TextRecognizer.Builder(this).build();
        if(!teReco.isOperational()){
            Log.i("tereco","library not downloaded");
        }else{
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);
            }
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i,1);
                }
            });
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
//            if(ActivityCompat.checkSelfPermission(this,"Manifest.permission.CAMERA")!= PackageManager.PERMISSION_GRANTED)
//                return;
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null){
            Bitmap bm = (Bitmap) data.getExtras().get("data");
            iv.setImageBitmap(bm);
            Frame frame = new Frame.Builder().setBitmap(bm).build();
            SparseArray<TextBlock> texts = teReco.detect(frame);
            StringBuilder builder = new StringBuilder();
            for(int i=0;i<texts.size();i++){
                TextBlock tb = texts.valueAt(i);
                builder.append(tb.getValue());
                builder.append("\n");
            }
            tv.setText(builder.toString());
        }
    }
}
