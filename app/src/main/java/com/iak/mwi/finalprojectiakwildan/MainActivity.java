package com.iak.mwi.finalprojectiakwildan;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.iak.mwi.finalprojectiakwildan.Adapter.AdapterUtama;
import com.iak.mwi.finalprojectiakwildan.Model.Movie;
import com.karumi.dividers.DividerBuilder;
import com.karumi.dividers.DividerItemDecoration;
import com.karumi.dividers.Layer;
import com.paginate.Paginate;
import com.paginate.recycler.LoadingListItemCreator;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int PAGE_SIZE = 20;
    public int TOTAL_PAGES;
    public int CURRENT_PAGE;
    private ArrayList<Movie> dataMovie = new ArrayList<>();
    private AdapterUtama adapter;
    private RecyclerView list;
    private GridLayoutManager gridLayoutManager;
    private String kategori;
    private boolean isLoading = false;
    private boolean adaKoneksi = true;
    Paginate.Callbacks callbacks;
    private Paginate paginate;
    private TextView title;
    RecyclerView.ItemDecoration itemDecoration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title = (TextView) toolbar.findViewById(R.id.txtJudul);
        //pagination
        CURRENT_PAGE = 1;
        kategori = "/movie/top_rated";
        title.setText("Top Rated Movies");
        list = (RecyclerView) findViewById(R.id.recyclerView);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
        getDataMovie(kategori);
        callbacks = new Paginate.Callbacks() {
            @Override
            public void onLoadMore() {
                if (!kategori.equals("")) {
                    loadMoreItems();
                }
            }

            @Override
            public boolean isLoading() {
                // Indicate whether new page loading is in progress or not
                return isLoading;
            }

            @Override
            public boolean hasLoadedAllItems() {
                // Indicate whether all data (pages) are loaded or not
                return CURRENT_PAGE == TOTAL_PAGES;
            }
        };
        Drawable exampleDrawable = getResources().getDrawable(R.drawable.divider);
        itemDecoration = new DividerItemDecoration(new Layer(DividerBuilder.get().with(exampleDrawable).build()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_kategori, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_top_rated) {
            dataMovie = new ArrayList<>();
            CURRENT_PAGE = 1;
            kategori = "/movie/top_rated";
            getDataMovie(kategori);
            title.setText("Top Rated Movies");
        } else if (id == R.id.action_popular) {
            dataMovie = new ArrayList<>();
            CURRENT_PAGE = 1;
            kategori = "/movie/popular";
            getDataMovie(kategori);
            title.setText("Popular Movies");
        } else if (id == R.id.action_upcoming) {
            dataMovie = new ArrayList<>();
            CURRENT_PAGE = 1;
            kategori = "/movie/upcoming";
            getDataMovie(kategori);
            title.setText("Upcoming Movies");
        } else if (id == R.id.action_now_playing) {
            dataMovie = new ArrayList<>();
            CURRENT_PAGE = 1;
            kategori = "/movie/now_playing";
            getDataMovie(kategori);
            title.setText("Now Playing Movies");
        } else if (id == R.id.action_favorite) {
            dataMovie = new ArrayList<>();
            title.setText("Favorite Movies");
            kategori="";
            getAllFavoriteMovies();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMoreItems() {
        isLoading = true;
        CURRENT_PAGE++;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getDataMovie(kategori);
            }
        }, 3000);
    }

    private void getAllFavoriteMovies() {
        List<Movie> movies = Movie.listAll(Movie.class);
        for (Movie film : movies) {
            dataMovie.add(film);
        }
        adapter = new AdapterUtama(getApplicationContext(), dataMovie);
        adapter.SetOnItemClickListener(new AdapterUtama.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Movie m = dataMovie.get(position);
                Intent intent = new Intent(MainActivity.this, ActivityMovieDetail.class)
                        .putExtra("film", m);
                startActivity(intent);
            }
        });
        list.setAdapter(adapter);
        list.setLayoutManager(gridLayoutManager);
        list.addItemDecoration(itemDecoration);
    }

    private void getDataMovie(final String kategori) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Config.URL_API + kategori + Config.API_KEY
                + "&page=" + Integer.toString(CURRENT_PAGE),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject respon = new JSONObject(response);
                            TOTAL_PAGES = respon.getInt("total_pages");
                            JSONArray hasil = respon.getJSONArray("results");
                            for (int i = 0; i < hasil.length(); i++) {
                                JSONObject object = hasil.getJSONObject(i);
                                String poster = object.getString("poster_path");
                                boolean adult = object.getBoolean("adult");
                                String overview = object.getString("overview");
                                String release = object.getString("release_date");
                                int id = object.getInt("id");
                                String title = object.getString("title");
                                String backdrop = object.getString("backdrop_path");
                                long popularity = object.getLong("popularity");
                                int vote_count = object.getInt("vote_count");
                                long vote_average = object.getLong("vote_average");
                                Movie film = new Movie(poster, overview, release, title, backdrop, adult,
                                        id, vote_count, popularity, vote_average);
                                dataMovie.add(film);
                            }
                            adaKoneksi = true;
                            adapter = new AdapterUtama(getApplicationContext(), dataMovie);
                            adapter.SetOnItemClickListener(new AdapterUtama.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Movie m = dataMovie.get(position);
                                    Intent intent = new Intent(MainActivity.this, ActivityMovieDetail.class)
                                            .putExtra("film", m);
                                    startActivity(intent);
                                }
                            });
                            list.setAdapter(adapter);
                            list.setLayoutManager(gridLayoutManager);
                            list.addItemDecoration(itemDecoration);
                            isLoading = false;
                            paginate.with(list, callbacks)
                                    .setLoadingTriggerThreshold(2)
                                    .addLoadingListItem(true)
                                    .setLoadingListItemCreator(new LoadingListItemCreator() {
                                        @Override
                                        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                                            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                                            View view = inflater.inflate(R.layout.custom_loading_list_item, parent, false);
                                            return new VH(view);
                                        }

                                        @Override
                                        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                                            VH vh = (VH) holder;
                                        }

                                        class VH extends RecyclerView.ViewHolder {
                                            AVLoadingIndicatorView avi;

                                            public VH(View itemView) {
                                                super(itemView);
                                                avi = (AVLoadingIndicatorView) findViewById(R.id.avi1);
                                            }
                                        }
                                    })
                                    .build();
                            if (callbacks.hasLoadedAllItems() || kategori.equals("")) {
                                paginate.unbind();
                            }
                            if (!callbacks.isLoading() && CURRENT_PAGE != 1) {
                                Toast.makeText(getApplicationContext(), "More Movie successfully loaded !", Toast.LENGTH_SHORT).show();
                            }
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
                            Toast.makeText(MainActivity.this, "Cant load data\nCheck your network connection", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

}
