package pl.elpassion.eltc

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.android.schedulers.AndroidSchedulers

abstract class BaseActivity : RxAppCompatActivity() {

    lateinit var model: MainModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    protected fun initModel() {
        model = ViewModelProviders.of(this).get(MainModel::class.java)
        model.state
                .bindToLifecycle(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    log("STATE: $it")
                    showState(it)
                }
    }

    abstract fun showState(state: AppState?)
}
