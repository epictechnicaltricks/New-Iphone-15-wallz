package uz.applewallz.frags;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import uz.applewallz.R;
import uz.applewallz.RequestNetwork;
import uz.applewallz.RequestNetworkController;
import uz.applewallz.Util;
import uz.applewallz.activity.ShowWallz;


public class Fragment_wallz_store extends  Fragment implements View.OnClickListener {


	private RecyclerView recyclerview1;
	private ArrayList<HashMap<String, Object>> home_walls_list = new ArrayList<>();



	private RequestNetwork api;
	private RequestNetwork.RequestListener _api_request_listener;

	 String API = "";
	 LinearLayout progress;
	 int cPage = 1;
	 String thumbnail_url = "";
	 String tPages = "";
	 String color_data = "";

	String api_query = "Abstract";


	Parcelable state;

	LinearLayout message_layput;
	TextView msg;
	Button reload_btn;

	TextView drk,
			rad_,
			amo,
			Nature_,
			foo,
			ani,
			color_,

			car_,
			tech_,
			space_,
			edu_,
			girls_;




	//////////
	SharedPreferences sf;
	SharedPreferences.Editor myEdit;
	int pos;

	ArrayList<HashMap<String, Object>> fav_img_list = new ArrayList<>();

	@NonNull
	@Override
	public View onCreateView(@NonNull LayoutInflater _inflater, @Nullable ViewGroup _container, @Nullable Bundle _savedInstanceState) {
		View _view = _inflater.inflate(R.layout.wallzstore_fragment, _container, false);
		initialize(_view);
		initializeLogic();
		return _view;
	}


