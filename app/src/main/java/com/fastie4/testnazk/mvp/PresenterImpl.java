package com.fastie4.testnazk.mvp;

import android.annotation.SuppressLint;

import com.fastie4.testnazk.di.scopes.ActivityScope;
import com.fastie4.testnazk.pojo.Item;
import com.fastie4.testnazk.retrofit.ApiInterface;
import com.fastie4.testnazk.room.Declaration;
import com.fastie4.testnazk.room.DeclarationDao;
import com.fastie4.testnazk.rx.RetryWhenHttp429WithDelay;
import com.jakewharton.rxbinding2.support.v7.widget.SearchViewQueryTextEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@ActivityScope
public class PresenterImpl implements MainActivityContract.Presenter {

    private ApiInterface mApiInterface;
    private MainActivityContract.View mView;
    private DeclarationDao mDao;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    @Inject
    public PresenterImpl(ApiInterface apiInterface, MainActivityContract.View view,
                         DeclarationDao dao) {
        mApiInterface = apiInterface;
        mView = view;
        mDao = dao;
    }

    @Override
    public void subscribeToSearch(Observable<SearchViewQueryTextEvent> queryText) {
        mDisposable.add(queryText.filter(SearchViewQueryTextEvent::isSubmitted)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(event -> mView.showProgress())
                .observeOn(Schedulers.io())
                .switchMap(searchViewQueryTextEvent ->
                    mApiInterface.getDeclarations(searchViewQueryTextEvent.queryText().toString())
                            .retryWhen(new RetryWhenHttp429WithDelay(5, 3000))
                            .observeOn(AndroidSchedulers.mainThread())
                            .onErrorResumeNext(throwable -> {
                                handleHttpError(throwable);
                                return Observable.empty();
                            }))
                .withLatestFrom(mDao.getFavouritesIdsAndNotes().toObservable(),
                        (declaration, favourites) -> {
                            HashMap<String, String> pairs = new HashMap<>();
                            for (Declaration pair : favourites) {
                                pairs.put(pair.getId(), pair.getNote());
                            }
                            if (declaration.items != null) {
                                int size = declaration.items.size();
                                for (int i = 0; i < size; i++) {
                                    Item item = declaration.items.get(i);
                                    if (item.isFavourite = pairs.containsKey(item.id)) {
                                        item.note = pairs.get(item.id);
                                    }
                                }
                            }
                            return declaration;
                        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(declaration -> {
                        mView.hideProgress();
                        if (declaration.items == null || declaration.items.isEmpty()) {
                            mView.noData(false);
                        } else mView.showData(declaration.items, false);
                    }));
    }

    @Override
    public void detach() {
        mDisposable.dispose();
        mView = null;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    @Override
    public void favourite(Item item) {
        Declaration declaration = new Declaration(item.id, item.firstname, item.lastname,
                item.placeOfWork, item.position, item.linkPDF, null);
        Completable.fromAction(() -> {
            if (item.isFavourite) {
                mDao.insert(declaration);
            } else {
                mDao.delete(declaration);
            }
        }).subscribeOn(Schedulers.io())
        .subscribe();
    }

    @SuppressLint("CheckResult")
    @Override
    public void openFavourites() {
        //noinspection ResultOfMethodCallIgnored
        mDao.getAll()
                .take(1)
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(s -> mView.showProgress())
                .observeOn(Schedulers.computation())
                .map(declarations -> {
                    List<Item> items = new ArrayList<>();
                    for (Declaration declaration: declarations) {
                        Item item = new Item();
                        item.id = declaration.getId();
                        item.firstname = declaration.getFirstName();
                        item.lastname = declaration.getLastName();
                        item.placeOfWork = declaration.getPlaceOfWork();
                        item.position = declaration.getPosition();
                        item.linkPDF = declaration.getLinkPdf();
                        item.note = declaration.getNote();
                        item.isFavourite = true;
                        items.add(item);
                    }
                    return items;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    mView.hideProgress();
                    if (list.isEmpty()) {
                        mView.noData(true);
                    } else mView.showData(list, true);
                });
    }

    @Override
    public void saveNote(String id, String note) {
        Completable.fromAction(() -> mDao.updateNote(id, note))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    private void handleHttpError(Throwable throwable) {
        mView.hideProgress();
        mView.noConnection();
    }
}
