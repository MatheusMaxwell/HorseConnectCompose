package br.com.horseconnect

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.horseconnect.common.manager.SessionManager
import br.com.horseconnect.common.toastprovider.LocalToasterDialog
import br.com.horseconnect.impl.data.mapper.toDomain
import br.com.horseconnect.impl.di.appModule
import br.com.horseconnect.impl.domain.model.Animal
import br.com.horseconnect.impl.domain.model.Pregnancy
import br.com.horseconnect.impl.domain.model.RelatedEntityType
import br.com.horseconnect.impl.presentation.animals.detail.AnimalDetailScreen
import br.com.horseconnect.impl.presentation.animals.genealogy.GenealogyScreen
import br.com.horseconnect.impl.presentation.animals.list.AnimalListScreen
import br.com.horseconnect.impl.presentation.animals.register.AnimalRegisterScreen
import br.com.horseconnect.impl.domain.model.FinanceEntry
import br.com.horseconnect.impl.presentation.finance.detail.FinanceDetailScreen
import br.com.horseconnect.impl.presentation.finance.list.FinanceListScreen
import br.com.horseconnect.impl.presentation.finance.register.FinanceRegisterScreen
import br.com.horseconnect.impl.presentation.home.HomeScreen
import br.com.horseconnect.impl.presentation.login.LoginScreen
import br.com.horseconnect.impl.presentation.notifications.list.NotificationListScreen
import br.com.horseconnect.impl.presentation.pregnancies.detail.PregnancyDetailScreen
import br.com.horseconnect.impl.presentation.pregnancies.list.PregnancyListScreen
import br.com.horseconnect.impl.presentation.pregnancies.register.PregnancyRegisterScreen
import br.com.horseconnect.impl.presentation.profile.ProfileScreen
import br.com.horseconnect.impl.presentation.reports.ReportsScreen
import br.com.horseconnect.impl.domain.model.Vaccine
import br.com.horseconnect.impl.presentation.vaccines.groupdetail.VaccineGroupDetailScreen
import br.com.horseconnect.impl.presentation.vaccines.list.VaccineListScreen
import br.com.horseconnect.impl.presentation.vaccines.register.VaccineRegisterScreen
import br.com.horseconnect.presentation.splash.SplashScreen
import br.com.horseconnect.theme.HorseTheme
import br.com.horseconnect.theme.color.HorseColor
import br.com.horseconnect.theme.components.bottombar.BottomBar
import br.com.horseconnect.theme.components.bottombar.BottomBarDestination
import br.com.horseconnect.theme.components.loading.component.LoadingDialogComponent
import br.com.horseconnect.theme.components.loading.provider.LocalLoadingDialog
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.dokar.sonner.Toaster
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.FileSystem
import org.koin.compose.KoinApplication

@Serializable
object Splash

@Serializable
object Login

@Serializable
object Home

@Serializable
object AnimalsList

@Serializable
object Finance

@Serializable
data class FinanceDetail(val entry: String)

@Serializable
data class FinanceRegister(val entry: String? = null)

@Serializable
object Profile

@Serializable
object Reports

@Serializable
data class AnimalDetail(val animalId: String)

@Serializable
data class Genealogy(val animalId: String, val animalName: String)

@Serializable
data class Register(val animal: String? = null)

@Serializable
object PregnancyList

@Serializable
data class PregnancyDetail(val pregnancy: String)

@Serializable
data class PregnancyRegister(val pregnancy: String? = null)

@Serializable
object NotificationList

@Serializable
object VaccineList

@Serializable
data class VaccineGroupDetail(val vaccines: String)

@Serializable
data class VaccineRegister(val vaccines: String? = null)

