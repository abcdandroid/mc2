package com.example.mechanic2.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.mechanic2.R;
import com.example.mechanic2.app.Application;
import com.example.mechanic2.interfaces.TagMessage;

import java.util.ArrayList;
import java.util.List;

public class TagLayoutView extends RelativeLayout {
    Context context;
    AttributeSet attrs;
    ViewGroup parent;
    List<String> tagViewTitles = new ArrayList<>();
    TagMessage tagMessage;

    public TagMessage getTagMessage() {
        return tagMessage;
    }

    public void setTagMessage(TagMessage tagMessage) {
        this.tagMessage = tagMessage;
    }

    public TagLayoutView(Context context) {
        super(context);
        init(context, null);
    }

    public TagLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);

    }


    public TagLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    public TagLayoutView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    public void addTag(TagView tagView) {
        if (!tagViewTitles.contains(tagView.getTagTitle())) {
            if (checkSize()) return;
            tagViewTitles.add(tagView.getTagTitle());
            parent.addView(tagView);
            ImageView removeTag = tagView.findViewById(R.id.remove_tag);
            removeTag.setOnClickListener(view -> {
                removeTag(tagView);
                if (checkSize()) return;
                if (tagView.getOnTagCrossClickListener() != null)
                    tagView.getOnTagCrossClickListener().onClick();
            });
            invalidate();
        }
    }

    public void addTags(List<String> tagViewTitles){
        for (String title:tagViewTitles) {
            TagView tagView=new TagView(Application.getContext());
            tagView.setTagTitle(title);
            addTag(tagView);
        }
    }

    private boolean checkSize() {
        if (tagViewTitles.size() >= 3) {
            getTagMessage().showError();
            return true;
        }
        return false;
    }

    public List<String> getAllTags() {
        return tagViewTitles;
    }

    public void removeTag(TagView tagView) {
        parent.removeView(tagView);
        tagViewTitles.remove(tagView.getTagTitle());
    }


    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        this.attrs = attrs;
        View v = inflate(context, R.layout.view_tag_layout, this);
        parent = v.findViewById(R.id.parent);

    }


}
