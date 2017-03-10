package com.example.thecheckinapp;


import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
 
public class SpalshScreenActivity extends Activity {
	public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.mainloading);
    CountDown _tik;
	_tik=new CountDown(2000,2000,this,PlayGame.class);// It delay the screen for 1 second and after that switch to YourNextActivity
	_tik.start();
    StartAnimations();
}
private void StartAnimations() {
    Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
    anim.reset();
    LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
    l.clearAnimation();
    l.startAnimation(anim);

    anim = AnimationUtils.loadAnimation(this, R.anim.translate);
    anim.reset();
    ImageView iv = (ImageView) findViewById(R.id.logo2);
    iv.clearAnimation();
    iv.startAnimation(anim);

}


}
