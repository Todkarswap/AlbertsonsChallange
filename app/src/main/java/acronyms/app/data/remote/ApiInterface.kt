package acronyms.app.data.remote

import acronyms.app.data.model.AbbrevationResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("software/acromine/dictionary.py/")
    suspend fun getFullFormFromAcronym(@Query(value="sf", encoded=true) query: String): Response<List<AbbrevationResult>>

    @GET("software/acromine/dictionary.py/")
    suspend fun getAcronymFromFullForm(@Query(value="lf", encoded=true) query: String): Response<List<AbbrevationResult>>


}