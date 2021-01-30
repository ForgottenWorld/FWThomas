package it.forgottenworld.thomas

import com.okkero.skedule.BukkitDispatcher
import it.forgottenworld.thomas.command.ThomasCommand
import it.forgottenworld.thomas.command.UnthomasCommand
import it.forgottenworld.thomas.config.Config
import it.forgottenworld.thomas.manager.ThomasManager
import it.forgottenworld.thomas.utils.Dispatchers
import kotlinx.coroutines.CoroutineScope
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

@KoinApiExtension
class FWThomasPlugin: JavaPlugin(), KoinComponent {

    private val thomasManager by inject<ThomasManager>()

    override fun onEnable() {
        saveDefaultConfig()

        startKoin {
            modules(
                module {
                    single { this@FWThomasPlugin }
                    single { Config(get()) }

                    single { CoroutineScope(get<BukkitDispatcher>(named(Dispatchers.MAIN))) }
                    single(named(Dispatchers.MAIN)) { BukkitDispatcher(get<FWThomasPlugin>()) }
                    single(named(Dispatchers.ASYNC)) { BukkitDispatcher(get<FWThomasPlugin>()) }

                    single { ThomasManager() }
                    factory { ThomasCommand(get()) }
                    factory { UnthomasCommand(get()) }
                }
            )
        }

        getCommand("thomas")?.setExecutor(get<ThomasCommand>())
        getCommand("unthomas")?.setExecutor(get<UnthomasCommand>())

        server.pluginManager.registerEvents(get<ThomasManager>().EventListener(), this)

        thomasManager.loadFromYaml()
        thomasManager.scheduleSerialization()
        thomasManager.scheduleCleanup()
    }

    override fun onDisable() {
        logger.info("Disabling FWThomas...")
    }
}