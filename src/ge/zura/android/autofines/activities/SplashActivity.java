//package ge.zura.android.autofines.activities;
//
//import ge.zuraba.android.autofines.R;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//
//public class SplashActivity extends Activity {
//	
//	MediaPlayer policeSound;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.splash_screen);
//		policeSound = MediaPlayer.create(SplashActivity.this, R.raw.police_sound);
//		policeSound.start();
//		Thread timer = new Thread() {
//			public void run(){
//				try{
//					sleep(4000);
//				} catch(InterruptedException ie){
//					ie.printStackTrace();
//				} finally {
//					Intent intent = new Intent(SplashActivity.this, VideoPenaltiesActivity.class);
//					startActivity(intent);
//				}
//			}
//		};
//		timer.start();
//	}
//
//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//		policeSound.release();
//		finish();
//	}
//}
