package br.com.horseconnect.impl.di

import br.com.horseconnect.data.datasource.user.UserDataSource
import br.com.horseconnect.impl.data.datasource.animal.AnimalDataSource
import br.com.horseconnect.impl.data.datasource.animal.AnimalDataSourceImpl
import br.com.horseconnect.impl.data.datasource.farm.FarmDataSource
import br.com.horseconnect.impl.data.datasource.farm.FarmDataSourceImpl
import br.com.horseconnect.impl.data.datasource.finance.FinanceEntryDataSource
import br.com.horseconnect.impl.data.datasource.finance.FinanceEntryDataSourceImpl
import br.com.horseconnect.impl.data.datasource.image.ImageDataSource
import br.com.horseconnect.impl.data.datasource.image.ImageDataSourceImpl
import br.com.horseconnect.impl.data.datasource.notification.NotificationDataSource
import br.com.horseconnect.impl.data.datasource.notification.NotificationDataSourceImpl
import br.com.horseconnect.impl.data.datasource.pregnancy.PregnancyDataSource
import br.com.horseconnect.impl.data.datasource.pregnancy.PregnancyDataSourceImpl
import br.com.horseconnect.impl.data.datasource.user.UserDataSourceImpl
import br.com.horseconnect.impl.data.datasource.vaccine.VaccineDataSource
import br.com.horseconnect.impl.data.datasource.vaccine.VaccineDataSourceImpl
import br.com.horseconnect.impl.data.repository.animal.AnimalRepository
import br.com.horseconnect.impl.data.repository.animal.AnimalRepositoryImpl
import br.com.horseconnect.impl.data.repository.farm.FarmRepository
import br.com.horseconnect.impl.data.repository.farm.FarmRepositoryImpl
import br.com.horseconnect.impl.data.repository.finance.FinanceEntryRepository
import br.com.horseconnect.impl.data.repository.finance.FinanceEntryRepositoryImpl
import br.com.horseconnect.impl.data.repository.image.ImageRepository
import br.com.horseconnect.impl.data.repository.image.ImageRepositoryImpl
import br.com.horseconnect.impl.data.repository.notification.NotificationRepository
import br.com.horseconnect.impl.data.repository.notification.NotificationRepositoryImpl
import br.com.horseconnect.impl.data.repository.pregnancy.PregnancyRepository
import br.com.horseconnect.impl.data.repository.pregnancy.PregnancyRepositoryImpl
import br.com.horseconnect.impl.data.repository.user.UserRepository
import br.com.horseconnect.impl.data.repository.user.UserRepositoryImpl
import br.com.horseconnect.impl.data.repository.vaccine.VaccineRepository
import br.com.horseconnect.impl.data.repository.vaccine.VaccineRepositoryImpl
import br.com.horseconnect.impl.domain.usecase.animal.DeleteAnimalUseCase
import br.com.horseconnect.impl.domain.usecase.user.DoLoginUseCase
import br.com.horseconnect.impl.domain.usecase.animal.GetAnimalByIdUseCase
import br.com.horseconnect.impl.domain.usecase.animal.GetAnimalsUseCase
import br.com.horseconnect.impl.domain.usecase.animal.GetGenealogyByAnimalIdUseCase
import br.com.horseconnect.impl.domain.usecase.finance.DeleteFinanceEntryUseCase
import br.com.horseconnect.impl.domain.usecase.finance.GetFinanceEntriesUseCase
import br.com.horseconnect.impl.domain.usecase.finance.RegisterFinanceEntryUseCase
import br.com.horseconnect.impl.domain.usecase.finance.UpdateFinanceEntryUseCase
import br.com.horseconnect.impl.domain.usecase.user.GetUserFarmUseCase
import br.com.horseconnect.impl.domain.usecase.animal.RegisterAnimalUseCase
import br.com.horseconnect.impl.domain.usecase.animal.UpdateAnimalUseCase
import br.com.horseconnect.impl.domain.usecase.animal.UpdateGenealogyUseCase
import br.com.horseconnect.impl.domain.usecase.image.DeleteImageUseCase
import br.com.horseconnect.impl.domain.usecase.image.UploadImageUseCase
import br.com.horseconnect.impl.domain.usecase.notification.CreateNotificationUseCase
import br.com.horseconnect.impl.domain.usecase.notification.GetNotificationsUseCase
import br.com.horseconnect.impl.domain.usecase.notification.DeleteAllNotificationsUseCase
import br.com.horseconnect.impl.domain.usecase.notification.DeleteNotificationUseCase
import br.com.horseconnect.impl.domain.usecase.pregnancy.DeletePregnancyUseCase
import br.com.horseconnect.impl.domain.usecase.pregnancy.GetPregnanciesUseCase
import br.com.horseconnect.impl.domain.usecase.pregnancy.RegisterPregnancyUseCase
import br.com.horseconnect.impl.domain.usecase.pregnancy.UpdatePregnancyUseCase
import br.com.horseconnect.impl.domain.usecase.user.DoLogoutUseCase
import br.com.horseconnect.impl.domain.usecase.vaccine.DeleteVaccineUseCase
import br.com.horseconnect.impl.domain.usecase.vaccine.GetVaccinesUseCase
import br.com.horseconnect.impl.domain.usecase.vaccine.RegisterVaccineUseCase
import br.com.horseconnect.impl.domain.usecase.vaccine.UpdateVaccineUseCase
import br.com.horseconnect.impl.presentation.animals.detail.AnimalDetailViewModel
import br.com.horseconnect.impl.presentation.animals.genealogy.GenealogyViewModel
import br.com.horseconnect.impl.presentation.animals.list.AnimalsListViewModel
import br.com.horseconnect.impl.presentation.animals.register.AnimalRegisterViewModel
import br.com.horseconnect.impl.presentation.finance.detail.FinanceDetailViewModel
import br.com.horseconnect.impl.presentation.finance.list.FinanceListViewModel
import br.com.horseconnect.impl.presentation.finance.register.FinanceRegisterViewModel
import br.com.horseconnect.impl.presentation.home.HomeViewModel
import br.com.horseconnect.impl.presentation.login.LoginViewModel
import br.com.horseconnect.impl.presentation.notifications.list.NotificationListViewModel
import br.com.horseconnect.impl.presentation.pregnancies.detail.PregnancyDetailViewModel
import br.com.horseconnect.impl.presentation.pregnancies.list.PregnancyListViewModel
import br.com.horseconnect.impl.presentation.pregnancies.register.PregnancyRegisterViewModel
import br.com.horseconnect.impl.presentation.profile.ProfileViewModel
import br.com.horseconnect.impl.presentation.reports.ReportsViewModel
import br.com.horseconnect.impl.presentation.vaccines.groupdetail.VaccineGroupDetailViewModel
import br.com.horseconnect.impl.presentation.vaccines.list.VaccineListViewModel
import br.com.horseconnect.impl.presentation.vaccines.register.VaccineRegisterViewModel
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

    single<PregnancyDataSource> {
        PregnancyDataSourceImpl()
    }

    single<NotificationDataSource> {
        NotificationDataSourceImpl()
    }

    single<VaccineDataSource> {
        VaccineDataSourceImpl()
    }

    single<FinanceEntryDataSource> {
        FinanceEntryDataSourceImpl()
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

    single<PregnancyRepository> {
        PregnancyRepositoryImpl(dataSource = get())
    }

    single<NotificationRepository> {
        NotificationRepositoryImpl(dataSource = get())
    }

    single<VaccineRepository> {
        VaccineRepositoryImpl(dataSource = get())
    }

    single<FinanceEntryRepository> {
        FinanceEntryRepositoryImpl(dataSource = get())
    }

    single {
        DoLoginUseCase(repository = get())
    }

    single {
        DoLogoutUseCase(repository = get())
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
        UpdateGenealogyUseCase(repository = get())
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

    single {
        GetPregnanciesUseCase(
            repository = get()
        )
    }

    single {
        RegisterPregnancyUseCase(repository = get())
    }

    single {
        UpdatePregnancyUseCase(repository = get())
    }

    single {
        DeletePregnancyUseCase(repository = get())
    }

    single {
        GetNotificationsUseCase(repository = get())
    }

    single {
        DeleteNotificationUseCase(repository = get())
    }

    single {
        DeleteAllNotificationsUseCase(repository = get())
    }

    single {
        CreateNotificationUseCase(repository = get())
    }

    single {
        GetVaccinesUseCase(repository = get())
    }

    single {
        RegisterVaccineUseCase(repository = get())
    }

    single {
        UpdateVaccineUseCase(repository = get())
    }

    single {
        DeleteVaccineUseCase(repository = get())
    }

    single {
        GetFinanceEntriesUseCase(repository = get())
    }

    single {
        RegisterFinanceEntryUseCase(repository = get())
    }

    single {
        UpdateFinanceEntryUseCase(repository = get())
    }

    single {
        DeleteFinanceEntryUseCase(repository = get())
    }

    viewModel {
        LoginViewModel(
            doLoginUseCase = get()
        )
    }

    viewModel {
        HomeViewModel(
            getUserFarmUseCase = get(),
            getAnimalsUseCase = get(),
            getPregnanciesUseCase = get(),
            getVaccinesUseCase = get(),
            getFinanceEntriesUseCase = get(),
            getNotificationsUseCase = get(),
            createNotificationUseCase = get(),
            doLogoutUseCase = get()
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
            getGenealogy = get(),
            updateGenealogyUseCase = get()
        )
    }

    viewModel {
        ProfileViewModel(
            doLogoutUseCase = get()
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

    viewModel {
        PregnancyListViewModel(
            getPregnanciesUseCase = get()
        )
    }

    viewModel {
        PregnancyDetailViewModel(
            deletePregnancyUseCase = get()
        )
    }

    viewModel {
        PregnancyRegisterViewModel(
            getAnimalsUseCase = get(),
            registerPregnancyUseCase = get(),
            updatePregnancyUseCase = get()
        )
    }

    viewModel {
        NotificationListViewModel(
            getNotificationsUseCase = get(),
            deleteNotificationUseCase = get(),
            deleteAllNotificationsUseCase = get()
        )
    }

    viewModel {
        VaccineListViewModel(
            getVaccinesUseCase = get()
        )
    }

    viewModel {
        VaccineRegisterViewModel(
            getAnimalsUseCase = get(),
            registerVaccineUseCase = get(),
            updateVaccineUseCase = get(),
            deleteVaccineUseCase = get()
        )
    }

    viewModel {
        VaccineGroupDetailViewModel(
            deleteVaccineUseCase = get()
        )
    }

    viewModel {
        FinanceListViewModel(
            getFinanceEntriesUseCase = get()
        )
    }

    viewModel {
        FinanceRegisterViewModel(
            registerFinanceEntryUseCase = get(),
            updateFinanceEntryUseCase = get()
        )
    }

    viewModel {
        FinanceDetailViewModel(
            deleteFinanceEntryUseCase = get()
        )
    }

    viewModel {
        ReportsViewModel(
            getAnimalsUseCase = get(),
            getPregnanciesUseCase = get(),
            getVaccinesUseCase = get(),
            getFinanceEntriesUseCase = get()
        )
    }
}