package com.example.engineer_jsp_qrcode_master;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class QrcodeUtil {
	// qrcode image width and height youself can setting 
	private static final int IMAGE_HALFWIDTH = 20;
	public static final String FileSavePath = "/mnt/sdcard/DCIM/";
	/**
	 * the method create logo qrcode image
	 * @param content qrcode content
	 * @param resources resources file
	 * @throws WriterException 
	 * */
	public static Bitmap onCreateLogoQrcode(Resources resources,String content) throws WriterException{
		File file = new File(FileSavePath+"logo_qrcode.png");
		if(file.exists()){file.delete();}
		// replace your logo image
		Bitmap mBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_launcher);
		Matrix mMatrix = new Matrix();
		float x = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
		float y = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getHeight();
		mMatrix.setScale(x, y);
		mBitmap = Bitmap.createBitmap(mBitmap, 0, 0,
				mBitmap.getWidth(), mBitmap.getHeight(), mMatrix, false);
		mBitmap=onCreateBitmap(content, mBitmap);
		writeBitmap(mBitmap);
		return mBitmap;
	}
	
	public static Bitmap onCreateBitmap(String content,Bitmap mBitmap) throws WriterException {
		    Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();  
	        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  
	        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");  
	        hints.put(EncodeHintType.MARGIN, 1); 
		 
		BitMatrix matrix= new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 300, 300, hints);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int halfW = width / 2;
		int halfH = height / 2;
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
						&& y > halfH - IMAGE_HALFWIDTH
						&& y < halfH + IMAGE_HALFWIDTH) {
					pixels[y * width + x] = mBitmap.getPixel(x - halfW
							+ IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
				} else {
					if (matrix.get(x, y)) {
						pixels[y * width + x] = 0xff000000;
					}else {                             
						pixels[y * width + x] = 0xffffffff;
					}
				}

			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
	
	public static boolean writeBitmap(Bitmap b) {
		String fn = "logo_qrcode.png";
		ByteArrayOutputStream by = new ByteArrayOutputStream();
		b.compress(Bitmap.CompressFormat.PNG, 100, by);
		byte[] stream = by.toByteArray();
		return writeToSdcard(stream, FileSavePath, fn);
	}
	
	public static boolean writeToSdcard(byte[]data,String path,String fileName)
	{
		FileOutputStream fos=null;
		try {
				File filePath=new File(path);
				if(!filePath.exists())
				{
					filePath.mkdirs();
				}
				File file=new File(path+fileName);
				if (file.exists())
				{
					file.delete();
				}
				fos=new FileOutputStream(file);
				fos.write(data);
				fos.flush();
				return true;
		} catch (Exception e) {
			return false;
		}finally
		{
			try {
				if (fos!=null)
					fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static int QR_WIDTH = 200;
	private static int QR_HEIGHT = 200;
	public static Bitmap onCreateQRImage(String content)
	{
		File file = new File(FileSavePath+"qrcode.png");
		if(file.exists()){file.delete();}
		Bitmap temp = null;
		try
		{
			if (content == null || "".equals(content) || content.length() < 1)
			{
				return null;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
			int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
			for (int y = 0; y < QR_HEIGHT; y++)
			{
				for (int x = 0; x < QR_WIDTH; x++)
				{
					if (bitMatrix.get(x, y))
					{
						pixels[y * QR_WIDTH + x] = 0xff000000;
					}
					else
					{
						pixels[y * QR_WIDTH + x] = 0xffffffff;
					}
				}
			}
			Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
			ByteArrayOutputStream logoStream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG,100,logoStream);
			byte[] logoBuf = logoStream.toByteArray();
			temp = BitmapFactory.decodeByteArray(logoBuf,0,logoBuf.length);
			saveMyBitmap(temp);
		}catch (WriterException e)
			{
				e.printStackTrace();
			}
		return temp;
	}
	public static void saveMyBitmap(Bitmap mBitmap){
		 File f = new File(FileSavePath+"qrcode.png");
		 try {
		  f.createNewFile();
		 } catch (IOException e) {
		 }
		 FileOutputStream fOut = null; 
		 try {
		  fOut = new FileOutputStream(f);
		 } catch (Exception e) {
		  e.printStackTrace();
		 }
		 mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		 try {
		  fOut.flush();
		 } catch (IOException e) {
		  e.printStackTrace();
		 }
		 try {
		  fOut.close();
		 } catch (IOException e) {
		  e.printStackTrace();
		 }
		}
}