	private void initialize(View _view) {

		message_layput = _view.findViewById(R.id.message_layout);
		msg = _view.findViewById(R.id.msg);
		reload_btn = _view.findViewById(R.id.reload_btn);

		message_layput.setVisibility(View.GONE);

		        drk      =            _view.findViewById(R.id.drk    );
				rad_     =            _view.findViewById(R.id.rad_   );
				amo      =            _view.findViewById(R.id.amo    );
				Nature_  =            _view.findViewById(R.id.Nature_);
				foo      =            _view.findViewById(R.id.foo    );
				ani      =            _view.findViewById(R.id.ani    );
				color_   =            _view.findViewById(R.id.color_ );
				car_     =            _view.findViewById(R.id.car_   );
				tech_    =            _view.findViewById(R.id.tech_  );
				space_   =            _view.findViewById(R.id.space_ );
				edu_     =            _view.findViewById(R.id.edu_   );
				girls_   =            _view.findViewById(R.id.girls_ );



		drk.setOnClickListener(this);
		rad_.setOnClickListener(this);
		amo.setOnClickListener(this);
		Nature_.setOnClickListener(this);
		foo.setOnClickListener(this);
		ani.setOnClickListener(this);
		color_.setOnClickListener(this);
		car_.setOnClickListener(this);
		tech_.setOnClickListener(this);
		space_.setOnClickListener(this);
		edu_.setOnClickListener(this);
		girls_.setOnClickListener(this);

		//drk.performClick();



		recyclerview1 = _view.findViewById(R.id.recyclerview1);
		progress = _view.findViewById(R.id.progress);


		reload_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				findAPI(api_query);
				/*Snackbar.make(recyclerview1, ,
						Snackbar.LENGTH_SHORT).setAction("", new View.OnClickListener(){
					@Override
					public void onClick(View _view) {

					}
				}).show();*/

				show_error("Connecting..");
			}
		});


		sf = Objects.requireNonNull(getActivity()).getSharedPreferences("MySharedPref2", Context.MODE_PRIVATE);
		myEdit = sf.edit();

		api = new RequestNetwork(getActivity());

		_api_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
				Log.d("wall_store_response",response);




				refresh_recycler(response,false);



				hide_error();


			}

			@Override
			public void onErrorResponse(String tag, final String message) {
				progress.setVisibility(View.GONE);


				show_error("No internet!");


				Log.d("wall_store",message);
			}
		};


		recyclerview1.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				if (! recyclerView.canScrollVertically(1))
				{

					progress.setVisibility(View.VISIBLE);
						if (Util.isConnected(Objects.requireNonNull(getActivity()))) {

						cPage++;
						//Toast.makeText(getContext(), cPage+"", Toast.LENGTH_SHORT).show();

						findAPI(api_query);



					} else
					{
						progress.setVisibility(View.GONE);
						Toast.makeText(getContext(), "No internet!", Toast.LENGTH_SHORT).show();
						//show_error("No internet! 232");
					}


				} } });

	}





	void findAPI(String _query)
	{
		//Toast.makeText(getActivity(), "api call", Toast.LENGTH_SHORT).show();
		progress.setVisibility(View.VISIBLE);
			api.startRequestNetwork(
					    RequestNetworkController.GET,
					API+"&query="+_query+"&page="+cPage
					, "",
		_api_request_listener);

			api_query = _query;


	}
	
	
	private void initializeLogic() {





		fav_img_list = new Gson().fromJson(sf.getString("fav_data",""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());



		API = "https://api.unsplash.com/search/photos?client_id="+getResources().getString(R.string.api_url_u_splash);
		home_walls_list.clear();
		recyclerview1.setLayoutManager(new GridLayoutManager(getActivity(), 2));
		recyclerview1.setAdapter(new WallzAdapter(home_walls_list));
		//Toast.makeText(getActivity(), "init", Toast.LENGTH_SHORT).show();




		if(sf.getString("offline_data","").equals("") || sf.getString("offline_data","").equals("[]"))
		{
			drk.performClick();
		//	Toast.makeText(getActivity(), "dsfs32222222", Toast.LENGTH_SHORT).show();
			//findAPI("dark");

		} else
		{
		//	Toast.makeText(getActivity(), "dsfs", Toast.LENGTH_SHORT).show();
			// this is for optimization

			refresh_recycler(sf.getString("offline_data",""), true);
		}


		//Toast.makeText(getActivity(), "RESUME", Toast.LENGTH_SHORT).show();
		if(sf.getString("fav_data","").equals(""))
		{
			myEdit.putString("fav_data","[]");
			myEdit.apply();
		}



	}



	@Override
	public void onClick(View view) {
		if (drk    .equals(view)) {   reset_btn_color_with_api(drk    );     }
		if (rad_   .equals(view)) {   reset_btn_color_with_api(rad_   );     }
		if (amo    .equals(view)) {   reset_btn_color_with_api(amo    );     }
		if (Nature_.equals(view)) {   reset_btn_color_with_api(Nature_);     }
		if (foo    .equals(view)) {   reset_btn_color_with_api(foo    );     }
		if (ani    .equals(view)) {   reset_btn_color_with_api(ani    );     }
		if (color_ .equals(view)) {   reset_btn_color_with_api(color_ );     }
		if (car_   .equals(view)) {   reset_btn_color_with_api(car_   );     }
		if (tech_  .equals(view)) {   reset_btn_color_with_api(tech_  );     }
		if (space_ .equals(view)) {   reset_btn_color_with_api(space_ );     }
		if (edu_   .equals(view)) {   reset_btn_color_with_api(edu_   );     }
		if (girls_ .equals(view)) {   reset_btn_color_with_api(girls_ );     }

	}


	void reset_btn_color_with_api(TextView v)
	{


		Animation slide_up = AnimationUtils.loadAnimation(getActivity(),
				R.anim.slide_up_slow );
		recyclerview1.startAnimation(slide_up);


		drk        .setTextColor(0xffffffff);
		rad_       .setTextColor(0xffffffff);
		amo        .setTextColor(0xffffffff);
		Nature_    .setTextColor(0xffffffff);
		foo        .setTextColor(0xffffffff);
		ani        .setTextColor(0xffffffff);
		color_     .setTextColor(0xffffffff);
		car_       .setTextColor(0xffffffff);
		tech_      .setTextColor(0xffffffff);
		space_     .setTextColor(0xffffffff);
		edu_       .setTextColor(0xffffffff);
		girls_     .setTextColor(0xffffffff);
		v.setTextColor(0xff8A5BDF);


		set_Bgcolor_reset(drk     );
		set_Bgcolor_reset(rad_    );
		set_Bgcolor_reset(amo     );
		set_Bgcolor_reset(Nature_ );
		set_Bgcolor_reset(foo     );
		set_Bgcolor_reset(ani     );
		set_Bgcolor_reset(color_  );
		set_Bgcolor_reset(car_    );
		set_Bgcolor_reset(tech_   );
		set_Bgcolor_reset(space_  );
		set_Bgcolor_reset(edu_    );
		set_Bgcolor_reset(girls_  );







		//v.setPaintFlags(v.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

		v.setBackground(new GradientDrawable()
		{ public GradientDrawable getIns(int a, int b, int c, int d) {
			this.setCornerRadius(a);
			this.setStroke(b, c);
			this.setColor(d);
			return this;
		}
		}
		.getIns(100, 1,
				Color.parseColor("#8A5BDF"),
				Color.parseColor("#ff000000")));




		api_query = v.getText().toString().trim();
		home_walls_list.clear();
		findAPI(api_query);



		if (Util.isConnected(Objects.requireNonNull(getActivity()))) {



		} else
		{
			if(sf.getString("offline_data","").equals("") || sf.getString("offline_data","").equals("[]") ) {
				Toast.makeText(getContext(), "No internet!", Toast.LENGTH_SHORT).show();
			} else {
				// this is for optimization

				refresh_recycler(sf.getString("offline_data",""),true);


			}

		}



		Animation fade_in = AnimationUtils.loadAnimation(getActivity(),
				R.anim.fade_in );
		v.startAnimation(fade_in);




	}

	void set_Bgcolor_reset(View v)
	{
		v.setBackground(new GradientDrawable()
		{ public GradientDrawable getIns(int a, int b, int c, int d) {
			this.setCornerRadius(a);
			this.setStroke(b, c);
			this.setColor(d);
			return this;
		}
		}
				.getIns(100, 0,
						Color.parseColor("#8A5BDF"),
						Color.parseColor("#ff000000")));
	}

	public class WallzAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
	{

		ArrayList<HashMap<String, Object>> data;

		public WallzAdapter(ArrayList<HashMap<String, Object>> _array) {
			data = _array;
		}

		@NonNull
		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
			LayoutInflater inflater = (LayoutInflater) requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(R.layout.wall_custom_layout, null);
			RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			v.setLayoutParams(lp);
			return new ViewHolder(v);
		}

		@Override
		public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
			View _view = viewHolder.itemView;
			final CardView card = _view.findViewById(R.id.cardview1);
			final ImageView wall = _view.findViewById(R.id.wall_img);
			final  ImageView fav = _view.findViewById(R.id.fav_btn);

			int ran= Util.getRandomNo(1, 5);
			if (ran == 1) {       wall.setBackgroundColor(0xFF89039e);
			} else if(ran  == 2){ wall.setBackgroundColor(0xFF039e72);
			}else if(ran == 3){   wall.setBackgroundColor(0xFF035b9e);
			}else if(ran == 4){   wall.setBackgroundColor(0xFF9e0d03);
			}else if(ran == 5) {  wall.setBackgroundColor(0xFF46039e);
			}

			try {

				final String img = Objects.requireNonNull(home_walls_list.get(i).get("thumb")).toString();
				final String id = Objects.requireNonNull(home_walls_list.get(i).get("id")).toString();

				Glide.with(Objects.requireNonNull(getActivity()))
						.load(Uri.parse(img))
						.thumbnail(0.01f)
						.transition(withCrossFade())
						.into(wall);


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

				//////////////////////////////////////////


				if(sf.getString("fav_data","").contains(id)) {

					Log.d("fav2","fill");
					fav.setImageResource(R.drawable.fav_filled);
				}
				else {
					Log.d("fav2","out");
					fav.setImageResource(R.drawable.fav_outline);
				}




				fav.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						HashMap<String, Object>  new_map = new HashMap<>();

						fav_img_list = new Gson().fromJson(sf.getString("fav_data",""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());

						pos = getPos_img_url(id,fav_img_list);

						if(pos>=0)
						{
							fav.setImageResource(R.drawable.fav_outline);
							fav_img_list.remove(pos);
							Log.d("fav1",fav_img_list.size()+"\n"+ fav_img_list+"\npos -"+pos);


							Toast.makeText(getContext(), "Removed", Toast.LENGTH_LONG).show();

						} else {

							fav.setImageResource(R.drawable.fav_filled);
							new_map.put("img_url",img);
							new_map.put("img_id",id);
							fav_img_list.add(new_map);

							Vibrator vb = (Vibrator)   getActivity().getSystemService(Context.VIBRATOR_SERVICE);
							vb.vibrate(100);



							Toast.makeText(getContext(), "Added to Favs ", Toast.LENGTH_LONG).show();

						}
						myEdit.putString("fav_data",new Gson().toJson(fav_img_list));
						myEdit.apply();


					}
				});



				////////////////////////////////////////


			} catch (Exception e){

				Toast.makeText(getContext(), "Error getting wallz", Toast.LENGTH_SHORT).show();
			}


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


	int getPos_img_url(String img_url, ArrayList<HashMap<String, Object>> arrayList)
	{
		for(int x=0; x<arrayList.size(); x++)
		{
			if(Objects.requireNonNull(arrayList.get(x).get("img_id")).toString().trim().equals(img_url))
			{
				return x;
			}
		}

		return -1;
	}

	void show_error(String msg_)
	{
		message_layput.setVisibility(View.VISIBLE);
		msg.setText(msg_);

	}

	void hide_error()
	{
		message_layput.setVisibility(View.GONE);

	}

	void refresh_recycler(String response, boolean isOffline_)


	{
		progress.setVisibility(View.GONE);
		if(isOffline_)
		{
			API = "https://api.unsplash.com/search/photos?client_id="+getResources().getString(R.string.backup_api_url);

			// must put black array list adapter on recycler view

			recyclerview1.setLayoutManager(new GridLayoutManager(getActivity(), 2));

			Log.d("isOffline",response);

			Type listType = new TypeToken<ArrayList<HashMap<String, Object>>>() {}.getType();

			ArrayList<HashMap<String, Object>> offlineData;
			offlineData = new Gson().fromJson(response, listType);
			Log.d("isOffline",offlineData.size()+"");
			home_walls_list = offlineData;

			if(offlineData.size()>0)
			{
				recyclerview1.setAdapter(new WallzAdapter(home_walls_list));

				Log.d("isOffline",new Gson().toJson(home_walls_list, listType));
			}




		} else
		{
			try {
				Log.d("wall_store","inside try");

				JSONObject object = new JSONObject(response);
				JSONArray array = object.getJSONArray("results");
				tPages = object.getString("total_pages");



				Log.d("wall_store",tPages);




				for(int i=0;i<array.length();i++)
				{
					HashMap<String, Object> map = new HashMap<>();

					JSONObject obj = array.getJSONObject(i);
					thumbnail_url = obj.getJSONObject("urls").getString("small");
					color_data = obj.getString("color");
					map.put("color", color_data);
					map.put("thumb", thumbnail_url);
					map.put("id", obj.getString("id"));
					home_walls_list.add(map);



				}


				Log.d("wall_store",array.length()+"");

				state = Objects.requireNonNull(recyclerview1.getLayoutManager()).onSaveInstanceState();

				// must put black array list adapter on recycler view

				recyclerview1.setLayoutManager(new GridLayoutManager(getActivity(), 2));
				recyclerview1.setAdapter(new WallzAdapter(home_walls_list));

				if(home_walls_list.size()>0)
				{
					//store on offline
					Type listType = new TypeToken<ArrayList<HashMap<String, Object>>>() {}.getType();
					myEdit.putString("offline_data",new Gson().toJson(home_walls_list, listType));
					myEdit.apply();
				}



				recyclerview1.getLayoutManager().onRestoreInstanceState(state);



			} catch (JSONException e)
			{
				progress.setVisibility(View.GONE);
				Log.d("wall_store","error 111\n"+e);

				if (Util.isConnected(Objects.requireNonNull(getActivity()))) {
					cPage++;
					API = "https://api.unsplash.com/search/photos?client_id="+getResources().getString(R.string.backup_api_url);
				}


				show_error("Server down!, It will online soon..");

				/*	Snackbar.make(recyclerview1, , Snackbar.LENGTH_SHORT)
							.setAction("Reload", new View.OnClickListener(){
						@Override
						public void onClick(View _view) {

						}
					}).show();*/


			}
		}



	}



}
