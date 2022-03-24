package club.devsoc.scanf.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import club.devsoc.scanf.R
import club.devsoc.scanf.model.GalleryModel
import kotlinx.android.synthetic.main.gallery_photo_view.view.*
import kotlinx.android.synthetic.main.pdf_files_view.view.*

class GalleryAdapter(private var galleryModel : ArrayList<GalleryModel>):RecyclerView.Adapter<GalleryAdapter.GalleryVH>() {

    class GalleryVH(itemView: View) :RecyclerView.ViewHolder(itemView){

        fun bind(galleryModel: GalleryModel)
        {
            itemView.gallery_photo.setImageURI(galleryModel.path)
            Log.e("yeet", galleryModel.path.toString())
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        GalleryVH(LayoutInflater.from(parent.context).inflate(R.layout.gallery_photo_view,parent,false))


    override fun onBindViewHolder(holder: GalleryVH, position: Int) {
        holder.bind(galleryModel[position])
    }

    override fun getItemCount(): Int {
        return galleryModel.size
    }
}