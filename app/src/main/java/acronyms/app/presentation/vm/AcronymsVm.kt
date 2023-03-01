package acronyms.app.presentation.vm

import acronyms.app.data.model.AbbrevationResult
import acronyms.app.data.repository.AcronymsRepo
import acronyms.app.di.IoDispatcher
import acronyms.app.utils.ApiResult
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class AcronymsVm @Inject constructor(
    private val acronymsRepo: AcronymsRepo,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    var title = mutableStateOf("")

    var viewState = mutableStateOf<ViewState>(Default)

    val isFullForm = mutableStateOf(true)

    val debounceQuery = MutableStateFlow("")

    @FlowPreview
    val stateFLow = debounceQuery.debounce(1000).distinctUntilChanged().map {
        if (it.isNotEmpty() && it.length >= 3) {

            if (isFullForm.value) {
                callGetFullForm(it)
            } else {
                callGetShortForm(it)
            }
        }
    }.stateIn(viewModelScope + dispatcher, SharingStarted.Eagerly, "")


    suspend fun searchFullForms(query: String) {
        if (query.isNotEmpty()) {
            debounceQuery.emit(query)
        } else {
            viewState.value = Default
        }
    }

    private suspend fun callGetFullForm(query: String) {
        acronymsRepo.getFullFormFromAcronym(query = query)
            .collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        viewState.value = ProfileLoaded(result.value)
                    }
                    is ApiResult.Error -> {
                        result.message?.let {
                            viewState.value = ProfileLoadFailure(result.message.toString())
                        }

                    }
                    is ApiResult.Loading -> {
                        viewState.value = Loading
                    }
                }
            }
    }


    private suspend fun callGetShortForm(query: String) {
        acronymsRepo.getAcronymFromFullForm(query = query)
            .collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        viewState.value = ProfileLoaded(result.value)
                    }
                    is ApiResult.Error -> {
                        viewState.value = ProfileLoadFailure(result.message.toString())
                    }
                    is ApiResult.Loading -> {
                        viewState.value = Loading
                    }
                }
            }
    }

}

sealed class ViewState
data class ProfileLoaded(val data: List<AbbrevationResult>?) : ViewState()
object Loading : ViewState()
object Default : ViewState()
class ProfileLoadFailure(val error: String? = null) : ViewState()


