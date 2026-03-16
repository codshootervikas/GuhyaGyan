import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vikas.guhyagyan.R
import com.vikas.guhyagyan.models.pdf.Data

class PdfAdapter(private val pdfList: List<Data>) :
    RecyclerView.Adapter<PdfAdapter.PdfViewHolder>() {

    class PdfViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val fileName: TextView = itemView.findViewById(R.id.fileName)
        val fileInfo: TextView = itemView.findViewById(R.id.fileInfo)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pdf, parent, false)

        return PdfViewHolder(view)
    }

    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {

        val pdf = pdfList[position]

        holder.fileName.text = pdf.fileName
        holder.fileInfo.text = pdf.fileSize.toString()
    }

    override fun getItemCount(): Int {
        return pdfList.size
    }
}