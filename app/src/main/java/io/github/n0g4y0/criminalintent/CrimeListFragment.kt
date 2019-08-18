package io.github.n0g4y0.criminalintent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    private lateinit var crimeRecyclerView : RecyclerView

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // inicialmente, al llamar al fragment, solo mostrara esto en consola:
        Log.d(TAG, "Total crimes: ${crimeListViewModel.crimes.size}")
    }

    /*
    * funcion que visualiza o INFLATE la vista del fragment (en este caso, el de FRAGMENT_CRIME_LIST):
    *
    * */

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list,container,false)

        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView

        crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }


    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }
}