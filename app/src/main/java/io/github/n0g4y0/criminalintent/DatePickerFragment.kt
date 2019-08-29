package io.github.n0g4y0.criminalintent

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

private const val ARG_DATE = "date"

class DatePickerFragment : DialogFragment() {
    /*
    * creamos una interface llamada Callbacks, que CrimeFragment la implementara
    * */
    interface Callbacks {
        fun onDateSelected(date: Date)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // agregando un escuchador, oyente (LISTENER), para que envie la nueva FECHA de regreso a CrimeFragment:

        // OnDateSetListener -> es usado para recibir la fecha que selecciona el usuario:
        val dateListener = DatePickerDialog.OnDateSetListener {
            // el signo _ ,significa que la variable, no esta la esta utilizando, es una convencion de
            //kotlin para denotar parametros que no se utilizan:
                 _: DatePicker, year: Int, month: Int, day: Int ->

                val resultDate : Date = GregorianCalendar(year, month, day).time

                targetFragment?.let { fragment ->
                    (fragment as Callbacks).onDateSelected(resultDate)

                }
        }

        // pasando la fecha DATE, dada desde CrimeFragment
        val date = arguments?.getSerializable(ARG_DATE) as Date
        val calendar = Calendar.getInstance()
        calendar.time = date
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDate = calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            requireContext(),
            dateListener,
            initialYear,
            initialMonth,
            initialDate
        )
    }

    /*
    * creando un nueva instancia, para guardar y pasar los argumentos a otro fragment,
     * mediante la tecnico de paso de ARGUMENTOS, en este caso, la fecha:
    *
    * */
    companion object {
        fun newInstance(date: Date) : DatePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_DATE, date)
            }
            return DatePickerFragment().apply {
                arguments = args
            }
        }
    }
}