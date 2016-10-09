package com.grant.privately.file.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grant.privately.file.R;
import com.grant.privately.file.common.Constant;
import com.grant.privately.file.db.TbFileEncodeInfoHelper;
import com.grant.privately.file.db.entry.FileEncodeInfo;
import com.grant.privately.file.fragment.dummy.MediaPathEntry;
import com.grant.privately.file.fragment.dummy.MediaType;
import com.grant.privately.file.task.FileDecodeTask;
import com.grant.privately.file.task.FileEncodeTask;
import com.grant.privately.file.task.OnTaskFinishedListener;
import com.grant.privately.file.utils.ImageLoaderHelper;
import com.grant.privately.file.utils.MediaUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FileItemFragment extends Fragment implements DialogInterface.OnCancelListener{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_TAB_ID = "tabId";
    private static final String ARG_MEDIA_TYPE = "listId";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private int mTabId;
    private int mListId;
    private OnListFragmentInteractionListener mListener;

    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private FileItemRecyclerViewAdapter fileItemRecyclerViewAdapter;
    private ItemTask itemTask;

    /**操作选项*/
    private int optionId = -1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FileItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FileItemFragment newInstance(int tabId, MediaType mediaType) {
        FileItemFragment fragment = new FileItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, 4);
        args.putInt(ARG_TAB_ID, tabId);
        args.putInt(ARG_MEDIA_TYPE, mediaType.getIndex());

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            mTabId = getArguments().getInt(ARG_TAB_ID);
            mListId = getArguments().getInt(ARG_MEDIA_TYPE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
                recyclerView.addItemDecoration(new SpaceItemDecoration(5));
            }
            recyclerView.setOnScrollListener(ImageLoaderHelper.getRecyclerViewPauseOnScrollListener());

            progressDialog = new ProgressDialog(getContext());
            progressDialog.setOnCancelListener(this);
            progressDialog.setCanceledOnTouchOutside(false);
            //执行查询
            itemTask = new ItemTask();
            itemTask.execute();

        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (itemTask!=null){
            itemTask.cancel(true);
            itemTask = null;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(MediaPathEntry item);
    }

    private class ItemTask extends AsyncTask<Object,Integer,List<MediaPathEntry>>{
        @Override
        protected void onPreExecute() {

            progressDialog.show();

        }

        @Override
        protected List<MediaPathEntry> doInBackground(Object... params) {
            MediaType mediaType = MediaType.values()[mListId];
            List<MediaPathEntry>datas = null;
            switch (mTabId){

                case 0://已经加密的
                    TbFileEncodeInfoHelper helper = new TbFileEncodeInfoHelper(getContext());
                    List<FileEncodeInfo> fileEncodeInfoList = helper.findAll(mediaType);
                    if (fileEncodeInfoList!=null && !fileEncodeInfoList.isEmpty()){
                        datas = new ArrayList<>();
                        byte[]buff = new byte[64*1024];
                        for (FileEncodeInfo info:fileEncodeInfoList){
                            MediaPathEntry entry = new MediaPathEntry();
                            entry.setThumbPath(info.getFileShot());
                            entry.setMediaType(info.getMediaType());

                            FileOutputStream fos = null;
                            FileInputStream fis = null;
                            try {
                                File encodeFile = new File(info.getNewFilePath());

                                File tempFile = File.createTempFile(info.getOldFileName(),null,getContext().getExternalCacheDir());
                                fos = new FileOutputStream(tempFile);
                                fos.write(info.getFileHead());
                                fis = new FileInputStream(encodeFile);
                                int readSize = 0;
                                while((readSize = fis.read(buff))!=-1){
                                    fos.write(buff,0,readSize);
                                }
                                fos.flush();
                                entry.setImgPath(tempFile.getAbsolutePath());
                                datas.add(entry);
                            }catch (Exception e){
                                e.printStackTrace();
                            }finally {if (fis!=null){
                                try {
                                    fis.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                                if (fos!=null){
                                    try {
                                        fos.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }


                        }
                    }
                    break;
                case 1://未加密的
                    switch (mediaType){
                        case IMAGE:
                            datas = new MediaUtil().scanImages(getContext());
                            break;
                        case VIDEO:
                            datas = new MediaUtil().scanVideo(getContext());
                            break;
                        case VOICE:
                            break;
                    }
                    break;
            }

            return datas;
        }

        @Override
        protected void onPostExecute(List<MediaPathEntry> o) {
            if (o!=null){
                fileItemRecyclerViewAdapter = new FileItemRecyclerViewAdapter(o, mListener);
                recyclerView.setAdapter(fileItemRecyclerViewAdapter);
            }else {

            }
            progressDialog.dismiss();

        }

        @Override
        protected void onCancelled(List<MediaPathEntry> mediaPathEntries) {


        }
    }

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        int mSpace ;

        public SpaceItemDecoration(int space) {
            this.mSpace = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int itemCount = parent.getAdapter().getItemCount();
            int pos = parent.getChildAdapterPosition(view);
            outRect.left = 2;
            outRect.top = 2;
            outRect.bottom = 2;

            if (pos != (itemCount -1)) {
                outRect.right = mSpace;
            } else {
                outRect.right = 2;
            }
        }
    }

    public void setOptionId(int optionId){
        this.optionId = optionId;
        if (this.optionId == Constant.OPTION_ID_ENCODE && fileItemRecyclerViewAdapter !=null){
            new FileEncodeTask(progressDialog,new TaskFinishedListener()).execute(fileItemRecyclerViewAdapter.getValues());
            return;
        }

        if (this.optionId == Constant.OPTION_ID_DECODE && fileItemRecyclerViewAdapter !=null){
            new FileDecodeTask(progressDialog,new TaskFinishedListener()).execute(fileItemRecyclerViewAdapter.getValues());
            return;
        }



        if (fileItemRecyclerViewAdapter !=null){
            fileItemRecyclerViewAdapter.setOptionId(optionId);
        }


    }

    private class TaskFinishedListener implements OnTaskFinishedListener{
        @Override
        public void onFinished() {
            if (fileItemRecyclerViewAdapter!=null){
                fileItemRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }
}
