package acronyms.app.repo

import acronyms.app.R
import acronyms.app.data.model.AbbrevationResult
import acronyms.app.data.remote.ApiInterface
import acronyms.app.data.repository.AcronymsRepo
import acronyms.app.utils.ApiResult
import android.content.Context
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
internal class AcronymsRepoTest {

    lateinit var repo : AcronymsRepo
    private val apiService = mockk<ApiInterface>()
    private val context = mockk<Context>(relaxed = true)

    @Before
    fun setUp() {
        repo = AcronymsRepo(context,apiService)
    }

    @After
    fun tearDown() {
    }


    @Test
    fun `getFullFormFromAcronym return success`() = runTest {
        val responseMock: Response<List<AbbrevationResult>> = mockk(relaxed = true)
        val abbreviationList: List<AbbrevationResult> =listOf(AbbrevationResult())
        coEvery { apiService.getFullFormFromAcronym("HHH") } returns responseMock
        every { responseMock.isSuccessful } returns true
        every { responseMock.body() } returns abbreviationList

        repo.getFullFormFromAcronym("HHH").collect {
            when (it) {
                is ApiResult.Success -> {
                    TestCase.assertEquals(1, it.value.size)
                    TestCase.assertTrue(true)
                }
                is ApiResult.Error -> {
                    TestCase.assertTrue(false)
                }
                is ApiResult.Loading -> {
                    TestCase.assertTrue(true)
                }
            }
        }

        every { responseMock.body() } returns emptyList()
        every { context.getString(R.string.lbl_no_data_found) }.returns("No data found")

        repo.getFullFormFromAcronym("HHH").collect {
            when (it) {
                is ApiResult.Success -> {
                    TestCase.assertEquals(0, it.value.size)
                    TestCase.assertTrue(false)
                }
                is ApiResult.Error -> {
                    TestCase.assertEquals("No data found", it.message)
                    TestCase.assertTrue(true)
                }
                is ApiResult.Loading -> {
                    TestCase.assertTrue(true)
                }
            }
        }
    }

    @Test
    fun `getFullFormFromAcronym return error`() = runTest {
        val responseMock: Response<List<AbbrevationResult>> = mockk(relaxed = true)
        coEvery { apiService.getFullFormFromAcronym("HHH") } returns responseMock
        every { responseMock.isSuccessful } returns false
        every { responseMock.code() } returns 400
        every { context.getString(R.string.error_bad_request) }.returns("Bad request")

        repo.getFullFormFromAcronym("HHH").collect {
            when (it) {
                is ApiResult.Success -> {
                    TestCase.assertEquals(1, it.value.size)
                    TestCase.assertTrue(false)
                }
                is ApiResult.Error -> {
                    TestCase.assertEquals("Bad request",it.message)
                    TestCase.assertTrue(true)
                }
                is ApiResult.Loading -> {
                    TestCase.assertTrue(true)
                }
            }
        }
    }


    @Test
    fun `getAcronymFromFullForm return success`() = runTest {
        val responseMock: Response<List<AbbrevationResult>> = mockk(relaxed = true)
        val abbreviationList: List<AbbrevationResult> =listOf(AbbrevationResult())
        coEvery { apiService.getAcronymFromFullForm("HHH") } returns responseMock
        every { responseMock.isSuccessful } returns true
        every { responseMock.body() } returns abbreviationList
        every { context.getString(R.string.error_bad_request) }.returns("Bad request")

        repo.getAcronymFromFullForm("HHH").collect {
            when (it) {
                is ApiResult.Success -> {
                    TestCase.assertEquals(1, it.value.size)
                    TestCase.assertTrue(true)
                }
                is ApiResult.Error -> {
                    TestCase.assertTrue(false)
                }
                is ApiResult.Loading -> {
                    TestCase.assertTrue(true)
                }
            }
        }

        every { responseMock.body() } returns emptyList()
        every { context.getString(R.string.lbl_no_data_found) }.returns("No data found")

        repo.getAcronymFromFullForm("HHH").collect {
            when (it) {
                is ApiResult.Success -> {
                    TestCase.assertEquals(0, it.value.size)
                    TestCase.assertTrue(false)
                }
                is ApiResult.Error -> {
                    TestCase.assertEquals("No data found", it.message)
                    TestCase.assertTrue(true)
                }
                is ApiResult.Loading -> {
                    TestCase.assertTrue(true)
                }
            }
        }
    }

    @Test
    fun `getAcronymFromFullForm return error`() = runTest {
        val responseMock: Response<List<AbbrevationResult>> = mockk(relaxed = true)
        coEvery { apiService.getAcronymFromFullForm("HHH") } returns responseMock
        every { responseMock.isSuccessful } returns false
        every { responseMock.code() } returns 400
        every { context.getString(R.string.error_bad_request) }.returns("Bad request")

        repo.getAcronymFromFullForm("HHH").collect {
            when (it) {
                is ApiResult.Success -> {
                    TestCase.assertEquals(1, it.value.size)
                    TestCase.assertTrue(false)
                }
                is ApiResult.Error -> {
                    TestCase.assertEquals("Bad request",it.message)
                    TestCase.assertTrue(true)
                }
                is ApiResult.Loading -> {
                    TestCase.assertTrue(true)
                }
            }
        }
    }

    @Test
    fun `test handle error code message`() = runTest{
        every { context.getString(R.string.error_bad_request) }.returns("Bad request")
        every { context.getString(R.string.error_unauthorised_service) }.returns("Unauthorised service")
        every { context.getString(R.string.error_service_forbidden) }.returns("Service Forbidden")
        every { context.getString(R.string.error_not_found) }.returns("Not Found")
        every { context.getString(R.string.error_internal_server_error) }.returns("Internal server error")
        every { context.getString(R.string.error_service_unavailable) }.returns("Service Unavailable")
        every { context.getString(R.string.error) }.returns("Error")
        repo.handleError(400){
            TestCase.assertEquals("Bad request",it)
        }
        repo.handleError(401){
            TestCase.assertEquals("Unauthorised service",it)
        }

        repo.handleError(403){
            TestCase.assertEquals("Service Forbidden",it)
        }

        repo.handleError(404){
            TestCase.assertEquals("Not Found",it)
        }

        repo.handleError(500){
            TestCase.assertEquals("Internal server error",it)
        }

        repo.handleError(503){
            TestCase.assertEquals("Service Unavailable",it)
        }
    }
}