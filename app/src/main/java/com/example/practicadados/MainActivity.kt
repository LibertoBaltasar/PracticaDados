package com.example.practicadados

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.practicadados.databinding.ActivityMainBinding
import kotlin.random.Random
import kotlin.text.toIntOrNull

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
        binding.indicadorEstado.visibility = View.INVISIBLE
        binding.mensajeVictoria.visibility=View.INVISIBLE
        binding.reset.visibility=View.INVISIBLE
        //TODO:implementar ocultar imagendado
    }

    private fun aparecerPartida(opcion: Int) {
        binding.casillero.visibility=View.VISIBLE
        binding.j1Row.visibility = View.VISIBLE
        binding.j2Row.visibility = View.VISIBLE
        binding.Play.visibility = View.VISIBLE
        binding.indicadorEstado.visibility = View.VISIBLE
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
            val numero:Int = tirarDado()
            seleccionarDado(numero)
            if (numero==1){
                Toast.makeText(this@MainActivity,"Lo siento, turno del siguiente",Toast.LENGTH_SHORT).show()
                pasarTurno(numJugadores,true)
            }else{
                actualizarPuntuacionActualJugador(numero)
                Toast.makeText(this@MainActivity,"Felicidades has anotado $numero",Toast.LENGTH_SHORT).show()
                binding.pasar.visibility=View.VISIBLE
            }
        }
        binding.pasar.setOnClickListener {
            pasarTurno(numJugadores,false)
        }
    }

    private fun pasarTurno(numJugadores: Int, fallo:Boolean){
        val jugadorActual = binding.jugadorActual.text.toString().toInt()
        val rondaActual = binding.rondaActual.text.toString().toInt()
        if (fallo){
            vaciarPuntuacionActual()
        }
        actualizarPuntuacionFinal()
        //TODO:revisar el bucle
        binding.pasar.visibility=View.INVISIBLE
        if (rondaActual<5){
            if (jugadorActual<numJugadores){
                binding.jugadorActual.text =
                    (jugadorActual + 1).toString()
            }else{
                binding.rondaActual.text =
                    (rondaActual + 1).toString()
                binding.jugadorActual.text = "1"
            }
        }else{
            if (jugadorActual<numJugadores){
                binding.jugadorActual.text =
                    (jugadorActual + 1).toString()
            }else{
                finalizarPartida()
            }
        }
    }

    private fun finalizarPartida() {
        Toast.makeText(this@MainActivity, "Has terminado la partida", Toast.LENGTH_SHORT).show()
        binding.pasar.visibility=View.INVISIBLE
        binding.Play.visibility = View.INVISIBLE
        binding.indicadorEstado.visibility = View.INVISIBLE
        binding.mensajeVictoria.text="Has ganado la partida jugador:${seleccionarGanador()}"
        binding.mensajeVictoria.visibility=View.VISIBLE
        binding.imagenDado.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dadovictoria))
//        binding.reset.visibility=View.VISIBLE
//        reiniciarPartida()
    }

    private fun seleccionarGanador():Int{
        val puntuaciones = listOf(
            binding.j1Total.text.toString().toIntOrNull() ?: 0,
            binding.j2Total.text.toString().toIntOrNull() ?: 0,
            binding.j3Total.text.toString().toIntOrNull() ?: 0,
            binding.j4Total.text.toString().toIntOrNull() ?: 0
        )

        var ganador = 0
        var maxPuntuacion = puntuaciones[0]

        for (i in 1..3) {
            if (puntuaciones[i] > maxPuntuacion) {
                maxPuntuacion = puntuaciones[i]
                ganador = i
            }
        }

        return ganador + 1
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

    private fun actualizarPuntuacionFinal() {
        val jugador: Int = binding.jugadorActual.text.toString().toInt()
        val column: Int = binding.rondaActual.text.toString().toInt()
        val cellId = resources.getIdentifier("j${jugador}_${column}", "id", packageName)
        val cellView = binding.casillero.findViewById<TextView>(cellId)
        val cellFinalId = resources.getIdentifier("j${jugador}Total", "id", packageName)
        val cellFinalView = binding.casillero.findViewById<TextView>(cellFinalId)
        val valor1 = cellView?.text.toString().toIntOrNull() ?: 0
        val valor2 = cellFinalView?.text.toString().toIntOrNull() ?: 0
        val suma = valor1 + valor2
        cellFinalView?.text = suma.toString()
    }

    private fun actualizarPuntuacionActualJugador(puntos: Int) {
        val row:Int=binding.jugadorActual.text.toString().toInt()
        val column:Int=binding.rondaActual.text.toString().toInt()
        val cellId = resources.getIdentifier("j${row}_${column}", "id", packageName)
        val cellView = binding.casillero.findViewById<TextView>(cellId)
        cellView?.let {
            it.text = ((it.text.toString().toIntOrNull() ?: 0) + puntos).toString()
        }
    }

    private fun vaciarPuntuacionActual(){
        val row:Int=binding.jugadorActual.text.toString().toInt()
        val column:Int=binding.rondaActual.text.toString().toInt()
        val cellId = resources.getIdentifier("j${row}_${column}", "id", packageName)
        val cellView = binding.casillero.findViewById<TextView>(cellId)
        cellView?.text = "0"
    }

//    private fun reiniciarPartida(){
//        binding.reset.setOnClickListener {
//
//    }

}