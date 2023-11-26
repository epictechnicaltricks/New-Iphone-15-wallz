package uz.applewallz.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import uz.applewallz.R;
import uz.applewallz.frags.Fragment_fav;
import uz.applewallz.frags.Fragment_home;
import uz.applewallz.frags.Fragment_wallz_store;


public class MainActivity extends  AppCompatActivity  { 
	
	
//	private ViewPager viewpager1;
	private BottomNavigationView bottomnavigation1;
	BlurView blurView;



	Fragment_home fragment1 = new Fragment_home();
	Fragment_wallz_store fragment2 = new Fragment_wallz_store();
	Fragment_fav fragment3 = new Fragment_fav();
	Fragment active = fragment1;
	final FragmentManager fm = getSupportFragmentManager();



	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);


		fm.beginTransaction().add(R.id.frame_layout, fragment3, "3").hide(fragment3).commit();
		fm.beginTransaction().add(R.id.frame_layout, fragment2, "2").hide(fragment2).commit();
		fm.beginTransaction().add(R.id.frame_layout,fragment1, "1").commit();


		initialize();
		initializeLogic();
	}
	
	private void initialize() {
		
		//viewpager1 = findViewById(R.id.viewpager1);
		bottomnavigation1 = findViewById(R.id.bottomnavigation1);



		blurView = findViewById(R.id.blurView);
		float radius = 16f;

		View decorView = getWindow().getDecorView();
		ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);

		Drawable windowBackground = decorView.getBackground();

		blurView.setupWith(rootView, new RenderScriptBlur(this)) // or RenderEffectBlur
				.setFrameClearDrawable(windowBackground) // Optional
				.setBlurRadius(radius);


		Window w = getWindow(); // in Activity's onCreate() for instance
		w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
				WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


	/*	Fragment_home fragment = new Fragment_home();
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.frame_layout, fragment, "");
		fragmentTransaction.commit();
*/

		fm.beginTransaction().hide(active).show(fragment1).commit();
		active = fragment1;


		bottomnavigation1.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				//viewpager1.setCurrentItem((int)_itemId);
				bottomnavigation1.getMenu().getItem(item.getItemId()).setChecked(true);
				switch (item.getItemId())
				{
					case 0 :


						fm.beginTransaction().hide(active).show(fragment1).commit();
						active = fragment1;


						/*FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
						fragmentTransaction.replace(R.id.frame_layout, fragment, "");

						fragmentTransaction.commit();*/

						break;

					case 1 :

						fm.beginTransaction().hide(active).show(fragment2).commit();
						active = fragment2;
/*
						FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
						fragmentTransaction1.replace(R.id.frame_layout, fragment1, "");

						fragmentTransaction1.commit();*/

						break;

					case 2 :

						fm.beginTransaction().hide(active).show(fragment3).commit();
						active = fragment3;

						/*FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
						fragmentTransaction2.replace(R.id.frame_layout, fragment2, "");

						fragmentTransaction2.commit();*/

						break;
				}

				return false;
			}
		});
	}
	
	private void initializeLogic() {


		bottomnavigation1.getMenu().add(0, 0, 0, "Home").setIcon(R.drawable.ic_home_grey);
		bottomnavigation1.getMenu().add(0, 1, 0, "Wallz Store").setIcon(R.drawable.ic_filter_hdr_grey);
		bottomnavigation1.getMenu().add(0, 2, 0, "Favs").setIcon(R.drawable.anim_settings);
		bottomnavigation1.getMenu().getItem(0).setChecked(true);
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		
		super.onActivityResult(_requestCode, _resultCode, _data);

	}
	

	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}




}
