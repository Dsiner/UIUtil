package com.d.lib.common.component.lv;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * CommonHolder for ListView
 * Created by D on 2017/4/25.
 */
public class CommonHolder {
    private final SparseArray<View> mViews;
    private View mConvertView;
    public int mLayoutId;
    private int mPosition;

    private CommonHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mLayoutId = layoutId;
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    /**
     * Get the CommonHolder object
     */
    public static CommonHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new CommonHolder(context, parent, layoutId, position);
        }
        CommonHolder holder = (CommonHolder) convertView.getTag();
        if (holder.mLayoutId != layoutId) {
            return new CommonHolder(context, parent, layoutId, position);
        }
        holder.mPosition = position;
        return holder;
    }

    public int getPosition() {
        return mPosition;
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * Finds the first descendant view with the given ID
     */
    public <T extends View> T getView(@IdRes int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * Sets the text to be displayed
     */
    public CommonHolder setText(@IdRes int viewId, CharSequence text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;
    }

    /**
     * Changes the enabled state of this button.
     */
    public CommonHolder setEnable(@IdRes int viewId, boolean enable) {
        Button btn = getView(viewId);
        btn.setEnabled(enable);
        return this;
    }

    /**
     * Changes the checked state of this button.
     */
    public CommonHolder setChecked(@IdRes int viewId, boolean checked) {
        CheckBox checkBox = getView(viewId);
        checkBox.setChecked(checked);
        return this;
    }

    /**
     * Set the visibility state of this view.
     */
    public CommonHolder setVisibility(@IdRes int viewId, int visibility) {
        View view = getView(viewId);
        view.setVisibility(visibility);
        return this;
    }

    public CommonHolder setOnClickListener(@IdRes int viewId, @Nullable View.OnClickListener l) {
        View view = getView(viewId);
        view.setOnClickListener(l);
        return this;
    }

    public CommonHolder setTag(@IdRes int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public Object getTag(@IdRes int viewId) {
        return getView(viewId).getTag();
    }

    /**
     * Sets a drawable as the content of this ImageView.
     */
    public CommonHolder setImageResource(@IdRes int viewId, @DrawableRes int resId) {
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resId);
        return this;
    }

    /**
     * Sets a Bitmap as the content of this ImageView.
     */
    public CommonHolder setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
        ImageView imageView = getView(viewId);
        imageView.setImageBitmap(bitmap);
        return this;
    }

    /**
     * Set the background to a given resource.
     */
    public CommonHolder setBackgroundResource(@IdRes int viewId, @DrawableRes int resid) {
        View view = getView(viewId);
        view.setBackgroundResource(resid);
        return this;
    }

    /**
     * Sets the background color for this view.
     */
    public CommonHolder setBackgroundColor(@IdRes int viewId, @ColorInt int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * Sets the text color for all the states (normal, selected,
     * focused) to be this color.
     */
    public CommonHolder setTextColor(@IdRes int viewId, int res) {
        TextView textView = getView(viewId);
        textView.setTextColor(res);
        return this;
    }
}
