package com.fastie4.testnazk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcelable;

import androidx.constraintlayout.widget.Group;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fastie4.testnazk.adapter.RecyclerViewAdapter;
import com.fastie4.testnazk.di.qualifier.ActivityContext;
import com.fastie4.testnazk.mvp.MainActivityContract;
import com.fastie4.testnazk.mvp.PresenterImpl;
import com.fastie4.testnazk.pojo.Item;
import com.google.android.material.button.MaterialButton;
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;
import com.jakewharton.rxbinding2.view.MenuItemActionViewExpandEvent;
import com.jakewharton.rxbinding2.view.RxMenuItem;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {
    static final String START = "start";
    static final String LIST_DATA = "list_data";
    static final String LIST = "list";
    static final String PROGRESS = "progress";
    static final String NO_CONNECTION = "no_connection";
    static final String NO_RESULTS = "no_results";
    static final String IS_FAVOURITES = "is_favourites";

    @BindView(R.id.progress)
    ProgressBar mProgress;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.no_connection)
    Group mNoConnection;
    @BindView(R.id.no_results)
    TextView mNoResults;
    @BindView(R.id.search_group)
    Group mStartSearch;
    @BindView(R.id.start_search)
    MaterialButton mSearch;

    @ActivityContext
    @Inject
    Context mContext;
    @Inject
    PresenterImpl mPresenter;
    @Inject
    RecyclerViewAdapter mAdapter;
    private MenuItem mSearchView;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mProgress.setVisibility(View.GONE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        }
        RxView.clicks(mSearch).subscribe(click -> mSearchView.expandActionView());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        List<Item> data = mAdapter.getItems();
        if (!data.isEmpty()) {
            outState.putParcelableArrayList(LIST_DATA, (ArrayList<? extends Parcelable>) data);
        }
        outState.putInt(START, mStartSearch.getVisibility());
        outState.putInt(LIST, mRecyclerView.getVisibility());
        outState.putInt(PROGRESS, mProgress.getVisibility());
        outState.putInt(NO_CONNECTION, mNoConnection.getVisibility());
        outState.putInt(NO_RESULTS, mNoResults.getVisibility());
        outState.putBoolean(IS_FAVOURITES, mAdapter.isFavourites());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detach();
        super.onDestroy();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        mSearchView = menu.findItem(R.id.search);
        ((SearchView) mSearchView.getActionView())
                .setQueryHint(getResources().getString(R.string.search_hint));
        RxMenuItem.actionViewEvents(mSearchView)
                .subscribe(event -> {
                    if (event instanceof MenuItemActionViewExpandEvent) {
                        mStartSearch.setVisibility(View.GONE);
                    } else {
                        if (mNoResults.getVisibility() == View.VISIBLE) {
                            mNoResults.setVisibility(View.GONE);
                        }
                        if ((mProgress.getVisibility()
                                & mNoConnection.getVisibility()
                                & mNoResults.getVisibility()
                                & mRecyclerView.getVisibility()) == View.GONE) {
                            mStartSearch.setVisibility(View.VISIBLE);
                        }
                    }
                });
        RxMenuItem.clicks(menu.findItem(R.id.favourites))
                .subscribe(event -> mPresenter.openFavourites());

        mPresenter.subscribeToSearch(RxSearchView
                .queryTextChangeEvents((SearchView) mSearchView.getActionView()));
        return true;
    }

    @Override
    public void showProgress() {
        mSearchView.getActionView().clearFocus();
        mProgress.setVisibility(View.VISIBLE);

        mNoResults.setVisibility(View.GONE);
        mNoConnection.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        mStartSearch.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void noConnection() {
        mNoConnection.setVisibility(View.VISIBLE);
    }

    @Override
    public void noData(boolean isFavourites) {
        mRecyclerView.setVisibility(View.GONE);
        mAdapter.setFavourites(isFavourites);
        mNoResults.setText(getResources().getString(
                isFavourites? R.string.no_results_favourites : R.string.no_results));
        mNoResults.setVisibility(View.VISIBLE);
    }

    @Override
    public void showData(List<Item> data, boolean isFavourites) {
        mAdapter.setFavourites(isFavourites);
        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter.setItems(data);
        mRecyclerView.scrollToPosition(0);
    }

    private void restoreState(Bundle savedInstanceState) {
        List<Item> data = savedInstanceState.getParcelableArrayList(LIST_DATA);
        mAdapter.setFavourites(savedInstanceState.getBoolean(IS_FAVOURITES));
        if (data != null) {
            mAdapter.setItems(data);
        }
        mStartSearch.setVisibility(savedInstanceState.getInt(START));
        mRecyclerView.setVisibility(savedInstanceState.getInt(LIST));
        mProgress.setVisibility(savedInstanceState.getInt(PROGRESS));
        mNoConnection.setVisibility(savedInstanceState.getInt(NO_CONNECTION));
        mNoResults.setVisibility(savedInstanceState.getInt(NO_RESULTS));
        mNoResults.setText(getResources().getString(
                mAdapter.isFavourites()? R.string.no_results_favourites : R.string.no_results));
    }
}
