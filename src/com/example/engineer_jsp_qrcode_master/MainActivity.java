package com.example.engineer_jsp_qrcode_master;

import com.google.zxing.WriterException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
public class MainActivity extends Activity {
    private Button create_qrcode,create_logo_qrcode;
    private ImageView qrcode_show;
    private Resources mResources;
    private Handler mHandler;
    private static final int CREATE_QRCODE = 1001;
    private static final int CREATE_LOGO_QRCODE = 1002;
    private static final int CREATE_NORMAL_QRCODE = 1003;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mResources = getResources();
		initView();
		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				int value = msg.arg1;
				switch (value) {
				case CREATE_QRCODE:
					int code = msg.arg2;
					switch (code) {
					case CREATE_NORMAL_QRCODE:
						Bitmap map1 = (Bitmap) msg.obj;
						qrcode_show.setImageBitmap(map1);
						Toast.makeText(getApplicationContext(), 
								"SavePath:"+QrcodeUtil.FileSavePath+"qrcode.png",Toast.LENGTH_LONG).show();
						break;
					case CREATE_LOGO_QRCODE:
						Bitmap map2 = (Bitmap) msg.obj;
						qrcode_show.setImageBitmap(map2);
						Toast.makeText(getApplicationContext(), 
								"SavePath:"+QrcodeUtil.FileSavePath+"logo_qrcode.png",Toast.LENGTH_LONG).show();
						break;	
					}
				break;
				
				default:
					break;
				}
			}
		};
	}
	
	private void initView(){
		qrcode_show = (ImageView) findViewById(R.id.qrcodeimg_show);
		create_qrcode = (Button) findViewById(R.id.create_qrcode);
		create_logo_qrcode = (Button) findViewById(R.id.create_logo_qrcode);
		create_qrcode.setOnClickListener(QrcodeListener);
		create_logo_qrcode.setOnClickListener(QrcodeListener);
	}
	
	private OnClickListener QrcodeListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			// qrcode image content youself can setting
			String content = "http://blog.csdn.net/jspping";
			int value = view.getId();
			switch (value) {
			case R.id.create_qrcode:
				Bitmap map_noe = QrcodeUtil.onCreateQRImage(content);
				Message msg1 = mHandler.obtainMessage();
				msg1.obj = map_noe;
				msg1.arg1 = CREATE_QRCODE;
				msg1.arg2 = CREATE_NORMAL_QRCODE;
				mHandler.sendMessage(msg1);
				break;
				
			case R.id.create_logo_qrcode:
				try {
				Bitmap map_two = QrcodeUtil.onCreateLogoQrcode(mResources, content);
				Message msg2 = mHandler.obtainMessage();
				msg2.obj = map_two;
				msg2.arg1 = CREATE_QRCODE;
				msg2.arg2 = CREATE_LOGO_QRCODE;
				mHandler.sendMessage(msg2);
				} catch (WriterException e) {
					e.printStackTrace();
				}
				break;	
			}
		}
	};
}
