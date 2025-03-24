package br.com.horseconnect.impl.presentation.animals.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.domain.model.Animal
import br.com.horseconnect.impl.domain.usecase.RegisterAnimalUseCase
import br.com.horseconnect.impl.domain.usecase.UploadImageUseCase
import br.com.horseconnect.impl.presentation.animals.list.AnimalsViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AnimalRegisterViewModel(
    private val uploadImageUseCase: UploadImageUseCase,
    private val registerAnimalUseCase: RegisterAnimalUseCase
): ViewModel() {

    private val _animalRegisterState: MutableStateFlow<AnimalRegisterViewState> = MutableStateFlow(
        AnimalRegisterViewState.Initial
    )
    val animalRegisterState: StateFlow<AnimalRegisterViewState> get() = _animalRegisterState

    fun uploadImage(image: ByteArray, animal: Animal) {
        _animalRegisterState.tryEmit(AnimalRegisterViewState.Loading)
        viewModelScope.launch {
            when(val result = uploadImageUseCase.invoke(image)) {
                is ResponseResult.Success -> {
                    result.data.run {
                        registerAnimal(animal.copy(imageId = id, imageUrl = url))
                    }
                }
                is ResponseResult.Error -> {
                    _animalRegisterState.tryEmit(AnimalRegisterViewState.Error(result.resultError.msg))
                }
            }
        }
    }

    fun registerAnimal(animal: Animal) {
        _animalRegisterState.tryEmit(AnimalRegisterViewState.Loading)
        viewModelScope.launch {
            when(val result = registerAnimalUseCase.invoke(animal)) {
                is ResponseResult.Success -> {
                    _animalRegisterState.tryEmit(AnimalRegisterViewState.RegisterSuccess)
                }
                is ResponseResult.Error -> {
                    _animalRegisterState.tryEmit(AnimalRegisterViewState.Error(result.resultError.msg))
                }
            }
        }
    }
}

sealed class AnimalRegisterViewState() {
    data object Initial : AnimalRegisterViewState()
    data object Loading : AnimalRegisterViewState()
    data class Error(val msg: String) : AnimalRegisterViewState()
    data object RegisterSuccess : AnimalRegisterViewState()
}