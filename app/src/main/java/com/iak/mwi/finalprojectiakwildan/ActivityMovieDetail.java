package com.iak.mwi.finalprojectiakwildan;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.iak.mwi.finalprojectiakwildan.Adapter.AdapterTrailer;
import com.iak.mwi.finalprojectiakwildan.Model.Movie;
import com.iak.mwi.finalprojectiakwildan.Model.Trailer;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;

public class ActivityMovieDetail extends AppCompatActivity {
    private ArrayList<Trailer> dataTrailer = new ArrayList<>();
    private AdapterTrailer adapter;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.myCollapsToolbar);
        ImageView backdrop = (ImageView) collapsingToolbar.findViewById(R.id.backdrop);
        ImageView poster = (ImageView) findViewById(R.id.poster);
        ImageView logo = (ImageView) findViewById(R.id.logotmdb);
        TextView tahun = (TextView) findViewById(R.id.tahun);
        TextView rating = (TextView) findViewById(R.id.rating);
        TextView rilis = (TextView) findViewById(R.id.release);
        TextView overview = (TextView) findViewById(R.id.overview);
        final CircleButton btnFav = (CircleButton) findViewById(R.id.btnFav);
        list = (ListView) findViewById(R.id.listTrailer);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            final Movie m = b.getParcelable("film");
            List<Movie> favorite = Movie.find(Movie.class, "Movieid = ?", String.valueOf(m.getMovieid()));
            if (favorite.size() <= 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btnFav.setImageDrawable(getDrawable(R.drawable.fav_notyet));
                } else {
                    btnFav.setImageDrawable(getResources().getDrawable(R.drawable.fav_notyet));
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btnFav.setImageDrawable(getDrawable(R.drawable.fav_done));
                } else {
                    btnFav.setImageDrawable(getResources().getDrawable(R.drawable.fav_done));
                }
            }
            collapsingToolbar.setTitle(m.getTitle());
            Picasso.with(getApplicationContext()).load(m.getBackdrop()).fit().into(backdrop);
            Picasso.with(getApplicationContext()).load(m.getPoster()).fit().into(poster);
            Picasso.with(getApplicationContext()).load(R.drawable.themoviedb).fit().into(logo);
            tahun.setText(m.getRelease_date().substring(0, 4));
            rating.setText("Rating: " + Long.toString(m.getVote_average()));
            rilis.setText("Release Date: " + m.getRelease_date());
            overview.setText(m.getOverview());
            getDataTrailer(m.getMovieid());
            btnFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<Movie> favorite = Movie.find(Movie.class, "Movieid = ?", String.valueOf(m.getMovieid()));
                    if (favorite.size() <= 0) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            btnFav.setImageDrawable(getDrawable(R.drawable.fav_done));
                        } else {
                            btnFav.setImageDrawable(getResources().getDrawable(R.drawable.fav_done));
                        }
                        m.save();
                        Toast.makeText(ActivityMovieDetail.this, "This movie has been add to your favorite", Toast.LENGTH_SHORT).show();
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            btnFav.setImageDrawable(getDrawable(R.drawable.fav_notyet));
                        } else {
                            btnFav.setImageDrawable(getResources().getDrawable(R.drawable.fav_notyet));
                        }
                        Movie movie = favorite.get(0);
                        movie.delete();
                        Toast.makeText(ActivityMovieDetail.this, "This movie has been remove from your favorite", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void getDataTrailer(int id) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.URL_API + "/movie/" + id + "/videos" + Config.API_KEY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject result = new JSONObject(response);
                            JSONArray hasil = result.getJSONArray("results");
                            for (int i = 0; i < hasil.length(); i++) {
                                JSONObject object = hasil.getJSONObject(i);
                                String title = object.getString("name");
                                String url = object.getString("key");
                                String tipe = object.getString("type");
                                dataTrailer.add(new Trailer(title, url, tipe));
                            }
                            adapter = new AdapterTrailer(getApplicationContext(), dataTrailer);
                            list.setAdapter(adapter);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                    Toast.makeText(ActivityMovieDetail.this, dataTrailer.get(position).getUrl(), Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(dataTrailer.get(position).getUrl()));
                                    startActivity(i);
                                }
                            });
                        } catch (Exception e) {
                            Log.e("Exception", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error Volley", error.getMessage());
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(ActivityMovieDetail.this, "Cant load data\nCheck your network connection", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}
