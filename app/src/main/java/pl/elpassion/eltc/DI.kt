package pl.elpassion.eltc

import android.app.Application
import pl.elpassion.eltc.api.TeamCityApiImpl
import pl.elpassion.eltc.builds.BuildsRepositoryImpl
import pl.elpassion.eltc.login.LoginRepositoryImpl
import pl.elpassion.eltc.recap.RecapNotifier
import pl.elpassion.eltc.recap.RecapNotifierImpl
import pl.elpassion.eltc.recap.RecapRepository
import pl.elpassion.eltc.recap.RecapRepositoryImpl

object DI {

    private val model by lazy {
        TeamCityModelImpl(provideTeamCityApi(), provideLoginRepository(), provideBuildsRepository())
    }

    var provideTeamCityModel: () -> TeamCityModel = { model }

    var provideTeamCityApi = { TeamCityApiImpl }

    var provideLoginRepository = { LoginRepositoryImpl(provideApplication()) }

    var provideBuildsRepository = { BuildsRepositoryImpl(provideApplication()) }

    var provideApplication: () -> Application = { throw UnsupportedOperationException("Application provider not initialized") }

    object Recap {

        var provideRepository: () -> RecapRepository = { RecapRepositoryImpl(provideApplication()) }

        var provideNotifier: () -> RecapNotifier = { RecapNotifierImpl() }
    }
}