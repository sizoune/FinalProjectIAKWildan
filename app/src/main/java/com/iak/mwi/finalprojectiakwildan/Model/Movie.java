package com.iak.mwi.finalprojectiakwildan.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

/**
 * Created by mwi on 5/23/17.
 */

public class Movie extends SugarRecord implements Parcelable {
    private String poster, overview, release_date, title, backdrop;
    private boolean adult;
    private int Movieid, vote_count;
    private long popularity, vote_average;

    public Movie() {
    }

    public Movie(String poster, String overview, String release_date, String title, String backdrop,
                 boolean adult, int id, int vote_count, long popularity, long vote_average) {
        this.poster = "http://image.tmdb.org/t/p/w185" + poster;
        this.overview = overview;
        this.release_date = release_date;
        this.title = title;
        this.backdrop = "http://image.tmdb.org/t/p/w780" + backdrop;
        this.adult = adult;
        this.Movieid = id;
        this.vote_count = vote_count;
        this.popularity = popularity;
        this.vote_average = vote_average;
    }

    protected Movie(Parcel in) {
        poster = in.readString();
        overview = in.readString();
        release_date = in.readString();
        title = in.readString();
        backdrop = in.readString();
        adult = in.readByte() != 0;
        Movieid = in.readInt();
        vote_count = in.readInt();
        popularity = in.readLong();
        vote_average = in.readLong();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public int getMovieid() {
        return Movieid;
    }

    public void setMovieid(int movieid) {
        Movieid = movieid;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public long getPopularity() {
        return popularity;
    }

    public void setPopularity(long popularity) {
        this.popularity = popularity;
    }

    public long getVote_average() {
        return vote_average;
    }

    public void setVote_average(long vote_average) {
        this.vote_average = vote_average;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(poster);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(title);
        dest.writeString(backdrop);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeInt(Movieid);
        dest.writeInt(vote_count);
        dest.writeLong(popularity);
        dest.writeLong(vote_average);
    }
}
