package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.data.StudioRepository
import com.example.data.WestCoastDatabase
import com.example.ui.StudioApp
import com.example.ui.StudioViewModel
import com.example.ui.theme.StudioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = Room.databaseBuilder(
            applicationContext,
            WestCoastDatabase::class.java, "west-coast-node-db"
        )
            .fallbackToDestructiveMigration()
            .build()
        val repository = StudioRepository(
            db.studioDao(),
            db.sessionDao(),
            db.musicDao(),
            db.arConfigDao()
        )
        
        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return StudioViewModel(repository) as T
            }
        }
        val viewModel = ViewModelProvider(this, viewModelFactory)[StudioViewModel::class.java]

        setContent {
            StudioTheme {
                StudioApp(viewModel)
            }
        }
    }
}
