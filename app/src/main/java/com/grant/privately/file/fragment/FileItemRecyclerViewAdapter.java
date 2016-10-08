package com.grant.privately.file.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.grant.privately.file.R;
import com.grant.privately.file.common.Constant;
import com.grant.privately.file.fragment.FileItemFragment.OnListFragmentInteractionListener;
import com.grant.privately.file.fragment.dummy.MediaPathEntry;
import com.grant.privately.file.utils.ImageLoaderHelper;

import java.util.List;

/**
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class FileItemRecyclerViewAdapter extends RecyclerView.Adapter<FileItemRecyclerViewAdapter.ViewHolder> {

    private final List<MediaPathEntry> mValues;
    private final OnListFragmentInteractionListener mListener;
    /**操作选项*/
    private int optionId = -1;
    private boolean showCheckbox = false;

    public FileItemRecyclerViewAdapter(List<MediaPathEntry> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        if (optionId == Constant.OPTION_ID_CHECKED_ALL){
            holder.mItem.setSelected(true);
        }else if(optionId == Constant.OPTION_ID_CHECKED_HIDDEN){
            holder.mItem.setSelected(false);
        }

        holder.checkBox.setVisibility(showCheckbox?View.VISIBLE:View.GONE);
        holder.checkBox.setChecked(holder.mItem.isSelected());
        ImageLoaderHelper.displayImageViewNoRadius(ImageLoaderHelper.PRIFIX_FILE+holder.mItem.getThumbPath(),holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showCheckbox = true;
                holder.checkBox.performClick();
                notifyDataSetChanged();
                return true;
            }
        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = holder.checkBox.isChecked();
                holder.mItem.setSelected(checked);
                holder.checkBox.setChecked(checked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imageView;
        public final CheckBox checkBox;
        public MediaPathEntry mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imageView = (ImageView) view.findViewById(R.id.private_file_imageView);
            checkBox = (CheckBox) view.findViewById(R.id.private_file_iv_seleted);
        }

    }

    public void setOptionId(int optionId){
        this.optionId = optionId;
        showCheckbox = optionId != Constant.OPTION_ID_CHECKED_HIDDEN;

        if (optionId == Constant.OPTION_ID_CHECKED_INVERT){
            for (int i=0;i< getItemCount();i++){
                MediaPathEntry entry = mValues.get(i);
                entry.setSelected(!entry.isSelected());
            }
        }
        notifyDataSetChanged();
    }

    public List<MediaPathEntry> getValues(){
        return mValues;
    }

}
