package uz.applewallz.frags;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import androidx.annotation.*;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.content.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

import uz.applewallz.R;
import uz.applewallz.Util;
import uz.applewallz.activity.ShowWallz;


public class Fragment_home extends  Fragment  {
	
	

	private RecyclerView recyclerview1,recyclerview2_shimmer;

	private final ArrayList<HashMap<String, Object>> home_wallz_list = new ArrayList<>();

	final String[] dummy_for_shimmer= new String[]{"", "", "","","",""};

	ShimmerFrameLayout container;
	// Shimmer effect

	ImageView menu;


    ArrayList<HashMap<String, Object>> fav_img_list = new ArrayList<>();

    SharedPreferences sf;
    SharedPreferences.Editor myEdit;
    int pos;

	Handler h;

    @NonNull
	@Override
	public View onCreateView(@NonNull LayoutInflater _inflater, @Nullable ViewGroup _container, @Nullable Bundle _savedInstanceState) {
		View _view = _inflater.inflate(R.layout.home_fragment, _container, false);
		initialize(_view);
		initializeLogic();
		return _view;
	}
	
	private void initialize(View _view) {

		h = new Handler();

		menu = _view.findViewById(R.id.menu_);

		recyclerview1 = _view.findViewById(R.id.recyclerview1);
		recyclerview2_shimmer = _view.findViewById(R.id.recyclerview2_shimmer);

		recyclerview1.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
		recyclerview2_shimmer.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));


		sf = Objects.requireNonNull(getActivity()).getSharedPreferences("MySharedPref2", Context.MODE_PRIVATE);
        myEdit = sf.edit();

		container = _view.findViewById(R.id.shimmer_view_container);

		menu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				menuOnClick();
			}
		});

	}

	public void menuOnClick() {

		new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK)
				.setTitle("Info")
				.setMessage("iPhone Wallz app: A lite and user-friendly wallpaper application offering a vast collection of high-quality wallpapers.")
				.setPositiveButton("Rate us 🌟", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						Intent inn2 = new Intent(Intent.ACTION_VIEW);
						inn2.setData(Uri.parse("https://play.google.com/store/apps/details?id="+getActivity().getPackageName()));
						startActivity(inn2);
					}
				})
				.setNeutralButton("Privacy Policy", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {

						Intent inn = new Intent(Intent.ACTION_VIEW);
						inn.setData(Uri.parse("https://www.freeprivacypolicy.com/live/edb97c58-2614-48cf-84d1-b01d5f190090"));
						startActivity(inn);
					}
				}).create().show();


	/*	PopupMenu popupMenu = new PopupMenu(getActivity(),menu);

		// Inflating popup menu from popup_menu.xml file
		popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
		popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				// Toast message on menu item clicked

				Toast.makeText(getActivity(), "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();

				return true;
			}
		});
		// Showing the popup menu
		popupMenu.setDropDownGravity(Gravity.RIGHT);
		popupMenu.show();*/

	}



	public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	ArrayList<HashMap<String, Object>> data;

	public HomeAdapter(ArrayList<HashMap<String, Object>> _array) {
		data = _array;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		//LayoutInflater inflate_ = getLayoutInflater();
		//View v = inflate_.inflate(R.layout.wall_custom_layout, , false);
		//return new ViewHolder(v);

		LayoutInflater inflater = (LayoutInflater) requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.wall_custom_layout, null);
		RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		v.setLayoutParams(lp);
		return new ViewHolder(v);


	}

	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
		View _view = viewHolder.itemView;

		final ImageView wall = _view.findViewById(R.id.wall_img);
		final CardView card = _view.findViewById(R.id.cardview1);
		final  ImageView fav = _view.findViewById(R.id.fav_btn);


		int ran= Util.getRandomNo(1, 5);
		if (ran == 1) {       wall.setBackgroundColor(0xFF89039e);
		} else if(ran  == 2){ wall.setBackgroundColor(0xFF039e72);
		}else if(ran == 3){  wall.setBackgroundColor(0xFF035b9e);
		}else if(ran == 4){  wall.setBackgroundColor(0xFF9e0d03);
		}else if(ran == 5) { wall.setBackgroundColor(0xFF46039e);
		}






		Animation slide_up = AnimationUtils.loadAnimation(getActivity(),
				R.anim.slide_up );
		card.startAnimation(slide_up);





		final String img = Objects.requireNonNull(home_wallz_list.get(i).get("image")).toString();
		Glide.with(Objects.requireNonNull(getActivity())).load(Uri.parse(img))
				.transition(withCrossFade())
				.listener(new RequestListener<Drawable>() {
					@Override
					public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
						return false;
					}

					@Override
					public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
						// If auto-start is set to false
						/*if(i==4)
						container.stopShimmer();*/
						return false;
					}
				})
				.thumbnail(0.01f).into(wall);



        //////////////////////////////////////////


		if(sf.getString("fav_data","").contains(img)) {

            fav.setImageResource(R.drawable.fav_filled);
        }
        else {
            fav.setImageResource(R.drawable.fav_outline);
        }

		card.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {


				Intent i = new Intent();
				i.setClass(getContext(), ShowWallz.class);
				i.putExtra("wallz_url",img);

				if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
					Util.setActivityAnimation(card, "shubham",getActivity(),i);

				} else {

					startActivity(i);
				}
			}
		});
        ////////////////////////////////////////


		fav.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

                HashMap<String, Object>  new_map = new HashMap<>();

                fav_img_list = new Gson().fromJson(sf.getString("fav_data",""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());

                pos = getPos_img_url(img,fav_img_list);

                    if(pos>=0)
                    {
                        fav.setImageResource(R.drawable.fav_outline);
                        fav_img_list.remove(pos);

						Toast.makeText(getContext(), "Removed", Toast.LENGTH_SHORT).show();

					/*	Snackbar.make(recyclerview1, "Removed", Snackbar.LENGTH_SHORT)
								.setAction("", new View.OnClickListener(){
									@Override
									public void onClick(View _view) {

									}
								}).show();
*/
                        Log.d("fav1",fav_img_list.size()+"\n"+ fav_img_list+"\npos -"+pos);

                    } else {

                        fav.setImageResource(R.drawable.fav_filled);
                        new_map.put("img_url",img);
						new_map.put("img_id","0");
                        fav_img_list.add(new_map);

						Vibrator vb = (Vibrator)   getActivity().getSystemService(Context.VIBRATOR_SERVICE);
						vb.vibrate(100);


						Toast.makeText(getContext(), "Added to Favs", Toast.LENGTH_SHORT).show();


						/*Snackbar.make(recyclerview1, "Added to Favs", Snackbar.LENGTH_SHORT)
								.setAction("", new View.OnClickListener(){
									@Override
									public void onClick(View _view) {

									}
								}).show();*/

                    }
                myEdit.putString("fav_data",new Gson().toJson(fav_img_list));
                myEdit.apply();


            }
		});

	}




	@Override
	public int getItemCount() {
		return data.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(View v) {
			super(v);
		}
	}

}

