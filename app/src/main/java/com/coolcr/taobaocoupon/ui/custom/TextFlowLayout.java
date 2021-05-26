package com.coolcr.taobaocoupon.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coolcr.taobaocoupon.R;
import com.coolcr.taobaocoupon.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class TextFlowLayout extends ViewGroup {

    public static final float DEFAULT_SPACE = 4;

    private float mItemHorizontalSpace = DEFAULT_SPACE;
    private float mItemVerticalSpace = DEFAULT_SPACE;
    private int mSelfWidth;
    private int mItemHeight;

    private OnFlowTextItemClickListener mItemClickListener = null;

    /**
     * 暴露方法让外部去设置相关属性
     *
     * @return
     */
    public float getItemHorizontalSpace() {
        return mItemHorizontalSpace;
    }

    public void setItemHorizontalSpace(float itemHorizontalSpace) {
        this.mItemHorizontalSpace = itemHorizontalSpace;
    }

    public float getItemVerticalSpace() {
        return mItemVerticalSpace;
    }

    public void setItemVerticalSpace(float itemVerticalSpace) {
        this.mItemVerticalSpace = itemVerticalSpace;
    }

    private List<String> mTextList = new ArrayList<>();

    public TextFlowLayout(Context context) {
        this(context, null);
    }

    public TextFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //去拿到相关属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowTextStyle);

        mItemHorizontalSpace = typedArray.getDimension(R.styleable.FlowTextStyle_horizontalSpace, DEFAULT_SPACE);
        mItemVerticalSpace = typedArray.getDimension(R.styleable.FlowTextStyle_verticalSpace, DEFAULT_SPACE);

        //释放资源
        typedArray.recycle();

        LogUtils.d(this, "mItemHorizontalSpace -- > " + mItemHorizontalSpace);
        LogUtils.d(this, "mItemVerticalSpace -- > " + mItemVerticalSpace);
    }

    public void setTextList(List<String> textList) {
        this.mTextList = textList;
        //遍历内容
        for (String text : mTextList) {
            //添加子View
            TextView item = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view, this, false);
            item.setText(text);
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onFlowItemClick(text);
                }
            });
            addView(item);
        }
    }


    //这是描述所有的行
    private List<List<View>> lines = new ArrayList<>();


    // onMeasure 会多次调用
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //这是描述单行
        List<View> line = null;
        lines.clear();
        mSelfWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        LogUtils.d(this, "selfWidth -- > " + mSelfWidth);
        //测量
        LogUtils.d(this, "onMeasure -- > " + getChildCount());
        //测量孩子
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View itemView = getChildAt(i);
            if (itemView.getVisibility() != VISIBLE) {
                //不需要进行测绘
                continue;
            }
            //测量前
            LogUtils.d(this, "height -- > " + itemView.getMeasuredHeight());
            measureChild(itemView, widthMeasureSpec, heightMeasureSpec);
            //测量后
            LogUtils.d(this, "height -- > " + itemView.getMeasuredHeight());

            if (line == null) {
                //说明当前行空，可以添加
                line = createNewLine(itemView);
            } else {
                if (canBeAdd(itemView, line)) {
                    //可以添加
                    line.add(itemView);
                } else {
                    // 创建一行
                    line = createNewLine(itemView);
                }
            }
        }

        mItemHeight = getChildAt(0).getMeasuredHeight();
        int selfHeight = (int) (lines.size() * mItemHeight + mItemVerticalSpace * (lines.size() + 1) + 0.5f);

        //测量自己
        setMeasuredDimension(mSelfWidth, selfHeight);
    }

    private List<View> createNewLine(View itemView) {
        ArrayList<View> line = new ArrayList<>();
        line.add(itemView);
        lines.add(line);
        return line;
    }

    /**
     * 判断当前行是否可以继续添加新数据
     *
     * @param itemView
     * @param line
     */
    private boolean canBeAdd(View itemView, List<View> line) {
        //所有的已经添加的子View宽度相加+line。size() + 1 * 间距 + item.getMeasureWidth()
        //如果小于等于当前控件的宽度，则可以添加，否则不能添加
        int totalWith = itemView.getMeasuredWidth();
        for (View view : line) {
            totalWith += view.getMeasuredWidth();
        }
        //水平间距的宽度
        totalWith += mItemHorizontalSpace * (line.size() + 1);
        LogUtils.d(this, "totalWith -- > " + totalWith);
        if (totalWith <= mSelfWidth) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //摆放孩子
        LogUtils.d(this, "onLayout -- > " + getChildCount());

        int topOffset = (int) mItemHorizontalSpace;
        for (List<View> views : lines) {
            //views是每一行
            int leftOffset = (int) mItemHorizontalSpace;
            for (View view : views) {
                //view是每一行里面的item
                view.layout(leftOffset, topOffset, leftOffset + view.getMeasuredWidth(), topOffset + view.getMeasuredHeight());
                leftOffset += view.getMeasuredWidth() + mItemHorizontalSpace;
            }
            topOffset += mItemHeight + mItemHorizontalSpace;
        }

    }

    public void setOnFlowTextItemClickListener(OnFlowTextItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public interface OnFlowTextItemClickListener {
        void onFlowItemClick(String text);
    }
}
