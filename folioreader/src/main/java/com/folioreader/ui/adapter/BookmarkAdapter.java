package com.folioreader.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.folioreader.Config;
import com.folioreader.R;
import com.folioreader.model.HighlightImpl;
import com.folioreader.ui.view.UnderlinedTextView;
import com.folioreader.util.AppUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkHolder> {

    private ArrayList<HashMap>  bookmarks;
    private BookmarkAdapter.BookmarkAdapterCallback callback;
    private Context context;
    private Config config;


    public BookmarkAdapter(Context context, ArrayList<HashMap> bookmarks, BookmarkAdapter.BookmarkAdapterCallback callback, Config config) {
        this.context = context;
        this.bookmarks = bookmarks;
        this.callback = callback;
        this.config = config;
    }

    @Override
    public BookmarkAdapter.BookmarkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BookmarkAdapter.BookmarkHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_bookmark, parent, false));
    }

    @Override
    public void onBindViewHolder(final BookmarkHolder holder, final int position) {
        holder.container.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((AppCompatActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        holder.container.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                    }
                });
            }
        }, 10);
        holder.content.setText(bookmarks.get(position).get("name").toString());
        holder.date.setText(bookmarks.get(position).get("date").toString());
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onItemClick(getItem(position));
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.deleteBookmark(bookmarks.get(position).get("date").toString(), bookmarks.get(position).get("name").toString() );
                bookmarks.remove(position);
                notifyDataSetChanged();

            }
        });
        holder.container.postDelayed(new Runnable() {
            @Override
            public void run() {
                final int height = holder.container.getHeight();
                ((AppCompatActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ViewGroup.LayoutParams params =
                                holder.swipeLinearLayout.getLayoutParams();
                        params.height = height;
                        holder.swipeLinearLayout.setLayoutParams(params);
                    }
                });
            }
        }, 30);
        if (config.isNightMode()) {
            holder.container.setBackgroundColor(ContextCompat.getColor(context,
                    R.color.black));
            holder.date.setTextColor(ContextCompat.getColor(context,
                    R.color.white));
            holder.content.setTextColor(ContextCompat.getColor(context,
                    R.color.white));
        } else {
            holder.container.setBackgroundColor(ContextCompat.getColor(context,
                    R.color.white));
            holder.date.setTextColor(ContextCompat.getColor(context,
                    R.color.black));
            holder.content.setTextColor(ContextCompat.getColor(context,
                    R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        Log.i("bookmarkAdapter", "getItemCount: bookmarks.size = " + bookmarks.size());
        return bookmarks.size();
    }

    private HashMap getItem(int position) {

        return bookmarks.get(position);
    }


    static class BookmarkHolder extends RecyclerView.ViewHolder {
        private UnderlinedTextView content;
        private ImageView delete;
        private TextView date;
        private RelativeLayout container;
        private LinearLayout swipeLinearLayout;

        BookmarkHolder(View itemView) {
            super(itemView);
            container = (RelativeLayout) itemView.findViewById(R.id.bookmark_container);
            swipeLinearLayout = (LinearLayout) itemView.findViewById(R.id.bookmark_swipe_linear_layout);
            content = (UnderlinedTextView) itemView.findViewById(R.id.utv_bookmark_content);
            delete = (ImageView) itemView.findViewById(R.id.iv_bookmark_delete);
            date = (TextView) itemView.findViewById(R.id.tv_bookmark_date);
        }
    }


    public interface BookmarkAdapterCallback {
        void onItemClick(HashMap bookmark);

        void deleteBookmark(String date, String name);

    }
}
