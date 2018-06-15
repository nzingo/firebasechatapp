package com.boutik.nadhir.firebasechatapp.adapters;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.boutik.nadhir.firebasechatapp.fragments.ChatsFragment;
import com.boutik.nadhir.firebasechatapp.fragments.UsersFragment;

public class PagerAdapter extends FragmentPagerAdapter {


    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:

                UsersFragment tab1 = new UsersFragment();
                return tab1;
            case 1:

                ChatsFragment tab2 = new ChatsFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:

                return "USERS";
            case 1:

                return "CHATS";
            default:
                return null;
        }    }

    @Override
    public int getCount() {
        return 2;
    }
}