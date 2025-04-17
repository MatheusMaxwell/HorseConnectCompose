package br.com.horseconnect.impl.di

import br.com.horseconnect.data.datasource.user.UserDataSource
import br.com.horseconnect.impl.data.datasource.animal.AnimalDataSource
import br.com.horseconnect.impl.data.datasource.animal.AnimalDataSourceImpl
import br.com.horseconnect.impl.data.datasource.farm.FarmDataSource
import br.com.horseconnect.impl.data.datasource.farm.FarmDataSourceImpl
import br.com.horseconnect.impl.data.datasource.image.ImageDataSource
import br.com.horseconnect.impl.data.datasource.image.ImageDataSourceImpl
import br.com.horseconnect.impl.data.datasource.user.UserDataSourceImpl
import br.com.horseconnect.impl.data.repository.animal.AnimalRepository
import br.com.horseconnect.impl.data.repository.animal.AnimalRepositoryImpl
import br.com.horseconnect.impl.data.repository.farm.FarmRepository
import br.com.horseconnect.impl.data.repository.farm.FarmRepositoryImpl
import br.com.horseconnect.impl.data.repository.image.ImageRepository
import br.com.horseconnect.impl.data.repository.image.ImageRepositoryImpl
import br.com.horseconnect.impl.data.repository.user.UserRepository
import br.com.horseconnect.impl.data.repository.user.UserRepositoryImpl
import br.com.horseconnect.impl.domain.usecase.animal.DeleteAnimalUseCase
import br.com.horseconnect.impl.domain.usecase.user.DoLoginUseCase
import br.com.horseconnect.impl.domain.usecase.animal.GetAnimalByIdUseCase
import br.com.horseconnect.impl.domain.usecase.animal.GetAnimalsUseCase
import br.com.horseconnect.impl.domain.usecase.animal.GetGenealogyByAnimalIdUseCase
import br.com.horseconnect.impl.domain.usecase.user.GetUserFarmUseCase
import br.com.horseconnect.impl.domain.usecase.animal.RegisterAnimalUseCase
import br.com.horseconnect.impl.domain.usecase.animal.UpdateAnimalUseCase
import br.com.horseconnect.impl.domain.usecase.image.DeleteImageUseCase
import br.com.horseconnect.impl.domain.usecase.image.UploadImageUseCase
import br.com.horseconnect.impl.presentation.animals.detail.AnimalDetailViewModel
import br.com.horseconnect.impl.presentation.animals.genealogy.GenealogyViewModel
import br.com.horseconnect.impl.presentation.animals.list.AnimalsListViewModel
import br.com.horseconnect.impl.presentation.animals.register.AnimalRegisterViewModel
import br.com.horseconnect.impl.presentation.home.HomeViewModel
import br.com.horseconnect.impl.presentation.login.LoginViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<UserDataSource> {
        UserDataSourceImpl()
    }

    single<FarmDataSource> {
        FarmDataSourceImpl()
    }

    single<AnimalDataSource> {
        AnimalDataSourceImpl()
    }

    single<ImageDataSource> {
        ImageDataSourceImpl()
    }

    single<UserRepository> {
        UserRepositoryImpl(dataSource = get())
    }

    single<FarmRepository> {
        FarmRepositoryImpl(datasource = get())
    }

    single<AnimalRepository> {
        AnimalRepositoryImpl(dataSource = get())
    }

    single<ImageRepository> {
        ImageRepositoryImpl(dataSource = get())
    }

    single {
        DoLoginUseCase(repository = get())
    }

    single {
        GetUserFarmUseCase(repository = get())
    }

    single {
        GetAnimalsUseCase(repository = get())
    }

    single {
        GetAnimalByIdUseCase(repository = get())
    }

    single {
        GetGenealogyByAnimalIdUseCase(repository = get())
    }

    single {
        UploadImageUseCase(repository = get())
    }

    single {
        RegisterAnimalUseCase(repository = get())
    }

    single {
        UpdateAnimalUseCase(repository = get())
    }

    single {
        DeleteImageUseCase(
            imageRepository = get()
        )
    }

    single {
        DeleteAnimalUseCase(
            animalRepository = get(),
            imageRepository = get()
        )
    }

    viewModel {
        LoginViewModel(
            doLoginUseCase = get()
        )
    }

    viewModel {
        HomeViewModel(
            getUserFarmUseCase = get(),
            getAnimalsUseCase = get()
        )
    }

    viewModel {
        AnimalsListViewModel(
            getAnimalsUseCase = get()
        )
    }

    viewModel {
        AnimalDetailViewModel(
            getAnimalById = get(),
            deleteAnimalUseCase = get()
        )
    }

    viewModel {
        GenealogyViewModel(
            getGenealogy = get()
        )
    }

    viewModel {
        AnimalRegisterViewModel(
            uploadImageUseCase = get(),
            registerAnimalUseCase = get(),
            updateAnimalUseCase = get(),
            deleteImageUseCase = get()
        )
    }
}