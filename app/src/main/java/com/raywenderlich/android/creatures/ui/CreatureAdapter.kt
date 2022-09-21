package com.raywenderlich.android.creatures.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.app.inflate
import com.raywenderlich.android.creatures.model.Creature
import kotlinx.android.synthetic.main.activity_creature.view.*
import kotlinx.android.synthetic.main.list_item_creature.view.*

class CreatureAdapter(private val creatures: MutableList<Creature>): RecyclerView.Adapter<CreatureAdapter.ViewHolder>() {

    class ViewHolder(v: View) : View.OnClickListener, RecyclerView.ViewHolder(v) {
        private lateinit var creature: Creature

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(creature: Creature) {
            this.creature = creature
            val context = itemView.context
            itemView.creatureImage.setImageResource(context.resources.getIdentifier(creature.uri, null, context.packageName))
            itemView.fullNameText.text = creature.fullName
            itemView.nickname.text = creature.nickname

        }

        override fun onClick(view: View?) {
            view?.let {
                val context = it.context
                val intent = CreatureActivity.newIntent(context, creature.id)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.list_item_creature))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(creatures[position])
    }

    override fun getItemCount() = creatures.count()

    fun updateCreatures(creatures: List<Creature>) {
        this.creatures.clear()
        this.creatures.addAll(creatures)
        notifyDataSetChanged()
    }

}