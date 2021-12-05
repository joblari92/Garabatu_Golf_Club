package com.example.garabatugolfclub

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class TarjetaAdapter(val listaPartidos: MutableList<Tarjetas> = mutableListOf<Tarjetas>()):RecyclerView.Adapter<TarjetaAdapter.TarjetaHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarjetaHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TarjetaHolder(layoutInflater.inflate(R.layout.item_tarjetas, parent,false))
    }

    override fun onBindViewHolder(holder: TarjetaHolder, position: Int) {
        holder.render(listaPartidos[position])
        holder.view.setOnClickListener {
            val context = holder.view.context
            val intent = Intent(context, ResumenTarjeta::class.java)
            intent.putExtra("campoSeleccionado",holder.devuelveCampo())
            intent.putExtra("idPartido",holder.devuelveIdPartido())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listaPartidos.size
    }





    class TarjetaHolder(val view: View):RecyclerView.ViewHolder(view){
        var campo: String = ""
        var idPartido: String = ""


        fun render(tarjeta: Tarjetas){
           view.findViewById<TextView>(R.id.campoTarjeta).setText(tarjeta.campo)
            view.findViewById<TextView>(R.id.fechaTarjeta).setText(tarjeta.fecha)
            view.findViewById<TextView>(R.id.horaTarjeta).setText(tarjeta.hora)
            view.findViewById<TextView>(R.id.puntosTarjeta).setText(tarjeta.puntos)
            campo = tarjeta.campo
            idPartido= tarjeta.fecha + tarjeta.hora
        }
        fun devuelveCampo(): String {
            return campo
        }
        fun devuelveIdPartido(): String{
            return idPartido
        }


    }



}