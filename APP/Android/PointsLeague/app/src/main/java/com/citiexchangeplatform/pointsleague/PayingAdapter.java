package com.citiexchangeplatform.pointsleague;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class PayingAdapter extends RecyclerView.Adapter<PayingAdapter.MyViewHolder>implements Filterable {
    //数据源
    private List<String> maxExchangePoints;
    private List<String> exchangePoints;
    private List<String> targetPoints;
    private List<Double> rates;
    private List<String> names;
    private List<String> logos;
    private List<Integer> img_list;
    private Context context;
    //是否显示单选框,默认false
    //private boolean isshowBox = false;
    private int total;

    private HashMap<Integer, Boolean> map = new HashMap<>();
    private HashMap<Integer, Double> totals = new HashMap<>();
    private ButtonInterface buttonInterface;
    private CheckBoxInterface checkBoxInterface;

    KeyListener storedKeylistener;



    //构造方法
    public PayingAdapter(Context context) {

        maxExchangePoints = new ArrayList<String>();
        exchangePoints = new ArrayList<String>();
        targetPoints = new ArrayList<String>();
        rates = new ArrayList<Double>();
        names = new ArrayList<String>();
        logos = new ArrayList<String>();
        img_list = new ArrayList<Integer>();
        this.context = context;
        this.total = 0;
        initMap();
    }

    public void addData(String posses, String target,String rate, String name, String logoURL) {
        maxExchangePoints.add(posses);
        exchangePoints.add(posses);
        targetPoints.add(target);
        rates.add(Double.parseDouble(rate));
        names.add(name);
        logos.add(logoURL);
        notifyDataSetChanged();
    }

    //初始化map集合,默认为不选中
    private void initMap() {
        for (int i = 0; i < 100; i++) {
            map.put(i, false);
        }
    }


    /**
     *按钮点击事件需要的方法
     */
    public void buttonSetOnclick(ButtonInterface buttonInterface){
        this.buttonInterface=buttonInterface;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    //mFilterList = mSourceList;
                } else {
                    List<String> filteredList = new ArrayList<>();
                    //for (String str : mSourceList) {
                    //    //这里根据需求，添加匹配规则
                    //    if (str.contains(charString)) {
                    //        filteredList.add(str);
                    //    }
                    //}
                    //
                    //mFilterList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                //filterResults.values = mFilterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                //mFilterList = (ArrayList<String>) filterResults.values;
                //刷新数据
                notifyDataSetChanged();
            }
        };
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface ButtonInterface{
        public void onclick( View view,int position);
    }


    /**
     *checkbox点击事件需要的方法
     */
    public void checkBoxSetOnclick(CheckBoxInterface checkBoxInterface){
        this.checkBoxInterface=checkBoxInterface;
    }

    /**
     * checkbox点击事件对应的接口
     */
    public interface CheckBoxInterface{
        public void onclick( View view,int position);
    }


    @NonNull
    @Override
    public PayingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*列表布局*/
        return new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item_paying, parent, false));
    }

    /*为列表内容配置数据*/
    @Override
    public void onBindViewHolder(@NonNull final PayingAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.setIsRecyclable(false);
        //设置列表中积分信息
        holder.editPoint.setText(exchangePoints.get(position));
        holder.exchangePoint.setText(targetPoints.get(position));
        //设置商家图片
        Glide.with(context)
                .load(logos.get(position))
                .placeholder(R.drawable.ic_points_black_24dp)
                .error(R.drawable.ic_mall_black_24dp)
                .override(60,60)
                .into(holder.logo);
        //holder.logo.setImageResource(img_list.get(position));
        //设置商户名
        holder.name.setText(names.get(position));
        //初始编辑框为不可编辑状态
        holder.editPoint.setFocusable(false);
        holder.editPoint.setFocusableInTouchMode(false);

        holder.modifyButton.setOnClickListener(new View.OnClickListener() {
            Boolean button_status = false;
            @Override
            public void onClick(View v) {
                if(!button_status){
                    holder.modifyButton.setBackgroundResource(R.drawable.ic_check_24dp);
                    //编辑框可修改
                    holder.editPoint.setFocusableInTouchMode(true);
                    holder.editPoint.setFocusable(true);
                    holder.editPoint.requestFocus();
                    button_status = true;
                }

                else{
                    holder.modifyButton.setBackgroundResource(R.drawable.ic_modify_24dp);
                    holder.editPoint.setFocusable(false);
                    holder.editPoint.setFocusableInTouchMode(false);
                    button_status = false;
                }

                if(buttonInterface!=null) {
                    //接口实例化后的而对象，调用重写后的方法
                    buttonInterface.onclick(v,position);
                }

            }
        });

        holder.Checkbox_Choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(checkBoxInterface!=null) {
                    //接口实例化后的而对象，调用重写后的方法
                    checkBoxInterface.onclick(v,position);
                }

            }
        });

        //设置checkBox改变监听
        holder.Checkbox_Choose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Boolean previous_status = map.get(position);
                //用map集合保存
                map.put(position, isChecked);

                //计算选择的积分可转换成的通用积分
                double add_point = Double.parseDouble(holder.exchangePoint.getText().toString());

                if(map.get(position)){
                    if(!totals.containsKey(position))
                     totals.put(position,add_point);
                    total = 0;
                    for (Integer key : totals.keySet()) {
                        total += totals.get(key);
                    }
                }
                else if(previous_status&&!map.get(position)&&totals.containsKey(position)){
                    totals.remove(position);
                    total = 0;
                    for (Integer key : totals.keySet()) {
                        total += totals.get(key);
                    }
                    //total -= add_point;
                }

                if (map.get(position)) {
                    holder.modifyButton.setVisibility(View.VISIBLE);

                } else {
                    holder.modifyButton.setVisibility(View.INVISIBLE);
                    //取消选择后编辑框为不可编辑状态
                    holder.editPoint.setFocusable(false);
                    holder.editPoint.setFocusableInTouchMode(false);
                    holder.editPoint.setText(maxExchangePoints.get(position));

                }

            }
        });


        // 设置CheckBox的状态
        if (map.get(position) == null) {
            map.put(position, false);
        }
        holder.Checkbox_Choose.setChecked(map.get(position));
        if (map.get(position)) {
            holder.modifyButton.setVisibility(View.VISIBLE);
        } else {
            holder.modifyButton.setVisibility(View.INVISIBLE);
        }


        if (holder.editPoint.getTag() instanceof TextWatcher) {
            holder.editPoint.removeTextChangedListener((TextWatcher) holder.editPoint.getTag());
        }

        //编辑框中输入文字监听
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double rate = rates.get(position);
                double exchangedPoint = 0;
                if(s.length()!=0){
                    exchangedPoint = Double.parseDouble(s.toString())*rate;
                    exchangePoints.set(position,s.toString());

                    //超出最大值，自动更新为最大值
                    if(Double.parseDouble(s.toString()) > Double.parseDouble(maxExchangePoints.get(position))){
                        exchangedPoint = Double.parseDouble(maxExchangePoints.get(position))*rate;
                        exchangePoints.set(position,maxExchangePoints.get(position));
                        holder.editPoint.setText(maxExchangePoints.get(position));
                        Toast.makeText(context, "超出最大值，已自动更新为最大值", Toast.LENGTH_SHORT).show();
                    }
                }



                targetPoints.set(position,String.valueOf(exchangedPoint));
                holder.exchangePoint.setText(String.valueOf(exchangedPoint));
                //notifyItemChanged(position);
            }

            @Override
            public void afterTextChanged(Editable s) {
                holder.editPoint.removeTextChangedListener(this);
                double rate = rates.get(position);
                double exchangedPoint = 0;
                if(s.length()!=0){
                    exchangedPoint = Double.parseDouble(s.toString())*rate;
                    exchangePoints.set(position,s.toString());
                    //超出最大值，自动更新为最大值
                    if(Double.parseDouble(s.toString()) > Double.parseDouble(maxExchangePoints.get(position))){
                        exchangedPoint = Double.parseDouble(maxExchangePoints.get(position))*rate;
                        exchangePoints.set(position,maxExchangePoints.get(position));
                        holder.editPoint.setText(maxExchangePoints.get(position));
                        Toast.makeText(context, "超出最大值，已自动更新为最大值", Toast.LENGTH_SHORT).show();
                    }
                }
                holder.exchangePoint.setText(String.valueOf(exchangedPoint));

                targetPoints.set(position,String.valueOf(exchangedPoint));

                //修改total值
                double add_point = Double.parseDouble(holder.exchangePoint.getText().toString());

                if(totals.get(position)!=null){
                    totals.put(position,add_point);
                }

                total = 0;
                for (Integer key : totals.keySet()) {
                    total += totals.get(key);
                }

                //notifyItemChanged(position);
                holder.editPoint.addTextChangedListener(this);
            }
        };

        holder.editPoint.addTextChangedListener(watcher);
        holder.editPoint.setTag(watcher);

        //编辑框中输入文字监听
        //holder.editPoint.addTextChangedListener(new TextWatcher() {
        //    @Override
        //    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //        //double rate = rates.get(position);
        //        //double exchangedPoint = 0;
        //        //if(s.length()!=0){
        //        //    exchangedPoint = Double.parseDouble(s.toString())*rate;
        //        //}
        //        //
        //        //holder.exchangePoint.setText(String.valueOf(exchangedPoint));
        //
        //    }
        //
        //    @Override
        //    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //        double rate = rates.get(position);
        //        double exchangedPoint = 0;
        //        if(s.length()!=0){
        //            exchangedPoint = Double.parseDouble(s.toString())*rate;
        //        }
        //
        //
        //        holder.exchangePoint.setText(String.valueOf(exchangedPoint));
        //
        //    }
        //
        //    @Override
        //    public void afterTextChanged(Editable s) {
        //        holder.editPoint.removeTextChangedListener(this);
        //        double rate = rates.get(position);
        //        double exchangedPoint = 0;
        //        if(s.length()!=0){
        //            exchangedPoint = Double.parseDouble(s.toString())*rate;
        //        }
        //        holder.exchangePoint.setText(String.valueOf(exchangedPoint));
        //        exchangePoints.set(position,s.toString());
        //        targetPoints.set(position,String.valueOf(exchangedPoint));
        //        //修改total值
        //        double add_point = Double.parseDouble(holder.exchangePoint.getText().toString());
        //
        //        if(totals.get(position)!=null){
        //            totals.put(position,add_point);
        //        }
        //
        //        total = 0;
        //        for (Integer key : totals.keySet()) {
        //            total += totals.get(key);
        //        }
        //
        //
        //        holder.editPoint.addTextChangedListener(this);
        //
        //    }
        //});


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //private void specialUpdate(final int item_position) {
    //    Handler handler = new Handler();
    //    final Runnable r = new Runnable() {
    //        public void run() {
    //            notifyItemChanged(item_position);
    //        }
    //    };
    //    handler.post(r);
    //}


    /*返回列表长度*/
    @Override
    public int getItemCount() {
        return maxExchangePoints.size();
    }

    //设置是否显示CheckBox
    //public void setShowBox() {
    //    //取反
    //    isshowBox = !isshowBox;
    //}

    /*返回总计价格*/
    public int getTotal() {
        return total;
    }

    public List<String> getTargetPoints() {
        return targetPoints;
    }

    public List<String> getLogos() {
        return logos;
    }

    public List<String> getNames() {
        return names;
    }

    public List<String> getExchangePoints() {
        return exchangePoints;
    }

    //点击item选中CheckBox
    public void setSelectItem(int position) {
        //对当前状态取反
        if (map.get(position)) {
            map.put(position, false);
        } else {
            map.put(position, true);
        }
        notifyItemChanged(position);
    }

    //返回集合给MainActivity
    public HashMap<Integer, Double> getMap() {
        return totals;
    }


    /**
     * ViewHolder类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        View view;
        //转换的通用积分
        TextView exchangePoint;
        //会员卡商家logo
        ImageView logo;
        //选择框
        CheckBox Checkbox_Choose;
        //修改按钮
        Button modifyButton;
        //输入使用的会员卡积分数
        EditText editPoint;
        //会员卡商户名
        TextView name;



        public MyViewHolder(View view) {
            super(view);
            exchangePoint = view.findViewById(R.id.textview_points_transfer);
            logo = view.findViewById(R.id.image_business);
            Checkbox_Choose = view.findViewById(R.id.checkbox_choose);
            modifyButton = view.findViewById(R.id.button_modify);
            editPoint = view.findViewById(R.id.editText_points_posses);
            name = view.findViewById(R.id.textview_business_name);

            // 保存默认的KeyListener以便恢复
            storedKeylistener = editPoint.getKeyListener();
        }


    }
}