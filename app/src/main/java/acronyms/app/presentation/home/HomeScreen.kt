package acronyms.app.presentation.home

import acronyms.app.R
import acronyms.app.presentation.vm.*
import acronyms.app.ui.components.*
import acronyms.app.utils.networkconnection.ConnectionState
import acronyms.app.utils.networkconnection.connectivityState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val snackbarHostState = remember { SnackbarHostState() }
    val viewModel: AcronymsVm = hiltViewModel()

    val connection by connectivityState()
    val isConnected = connection === ConnectionState.Available
    val localCoroutineScope = rememberCoroutineScope()
    LaunchedEffect(key1 = isConnected) {
        if (!isConnected) {
            localCoroutineScope.launch {
                snackbarHostState.showSnackbar(
                    "No Internet Connection",
                    withDismissAction = true
                )
            }
        } else {
            snackbarHostState.currentSnackbarData?.dismiss()
        }
    }

    Scaffold(
        topBar = {
            TopBar(title = "Home")
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    modifier = Modifier
                        .border(2.dp, MaterialTheme.colorScheme.secondary)
                        .padding(12.dp),
                    action = {
                        TextButton(
                            onClick = { data.dismiss() },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.inversePrimary
                            )
                        ) { Text(data.visuals.actionLabel ?: "") }
                    }
                ) {
                    Text(data.visuals.message)
                }
            }
        }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    padding
                )
        ) {
            SearchScreen(viewModel, isConnected)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: AcronymsVm, isConnected: Boolean) {

    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val title = rememberSaveable {
        viewModel.title
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {

        val (search, selection, shimmerList, list, noDataFound) = createRefs()

        OutlinedTextField(
            value = title.value,
            maxLines = 2,
            onValueChange = {
                if (!it.startsWith(" ")) {
                    viewModel.title.value = it
                    scope.launch {
                        if (isConnected) {
                            viewModel.searchFullForms(title.value)
                        }
                    }
                }
            },
            modifier = Modifier
                .constrainAs(search) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth(.98f)
                .wrapContentHeight()
                .padding(10.dp),
            placeholder = { Text(text = stringResource(R.string.lbl_search)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }),
        )

        val apiType = listOf(
            stringResource(R.string.btn_short_form),
            stringResource(R.string.btn_long_form)
        )

        SegmentedControl(
            modifier = Modifier
                .constrainAs(selection) {
                    top.linkTo(search.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            items = apiType,
            onItemSelection = {
                viewModel.isFullForm.value = it == 0
                viewModel.title.value = ""
            },
        )

        viewModel.viewState.value.let {
            when (it) {
                is Loading -> {
                    LazyColumn(
                        modifier = Modifier
                            .constrainAs(shimmerList) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(selection.bottom)
                            },
                    ) {
                        repeat(6) {
                            item { LoadingShimmerEffect() }
                        }
                    }
                }
                is ProfileLoaded -> {
                    LazyColumn(modifier = Modifier
                        .constrainAs(list) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(selection.bottom)
                        }
                        .padding(5.dp)
                    ) {
                        it.data?.let {
                            items(it) { data ->
                                AcromineItem(abbreviationResult = data)
                            }
                        }
                    }
                }
                is ProfileLoadFailure -> {
                    Column(
                        modifier = Modifier.constrainAs(noDataFound) {
                            top.linkTo(selection.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                        verticalArrangement = Arrangement.Center,
                    ) {

                        it.error?.let {
                            Text(
                                text = it,
                                modifier = Modifier.wrapContentWidth(),
                                textAlign = TextAlign.Center,
                                color = Color.Black,
                                fontSize = 12.sp
                            )
                        }

                    }
                }
                is Default -> {
                    Column(
                        modifier = Modifier.constrainAs(noDataFound) {
                            top.linkTo(selection.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        },
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = stringResource(R.string.lbl_no_data_found),
                            modifier = Modifier.wrapContentWidth(),
                            textAlign = TextAlign.Center,
                            color = Color.Black,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}


