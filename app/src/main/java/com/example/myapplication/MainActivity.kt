package com.example.myapplication

import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.CalcularIngestaoDiaria
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var edit_peso: EditText
    private lateinit var edit_idade: EditText
    private lateinit var bt_calcular: Button
    private lateinit var txt_resultado: TextView
    private lateinit var ic_redefinir: Button
    private lateinit var bt_lembrete: Button
    private lateinit var bt_alarme: Button
    private lateinit var txt_hora: TextView
    private lateinit var txt_minute: TextView

    private lateinit var calcularIngestaoDiaria: CalcularIngestaoDiaria
    private var resultadoML = 0.0

    lateinit var timePickerDialog: TimePickerDialog
    lateinit var calendario: Calendar
    var horaAtual = 0
    var minutoAtual = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Iniciar todos os componentes.
        //IniciarComponentes()
        calcularIngestaoDiaria = CalcularIngestaoDiaria()
        // Eventos
        binding.btCalcular.setOnClickListener {
            if(binding.editPeso.text.toString().isEmpty()){
                Toast.makeText(this, "Preencha o campo peso!", Toast.LENGTH_SHORT).show()
            }else if(binding.editIdade.text.toString().isEmpty()){
                Toast.makeText(this, "Preencha o campo idade!", Toast.LENGTH_SHORT).show()
            }else{
                val peso = binding.editPeso.text.toString().toDouble()
                val idade = binding.editIdade.text.toString().toInt()
                calcularIngestaoDiaria.calcularTotalMl(peso, idade)
                // Pegar informações
                resultadoML = calcularIngestaoDiaria.ResultadoML()
                val formatar = NumberFormat.getNumberInstance(Locale("pt", "BR"))
                formatar.isGroupingUsed = false
                binding.txtResultado.text = formatar.format(resultadoML) + " " + "ml"

            }
        }
        binding.icRedefinir.setOnClickListener{
            var alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Deseja redefinir os dados ?")
                .setMessage("Deseja limpar todos os dados adicionados ?")
                .setPositiveButton("OK", {interfaceDialog, i ->
                    binding.editPeso.setText("")
                    binding.editIdade.setText("")
                    binding.txtResultado.text = ""
                    binding.txtHora.text = "00"
                    binding.txtMinute.text = "00"
                })

            alertDialog.setNegativeButton("Cancelar", {interfaceDialog, i -> })
            val dialog = alertDialog.create()
            dialog.show()
        }
        binding.btLembrete.setOnClickListener {
            calendario = Calendar.getInstance()
            horaAtual = calendario.get(Calendar.HOUR_OF_DAY)
            minutoAtual = calendario.get(Calendar.MINUTE)
            // Definir o time picker
            timePickerDialog = TimePickerDialog(this, {
                    timePicker: TimePicker, hourOfDay: Int, minutes: Int ->
                binding.txtHora.text = String.format("%02d", hourOfDay)
                binding.txtMinute.text = String.format("%02d", minutes)
            }, horaAtual, minutoAtual, true)
            timePickerDialog.show()
        }
        binding.btAlarme.setOnClickListener {
            if(!binding.txtHora.text.toString().isEmpty() && !binding.txtMinute.text.toString().isEmpty()){
                val intent = Intent(AlarmClock.ACTION_SET_ALARM)
                intent.putExtra(AlarmClock.EXTRA_HOUR, binding.txtHora.text.toString().toInt())
                intent.putExtra(AlarmClock.EXTRA_MINUTES, binding.txtMinute.text.toString().toInt())
                intent.putExtra(AlarmClock.EXTRA_MESSAGE, "Hora de Beber Água!")
                startActivity(intent)
                if(intent.resolveActivity(packageManager) != null){
                    startActivity(intent)
                }
            }

        }

    }

    /*private fun IniciarComponentes(){
        edit_peso = findViewById(R.id.edit_peso)
        edit_idade = findViewById(R.id.edit_idade)
        bt_calcular = findViewById(R.id.bt_calcular)
        txt_resultado = findViewById(R.id.txt_resultado)
        ic_redefinir = findViewById(R.id.ic_redefinir)
        bt_lembrete = findViewById(R.id.bt_lembrete)
        bt_alarme = findViewById(R.id.bt_alarme)
        txt_hora = findViewById(R.id.txt_hora)
        txt_minute = findViewById(R.id.txt_minute)
    }*/

}