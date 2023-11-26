package uz.applewallz;

import android.content.*;

import java.util.*;
import android.app.*;
import android.net.*;
import android.view.View;

public class Util {


		public static boolean isConnected(Context a) {
				ConnectivityManager connectivityManager = (android.net.ConnectivityManager) 
						a.getSystemService(Activity.CONNECTIVITY_SERVICE);
				NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
				return activeNetworkInfo != null && activeNetworkInfo.isConnected();
		}





		public static int getRandomNo(int _min, int _max) {
				Random random = new Random();
				return random.nextInt(_max - _min + 1) + _min;
		}

	public static String High_ImgQuality() {

		return "q=100&w=1000";
	}

	public static String Low_ImgQuality() {

		return "q=80&w=400";
	}


	public static void showAnimationName (final View view, final String AnimationName) {
		view.setTransitionName(AnimationName);
	}

	public static void setActivityAnimation (final View view, final String AnimationName, Activity ctx, final Intent _intent) {
		view.setTransitionName(AnimationName);
		android.app.ActivityOptions optionsCompat = android.app.ActivityOptions.makeSceneTransitionAnimation(ctx,
				view, AnimationName);
		ctx.startActivity(_intent, optionsCompat.toBundle());
	}


}