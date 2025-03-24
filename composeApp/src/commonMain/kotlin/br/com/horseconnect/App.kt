package br.com.horseconnect

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.horseconnect.common.manager.SessionManager
import br.com.horseconnect.common.toastprovider.LocalToasterDialog
import br.com.horseconnect.impl.data.mapper.toDomain
import br.com.horseconnect.impl.di.appModule
import br.com.horseconnect.impl.presentation.animals.detail.AnimalDetailScreen
import br.com.horseconnect.impl.presentation.animals.genealogy.GenealogyScreen
import br.com.horseconnect.impl.presentation.animals.list.AnimalListScreen
import br.com.horseconnect.impl.presentation.animals.register.AnimalRegisterScreen
import br.com.horseconnect.impl.presentation.home.HomeScreen
import br.com.horseconnect.impl.presentation.login.LoginScreen
import br.com.horseconnect.presentation.splash.SplashScreen
import br.com.horseconnect.theme.components.loading.component.LoadingDialogComponent
import br.com.horseconnect.theme.components.loading.provider.LocalLoadingDialog
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import coil3.disk.DiskCache
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.dokar.sonner.Toaster
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.serialization.Serializable
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
data class AnimalDetail(val animalId: String)

@Serializable
data class Genealogy(val animalId: String, val animalName: String)

@Serializable
object Register

@Composable
fun App() {
    val loadingManager = LocalLoadingDialog.current
    val toasterState = LocalToasterDialog.current

    KoinApplication(application = {
        modules(listOf(appModule))
    }) {
        MaterialTheme {

            setSingletonImageLoaderFactory { context ->
                getAsyncImageLoader(context)
            }

            val navController = rememberNavController()
            LoadingDialogComponent(loadingManager)
            Toaster(state = toasterState)
            NavHost(navController, startDestination = Splash) {
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
                        }
                    )
                }
                composable<AnimalsList> {
                    AnimalListScreen(
                        onItemClick = {
                            navController.navigate(route = AnimalDetail(it))
                        },
                        navigateToRegister = {
                            navController.navigate(route = Register)
                        },
                        popBackStack = { navController.popBackStack() }
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
                    AnimalRegisterScreen(
                        popBackStack = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

fun getAsyncImageLoader(context: PlatformContext) =
    ImageLoader.Builder(context).memoryCachePolicy(CachePolicy.ENABLED).memoryCache {
        MemoryCache.Builder().maxSizePercent(context, 0.3).strongReferencesEnabled(true).build()
    }.diskCachePolicy(CachePolicy.ENABLED).networkCachePolicy(CachePolicy.ENABLED).diskCache {
        newDiskCache()
    }.crossfade(true).logger(DebugLogger()).build()

fun newDiskCache(): DiskCache {
    return DiskCache.Builder().directory(FileSystem.SYSTEM_TEMPORARY_DIRECTORY / "image_cache")
        .maxSizeBytes(1024L * 1024 * 1024) // 512MB
        .build()
}
