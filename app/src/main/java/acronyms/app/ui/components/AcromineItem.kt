package acronyms.app.ui.components

import acronyms.app.data.model.AbbrevationResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun AcromineItem(
    abbreviationResult: AbbrevationResult,
    modifier: Modifier = Modifier
) {

    ConstraintLayout(modifier = modifier.fillMaxWidth().wrapContentHeight()) {

        val (shortForm, lonfFormList) = createRefs()

        Text(
            text = abbreviationResult.sf.toString(),
            modifier = Modifier
                .constrainAs(shortForm) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .wrapContentHeight(),
            textAlign = TextAlign.Start,
            color = Color.Black,
            fontSize = TextUnit.Unspecified
        )
        Column(modifier = Modifier.constrainAs(lonfFormList) {
            top.linkTo(shortForm.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }) {
            abbreviationResult.lfs.forEach {
                Text(
                    text = it.lf.toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    textAlign = TextAlign.Start,
                    color = Color.Black,
                    fontSize = TextUnit.Unspecified
                )
            }

        }
    }


}