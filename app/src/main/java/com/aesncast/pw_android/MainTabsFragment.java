package com.aesncast.pw_android;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainTabsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainTabsFragment extends Fragment implements TabLayoutMediator.TabConfigurationStrategy {
    private static final int INDEX_RECENT = 0;
    private static final int INDEX_DOMAINS_AND_USERS = 1;
    private static final int INDEX_SEQUENCES = 2;

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private FloatingActionButton addHoverButton;
    private RecentPasswordsFragment recentPasswordsFragment;
    private DomainsUsersFragment domainsUsersFragment;
    private SequencesFragment sequencesFragment;
    private List<String> titles;

    public MainTabsFragment() {
        // Required empty public constructor
    }

    public static MainTabsFragment newInstance() {
        MainTabsFragment fragment = new MainTabsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_tabs, container, false);
        setupTabs(v);
        setupHoverButton(v);
        return v;
    }

    private void setupHoverButton(View v) {
        addHoverButton = v.findViewById(R.id.addHoverButton);
        addHoverButton.setOnClickListener(l -> {
            MainActivity a = (MainActivity)AndroidUtil.getActivity(v);

            int cur = viewPager.getCurrentItem();

            if (cur == INDEX_RECENT || cur == INDEX_DOMAINS_AND_USERS)
                a.navigateToPasswordGenerator();
            else if (cur == INDEX_SEQUENCES)
                a.navigateToSequenceEditor();
        });
    }

    private void setupTabs(View v)
    {
        tabLayout = v.findViewById(R.id.main_tab_layout);
        viewPager = v.findViewById(R.id.main_view_pager);
        viewPager.setSaveEnabled(false);

        setViewPagerAdapter();
        new TabLayoutMediator(tabLayout, viewPager, this).attach();
    }

    private void setViewPagerAdapter()
    {
        ViewPagerFragmentAdapter a = new ViewPagerFragmentAdapter(getChildFragmentManager(), getLifecycle());
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