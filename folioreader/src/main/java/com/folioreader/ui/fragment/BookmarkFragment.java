package com.folioreader.ui.fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.folioreader.Config;
import com.folioreader.Constants;
import com.folioreader.FolioReader;
import com.folioreader.R;
import com.folioreader.model.sqlite.BookmarkTable;
import com.folioreader.ui.adapter.BookmarkAdapter;
import com.folioreader.util.AppUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BookmarkFragment extends Fragment implements BookmarkAdapter.BookmarkAdapterCallback {
    private static final String BOOKMARK_ITEM = "bookmark_item";

    private String mBookId;
    private View mRootView;
    private BookmarkAdapter adapter;

    public static BookmarkFragment newInstance(String bookId, String epubTitle) {
        BookmarkFragment bookmarkFragment = new BookmarkFragment();
        Bundle args = new Bundle();
        args.putString(FolioReader.EXTRA_BOOK_ID, bookId);
        args.putString(Constants.BOOK_TITLE, epubTitle);
        bookmarkFragment.setArguments(args);
        return bookmarkFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_bookmark_list, container, false);
        return mRootView;
    }

    @Override
    public void onItemClick(HashMap bookmark) {
        Intent intent = new Intent();
        Log.i("BookmarkFragment", "bookmark is clicked: " + bookmark.toString());
        intent.putExtra(BOOKMARK_ITEM,  bookmark);
        intent.putExtra(Constants.TYPE, Constants.BOOKMARK_SELECTED);

        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i("Bookmark fragment", "onViewCreated: inside onViewCreated ");
        super.onViewCreated(view, savedInstanceState);
        RecyclerView bookmarksView = (RecyclerView) mRootView.findViewById(R.id.rv_bookmarks);
        Config config = AppUtil.getSavedConfig(getActivity());

        mBookId = getArguments().getString(FolioReader.EXTRA_BOOK_ID);
        Log.i("Bookmark fragment", "onViewCreated: mbookID " + mBookId);

        if (config.isNightMode()) {
            mRootView.findViewById(R.id.rv_bookmarks).
                    setBackgroundColor(ContextCompat.getColor(getActivity(),
                            R.color.black));
        }
        bookmarksView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookmarksView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        adapter = new BookmarkAdapter(getActivity(), BookmarkTable.getBookmarksForID(mBookId, getActivity()), this, config);
        bookmarksView.setAdapter(adapter);
    }

    @Override
    public void deleteBookmark(String date, String name) {
        Log.i("BookmarkFragment", "deleteBookmark: ");
        BookmarkTable.deleteBookmark(date, name, getActivity());
    }
}
