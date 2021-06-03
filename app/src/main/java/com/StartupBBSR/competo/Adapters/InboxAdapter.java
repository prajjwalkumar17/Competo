package com.StartupBBSR.competo.Adapters;

import android.provider.Telephony;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.StartupBBSR.competo.Fragments.ConversationsFragment;
import com.StartupBBSR.competo.Fragments.MessageRequestFragment;

public class InboxAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public InboxAdapter(FragmentManager fm,int mNumOfTabs){
        super(fm);
        this.mNumOfTabs=mNumOfTabs;
    }
    @Override
    public Fragment getItem(int position){
      switch (position) {
          case 0:
              return new ConversationsFragment();
          case 1:
              return new MessageRequestFragment();
          default:
              return null;
      }
    }

    @Override
    public int getCount() {
       return mNumOfTabs;
    }
}
