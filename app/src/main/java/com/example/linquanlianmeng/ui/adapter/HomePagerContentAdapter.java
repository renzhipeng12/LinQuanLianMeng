package com.example.linquanlianmeng.ui.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.linquanlianmeng.R;
import com.example.linquanlianmeng.model.domain.IBaseInfo;
import com.example.linquanlianmeng.model.domain.ILinearItemInfo;
import com.example.linquanlianmeng.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePagerContentAdapter extends RecyclerView.Adapter<HomePagerContentAdapter.InnerHolder> {

    List<ILinearItemInfo> mData = new ArrayList<>();

//    private int testCount = 1;
    private OnListItemClickListener mItemClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //        LogUtils.d(this,"onCreateViewHolder..." + testCount);
//        testCount++;
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_linear_goods_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        //        LogUtils.d(this,"onBindViewHolder..." + position);

        ILinearItemInfo dataBean = mData.get(position);
        //设置数据
        holder.setData(dataBean);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(dataBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<? extends ILinearItemInfo> contents) {
        mData.clear();
        mData.addAll(contents);
        notifyDataSetChanged();
    }

    public void addData(List<? extends ILinearItemInfo> contents) {
        //添加之前拿到原来的size
        int olderSize = mData.size();
        mData.addAll(contents);
        //更新UI
        notifyItemRangeChanged(olderSize, contents.size());
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.goods_cover)
        public ImageView coverIv;

        @BindView(R.id.goods_title)
        public TextView title;

        @BindView(R.id.goods_off_prise)
        public TextView offPriceTv;

        @BindView(R.id.goods_after_off_prise)
        public TextView finalPriseTv;

        @BindView(R.id.goods_original_prise)
        public TextView originalPriseTv;

        @BindView(R.id.goods_sell_count)
        public TextView sellCountTv;


        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(ILinearItemInfo dataBean) {
            Context context = itemView.getContext();
            title.setText(dataBean.getTitle());
            //            LogUtils.d(this,"url ----"+dataBean.getPict_url());

            //ViewGroup.LayoutParams params = cover.getLayoutParams();
            /*int width = params.width;
            int height = params.height;
            int coverSize = (width > height ? width : height) / 2;*/
            String cover = dataBean.getCover();
            if (!TextUtils.isEmpty(cover)) {
                String coverPath = UrlUtils.getCoverPath(dataBean.getCover());
                Glide.with(context).load(coverPath).into(this.coverIv);
            } else {
                coverIv.setImageResource(R.mipmap.ic_launcher);
            }
            //LogUtils.d(this,coverPath);

            long couponAmount = dataBean.getCouponAmount();
            String finalPrise = dataBean.getFinalPrise();
            //            LogUtils.d(this, "final prise ---- " + finalPrise);
            float resultPrise = Float.parseFloat(finalPrise) - couponAmount;
            //            LogUtils.d(this, "result prise ---- " + resultPrise);
            finalPriseTv.setText(String.format("%.2f", resultPrise));

            originalPriseTv.setText(String.format(context.getString(R.string.text_goods_original_prise), finalPrise));
            originalPriseTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            sellCountTv.setText(String.format(context.getString(R.string.text_goods_sell_count), dataBean.getVolume()));

            offPriceTv.setText(String.format(context.getString(R.string.text_goods_off_prise), couponAmount));

        }
    }

    public void setOnListItemClickListener(OnListItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface OnListItemClickListener {
        void onItemClick(IBaseInfo item);
    }
}
