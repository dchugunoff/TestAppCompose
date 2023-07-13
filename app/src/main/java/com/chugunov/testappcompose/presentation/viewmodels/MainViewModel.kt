package com.chugunov.testappcompose.presentation.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.chugunov.testappcompose.data.RepositoryImpl
import com.chugunov.testappcompose.data.mappers.UsersMapper
import com.chugunov.testappcompose.data.network.ApiService
import com.chugunov.testappcompose.domain.GetUsersUseCase
import com.chugunov.testappcompose.domain.entity.User
import com.chugunov.testappcompose.presentation.utils.FragmentState
import com.chugunov.testappcompose.presentation.utils.LoadingState
import kotlinx.coroutines.delay

class MainViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val repository = RepositoryImpl(
        apiService = ApiService(),
        usersMapper = UsersMapper()
    )

    //Переменная, которая создает экземпляр GetUserUseCase
    private val getUsersUseCase = GetUsersUseCase(repository)

    //Переменная, которая хранит в себе TextFieldValue 1 числа
    @OptIn(SavedStateHandleSaveableApi::class)
    var firstNumber by savedStateHandle.saveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    //Переменная, которая хранит в себе TextFieldValue 2 числа
    @OptIn(SavedStateHandleSaveableApi::class)
    var secondNumber by savedStateHandle.saveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    //Функция для обновления 1 числа в TextField
    fun updateFirstNumber(newMessage: TextFieldValue) {
        firstNumber = newMessage
    }

    //Функция для обновления 2 числа в TextField
    fun updateSecondNumber(newMessage: TextFieldValue) {
        secondNumber = newMessage
    }

    //Функция подчета суммы двух чисел
    fun calculateSum() {
        val first = firstNumber.text.toIntOrNull() ?: 0
        val second = secondNumber.text.toIntOrNull() ?: 0
        _sum.value = first.plus(second)
    }
    //Лайвдата с сохранением суммы двух чисел с первого фрагмента
    private val _sum = MutableLiveData<Int>()
    val sum: LiveData<Int> = _sum

    //Лайвдата, в которой хранится список юзеров
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> = _users

    //Функция, которая вызывает метод getUsers у UseCase и помещает юзеров в лайвдату
    suspend fun getUsers() {
        _users.postValue(getUsersUseCase.getUsers())
    }

    //Лайвдата которая хранит в себе статус загрузки данных
    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState> = _loadingState

    //Иммитация загрузки данных
    suspend fun loadData() {
        if (_loadingState.value == null) {
            _loadingState.value = LoadingState.Loading
        }
        delay(2000)
        _loadingState.value = LoadingState.Loaded
    }

    //Передача значения состояния фрагмента в лайвдату _currentFragmentState
    fun setCurrentFragmentState(fragmentState: FragmentState) {
        _currentFragmentState.value = fragmentState
    }

    // Лайвдата с сохранением текущего фрагмента
    private val _currentFragmentState = MutableLiveData<FragmentState>()
    val currentFragmentState: LiveData<FragmentState> = _currentFragmentState
}

