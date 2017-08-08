package pl.elpassion.eltc.recap

import android.app.job.JobParameters
import android.app.job.JobService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import pl.elpassion.eltc.DI
import pl.elpassion.eltc.util.SchedulersSupplier

class RecapService : JobService() {

    private val controller by lazy {
        RecapController(
                repository = DI.Recap.provideRepository(),
                api = DI.provideTeamCityApi(),
                notifier = DI.Recap.provideNotifier(),
                onFinish = { jobFinished(null, true) },
                schedulers = SchedulersSupplier(
                        backgroundScheduler = Schedulers.io(),
                        uiScheduler = AndroidSchedulers.mainThread()))
    }

    override fun onStartJob(parameters: JobParameters?): Boolean {
        controller.onStart()
        return true
    }

    override fun onStopJob(parameters: JobParameters?): Boolean {
        controller.onStop()
        return true
    }
}