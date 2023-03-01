package acronyms.app.ui.components

import acronyms.app.R
import acronyms.app.data.model.AbbrevationResult
import acronyms.app.data.model.Vars
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AcronymItem(
    abbreviationResult: AbbrevationResult
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        abbreviationResult.lfs.forEach {
            Text(
                text = it.lf.toString().replaceFirstChar {
                    it.uppercaseChar()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                textAlign = TextAlign.Start,
                color = Color.Blue,
                fontSize = 18.sp,

                )

            it.vars.map { vari ->
                ShowVariation(variations = vari)
            }
        }

    }
}

@Composable
fun ShowVariation(variations: Vars) {

    Card(
        modifier = Modifier
            .fillMaxWidth(.98f)
            .wrapContentHeight(), shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {

            Text(
                text = buildValueString(stringResource(R.string.lbl_long_form), variations.lf),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                textAlign = TextAlign.Start,
                color = Color.Black,
                fontSize = TextUnit.Unspecified
            )
            Text(
                text = buildValueString(
                    stringResource(R.string.lbl_since),
                    "${variations.since}"
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                textAlign = TextAlign.Start,
                color = Color.Black,
                fontSize = TextUnit.Unspecified
            )
            Text(
                text = buildValueString(
                    stringResource(R.string.lbl_frequency),
                    "${variations.freq}"
                ),
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

fun buildValueString(boldString: String, valueString: String?): AnnotatedString {
    return buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
            append("$boldString ")
        }
        valueString?.let {
            append(valueString)
        }
    }
}