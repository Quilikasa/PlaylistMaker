package ru.quilikasa.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.track_list_item, parent, false)
) {

    private val trackImage: ImageView = itemView.findViewById(R.id.track_image)
    private val trackTitle: TextView = itemView.findViewById(R.id.track_title)
    private val trackDescription: TextView = itemView.findViewById(R.id.track_description)

    fun bind(model: Track) {
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(trackImage)

        trackTitle.setText(model.trackName)
        trackDescription.setText("${model.artistName} · ${model.trackTime}")
    }
}