package io.github.n0g4y0.criminalintent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.util.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(),
    CrimeListFragment.CallBacks {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // empezando a usar el FRAGMENT MANAGER:
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null){
            // asignamos el FRAGMENT que deseamos manejar, a una variable, para luego manipularlo.
            val fragment = CrimeListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                //desde aqui, especificamos que queremos hacer con el FRAGMENT en cuestion:
                .add(R.id.fragment_container,fragment)
                .commit()
        }

    }

    override fun oncrimeSelected(crimeID: UUID) {
        Log.d(TAG,"MainActivity.onCrimeSelected: $crimeID")
    }
}