public class ShimmerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

		String[] data;

		public ShimmerAdapter(String[] _array) {
			data = _array;
		}

		@NonNull
		@Override
		public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {


			LayoutInflater inflater = (LayoutInflater) requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(R.layout.wall_custom_layout, null);
			RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			v.setLayoutParams(lp);
			return new ViewHolder(v);


		}

		@Override
		public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i)
		{

			View _view = viewHolder.itemView;



		}




		@Override
		public int getItemCount() {
			return data.length;
		}

		public class ViewHolder extends RecyclerView.ViewHolder {
			public ViewHolder(View v) {
				super(v);
			}
		}

	}

    int getPos_img_url(String img_url, ArrayList<HashMap<String, Object>> arrayList)
    {
        for(int x=0; x<arrayList.size(); x++)
        {
            if(Objects.requireNonNull(arrayList.get(x).get("img_url")).toString().trim().equals(img_url))
            {
                return x;
            }
        }

        return -1;
    }


	@Override
	public void onResume() {




		super.onResume();

	}

	private void initializeLogic() {



		recyclerview1.setVisibility(View.GONE);
		container.startShimmer();

		recyclerview2_shimmer.setVisibility(View.VISIBLE);


		h.postDelayed(() ->
		{
			container.stopShimmer();
			/*final Animation fade_in = AnimationUtils.loadAnimation(getActivity(),
					R.anim.fade_in);
			recyclerview1.startAnimation(fade_in);*/
			recyclerview1.setVisibility(View.VISIBLE);
			recyclerview2_shimmer.setVisibility(View.GONE);


		}, 2000);





		wall_data();

		Collections.reverse(home_wallz_list);

		if(sf.getString("fav_data","").equals(""))
		{
			myEdit.putString("fav_data","[]");
			myEdit.apply();
		}

		fav_img_list = new Gson().fromJson(sf.getString("fav_data",""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());


		recyclerview2_shimmer.setAdapter(new ShimmerAdapter(dummy_for_shimmer));
		recyclerview1.setAdapter(new HomeAdapter(home_wallz_list));
		//Collections.shuffle(home_wallz_list);
		Log.d("wall",home_wallz_list.toString());








		Log.d("fav1-resume",fav_img_list.size()+"\n"+ fav_img_list+"\npos -"+pos);

	}


	@Override
	public void onStart() {
		/*recyclerview1.setVisibility(View.GONE);
		container.startShimmer();

		recyclerview2_shimmer.setVisibility(View.VISIBLE);


		h.postDelayed(() ->
		{
			container.stopShimmer();
			*//*final Animation fade_in = AnimationUtils.loadAnimation(getActivity(),
					R.anim.fade_in);
			recyclerview1.startAnimation(fade_in);*//*
			recyclerview1.setVisibility(View.VISIBLE);
			recyclerview2_shimmer.setVisibility(View.GONE);


		}, 2000);*/
		super.onStart();
	}

	void wallpaper_map(final String wall_url_) {
		HashMap<String, Object > wallpaper_map_ = new HashMap<>();
		wallpaper_map_.put("image", wall_url_);
		home_wallz_list.add(wallpaper_map_);
	}
	
	@Override
	public void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);

	}
	






	void wall_data()
	{

		wallpaper_map("https://wallpaperaccess.com/download/iphone-11-pro-max-1159176");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-11-pro-max-1340367");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-11-pro-max-1340371");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-11-pro-max-1340386");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-11-pro-max-1249569");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-11-pro-max-1340405");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-11-pro-max-1211153");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-11-pro-max-2069891");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-11-pro-max-1648098");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-11-pro-max-2225348");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-11-pro-max-2225349"); // iphone x

		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-2095398");
       // wallpaper_map("https://wallpaperaccess.com/download/iphone-11-1146081");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-11-1146098");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-11-857926");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-11-1146126");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-11-1187605");
		//wallpaper_map("https://wallpaperaccess.com/download/iphone-11-1586565");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-11-2225348");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-11-2225349");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-11-1187709");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-11-2158768");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-11-2158764");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-11-1146086");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-11-2225370");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-11-1618273");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-11-2225372"); // iphone 11

		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-pro-max-4296884");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-pro-max-4296882");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-4296859");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-4296861");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-4296862");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-pro-max-4296866");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-pro-max-4296859");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-pro-max-1529769");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-pro-max-3036606");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-pro-max-4296889");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-pro-max-748249");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-pro-max-4296893");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-pro-max-4296896");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-pro-max-4296898");  // iphone 12 pro

		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-pro-max-2140035");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-748249");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-2095365");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-2095381");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-1269682");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-1269764");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-2095389");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-2095406");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-1269683");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-1269765");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-2095425");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-2095432");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-1269810");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-1146126");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-2095459");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-748399");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-2095478");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-2095497");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-2095535");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-4296853");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-4296854"); // iphone 12

		wallpaper_map("https://wallpaperaccess.com/download/iphone-12-1269799");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-2835925");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-1269799");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-2386493");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-3508543");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-3508562");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-2386506");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-2386641");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-3508574");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-3508576");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-3508583");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-2386630");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-3508623");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-3508642");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-1340405");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-3508686");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-3508713");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-6998746");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-6998748");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-6922233"); // iphone 13

		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-6922227");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-7254629");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-7057234");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-6998739");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-7412152");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-6988025");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-6922233");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-7057235");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-7412156");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-6922229");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-7412157");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-7412158");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-7412159");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-7412161");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-7057277");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-7412165");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-7412175");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-7412176");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-6922244");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-7412179");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-7412180");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-7412185");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-7412190");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-7057289");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-6410120");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-7057294");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-982326");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-13-promax-7412204");  // iphone 13 pro max

		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-pro-max-8332305");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-pro-max-4720926");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-pro-max-5415659");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-pro-max-3493954");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-pro-max-8332308");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-pro-max-8332310");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-pro-max-8332311");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-pro-max-8193953");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-pro-max-8332315");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-pro-max-8332322");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-pro-max-8332327");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-pro-max-8332343");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-pro-max-4721052");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-pro-max-8332346");  // iphone 14 pro max

		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-4720926");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-3493954");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-4720944");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-3650702");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-4474264");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-3869916");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-3869928");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-4302334");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-3650695");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-3869932");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-3650711");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-4721052");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-4721112");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-6942557");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-6942564");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-6942597");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-6942606");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-6942611");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-4861770");
		wallpaper_map("https://wallpaperaccess.com/download/iphone-14-6942627"); //iPhone 14

		wallpaper_map("https://wallpaperaccess.com/download/ios-15-4737980");
		wallpaper_map("https://wallpaperaccess.com/download/ios-15-409074");
		wallpaper_map("https://wallpaperaccess.com/download/ios-15-4241303"); // iphone 15

		//wallpaper_map("https://i0.wp.com/9to5mac.com/wp-content/uploads/sites/6/2021/09/iphone-13-pro-header-9to5mac.jpg?w=1500&quality=82&strip=all&ssl=1"); // iphone 15

		wallpaper_map("https://9to5mac.com/wp-content/uploads/sites/6/2021/09/1971.Light_Beams_Gold_Light-428w-926h@3xiphone.jpeg?strip=info&w=1500");
		wallpaper_map("https://9to5mac.com/wp-content/uploads/sites/6/2021/09/1941.Light_Beams_Blue_Light-428w-926h@3xiphone.jpeg?strip=info&w=1500");
		wallpaper_map("https://9to5mac.com/wp-content/uploads/sites/6/2021/09/1951.Light_Beams_Silver_Light-428w-926h@3xiphone.jpeg?strip=info&w=1500"); // iphone 15
		wallpaper_map("https://9to5mac.com/wp-content/uploads/sites/6/2021/09/1961.Light_Beams_Dark_Gray_Light-428w-926h@3xiphone.jpeg?strip=info&w=1500"); // iphone 15


		wallpaper_map("https://9to5mac.com/wp-content/uploads/sites/6/2022/09/iPhone-14-wallpaper-5.jpeg?quality=82&strip=all");
		wallpaper_map("https://9to5mac.com/wp-content/uploads/sites/6/2022/09/iPhone-14-wallpaper-4.jpeg?quality=82&strip=all");
		wallpaper_map("https://9to5mac.com/wp-content/uploads/sites/6/2022/09/iPhone-14-wallpaper-3.jpeg?quality=82&strip=all");
		wallpaper_map("https://9to5mac.com/wp-content/uploads/sites/6/2022/09/iPhone-14-wallpaper-2.jpeg");
		wallpaper_map("https://9to5mac.com/wp-content/uploads/sites/6/2022/09/iPhone-14-wallpaper-1.jpeg");
		wallpaper_map("https://9to5mac.com/wp-content/uploads/sites/6/2022/09/iPhone-14-Pro-wallpaper-4.jpeg");
		wallpaper_map("https://9to5mac.com/wp-content/uploads/sites/6/2022/09/iPhone-14-Pro-wallpaper-3.jpeg");
		wallpaper_map("https://9to5mac.com/wp-content/uploads/sites/6/2022/09/iPhone-14-Pro-wallpaper-2.jpeg?quality=82&strip=all");



		wallpaper_map("https://1.bp.blogspot.com/-5kTsqSgJYqQ/ZQHrpKTCxPI/AAAAAAAAGNs/dsXvYHCliLomcX5-TAZeQy6Wl2Vc5DFFwCNcBGAsYHQ/s2532/iPhone%2B15%2BPro%2BWallpaper%2B1%2BTechRushi.com%2BExclusive%2B%25281%2529.webp");
		wallpaper_map("https://1.bp.blogspot.com/-KJozjtl1oOE/ZQHrpHN2UII/AAAAAAAAGNo/1Sap_axdMtclLCAIPogDEr6z8YN7Qp7pgCNcBGAsYHQ/s2532/iPhone%2B15%2BPro%2BWallpaper%2B2%2BTechRushi.com%2BExclusive.webp");
		wallpaper_map("https://1.bp.blogspot.com/-Rx9cWO4Sr-k/ZQHroQsyz4I/AAAAAAAAGNk/mgfL0q030oYRZ1f-Aue8fFiVqbfQpNlOgCNcBGAsYHQ/s4096/iPhone%2B15%2BPro%2BWallpaper%2B2%2BTechRushi.com.webp");
		wallpaper_map("https://1.bp.blogspot.com/-kIC1tl0mONQ/ZQHrqeOHuYI/AAAAAAAAGN0/HuO025e9YqcZ776m6OMxGFm44UYLMPkGwCNcBGAsYHQ/s4096/iPhone%2B15%2BPro%2BWallpaper%2B3%2BTechRushi.com.webp");
		wallpaper_map("https://1.bp.blogspot.com/-ZsFhiXxHTrI/ZQHrq7qY5jI/AAAAAAAAGN4/VDzdsYTArkwffAR2bnl2-AWwPAHKKKGLgCNcBGAsYHQ/s2532/iPhone%2B15%2BPro%2BWallpaper%2B5%2BTechRushi.com%2BExclusive.webp");
		wallpaper_map("https://1.bp.blogspot.com/-pjj7JUfhx7A/ZQHrrHTiSkI/AAAAAAAAGN8/oMc164OTIGoken_uHZVXhSoI9IeCNEXHgCNcBGAsYHQ/s2532/iPhone%2B15%2BWallpaper%2B1%2BTechRushi.com%2BExclusive.webp");
		wallpaper_map("https://1.bp.blogspot.com/-yvaWBZqGZWo/ZQHrrd4r8lI/AAAAAAAAGOA/0xF1n59DvJocy3QvLghD81Eo_6pjtzZRACNcBGAsYHQ/s2532/iPhone%2B15%2BWallpaper%2B2%2BTechRushi.com%2BExclusive.webp");


		wallpaper_map("https://1.bp.blogspot.com/-umYYjDij0gs/ZQHrr7tkT1I/AAAAAAAAGOE/pGq7xcHKZCwYv8bd6R5jyL9bFtBORAEIACNcBGAsYHQ/s2532/iPhone%2B15%2BWallpaper%2B4%2BTechRushi.com%2BExclusive.webp");
		wallpaper_map("https://1.bp.blogspot.com/-2oGqlgg9P_o/ZQHrsAgxxcI/AAAAAAAAGOI/PPsuHKlCIsMCRmhbgRxZYueFQXagp8gIgCNcBGAsYHQ/s2532/iPhone%2B15%2BWallpaper%2B5%2BTechRushi.com%2BExclusive.webp");





	}


}
