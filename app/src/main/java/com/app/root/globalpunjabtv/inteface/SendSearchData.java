package com.app.root.globalpunjabtv.inteface;

import com.app.root.globalpunjabtv.models.PostGetSet;

import java.util.List;

public interface SendSearchData {
    public void sendtoActivity(List<PostGetSet> list);

    public void sendtoFragment(List<PostGetSet> list);
}
