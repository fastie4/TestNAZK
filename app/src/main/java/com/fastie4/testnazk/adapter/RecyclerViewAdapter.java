package com.fastie4.testnazk.adapter;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fastie4.testnazk.R;
import com.fastie4.testnazk.mvp.MainActivityContract;
import com.fastie4.testnazk.pojo.Item;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    @NonNull
    private List<Item> mItems;
    private MainActivityContract.Presenter mPresenter;
    private MainActivityContract.View mView;
    private boolean isFavourites;

    @Inject
    public RecyclerViewAdapter(MainActivityContract.Presenter presenter, MainActivityContract.View view) {
        mItems = new ArrayList<>();
        mPresenter = presenter;
        mView = view;
    }

    public boolean isFavourites() {
        return isFavourites;
    }

    public void setFavourites(boolean favourites) {
        isFavourites = favourites;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_row, viewGroup, false));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Item item = mItems.get(i);
        viewHolder.mFirstName.setText(item.firstname);
        viewHolder.mLastName.setText(item.lastname);
        viewHolder.mPlaceOfWork.setText(item.placeOfWork);
        if (item.position == null) {
            viewHolder.mPosition.setVisibility(View.GONE);
        } else {
            viewHolder.mPosition.setVisibility(View.VISIBLE);
            viewHolder.mPosition.setText(item.position);
        }
        if (item.isFavourite && (isFavourites || item.note != null && !item.note.isEmpty())) {
            viewHolder.mNoteLayout.setVisibility(View.VISIBLE);
            viewHolder.mNote.setText(item.note);
        } else viewHolder.mNoteLayout.setVisibility(View.GONE);
        viewHolder.mNote.setOnEditorActionListener((view, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE) {
                item.note = view.getText().toString();
                mPresenter.saveNote(item.id, item.note);
                view.clearFocus();
                //Hide keyboard
                ((InputMethodManager) view.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            return false;
        });
        viewHolder.mNote.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus) {
                String note = ((EditText)view).getText().toString();
                if (note.isEmpty() && !isFavourites) {
                    viewHolder.mNoteLayout.setVisibility(View.GONE);
                }
                item.note = note;
                mPresenter.saveNote(item.id, note);
            }
        });
        viewHolder.mPdf.setOnClickListener(v -> openPdfLink(item.linkPDF, v.getContext()));
        viewHolder.mFavourite.setSelected(item.isFavourite);
        viewHolder.mFavourite.setOnClickListener(v -> clickFavourite(v, viewHolder));
    }

    private void clickFavourite(View v, ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        Item _item = mItems.get(position);
        v.setSelected(!v.isSelected());
        _item.isFavourite = !_item.isFavourite;
        if (_item.isFavourite) {
            //noinspection ConstantConditions
            viewHolder.mNote.getText().clear();
            viewHolder.mNoteLayout.setVisibility(View.VISIBLE);
        } else if (isFavourites) {
            mItems.remove(position);
            notifyItemRemoved(position);
            if (mItems.isEmpty()) {
                mView.noData(isFavourites);
            }
        }
        mPresenter.favourite(_item);
    }

    private void openPdfLink(String link, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        try {
             context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "No application", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @NonNull
    public List<Item> getItems() {
        return mItems;
    }

    public void setItems(@NonNull List<Item> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.first_name) TextView mFirstName;
        @BindView(R.id.last_name) TextView mLastName;
        @BindView(R.id.position) TextView mPosition;
        @BindView(R.id.place_of_work) TextView mPlaceOfWork;
        @BindView(R.id.pdf) ImageButton mPdf;
        @BindView(R.id.favourite) ImageButton mFavourite;
        @BindView(R.id.note_layout) TextInputLayout mNoteLayout;
        @BindView(R.id.note) TextInputEditText mNote;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mNote.setImeOptions(EditorInfo.IME_ACTION_DONE);
            mNote.setRawInputType(InputType.TYPE_CLASS_TEXT);
        }
    }
}
