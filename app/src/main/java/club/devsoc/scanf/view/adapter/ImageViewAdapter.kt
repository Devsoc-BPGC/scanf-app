package club.devsoc.scanf.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import club.devsoc.scanf.R
import club.devsoc.scanf.model.ImageModel
import kotlinx.android.synthetic.main.scanned_image_view.view.*

class ImageViewAdapter(private var images: ArrayList<ImageModel> ):
    RecyclerView.Adapter<ImageViewAdapter.ImageVH>() {
    class ImageVH(itemView: View):RecyclerView.ViewHolder(itemView) {
        fun bind(image:ImageModel){
            itemView.image_view_rv_item.setImageBitmap(image.bitmap)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        ImageVH(LayoutInflater.from(parent.context).inflate(R.layout.scanned_image_view,parent,false))

    override fun onBindViewHolder(holder: ImageViewAdapter.ImageVH, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int {
        return images.size
    }

    fun addData(list: ArrayList<ImageModel>) {
//        users.clear()
//        users = list;
        images.addAll(list)
    }

    fun clearList() {
        images.clear()
//        Log.d("TAG", "clearList: >>>>>>>>>>>>>>>>>>>>>>>>>>> ${images.size}")

    }
}