package acronyms.app.vm

import acronyms.app.data.model.AbbrevationResult
import acronyms.app.data.repository.AcronymsRepo
import acronyms.app.presentation.vm.*
import acronyms.app.utils.ApiResult
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalCoroutinesApi::class)
internal class AcronymsVmTest {

    private val dispatcher = StandardTestDispatcher()
    private val repo: AcronymsRepo = mockk(relaxed = true)
    private lateinit var vm: AcronymsVm

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        vm = AcronymsVm(repo, dispatcher)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun `test api call for Full Form Success`() = runTest {
        TestCase.assertEquals(Default, vm.uiState.value)
        coEvery { repo.getFullFormFromAcronym("HHH") } returns flow {
            emit(ApiResult.Success(listOf(AbbrevationResult())))
        }
        vm.isFullForm.value = true
        vm.searchFullForms("HHH")
        delay(2000)

        coVerify(exactly = 1) { repo.getFullFormFromAcronym("HHH") }

        TestCase.assertEquals(1, (vm.uiState.value as SuccessState).data?.size)

    }

    @Test
    fun `test api call for Full Form Error`() = runTest {
        TestCase.assertEquals(Default, vm.uiState.value)
        coEvery { repo.getFullFormFromAcronym("HHH") } returns flow {
            emit(ApiResult.Error("No data Found", null))
        }
         vm.searchFullForms("HHH")
        delay(2000)
        TestCase.assertNotSame(SuccessState(listOf()), vm.uiState.value)
        TestCase.assertEquals("No data Found", (vm.uiState.value as ErrorState).error.toString())

    }

    @Test
    fun `test api call for Full Form to show Loading`() = runTest {
        TestCase.assertEquals(Default, vm.uiState.value)
        coEvery { repo.getFullFormFromAcronym("HHH") } returns flow {
            emit(ApiResult.Loading)
        }
         vm.searchFullForms("HHH")
        delay(2000)
        TestCase.assertEquals(Loading, vm.uiState.value)
    }


    @Test
    fun `test api call for Short Form Success`() = runTest {
        TestCase.assertEquals(Default, vm.uiState.value)
        coEvery { repo.getAcronymFromFullForm("HHH") } returns flow {
            emit(ApiResult.Success(listOf(AbbrevationResult())))
        }

        vm.isFullForm.value = false
        vm.searchFullForms("HHH")
        delay(2000)

        coVerify(exactly = 1) { repo.getAcronymFromFullForm("HHH") }
        TestCase.assertEquals(1, (vm.uiState.value as SuccessState).data?.size)

    }

    @Test
    fun `test api call for Short Form Error`() = runTest {
        TestCase.assertEquals(Default, vm.uiState.value )
        coEvery { repo.getAcronymFromFullForm("HHH") } returns flow {
            emit(ApiResult.Error("No data Found", null))
        }
        vm.isFullForm.value = false
        vm.searchFullForms("HHH")
        delay(2000)

        TestCase.assertNotSame(SuccessState(listOf()), vm.uiState.value)
        TestCase.assertEquals("No data Found", (vm.uiState.value as ErrorState).error.toString())

    }

    @Test
    fun `test api call for Short Form to show Loading`() = runTest {
        TestCase.assertEquals(Default, vm.uiState.value)
        coEvery { repo.getAcronymFromFullForm("HHH") } returns flow {
            emit(ApiResult.Loading)
        }
        vm.isFullForm.value = false
        vm.searchFullForms("HHH")
        delay(2000)
        TestCase.assertEquals(Loading, vm.uiState.value)

    }
    @OptIn(ExperimentalTime::class)
    @Test
    fun `collect from shared flow`() = runTest {

        vm.debounceQuery.test {
            val firstItem = awaitItem()
            TestCase.assertEquals("", firstItem)
            vm.searchFullForms("A")
            val secondItem = awaitItem()
            TestCase.assertEquals("A", secondItem)
            expectNoEvents()
        }

    }

}