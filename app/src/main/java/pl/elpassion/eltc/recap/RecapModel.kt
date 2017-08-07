package pl.elpassion.eltc.recap

import pl.elpassion.eltc.Build
import pl.elpassion.eltc.api.TeamCityApi
import pl.elpassion.eltc.util.SchedulersSupplier
import java.util.*

class RecapModel(private val repository: RecapRepository,
                 private val api: TeamCityApi,
                 private val notifier: RecapNotifier,
                 private val onFinish: () -> Unit,
                 private val schedulers: SchedulersSupplier) {

    fun onStart() {
        val lastFinishDate = repository.lastFinishDate
        if (lastFinishDate == null) {
            repository.lastFinishDate = Date()
            onFinish()
        } else {
            getFinishedBuilds(lastFinishDate)
        }
    }

    private fun getFinishedBuilds(lastFinishDate: Date) {
        api.getFinishedBuilds(lastFinishDate)
                .subscribeOn(schedulers.backgroundScheduler)
                .observeOn(schedulers.uiScheduler)
                .subscribe(onFinishedBuilds, onError)
    }

    private val onFinishedBuilds: (List<Build>) -> Unit = { builds ->
        val finishDate = builds.lastFinishDate
        if (finishDate != null) {
            notifyAboutFailures(builds)
            repository.lastFinishDate = finishDate
        }
        onFinish()
    }

    private val onError: (Throwable) -> Unit = { onFinish() }

    private val List<Build>.lastFinishDate get() = finishDates.maxBy { it.time }

    private val List<Build>.finishDates get() = map { it.finishDate }.filterNotNull()

    private fun notifyAboutFailures(builds: List<Build>) {
        val failedBuilds = builds.filter { it.status == "FAILURE" }
        if (failedBuilds.isNotEmpty()) {
            notifier.showFailureNotification(failedBuilds)
        }
    }
}