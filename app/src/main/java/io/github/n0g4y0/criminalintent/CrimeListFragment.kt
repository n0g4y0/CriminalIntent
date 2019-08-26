package io.github.n0g4y0.criminalintent

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.n0g4y0.criminalintent.models.Crime
import java.util.*

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    /*
    * Interfaz requerida, para actividades de HOSTING.
    *
    * la siguiente interfaz, es utilizado para manipular FRAGMENT's
    * y mantener la indepencencia y facil manipulacion de los mismos.
    *
    * */
    interface CallBacks{
        fun oncrimeSelected(crimeID: UUID)
    }

    private var callbacks: CallBacks? = null



    private lateinit var crimeRecyclerView : RecyclerView
    //para conectar con el adaptador:
    private var adapter: CrimeAdapter? = CrimeAdapter(emptyList())

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeListViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        callbacks = context as CallBacks?
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

        // adaptando para los LiveDAta
        crimeRecyclerView.adapter = adapter

        // funcion para conectar el RecyclerView con el ADAPTER, para mostrar los ITEMS

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer {crimes ->
                crimes?.let {
                    Log.i(TAG,"Got crimes: ${crimes.size}")
                    updateUI(crimes)
                }

            }

        )
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun updateUI(crimes: List<Crime>) {
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
        // variable para manipular el ImageView:
        val solvedImageView: ImageView = itemView.findViewById(R.id.crime_solved)

        /*
        * es un escuchador (LISTENER) de cada item que se haga click
        * */

        init {
            itemView.setOnClickListener(this)
        }

        // funcion que vincula (ENLAZA) los datos del CRIME, con los WIDGETs
        fun bind(crime:Crime){

            this.crime = crime

            // asignando el texto, al atributo TEXT de los WIDGETs
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()
            // muestra la imagen, siempre y cuando este resuelto (valor = TRUE)
            solvedImageView.visibility = if (crime.isSolved){
                View.VISIBLE
            } else {
                View.GONE
            }

        }

        override fun onClick(v: View?) {

            // de esta manera, llamamos al callBack, y mandamos el CONTEXT, en este caso, el UUID del item CRIME.
            callbacks?.oncrimeSelected(crime.id)
        }
    }

    /*
    * funcion para manejar el adaptador de CRIME
    * */

    private inner class CrimeAdapter(var crimes: List<Crime>)
        :RecyclerView.Adapter<CrimeHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : CrimeHolder {
            val view = layoutInflater.inflate(R.layout.list_item_crime,parent,false)
            return CrimeHolder(view)

        }

        override fun getItemCount() = crimes.size


        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {

            val crime = crimes[position]
            holder.bind(crime)

        }


    }

}