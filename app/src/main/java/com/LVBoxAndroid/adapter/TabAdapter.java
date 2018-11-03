package com.LVBoxAndroid.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.LVBoxAndroid.fragment.Contacts.ContactsFragment;
import com.LVBoxAndroid.fragment.Files.FilesFragment;

public class TabAdapter extends FragmentStatePagerAdapter{

    private String[] tapTitles = {"Contatos","Arquivos"};

    public TabAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0 :
                fragment = new ContactsFragment();
                break;
            case 1 :
                fragment = new FilesFragment();
                break;
        }

        return fragment;
    }


    @Override
    public int getCount() {
        return tapTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tapTitles[position];
    }
}
