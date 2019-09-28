package io.github.n0g4y0.criminalintent

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
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
private const val REQUEST_CONTACT = 1
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

    // variable que contendra a un valor, de los contactos del telefono, un sospechoso.
    private lateinit var suspectButton : Button

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

        // conectando el boton de sospechoso, con su variable:

        suspectButton = view.findViewById(R.id.crime_suspect) as Button



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
        // si ya tenemos un sospechoso, se lo pondra como texto al texto del BOTON
        if (crime.suspect.isNotEmpty()){
            suspectButton.text = crime.suspect
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            resultCode != Activity.RESULT_OK -> return

            requestCode == REQUEST_CONTACT && data != null -> {
                val contactUri: Uri? = data.data
                // especificando que campos quieres consultar para retornar valores:
                val queryFields  = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
                // hara tu consulta, la variable contactUri es como la clausula WHERE aqui:
                val cursor = requireActivity().contentResolver
                    .query(contactUri,queryFields,null,null,null)
                cursor?.use {
                    // verificando si la variable CURSOR contiene al menos, 1 resultado
                    if (it.count == 0){
                        return
                    }

                    // trae la primera columna de la primera fila de datos
                    // ese seria el nombre del sospechoso, o del contacto
                    it.moveToFirst()
                    val suspect = it.getString(0)
                    crime.suspect = suspect
                    crimeDetailViewModel.saveCrime(crime)
                    suspectButton.text = suspect
                }
            }
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
                var chooserIntent =
                        Intent.createChooser(intent, getString(R.string.send_report))
                startActivity(chooserIntent)
            }
        }

        suspectButton.apply {
            val pickContactIntent =
                    Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            setOnClickListener {
                startActivityForResult(pickContactIntent, REQUEST_CONTACT)
            }
            // esta seccion de codigo, sirve para verificar si existe una APP de CONTACTOS en el dispositivo, si no lo hay ,bloquea el BOTON
            // agregando un codigo tonto, para ver si funciona el bloqueo de boton, si no hay la APP DE CONTANTOS:
            
            val packageManager : PackageManager = requireActivity().packageManager
            val resolvedActivity : ResolveInfo? =
                packageManager.resolveActivity(pickContactIntent,
                    PackageManager.MATCH_DEFAULT_ONLY)
            if (resolvedActivity == null ){
                isEnabled = false
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