package com.coolcr.taobaocoupon.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.coolcr.taobaocoupon.R;
import com.coolcr.taobaocoupon.ui.custom.TextFlowLayout;
import com.coolcr.taobaocoupon.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {

    @BindView(R.id.btn_toast)
    Button btn_toast;
    @BindView(R.id.test_text_flow)
    TextFlowLayout textFlow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        initListener();

        List<String> textList = new ArrayList<>();
        textList.add("电脑");
        textList.add("口红");
        textList.add("粉底液");
        textList.add("衬衫");
        textList.add("耳机");
        textList.add("肥宅");
        textList.add("电竞椅 家用");
        textList.add("湿巾 儿童 酒精");
        textList.add("bilibili");
        textList.add("iphone");
        textFlow.setTextList(textList);
        textFlow.setOnFlowTextItemClickListener(new TextFlowLayout.OnFlowTextItemClickListener() {
            @Override
            public void onFlowItemClick(String text) {
                ToastUtil.showToast(text);
            }
        });
    }

    private void initListener() {
        btn_toast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast("测试Toast...");
            }
        });
    }
}
