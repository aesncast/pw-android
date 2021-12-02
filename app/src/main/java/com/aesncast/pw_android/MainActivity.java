package com.aesncast.pw_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TabLayoutMediator.TabConfigurationStrategy {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private RecentPasswordsFragment recentPasswordsFragment;
    private DomainsUsersFragment domainsUsersFragment;
    private SequencesFragment sequencesFragment;
    private List<String> titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.main_tab_layout);
        viewPager = findViewById(R.id.main_view_pager);

        setViewPagerAdapter();
        new TabLayoutMediator(tabLayout, viewPager, this).attach();
    }

    public void setViewPagerAdapter()
    {
        ViewPagerFragmentAdapter a = new ViewPagerFragmentAdapter(getSupportFragmentManager(), getLifecycle());
        titles = new ArrayList<>();

        titles.add(getString(R.string.recent));
        recentPasswordsFragment = new RecentPasswordsFragment();

        titles.add(getString(R.string.domains_and_users));
        domainsUsersFragment = new DomainsUsersFragment();

        titles.add(getString(R.string.sequences));
        sequencesFragment = new SequencesFragment();

        a.addFragment(recentPasswordsFragment);
        a.addFragment(domainsUsersFragment);
        a.addFragment(sequencesFragment);

        viewPager.setAdapter(a);
    }

    @Override
    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        tab.setText(titles.get(position));
    }

    public static class ViewPagerFragmentAdapter extends FragmentStateAdapter {

        private List<Fragment> fragments = new ArrayList<>();

        public ViewPagerFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        public void setFragments(List<Fragment> fragments) {
            this.fragments = fragments;
        }

        public void addFragment(Fragment fragment) {
            fragments.add(fragment);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }
    }
}