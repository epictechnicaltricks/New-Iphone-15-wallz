package uz.applewallz.frags;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import androidx.annotation.*;

import android.net.Uri;
import android.os.*;
import android.view.*;
import android.content.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

import uz.applewallz.R;
import uz.applewallz.Util;
import uz.applewallz.activity.ShowWallz;


public class Fragment_fav extends  Fragment  {

	private RecyclerView recyclerview1;
	private ArrayList<HashMap<String, Object>> home_walls_list = new ArrayList<>();

	//////////
	SharedPreferences sf;
	SharedPreferences.Editor myEdit;

	int pos;
	SwipeRefreshLayout sw;
	TextView refresh2;

	LinearLayout prog;

	@NonNull
	@Override
	public View onCreateView(@NonNull LayoutInflater _inflater, @Nullable ViewGroup _container, @Nullable Bundle _savedInstanceState) {
		View _view = _inflater.inflate(R.layout.fav_fragment, _container, false);
		initialize(_view);
		initializeLogic();
		return _view;
	}
	
	private void initialize(View _view) {
		prog = _view.findViewById(R.id.progress);
		prog.setVisibility(View.GONE);
		recyclerview1 = _view.findViewById(R.id.recyclerview1);
		sf = Objects.requireNonNull(getActivity()).getSharedPreferences("MySharedPref2", Context.MODE_PRIVATE);
		myEdit = sf.edit();
		sw = _view.findViewById(R.id.refresh);
		refresh2 = _view.findViewById(R.id.refresh2);
		refresh2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Toast.makeText(getActivity(), "Refreshed", Toast.LENGTH_SHORT).show();
				refresh();
			}
		});
	}
	
	private void initializeLogic() {


		sw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				sw.setRefreshing(false);
				refresh();
			}
		});

		refresh();

	}

	@Override
	public void onResume() {



		super.onResume();
	}

	void refresh()
	{
		if(sf.getString("fav_data","").equals(""))
		{
			myEdit.putString("fav_data","[]");
			myEdit.apply();
		}

		home_walls_list = new Gson().fromJson(sf.getString("fav_data",""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());

		if(home_walls_list.size()>0) {
			Collections.reverse(home_walls_list);
			recyclerview1.setLayoutManager(new GridLayoutManager(getActivity(), 2));
			recyclerview1.setAdapter(new WallzAdapter(home_walls_list));

			prog.setVisibility(View.GONE);
			sw.setVisibility(View.VISIBLE);
		} else
		{
			prog.setVisibility(View.VISIBLE);
			sw.setVisibility(View.GONE);
		}
	}

	@Override
	public void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		
		super.onActivityResult(_requestCode, _resultCode, _data);

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
			return new WallzAdapter.ViewHolder(v);
		}

		@Override
		public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
			View _view = viewHolder.itemView;
			final ImageView wall = _view.findViewById(R.id.wall_img);
			final  ImageView fav = _view.findViewById(R.id.fav_btn);
			final CardView card = _view.findViewById(R.id.cardview1);

			int ran= Util.getRandomNo(1, 5);
			if (ran == 1) {       wall.setBackgroundColor(0xFF89039e);
			} else if(ran  == 2){ wall.setBackgroundColor(0xFF039e72);
			}else if(ran == 3){   wall.setBackgroundColor(0xFF035b9e);
			}else if(ran == 4){   wall.setBackgroundColor(0xFF9e0d03);
			}else if(ran == 5) {  wall.setBackgroundColor(0xFF46039e);
			}

			try {


				Animation slide_up = AnimationUtils.loadAnimation(getActivity(),
						R.anim.slide_up );
				card.startAnimation(slide_up);



				final String img = Objects.requireNonNull(home_walls_list.get(i).get("img_url")).toString();

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


				Glide.with(Objects.requireNonNull(getActivity()))
						.load(Uri.parse(img))
						.thumbnail(0.01f)
						.transition(withCrossFade())
						.into(wall);



				//////////////////////////////////////////
				fav.setImageResource(R.drawable.fav_filled);






				fav.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {

						home_walls_list = new Gson().fromJson(sf.getString("fav_data",""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());

						pos = getPos_img_url(img,home_walls_list);

						if(pos>=0)
						{
							fav.setImageResource(R.drawable.fav_outline);
							home_walls_list.remove(pos);


							/*Snackbar.make(recyclerview1, "Removed", Snackbar.LENGTH_SHORT)
									.setAction("", new View.OnClickListener(){
										@Override
										public void onClick(View _view) {

										}
									}).show();*/

						}
						myEdit.putString("fav_data",new Gson().toJson(home_walls_list));
						myEdit.apply();

						home_walls_list = new Gson().fromJson(sf.getString("fav_data",""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
						Collections.reverse(home_walls_list);
					//	recyclerview1.setAdapter(new WallzAdapter(home_walls_list));

						if(home_walls_list.size()>0) {
							Collections.reverse(home_walls_list);
							recyclerview1.setLayoutManager(new GridLayoutManager(getActivity(), 2));
							recyclerview1.setAdapter(new WallzAdapter(home_walls_list));

							prog.setVisibility(View.GONE);
							sw.setVisibility(View.VISIBLE);
						} else
						{
							prog.setVisibility(View.VISIBLE);
							sw.setVisibility(View.GONE);
						}


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
			if(Objects.requireNonNull(arrayList.get(x).get("img_url")).toString().trim().equals(img_url))
			{
				return x;
			}
		}

		return -1;
	}
	
}
