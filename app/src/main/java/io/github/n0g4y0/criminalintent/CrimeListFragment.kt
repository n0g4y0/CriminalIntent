package io.github.n0g4y0.criminalintent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.n0g4y0.criminalintent.models.Crime

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    private lateinit var crimeRecyclerView : RecyclerView
    //para conectar con el adaptador:
    private var adapter: CrimeAdapter? = null

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

        // funcion para conectar el RecyclerView con el ADAPTER, para mostrar los ITEMS
        updateUI()

        return view
    }

    private fun updateUI() {
        val crimes = crimeListViewModel.crimes
        adapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = adapter
    }


    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }

    /*
    * esta funcion maneja to.do el codigo que hara el trabajo de vinculacion (BIND)
    * */

    private inner class CrimeHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var crime: Crime

        val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        val dateTextView: TextView = itemView.findViewById(R.id.crime_date)

        /*
        * es un escuchador (LISTENER) de cada item que se haga click
        * */

        init {
            itemView.setOnClickListener(this)
        }

        // funcion que vincula los datos del CRIME, con los WIDGETs
        fun bind(crime:Crime){

            this.crime = crime

            // asignando el texto, al atributo TEXT de los WIDGETs
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()

        }

        override fun onClick(v: View?) {
            Toast.makeText(context, "${crime.title} Clickeado..!!",Toast.LENGTH_SHORT)
                .show()
        }
    }

    /*
    * funcion para manejar el adaptador de CRIME
    * */

    private inner class CrimeAdapter(var crimes: List<Crime>)
        :RecyclerView.Adapter<CrimeHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : CrimeHolder {

            //getItemViewType()
            val view = layoutInflater.inflate(R.layout.list_item_crime,parent,false)
            return CrimeHolder(view)

        }

        override fun getItemViewType(position: Int): Int {
            return super.getItemViewType(position)
        }

        override fun getItemCount() = crimes.size


        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {

            val crime = crimes[position]
            holder.bind(crime)

        }


    }

}