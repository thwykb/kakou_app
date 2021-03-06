package com.sx.kakou.tricks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sx_kakou.R;
import com.google.gson.JsonArray;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.sx.kakou.view.MainActivity;

import org.json.JSONObject;

/**
 * Created by mglory on 2015/8/13.
 * HistoryItem
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private JsonArray marray;
    ImageLoader imageLoader;
    private MyItemClickListener mItemClickListener;
    public JsonArray getDataList(){
        return  marray;
    }
    public void setDataList(JsonArray jsonArray){
        this.marray = jsonArray;
    }

    public RecyclerViewAdapter() {
        imageLoader = ImageLoader.getInstance();
    }


    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public View rootView;
        public ImageView carinfo_img;
        public TextView hphm;
        public TextView jgsj;
        public TextView cllx;
        public TextView clpp;
        public TextView csys;
        public TextView hpys;
        public int position;
        public ViewHolder(View itemView) {
            super(itemView);
            carinfo_img = (ImageView) itemView.findViewById(R.id.h_catinfo_img);
            hphm = (TextView)itemView.findViewById(R.id.h_hphm);
            jgsj = (TextView)itemView.findViewById(R.id.h_jgsj);
            cllx = (TextView)itemView.findViewById(R.id.h_cllx);
            clpp = (TextView)itemView.findViewById(R.id.h_clpp);
            csys = (TextView)itemView.findViewById(R.id.h_csys);
            hpys = (TextView)itemView.findViewById(R.id.h_hpys);
            rootView = itemView.findViewById(R.id.card_view);
            rootView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener!=null){
                mItemClickListener.OnItemClick(getPosition());
            }
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)  {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_hsty_carinfo, parent, false);
        return new ViewHolder(view);
    }


    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //这里更新数据
        try{
            JSONObject mobject = new JSONObject(marray.get(position).toString());
            imageLoader.displayImage(mobject.getString("thumb_url"),holder.carinfo_img,getImageLoaderOpt());
            //imageLoader.displayImage(mobject.getString("imgurl"),holder.carinfo_img,getImageLoaderOpt());
            holder.hphm.setText(mobject.getString("hphm"));
            holder.jgsj.setText(mobject.getString("jgsj").substring(0, 10));
            holder.cllx.setText(mobject.getString("cllx"));
            holder.clpp.setText(mobject.getString("clpp"));
            holder.csys.setText(mobject.getString("csys"));
            holder.hpys.setText(mobject.getString("hpys"));
        }catch(Exception e){
        e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return marray.size();
    }

    public interface MyItemClickListener{
         void OnItemClick(int position);
    }

    /*
   * Imageloader的配置
   *
   * */
    public DisplayImageOptions getImageLoaderOpt(){
        final BitmapFactory.Options bo = new BitmapFactory.Options();
        bo.inJustDecodeBounds = true;
        bo.inSampleSize = calculateInSampleSize(bo,250,200);
        bo.inJustDecodeBounds = false;
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.skin_loading_icon) //加载时显示的页面
                .showImageForEmptyUri(R.drawable.board_gray)
                .showImageOnFail(R.drawable.board_gray)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .decodingOptions(bo)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new FadeInBitmapDisplayer(100)) // 设置加载后渐入动画时间
                .build();
        return options;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
}
