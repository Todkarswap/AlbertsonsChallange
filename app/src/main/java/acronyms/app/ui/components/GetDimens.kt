package acronyms.app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp


@Composable
fun getDP(dimenKey : Int) : Dp {
     return dimensionResource(id = dimenKey)
}
@Composable
fun getSP(key : Int) : Int {
     return integerResource(id = key)
}
@Composable
fun getString(dimenKey : Int) : String {
     return stringResource(id = dimenKey)
}