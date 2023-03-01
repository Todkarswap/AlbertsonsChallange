package acronyms.app.data.repository

import acronyms.app.R
import acronyms.app.data.remote.ApiInterface
import acronyms.app.utils.ApiResult
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class AcronymsRepo @Inject constructor(
    @ApplicationContext val context: Context,
    private var client: ApiInterface
) {

    suspend fun getFullFormFromAcronym(query: String) = flow {
        emit(ApiResult.Loading)
        val response = client.getFullFormFromAcronym(query)
        if (response.isSuccessful) {
            response.body()?.let {
                if(it.isEmpty()){
                    emit(ApiResult.Error(context.getString(R.string.lbl_no_data_found),null))
                }else {
                    emit(ApiResult.Success(it))
                }
            }
        } else {
            handleError(response.code()) {
                emit(ApiResult.Error(it, null))
            }
        }
    }

    suspend fun getAcronymFromFullForm(query: String) = flow {
        emit(ApiResult.Loading)
        val response = client.getAcronymFromFullForm(query)
        if (response.isSuccessful) {
            response.body()?.let {
                if(it.isEmpty()){
                    emit(ApiResult.Error(context.getString(R.string.lbl_no_data_found),null))
                }else {
                    emit(ApiResult.Success(it))
                }
            }
        } else {
            handleError(response.code()) {
                emit(ApiResult.Error(it, null))
            }
        }
    }

    suspend fun handleError(
        code: Int,
        emitError: suspend (message: String) -> Unit
    ) {
        when (code) {
            400 -> {//bad request
                emitError(context.getString(R.string.error_bad_request))
            }
            401 -> { //un authorised
                emitError(context.getString(R.string.error_unauthorised_service))
            }
            403 -> {
                // forbidden
                emitError(context.getString(R.string.error_service_forbidden))
            }
            404 -> {
                // not found
                emitError(context.getString(R.string.error_not_found))
            }
            500 -> {
                //Internal server error
                emitError(context.getString(R.string.error_internal_server_error))
            }
            503 -> {
                // Service Unavailable
                emitError(context.getString(R.string.error_service_unavailable))
            }
            else -> {
                emitError(context.getString(R.string.error))
            }

        }
    }
}