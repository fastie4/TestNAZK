package com.fastie4.testnazk.mvp;

import com.fastie4.testnazk.pojo.Item;
import com.jakewharton.rxbinding2.support.v7.widget.SearchViewQueryTextEvent;

import java.util.List;

import io.reactivex.Observable;

public interface MainActivityContract {
    interface View {
        void showProgress();
        void hideProgress();
        void noConnection();
        void noData(boolean isFavourites);
        void showData(List<Item> data, boolean isFavourites);
    }

    interface Presenter {
        void subscribeToSearch(Observable<SearchViewQueryTextEvent> queryText);
        void detach();
        void favourite(Item item);
        void openFavourites();
        void saveNote(String id, String note);
    }
}
