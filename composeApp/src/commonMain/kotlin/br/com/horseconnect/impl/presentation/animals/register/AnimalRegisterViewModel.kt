package br.com.horseconnect.impl.presentation.animals.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.horseconnect.common.result.ResponseResult
import br.com.horseconnect.impl.domain.model.Animal
import br.com.horseconnect.impl.domain.usecase.animal.RegisterAnimalUseCase
import br.com.horseconnect.impl.domain.usecase.animal.UpdateAnimalUseCase
import br.com.horseconnect.impl.domain.usecase.image.DeleteImageUseCase
import br.com.horseconnect.impl.domain.usecase.image.UploadImageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AnimalRegisterViewModel(
    private val uploadImageUseCase: UploadImageUseCase,
    private val registerAnimalUseCase: RegisterAnimalUseCase,
    private val updateAnimalUseCase: UpdateAnimalUseCase,
    private val deleteImageUseCase: DeleteImageUseCase
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

    fun updateAnimal(animal: Animal, hasUpdatedImage: Boolean = false, image: ByteArray? = null) {
        _animalRegisterState.tryEmit(AnimalRegisterViewState.Loading)
        viewModelScope.launch {
            if (hasUpdatedImage) {
                if (animal.imageId.isNotEmpty()) {
                    when (val result = deleteImageUseCase.invoke(animal.imageId)) {
                        is ResponseResult.Success -> {
                            uploadImageToUpdate(image, animal)
                        }

                        is ResponseResult.Error -> {
                            _animalRegisterState.tryEmit(AnimalRegisterViewState.Error(result.resultError.msg))
                        }
                    }
                } else {
                    uploadImageToUpdate(image, animal)
                }
            } else {
                updateOnlyAnimal(animal)
            }
        }
    }

    private fun uploadImageToUpdate(image: ByteArray?, animal: Animal) {
        image?.let {
            viewModelScope.launch {
                when (val result = uploadImageUseCase.invoke(image)) {
                    is ResponseResult.Success -> {
                        result.data.run {
                            updateOnlyAnimal(
                                animal.copy(
                                    imageId = id,
                                    imageUrl = url
                                )
                            )
                        }
                    }

                    is ResponseResult.Error -> {
                        _animalRegisterState.tryEmit(
                            AnimalRegisterViewState.Error(
                                result.resultError.msg
                            )
                        )
                    }
                }
            }
        }
    }

    private fun updateOnlyAnimal(animal: Animal) {
        viewModelScope.launch {
            when(val result = updateAnimalUseCase.invoke(animal)) {
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