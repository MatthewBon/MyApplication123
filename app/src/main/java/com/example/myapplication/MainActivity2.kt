package com.example.myapplication
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.repository.model.CountryResponse
import com.example.myapplication.repository.model.Emblem
import com.example.myapplication.repository.model.Name

class MainActivity2 : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: CountryDetailsViewModel by viewModels()

        val id = intent.getStringExtra("CUSTOM_ID")

        Toast.makeText(this, "$id details", Toast.LENGTH_SHORT).show()

        if (id != null) {
            viewModel.getData(id)
        }

        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(android.graphics.Color.parseColor("#f2dc91")))
            ){
            DetailView(viewModel = viewModel)
            }
        }
    }
}


@Composable
fun DetailView(viewModel: CountryDetailsViewModel) {
    val uiState by viewModel.immutableCountriesData.observeAsState(CountryDetailsViewModel.UiState())
    when {
        uiState.isLoading -> { MyLoadingView() }

        uiState.error != null -> { MyErrorView() }

        uiState.data != null -> { uiState.data?.let { DetailList(country = it) } }
    }
}


@Composable
fun DetailList(country: List<CountryResponse>) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .padding(start = 50.dp, end = 50.dp)
        ) {
            Text(
                text = "Details",
                color = Color.White,
                fontSize = 24.sp, // Set the font size to make it bigger
                textAlign = TextAlign.Center, // Center the text horizontally
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center) // Center the text vertically
            )
        }
        LazyColumn {
            items(country) { country ->
                DetailView(
                    name = country.name,
                    capital =country.capital,
                    region =country.region,
                    population =country.population,
                    landlocked = country.landlocked,
                    coatOfArms =country.coatOfArms,
                )
            }
        }
    }
}

@Composable
fun DetailView(
    name: Name,
    capital: List<String>,
    region: String,
    population: Int,
    landlocked: Boolean,
    coatOfArms: Emblem,
) {
    Column(modifier = Modifier
        .padding(all = 5.dp)){
        Row {
            Box(modifier = Modifier
                .border(BorderStroke(1.dp, Color.DarkGray), shape = RoundedCornerShape(8.dp))
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(all = 5.dp)
                .fillMaxWidth()){
                Column {
                    Text(text = name.official, fontSize = 30.sp, fontWeight = FontWeight(1000), color = Color.DarkGray)
                    Row {
                        Column {
                            AsyncImage(
                                model = coatOfArms.png,
                                contentDescription = "Flag of ${name.common}",
                                placeholder = painterResource(R.drawable.ic_launcher_foreground)
                            )
                            Text(text = "Region: $region")
                            Text(text = "Stolica: ${capital[0]}")
                            Text(text = "Populacja kraju: $population")
                            Text(text = if (landlocked)"Ten kraj nie ma dostępu do morza/oceanu" else "Ten kraj ma dostęp do morza/oceanu")
                        }
                    }
                }
            }
        }
    }
}
