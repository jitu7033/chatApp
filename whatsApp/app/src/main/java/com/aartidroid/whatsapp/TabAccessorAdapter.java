package com.aartidroid.whatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAccessorAdapter extends FragmentPagerAdapter {

    public TabAccessorAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                ChatsFragment ChatsFragment = new ChatsFragment();
                return ChatsFragment;
            case 1:
                  GroupsFragment  GroupsFragment= new GroupsFragment();
                return GroupsFragment;
            case 2:
                ContactsFragment ContactsFragment= new ContactsFragment();
                return ContactsFragment;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int i) {

        switch (i) {
            case 0:
                return "Chats";
            case 1:
                return "Group";
            case 2:
                return "Contacts";
            default:
                return null;
        }
    }
}
