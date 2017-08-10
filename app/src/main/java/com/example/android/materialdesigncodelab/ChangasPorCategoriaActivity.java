package com.example.android.materialdesigncodelab;

        import android.content.res.Resources;
        import android.os.Bundle;
        import android.support.graphics.drawable.VectorDrawableCompat;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentPagerAdapter;
        import android.support.v4.content.res.ResourcesCompat;
        import android.support.v4.view.GravityCompat;
        import android.support.v4.view.ViewPager;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.Menu;
        import android.view.MenuItem;

        import java.util.ArrayList;
        import java.util.List;


/**
 * Provides UI for the main screen.
 */
public class ChangasPorCategoriaActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changas_activas);
        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_ca);
        final Integer idCategoria = getIntent().getIntExtra("EXTRA_POSITION",0);
        setupViewPager(viewPager,idCategoria);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerCA);
        // Adding menu icon to Toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_ca));
        if (getSupportActionBar() != null) {
            VectorDrawableCompat indicator
                    = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(),R.color.white,getTheme()));
            //getSupportActionBar().setHomeAsUpIndicator(indicator);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Resources resources = getResources();
            String categoryName = ((resources.getStringArray(R.array.array_categories))[idCategoria].toString());
            getSupportActionBar().setTitle("Categoria: "+categoryName);
        }

    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager, Integer idCategoria) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        Fragment fragment = new ChangasPorCategoria();
        Bundle args = new Bundle();
        args.putInt("category_id",idCategoria);
        fragment.setArguments(args);
        adapter.addFragment(fragment, "En este momento");
        //adapter.addFragment(new ListContentFragment(), "Pedidos");
        //adapter.addFragment(new TileContentFragment(), "Categorias");
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
