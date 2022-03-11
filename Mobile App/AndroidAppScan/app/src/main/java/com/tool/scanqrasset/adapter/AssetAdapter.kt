package com.tool.scanqrasset.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.tool.scanqrasset.databinding.ActivityFirstBinding.inflate
import com.tool.scanqrasset.databinding.AdapterAssetItemViewBinding
import com.tool.scanqrasset.model.Asset
import com.tool.scanqrasset.ui.ScanQrCodeActivity

class AssetAdapter(private var listAsset: List<Asset>) : RecyclerView.Adapter<AssetAdapter.AssetViewHolder>() {


    class AssetViewHolder(val binding: AdapterAssetItemViewBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetViewHolder {
        val binding =
            AdapterAssetItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return AssetViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listAsset.size
    }

    fun update(assets: List<Asset>) {
        this.listAsset = assets
        notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AssetViewHolder, position: Int) {
        val asset = listAsset[position]

        holder.binding.tvPartId.text = "PartID: ${asset.partId}"
        holder.binding.tvDescription.text = asset.description

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, ScanQrCodeActivity::class.java)
            intent.putExtra("data", asset)
            it.context.startActivity(intent)
        }
    }
}