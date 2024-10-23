package com.example.practicadados

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.practicadados.databinding.ActivityMainBinding
import java.io.File
import java.io.FileInputStream
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    //binding
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        desaparecerPartida()
        gestorSpinner()
    }

    private fun desaparecerPartida() {
        binding.casillero.visibility=View.INVISIBLE
        binding.pasar.visibility=View.INVISIBLE
        binding.Play.visibility = View.INVISIBLE
        //TODO:implementar ocultar imagendado
    }

    private fun aparecerPartida(opcion: Int) {
        binding.casillero.visibility=View.VISIBLE
        binding.j1Row.visibility = View.VISIBLE
        binding.j2Row.visibility = View.VISIBLE
        binding.Play.visibility = View.VISIBLE
        when(opcion){
            2->{
                binding.j3Row.visibility=View.INVISIBLE
                binding.j4Row.visibility = View.INVISIBLE
            }
            3->{
                binding.j4Row.visibility = View.INVISIBLE

            }
            4->{

            }
        }

    }

    private fun gestorSpinner() {
        val listaJugadores = arrayOf(
            "Selecciona la cantidad de jugadores", "2 Jugadores", "3 Jugadores", "4 Jugadores")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaJugadores)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.selectorJugadores.adapter = adapter
        binding.selectorJugadores.setSelection(0, false)
        binding.selectorJugadores.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position) as String
                if (position > 0) {
                    Toast.makeText(this@MainActivity, "Has seleccionado: $selectedItem", Toast.LENGTH_SHORT).show()
                    usarJuego(position);
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Toast.makeText(
                    this@MainActivity, "Selecciona la cantidad de jugadores", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun usarJuego(opcion:Int){
        var opcionSeleccionada=opcion
        binding.selectorJugadores.visibility=View.INVISIBLE
        opcionSeleccionada++
        aparecerPartida(opcionSeleccionada)
        gestionarTirada(opcionSeleccionada)

    }


    private fun gestionarTirada(numJugadores:Int){
        binding.Play.setOnClickListener {
            var numero:Int
            numero=tirarDado()
            seleccionarDado(numero)
            if (numero==1){
                Toast.makeText(this@MainActivity,"Lo siento, turno del siguiente",Toast.LENGTH_SHORT).show()
                pasarTurno(numJugadores,true)
            }else{
                actualizarPuntuacionActualJugador(binding.jugadorActual.text.toString().toInt(),binding.rondaActual.text.toString().toInt(),numero)
                Toast.makeText(this@MainActivity,"Felicidades has anotado $numero",Toast.LENGTH_SHORT).show()
                binding.pasar.visibility=View.VISIBLE
            }
        }
        binding.pasar.setOnClickListener {
            pasarTurno(numJugadores,false)
        }
    }

    private fun pasarTurno(numJugadores: Int, fallo:Boolean){
        //TODO:implementar ronda++
        if (fallo){
            vaciarPuntuacionActual()
        }
        actualizarpuntuaciones()
        binding.pasar.visibility=View.INVISIBLE
        if (numJugadores>=binding.jugadorActual.text.toString().toInt()+1){
            binding.jugadorActual.text=(binding.jugadorActual.text.toString().toInt()+1).toString()
        }else{
            binding.rondaActual.text=(binding.rondaActual.text.toString().toInt()+1).toString()
            binding.jugadorActual.text="1"

        }
    }

    private fun seleccionarDado(opcion: Int) {
        when (opcion) {
            1 -> binding.imagenDado.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dado1))
            2 -> binding.imagenDado.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dado2))
            3 -> binding.imagenDado.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dado3))
            4 -> binding.imagenDado.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dado4))
            5 -> binding.imagenDado.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dado5))
            6 -> binding.imagenDado.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dado6))
        }
    }

    private fun tirarDado(): Int {
        return Random.nextInt(1, 7)
    }

    private fun actualizarpuntuaciones(){
        //TODO:implementar actualizacion de puntuaciones
    }
    private fun actualizarPuntuacionActualJugador(row: Int, column: Int, puntos: Int) {
        var cellId = resources.getIdentifier("j${row}_${column}", "id", packageName)
        var cellView = binding.casillero.findViewById<TextView>(cellId)
        cellView?.let {
            it.text = ((it.text.toString().toIntOrNull() ?: 0) + puntos).toString()
        }
    }
    private fun vaciarPuntuacionActual(){
        var row:Int=binding.jugadorActual.text.toString().toInt()
        var column:Int=binding.rondaActual.text.toString().toInt()
        var cellId = resources.getIdentifier("j${row}_${column}", "id", packageName)
        var cellView = binding.casillero.findViewById<TextView>(cellId)
        cellView?.text = "0"
    }
}