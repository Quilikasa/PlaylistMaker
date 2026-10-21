package ru.quilikasa.playlistmaker.presentation.search

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.quilikasa.playlistmaker.domain.models.Track

class TrackAdapter(private val onItemClick: (Track) -> Unit,) : RecyclerView.Adapter<TrackViewHolder>() {

    private var tracks = listOf<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onItemClick(tracks[position])
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun setTracks(tracks: List<Track>) {
        this.tracks = tracks;
    }
}