package com.example.locationtestapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.Manifest
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.locationtestapp.ui.theme.LocationTestAppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel:LocationViewModel = viewModel()
            LocationTestAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp(viewModel)
                }
            }
        }
    }

}
@Composable
fun MyApp(viewModel: LocationViewModel){
        val context = LocalContext.current
        val locationutils = LocationUtils(context)

        LocationDisplay(locationutils = locationutils, context = context, viewModel = viewModel )
}


@SuppressLint("SuspiciousIndentation")
@Composable
fun LocationDisplay(
    locationutils:LocationUtils,
    context:Context,
    viewModel:LocationViewModel
){
    val location = viewModel.location.value

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
                if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                    && permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true){

                }else{
                    val rationalRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                        context as MainActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                            ||
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                context,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                    if (rationalRequired) {
                        Toast.makeText(
                            context,
                            "location is required to this feauture", Toast.LENGTH_LONG
                        )
                            .show()

                    }else{
                        Toast.makeText(
                            context, "Go to setting to enable permission", Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }


        })




            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            )
            {
                if (location != null){
                    Text(text = "Address: ${location.latitude} / ${location.longitude}")
                }else{
                    Text(text = "Location Not Available")
                }


                Button(onClick = {
                    if (locationutils.hasLocationPermission(context)) {
                        //UPDATE LOCATION IF WE HAVE ACCESS GRANTED (TRUE)
                        locationutils.requestLocationUpdates(viewModel)
                    } else {
                        //  RESQUEST LOCATION PERMISSION
                        requestPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                })
                {
                    Text(text = "Get Location")
                }
            }
        }




