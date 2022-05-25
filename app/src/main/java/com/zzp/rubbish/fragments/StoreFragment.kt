package com.zzp.rubbish.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zzp.rubbish.R
import com.zzp.rubbish.data.Article
import com.zzp.rubbish.data.Goods
import com.zzp.rubbish.data.UserInfo
import com.zzp.rubbish.databinding.FragmentStoreBinding
import com.zzp.rubbish.databinding.ItemGoodsBinding
import com.zzp.rubbish.network.NetWork
import com.zzp.rubbish.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class StoreFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = StoreFragment()
    }

    private lateinit var binding: FragmentStoreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.IO).launch {
            getGoods(UserInfo.token)
        }
    }

    private suspend fun getGoods(token: String) {
        try {
            val response = NetWork.getGoods(token)
            CoroutineScope(Dispatchers.Main).launch {
                if (response.code == 0) {
                    binding.recyclerView.adapter = GoodsAdapter(response.data.goods)
                } else {
                    response.msg.showToast()
                }
            }
        } catch (e: Exception) {
            Log.e("TAG", "getGoods: ", e)
        }
    }

    private inner class GoodsAdapter(private val goodsList: List<Goods>) :
        RecyclerView.Adapter<GoodsAdapter.GoodsViewHolder>() {

        inner class GoodsViewHolder(val binding: ItemGoodsBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodsViewHolder{
            val binding =
                ItemGoodsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return GoodsViewHolder(binding)
        }

        override fun getItemCount() = goodsList.size

        override fun onBindViewHolder(holder: GoodsViewHolder, position: Int) {
            val goods = goodsList[position]
            Glide.with(this@StoreFragment).load(goods.image).into(holder.binding.image)
            holder.binding.name.text = goods.name
            holder.binding.price.text = "${goods.price}积分"
        }
    }
}