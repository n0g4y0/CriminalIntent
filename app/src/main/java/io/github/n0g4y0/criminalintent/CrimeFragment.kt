package io.github.n0g4y0.criminalintent

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import io.github.n0g4y0.criminalintent.models.Crime
import io.github.n0g4y0.criminalintent.models.CrimeDetailViewModel
import java.text.DateFormat
import java.util.*

private const val TAG = "CrimeFragment"
private const val ARG_CRIME_ID = "crime_id"
private const val DIALOG_DATE = "DialogDate"
// constante para solicitar CODIGO, para retornar valores a este Fragment:
private const val REQUEST_DATE = 0
private const val DATE_FORMAT = "EEE, MMM, dd"

class CrimeFragment : Fragment(), DatePickerFragment.Callbacks {
    private lateinit var crime :Crime
    // inicializando los WIDGETS:
    private lateinit var titleField: EditText

    // ahora el boton:
    private lateinit var dateButton: Button

    // ahora el checkbox:
    private lateinit var solvedCheckBox: CheckBox

    // variable para el boton enviar reporte:
    private lateinit var reportButton : Button

    // variable para mostrar la consulta del ID de un determinado Crime(utilizando LiveDatas:)
    private val crimeDetailViewModel : CrimeDetailViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()

        val crimeID: UUID = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        crimeDetailViewModel.loadCrime(crimeID)
    }

    /*
    * con esta funcion, creamos, o inflamos los GRAFICOS en el ACTIVITY PRINCIPAL:
    * NOTA: esta funcion, es el lugar donde se conectan los WIDGETS:
    * */

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view =  inflater.inflate(R.layout.fragment_crime,container,false)

        // conectando el WIDGET edittext (de titulo TITLE) a la variable del mismo tipo, inicializado a un inicio.
        titleField = view.findViewById(R.id.crime_title) as EditText
        // conectando al boton:
        dateButton = view.findViewById(R.id.crime_date) as Button
        // conectando el checkbox:
        solvedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox

        // conectando al boton de enviar reporte, enviar crimen:

        reportButton = view.findViewById(R.id.crime_report) as Button



        return view
    }

    /*
    * funcion que hace la implementacion de OBSERVERS (observadores) de los datos del CrimeDetailViewModel
    * y actualiza la UI, cada vez que cambien los datos (o se actualizen)
    *
    * */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        crimeDetailViewModel.crimeLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {crime ->
                crime?.let {
                    this.crime = crime
                    updateUI()
                }
            })
    }

    /*
    * actualiza las vistas UI
    *
    * */
    private fun updateUI(){
        titleField.setText(crime.title)
        dateButton.text = crime.date.toString()

        // mediante
        solvedCheckBox.apply {
            isChecked = crime.isSolved
            jumpDrawablesToCurrentState()
        }
    }

    /*
    * funcion para devolver un reporte de crimen
    * */

    private fun getCrimeReport(): String{
        val solvedString = if (crime.isSolved){
            getString(R.string.crime_report_solved)
        }else{
            getString(R.string.crime_report_unsolved)
        }
        val dateString = android.text.format.DateFormat.format(DATE_FORMAT, crime.date).toString()
        var suspect = if (crime.suspect.isBlank()){
            getString(R.string.crime_report_no_suspect)
        }else{
            getString(R.string.crime_report_suspect, crime.suspect)
        }

        return getString(R.string.crime_report,
                crime.title,dateString,solvedString,suspect)
    }



    /*
    * funcion para INICIALIZAR y agregar un ESCUCHA (LISTENER) al WIDGET:
    * */

    override fun onStart() {
        super.onStart()

        /*
        * una de las razones para implementar esto: es para evitar que se pierda el dato ingresado (edittext y checkbox)
        * al momento de girar la pantalla.
        * */
        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This space intentionally left blank
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                crime.title = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
                // this One too
            }

        }
        titleField.addTextChangedListener(titleWatcher)

        solvedCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->
                crime.isSolved = isChecked
            }
        }
        /*
        * usando el boton, para motrar,mediante un DialogFragment, un DataPicker, para cambiar la fecha:
        *
        * */

        dateButton.setOnClickListener {
            DatePickerFragment.newInstance(crime.date).apply {
                // hacemos que CrimeFragment sea el FRAGMENTO OBJETIVO de la instancia de DatePickerFragment:
                setTargetFragment(this@CrimeFragment, REQUEST_DATE)

                show(this@CrimeFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }

        // dandole funcionalidad al boton de enviar reporte del crimen:
        reportButton.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getCrimeReport())
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    getString(R.string.crime_report_subject))
            }.also { intent ->
                startActivity(intent)
            }
        }

    }

    /*
   *
   * sobreEscribimos el siguiente ciclo de vida, para cuando el fragment pase a un estado STOPPED
   * (cuando el fragment no se pueda ver), aprovechamos para guardar los datos en la BD.
   * */

    override fun onStop() {
        super.onStop()
        crimeDetailViewModel.saveCrime(crime)
    }

    /*
    * implementando el Callbacks  de DatePickerFragment:
    * */
    override fun onDateSelected(date: Date) {
        crime.date = date
        updateUI()
    }





    companion object {
        fun newInstance(crimeID: UUID): CrimeFragment{
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID,crimeID)
            }

            return CrimeFragment().apply {
                arguments = args
            }
        }
    }


}