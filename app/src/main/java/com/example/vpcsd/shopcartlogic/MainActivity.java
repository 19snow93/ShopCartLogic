package com.example.vpcsd.shopcartlogic;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.vpcsd.shopcartlogic.bean.ShopCartBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvShopCartSubmit,tvShopCartSelect,tvShopCartTotalNum;
    private View mEmtryView;

    private RecyclerView rlvShopCart,rlvHotProducts;
    private ShopCartAdapter mShopCartAdapter;
    private LinearLayout llPay;
    private RelativeLayout rlHaveProduct;
    private List<ShopCartBean.CartlistBean> mAllOrderList = new ArrayList<>();
    private ArrayList<ShopCartBean.CartlistBean> mGoPayList = new ArrayList<>();
    private List<String> mHotProductsList = new ArrayList<>();
    private TextView tvShopCartTotalPrice;
    private int mCount,mPosition;
    private float mTotalPrice1;
    private boolean mSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvShopCartSelect = (TextView) findViewById(R.id.tv_shopcart_addselect);
        tvShopCartTotalPrice = (TextView) findViewById(R.id.tv_shopcart_totalprice);
        tvShopCartTotalNum = (TextView) findViewById(R.id.tv_shopcart_totalnum);

        rlHaveProduct = (RelativeLayout) findViewById(R.id.rl_shopcart_have);
        rlvShopCart = (RecyclerView) findViewById(R.id.rlv_shopcart);
        mEmtryView = (View) findViewById(R.id.emtryview);
        mEmtryView.setVisibility(View.GONE);
        llPay = (LinearLayout) findViewById(R.id.ll_pay);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        llPay.setLayoutParams(lp);

        tvShopCartSubmit = (TextView) findViewById(R.id.tv_shopcart_submit);

        rlvShopCart.setLayoutManager(new LinearLayoutManager(this));
        mShopCartAdapter = new ShopCartAdapter(this,mAllOrderList);
        rlvShopCart.setAdapter(mShopCartAdapter);
        //删除商品接口
        mShopCartAdapter.setOnDeleteClickListener(new ShopCartAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(View view, int position,int cartid) {
                mShopCartAdapter.notifyDataSetChanged();
            }
        });
        //修改数量接口
        mShopCartAdapter.setOnEditClickListener(new ShopCartAdapter.OnEditClickListener() {
            @Override
            public void onEditClick(int position, int cartid, int count) {
                mCount = count;
                mPosition = position;
            }
        });
        //实时监控全选按钮
        mShopCartAdapter.setResfreshListener(new ShopCartAdapter.OnResfreshListener() {
            @Override
            public void onResfresh( boolean isSelect) {
                mSelect = isSelect;
                if(isSelect){
                    Drawable left = getResources().getDrawable(R.drawable.shopcart_selected);
                    tvShopCartSelect.setCompoundDrawablesWithIntrinsicBounds(left,null,null,null);
                }else {
                    Drawable left = getResources().getDrawable(R.drawable.shopcart_unselected);
                    tvShopCartSelect.setCompoundDrawablesWithIntrinsicBounds(left,null,null,null);
                }
                float mTotalPrice = 0;
                int mTotalNum = 0;
                mTotalPrice1 = 0;
                mGoPayList.clear();
                for(int i = 0;i < mAllOrderList.size(); i++)
                    if(mAllOrderList.get(i).getIsSelect()) {
                        mTotalPrice += Float.parseFloat(mAllOrderList.get(i).getPrice()) * mAllOrderList.get(i).getCount();
                        mTotalNum += 1;
                        mGoPayList.add(mAllOrderList.get(i));
                    }
                mTotalPrice1 = mTotalPrice;
                tvShopCartTotalPrice.setText("总价：" + mTotalPrice);
                tvShopCartTotalNum.setText("共" + mTotalNum + "件商品");
            }
        });

        //全选
        tvShopCartSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelect = !mSelect;
                if(mSelect){
                    Drawable left = getResources().getDrawable(R.drawable.shopcart_selected);
                    tvShopCartSelect.setCompoundDrawablesWithIntrinsicBounds(left,null,null,null);
                    for(int i = 0;i < mAllOrderList.size();i++){
                        mAllOrderList.get(i).setSelect(true);
                        mAllOrderList.get(i).setShopSelect(true);
                    }
                }else{
                    Drawable left = getResources().getDrawable(R.drawable.shopcart_unselected);
                    tvShopCartSelect.setCompoundDrawablesWithIntrinsicBounds(left,null,null,null);
                    for(int i = 0;i < mAllOrderList.size();i++){
                        mAllOrderList.get(i).setSelect(false);
                        mAllOrderList.get(i).setShopSelect(false);
                    }
                }
                mShopCartAdapter.notifyDataSetChanged();

            }
        });

        initData();
        mShopCartAdapter.notifyDataSetChanged();
    }

    private void initData(){
        for(int i = 0;i < 2;i ++){
            ShopCartBean.CartlistBean sb = new ShopCartBean.CartlistBean();
            sb.setShopId(1);
            sb.setPrice("1.0");
            sb.setDefaultPic("http://img2.3lian.com/2014/c7/25/d/40.jpg");
            sb.setProductName("狼牙龙珠鼠标");
            sb.setShopName("狼牙龙珠");
            sb.setColor("蓝色");
            sb.setCount(2);
            mAllOrderList.add(sb);
        }

        for(int i = 0;i < 2;i ++){
            ShopCartBean.CartlistBean sb = new ShopCartBean.CartlistBean();
            sb.setShopId(2);
            sb.setPrice("1.0");
            sb.setDefaultPic("http://img2.3lian.com/2014/c7/25/d/40.jpg");
            sb.setProductName("达尔优鼠标");
            sb.setShopName("达尔优");
            sb.setColor("绿色");
            sb.setCount(2);
            mAllOrderList.add(sb);
        }
        isSelectFirst(mAllOrderList);
    }

    public static void isSelectFirst(List<ShopCartBean.CartlistBean> list){
        if(list.size() > 0) {
            list.get(0).setIsFirst(1);
            for (int i = 1; i < list.size(); i++) {
                if (list.get(i).getShopId() == list.get(i - 1).getShopId()) {
                    list.get(i).setIsFirst(2);
                } else {
                    list.get(i).setIsFirst(1);
                }
            }
        }

    }
}