@Composable
fun App() {
    val loadingManager = LocalLoadingDialog.current
    val toasterState = LocalToasterDialog.current

    KoinApplication(application = {
        modules(listOf(appModule))
    }) {
        HorseTheme {

            setSingletonImageLoaderFactory { context ->
                getAsyncImageLoader(context)
            }

            val navController = rememberNavController()
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = backStackEntry?.destination
            val selectedTab = when {
                isCurrentRoute<Home>(currentDestination) -> BottomBarDestination.HOME
                isCurrentRoute<Reports>(currentDestination) -> BottomBarDestination.REPORTS
                isCurrentRoute<Profile>(currentDestination) -> BottomBarDestination.PROFILE
                else -> null
            }

            LoadingDialogComponent(loadingManager)
            Toaster(state = toasterState)

            Scaffold(
                backgroundColor = HorseColor.background,
                bottomBar = {
                    selectedTab?.let { tab ->
                        BottomBar(
                            selected = tab,
                            onSelect = { destination ->
                                val route: Any = when (destination) {
                                    BottomBarDestination.HOME -> Home
                                    BottomBarDestination.REPORTS -> Reports
                                    BottomBarDestination.PROFILE -> Profile
                                }
                                navController.navigate(route) {
                                    popUpTo(Home) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController,
                    startDestination = Splash,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable<Splash> { backStackEntry ->
                        SplashScreen() {
                            val user = Firebase.auth.currentUser
                            if (user != null) {
                                SessionManager.user = user.toDomain()
                                navController.navigate(route = Home)
                            } else {
                                navController.navigate(route = Login)
                            }
                        }
                    }
                    composable<Login> {
                        LoginScreen(
                            navigateToHome = {
                                navController.navigate(route = Home)
                            }
                        )
                    }
                    composable<Home> {
                        HomeScreen(
                            onClickAnimals = {
                                navController.navigate(route = AnimalsList)
                            },
                            navigateToPregnancyList = {
                                navController.navigate(route = PregnancyList)
                            },
                            navigateToPregnancyDetail = { pregnancy ->
                                val json = Json.encodeToString(pregnancy)
                                navController.navigate(route = PregnancyDetail(json))
                            },
                            navigateToVaccineList = {
                                navController.navigate(route = VaccineList)
                            },
                            navigateToFinance = {
                                navController.navigate(route = Finance)
                            },
                            navigateToNotifications = {
                                navController.navigate(route = NotificationList)
                            },
                            navigateToSplashScreen = {
                                navController.navigate(route = Splash) {
                                    popUpTo(0)
                                }
                            }
                        )
                    }
                    composable<AnimalsList> {
                        AnimalListScreen(
                            onItemClick = {
                                navController.navigate(route = AnimalDetail(it))
                            },
                            navigateToRegister = {
                                navController.navigate(route = Register())
                            },
                            popBackStack = { navController.popBackStack() }
                        )
                    }
                    composable<Finance> {
                        FinanceListScreen(
                            onItemClick = { entry ->
                                val json = Json.encodeToString(entry)
                                navController.navigate(route = FinanceDetail(json))
                            },
                            popBackStack = { navController.popBackStack() },
                            navigateToRegister = {
                                navController.navigate(route = FinanceRegister())
                            }
                        )
                    }
                    composable<FinanceDetail> {
                        val json = it.arguments?.getString("entry").orEmpty()
                        val entry = Json.decodeFromString<FinanceEntry>(json)
                        FinanceDetailScreen(
                            entry = entry,
                            popBackStack = { navController.popBackStack() },
                            navigateToEdit = { current ->
                                val editJson = Json.encodeToString(current)
                                navController.navigate(route = FinanceRegister(entry = editJson))
                            }
                        )
                    }
                    composable<FinanceRegister> {
                        var entry: FinanceEntry? = null
                        it.arguments?.getString("entry")?.let { json ->
                            entry = Json.decodeFromString<FinanceEntry>(json)
                        }
                        FinanceRegisterScreen(
                            currentEntry = entry,
                            popBackStack = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable<Reports> {
                        ReportsScreen()
                    }
                    composable<Profile> {
                        ProfileScreen(
                            navigateToSplashScreen = {
                                navController.navigate(route = Splash) {
                                    popUpTo(0)
                                }
                            }
                        )
                    }
                    composable<AnimalDetail> {
                        val id = it.arguments?.getString("animalId").orEmpty()
                        AnimalDetailScreen(
                            animalId = id,
                            popBackStack = {
                                navController.popBackStack()
                            },
                            navigateToGenealogy = { animalId, animalName ->
                                navController.navigate(route = Genealogy(animalId, animalName))
                            },
                            navigateToEdit = {
                                val animalJson = Json.encodeToString(it)
                                navController.navigate(route = Register(animal = animalJson))
                            }
                        )
                    }
                    composable<Genealogy> {
                        val id = it.arguments?.getString("animalId").orEmpty()
                        val name = it.arguments?.getString("animalName").orEmpty()
                        GenealogyScreen(
                            animalId = id,
                            animalName = name,
                            popBackStack = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable<Register> {
                        var animal: Animal? = null
                        it.arguments?.getString("animal")?.let { json ->
                            animal = Json.decodeFromString<Animal>(json)
                        }
                        AnimalRegisterScreen(
                            currentAnimal = animal,
                            popBackStack = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable<PregnancyList> {
                        PregnancyListScreen(
                            onItemClick = { pregnancy ->
                                val json = Json.encodeToString(pregnancy)
                                navController.navigate(route = PregnancyDetail(json))
                            },
                            navigateToRegister = {
                                navController.navigate(route = PregnancyRegister())
                            },
                            popBackStack = { navController.popBackStack() }
                        )
                    }
                    composable<PregnancyDetail> {
                        val json = it.arguments?.getString("pregnancy").orEmpty()
                        val pregnancy = Json.decodeFromString<Pregnancy>(json)
                        PregnancyDetailScreen(
                            pregnancy = pregnancy,
                            popBackStack = { navController.popBackStack() },
                            navigateToEdit = { current ->
                                val editJson = Json.encodeToString(current)
                                navController.navigate(route = PregnancyRegister(pregnancy = editJson))
                            }
                        )
                    }
                    composable<PregnancyRegister> {
                        var pregnancy: Pregnancy? = null
                        it.arguments?.getString("pregnancy")?.let { json ->
                            pregnancy = Json.decodeFromString<Pregnancy>(json)
                        }
                        PregnancyRegisterScreen(
                            currentPregnancy = pregnancy,
                            popBackStack = {
                                navController.popBackStack()
                            }
                        )
                    }
                    composable<NotificationList> {
                        NotificationListScreen(
                            onItemClick = { notification ->
                                if (notification.relatedEntityType == RelatedEntityType.ANIMAL &&
                                    notification.relatedEntityId.isNotEmpty()
                                ) {
                                    navController.navigate(route = AnimalDetail(notification.relatedEntityId))
                                }
                            },
                            popBackStack = { navController.popBackStack() }
                        )
                    }
                    composable<VaccineList> {
                        VaccineListScreen(
                            onGroupClick = { vaccines ->
                                val json = Json.encodeToString(vaccines)
                                navController.navigate(route = VaccineGroupDetail(vaccines = json))
                            },
                            navigateToRegister = {
                                navController.navigate(route = VaccineRegister())
                            },
                            popBackStack = { navController.popBackStack() }
                        )
                    }
                    composable<VaccineGroupDetail> {
                        val json = it.arguments?.getString("vaccines").orEmpty()
                        val vaccines = Json.decodeFromString<List<Vaccine>>(json)
                        VaccineGroupDetailScreen(
                            vaccines = vaccines,
                            popBackStack = { navController.popBackStack() },
                            navigateToEdit = { current ->
                                val editJson = Json.encodeToString(current)
                                navController.navigate(route = VaccineRegister(vaccines = editJson))
                            }
                        )
                    }
                    composable<VaccineRegister> {
                        var vaccines: List<Vaccine>? = null
                        it.arguments?.getString("vaccines")?.let { json ->
                            vaccines = Json.decodeFromString<List<Vaccine>>(json)
                        }
                        VaccineRegisterScreen(
                            vaccines = vaccines,
                            popBackStack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}

private inline fun <reified T : Any> isCurrentRoute(destination: NavDestination?): Boolean {
    return destination?.route == T::class.qualifiedName
}

fun getAsyncImageLoader(context: PlatformContext) =
    ImageLoader.Builder(context).memoryCachePolicy(CachePolicy.ENABLED).memoryCache {
        MemoryCache.Builder().maxSizePercent(context, 0.3).strongReferencesEnabled(true).build()
    }.diskCachePolicy(CachePolicy.ENABLED).networkCachePolicy(CachePolicy.ENABLED).diskCache {
        newDiskCache()
    }.components {
        add(KtorNetworkFetcherFactory())
    }.crossfade(true).logger(DebugLogger()).build()

fun newDiskCache(): DiskCache {
    return DiskCache.Builder().directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "image_cache")
        .maxSizeBytes(1024L * 1024 * 1024) // 512MB
        .build()
}
