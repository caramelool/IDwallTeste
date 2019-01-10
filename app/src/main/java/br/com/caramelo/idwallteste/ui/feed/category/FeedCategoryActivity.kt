package br.com.caramelo.idwallteste.ui.feed.category

import android.os.Bundle
import android.support.v4.app.Fragment
import br.com.caramelo.idwallteste.R
import br.com.caramelo.idwallteste.ui.base.BaseActivity
import br.com.caramelo.idwallteste.ui.base.BaseViewModel
import br.com.caramelo.idwallteste.ui.base.State
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_feed.*
import javax.inject.Inject

class FeedCategoryActivity : BaseActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var factory: CategoryViewModel.Factory

    private val categoryViewModel by lazy { viewModel<CategoryViewModel>(factory).value }

    private val adapter by lazy { CategoryAdapter(supportFragmentManager) }

    override val viewModels: Array<BaseViewModel>
        get() = arrayOf(categoryViewModel)

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        viewPager.adapter = adapter
    }

    override fun render(state: State) {
        when (state) {
            is CategoryViewModelState.CategoryList -> {
                adapter.list = state.list
            }
        }
    }

    override fun supportFragmentInjector() = fragmentDispatchingAndroidInjector
}
