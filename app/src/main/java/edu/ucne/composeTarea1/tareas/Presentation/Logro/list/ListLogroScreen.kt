package edu.ucne.composeTarea1.tareas.Presentation.Logro.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import edu.ucne.composeTarea1.domain.model.Logro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListLogroScreen(
    navController: NavController,
    viewModel: ListLogroViewModel = hiltViewModel()
){

    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Logros") },
                windowInsets = WindowInsets(top = 0.dp),
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("edit_logro_screen/0")
            }){
                Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir logro")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ){
            if(state.isLoading){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }else if(state.logros.isEmpty()){
                Text(
                    text = "No hay logros disponibles",
                    modifier = Modifier.align(Alignment.Center)
                )
            }else{
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.logros, key = {logro -> logro.logroId}){
                        LogroItem(
                            logro = it,
                            onLogroClick = {
                                navController.navigate("edit_logro_screen/${it.logroId}")
                            }
                        )
                    }

                }
            }
        }
    }
}

@Composable
private fun LogroItem(
    logro: Logro,
    onLogroClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onLogroClick)
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically

        ){
            Column  (
                modifier = Modifier.weight(1f)
            ){
                Text(
                    text = logro.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = logro.descripcion,
                    fontSize = 14.sp,
                    maxLines = 2
                )
            }
                Icon(
                    imageVector = Icons.Default.Stars,
                    contentDescription = if(logro.isCompletado) "Logro completado" else "Logro no completado",
                    tint = if(logro.isCompletado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )

        }
    }
}

@Preview
@Composable
fun ItemScreemPreview(){
    LogroItem(
        logro = Logro(
            logroId = 1,
            nombre = "Fria venganza",
            descripcion = "Derrotaste a tu oponente luego de que el te haya ganada anteriormente",
            isCompletado = false
        ),
        onLogroClick = {}
    )
}

@Preview
@Composable
fun ItemScreenCompletadoPreview(){
    LogroItem(
        logro = Logro(
            logroId = 1,
            nombre = "Fria venganza",
            descripcion = "Derrotaste a tu oponente luego de que el te haya ganada anteriormente",
            isCompletado = true
        ),
        onLogroClick = {}
    )
}