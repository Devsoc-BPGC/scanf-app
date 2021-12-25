package club.devsoc.scanf.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import club.devsoc.scanf.R
import club.devsoc.scanf.model.PdfModel
import kotlinx.android.synthetic.main.pdf_files_view.view.*

class PdfRvAdapter(private var pdfModel : ArrayList<PdfModel>):RecyclerView.Adapter<PdfRvAdapter.PdfVH>() {

    class PdfVH(itemView: View) :RecyclerView.ViewHolder(itemView){

        fun bind(pdfModel: PdfModel)
        {
            itemView.pdf_files_nametv.setText(pdfModel.name)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        PdfVH(LayoutInflater.from(parent.context).inflate(R.layout.pdf_files_view,parent,false))


    override fun onBindViewHolder(holder: PdfVH, position: Int) {
        holder.bind(pdfModel[position])
    }

    override fun getItemCount(): Int {
        return pdfModel.size
    }

    fun addData(list: ArrayList<PdfModel>)
    {
        pdfModel.addAll(list)
    }
}