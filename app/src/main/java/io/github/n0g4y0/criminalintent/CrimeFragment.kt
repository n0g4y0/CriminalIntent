package io.github.n0g4y0.criminalintent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import io.github.n0g4y0.criminalintent.models.Crime

class CrimeFragment : Fragment() {
    private lateinit var crime :Crime
    // inicializando los WIDGETS:
    private lateinit var titleField: EditText

    // ahora el boton:
    private lateinit var dateButton: Button

    // ahora el checkbox:
    private lateinit var solvedCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()
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

        dateButton.apply {
            text = crime.date.toString()
            isEnabled = false
        }

        return view
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

    }

}