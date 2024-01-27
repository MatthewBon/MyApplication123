package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import com.example.myapplication.repository.model.CountryResponse
import com.example.myapplication.repository.model.Flag
import com.example.myapplication.repository.model.Name

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getData()

        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(android.graphics.Color.parseColor("#f2dc91")))
            ){
                MainView(
                    viewModel = viewModel,
                    onClick = { id -> navigateToDetailsActivity(id) })
            }
        }
    }

    private fun navigateToDetailsActivity(id: String) {
        val detailsIntent = Intent(this, MainActivity2::class.java)
        detailsIntent.putExtra("CUSTOM_ID", id)
        startActivity(detailsIntent)
    }

}

@Composable
fun MainView(viewModel: MainViewModel, onClick: (String) -> Unit) {
    val uiState by viewModel.immutableCountriesData.observeAsState(MainViewModel.UiState())
    when {
        uiState.isLoading -> { MyLoadingView() }

        uiState.error != null -> { MyErrorView() }

        uiState.data != null -> { uiState.data?.let { MyListView(country = it, onClick = onClick) } }
    }
}
@Composable
fun MyErrorView() {
//    wyświetla tekst z błędem lub snackbar
}

@Composable
fun MyLoadingView() {
//    wyświetla loader
}

@Composable
fun MyListView(country: List<CountryResponse>, onClick: (String) -> Unit) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .padding(start = 50.dp, end = 50.dp)
        ) {
            Text(
                text = "Countries App",
                color = Color.White,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            )
        }
        LazyColumn {
            items(country) { country ->
                CountryView(
                    name = country.name,
                    capital =country.capital,
                    flags = country.flags,
                    id = country.name.common,
                    onClick = { id -> onClick.invoke(id) }
                )
            }
        }
    }
}

@Composable
fun CountryView(
    name: Name,
    capital: List<String>,
    flags: Flag,
    id: String,
    onClick: (String) -> Unit,
    ) {
        Column(modifier = Modifier
            .padding(all = 5.dp)
            .clickable { onClick.invoke(id) }){
            Row {
                Box(modifier = Modifier
                    .border(BorderStroke(1.dp, Color.DarkGray), shape = RoundedCornerShape(8.dp))
                    .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                    .padding(all = 5.dp)
                    .fillMaxWidth()){
                    Column {
                        Text(text = name.common, fontSize = 20.sp, fontWeight = FontWeight(1000), color = Color.DarkGray)
                        Row {
                            Column {
                                AsyncImage(
                                    model = flags.png,
                                    contentDescription = "Flag of ${name.common}",
                                    placeholder = painterResource(R.drawable.ic_launcher_foreground)
                                )
                                Text(text = "Stolica: ${capital[0]}")
                                Text(text = "")
                                Text(text = "")
                            }
                        }
                        Text(text = "Click there to see ${name.common} details", textAlign = TextAlign.Center)
                    }
                }
            }
        }
}