package acronyms.app.ui.components

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun AcromineItem(
    abbreviationResult: AbbrevationResult,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(.98f)
            .padding(top = 5.dp)
            .wrapContentHeight(), shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors()
    ) {

        Column(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp)
        ) {

            Text(
                text = abbreviationResult.sf.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                maxLines = 2,
                textAlign = TextAlign.Center,
                color = Color.Black,
                fontSize = TextUnit.Unspecified
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(5.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
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

                    it.vars.map { vari ->
                        ShowVariation(variations = vari)
                    }
                }

            }
        }

    }
}

@Composable
fun ShowVariation(variations: Vars) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = "Long Form : ${variations.lf}",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            textAlign = TextAlign.Start,
            color = Color.Black,
            fontSize = TextUnit.Unspecified
        )
        Text(
            text = "Since : ${variations.since}",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            textAlign = TextAlign.Start,
            color = Color.Black,
            fontSize = TextUnit.Unspecified
        )
        Text(
            text = "Frequency : ${variations.freq}",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            textAlign = TextAlign.Start,
            color = Color.Black,
            fontSize = TextUnit.Unspecified
        )
    }

}