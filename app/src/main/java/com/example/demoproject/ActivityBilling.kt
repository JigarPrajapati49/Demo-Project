package com.example.demoproject

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.baseclasses.BaseActivity
import com.example.demoproject.databinding.ActivityBillingBinding
import com.example.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActivityBilling : BaseActivity<MainViewModel>() {
    lateinit var binding: ActivityBillingBinding
    var isLoading = false
    private val messageArrayList = ArrayList<String>()
    private lateinit var adapter: TestAdapter // Replace with your actual adapter class
    override val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setDataBindingView(R.layout.activity_billing)


        adapter = TestAdapter(messageArrayList) // Replace with your actual adapter class
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter





        addSampleData()


        val positionToUpdate = 1 // Replace with the position you want to update
        updateItem(positionToUpdate)


        val insertItem = 2 // Replace with the position you want to update
        notifyItemInserted(insertItem)


        val startPosition = 6 // Replace with the start position of the range to update
        val itemCount = 9 // Replace with the number of items to update in the range
        updateItemRange(startPosition, itemCount)

        val startPosition1 = 10 // Replace with the start position of the range to update
        val itemCount1 = 13 // Replace with the number of items to update in the range
        notifyItemRangeChanged(startPosition1, itemCount1)

        val fromPosition = 2 // Replace with the current position of the item
        val toPosition = 5 // Replace with the new position for the item
//        moveItem(fromPosition, toPosition)

        binding.bntclick.setOnClickListener {
            scrollToBottomLikeWhatsapp()
        }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount


                /*// Check if the user has reached the end of the list
                if (!recyclerView.canScrollVertically(1)) {
                    // User has reached the end of the list
                    // Load more data (for example, add more messages)
                    binding.progressDialog.visibility = View.VISIBLE
                    loadMoreData()
                }*/
                if (!isLoading && lastVisibleItemPosition == totalItemCount - 1) {
                    showProgressBar(true)
                    loadMoreData()
                }

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        // The RecyclerView is not currently scrolling
                        // Implement any actions you want when scrolling stops
//                        toast("SCROLL_STATE_IDLE")
                    }

                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        // The user is actively dragging the RecyclerView
                        // Implement any actions you want when scrolling is actively dragging
//                        toast("SCROLL_STATE_DRAGGING")
                    }

                    RecyclerView.SCROLL_STATE_SETTLING -> {
                        // The RecyclerView is in the process of settling after a fling
                        // Implement any actions you want during settling
//                        toast("SCROLL_STATE_SETTLING")
                    }

                }
            }
        })
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_billing
    }


    private fun addSampleData() {
        messageArrayList.add("Message 1")
        messageArrayList.add("Message 2")
        messageArrayList.add("Message 2")
        messageArrayList.add("Message 2")
        messageArrayList.add("Message 2")
        messageArrayList.add("Message 2")
        messageArrayList.add("Message 2")
        messageArrayList.add("Message 2")
        messageArrayList.add("Message 2")
        messageArrayList.add("Message 2")
        messageArrayList.add("Message 2")
        messageArrayList.add("Message 2")
        messageArrayList.add("Message 2")
        messageArrayList.add("Message 2")
        messageArrayList.add("Message 2")
        messageArrayList.add("Message 2")
        messageArrayList.add("Message 2")
        messageArrayList.add("Message 2")
        messageArrayList.add("Message 2")
        messageArrayList.add("Message 2")
        messageArrayList.add("Message 2")
        messageArrayList.add("Message 2")
        messageArrayList.add("Message 2")
        messageArrayList.add("Message 2")
        messageArrayList.add("Message 2")
        // Add more messages as needed
        adapter.notifyDataSetChanged()
    }

    private fun loadMoreData() {
        // Simulate loading more data (add more messages)
        isLoading = true
        val newData = mutableListOf("New Message 1", "New Message 2")
        val newData1 = mutableListOf("New Message 1", "New Message 2")
        val newData2 = mutableListOf("New Message 1", "New Message 2")
        val newData3 = mutableListOf("New Message 1", "New Message 2")
        val newData4 = mutableListOf("New Message 1", "New Message 2")
        val newData5 = mutableListOf("New Message 1", "New Message 2")
        val newData6 = mutableListOf("New Message 1", "New Message 2")
        val newData7 = mutableListOf("New Message 1", "New Message 2")
        val newData8 = mutableListOf("New Message 1", "New Message 2")
        val newData9 = mutableListOf("New Message 1", "New Message 2")
        val newData10 = mutableListOf("New Message 1", "New Message 2")
        val newData11 = mutableListOf("New Message 1", "New Message 2")
        val newData12 = mutableListOf("New Message 1", "New Message 2")
        val newData13 = mutableListOf("New Message 1", "New Message 2")
        val newData14 = mutableListOf("New Message 1", "New Message 2")
        val newData15 = mutableListOf("New Message 1", "New Message 2")
        val newData16 = mutableListOf("New Message 1", "New Message 2")
        val newData17 = mutableListOf("New Message 1", "New Message 2")
        val newData18 = mutableListOf("New Message 1", "New Message 2")
        messageArrayList.addAll(newData)
        messageArrayList.addAll(newData1)
        messageArrayList.addAll(newData2)
        messageArrayList.addAll(newData3)
        messageArrayList.addAll(newData4)
        messageArrayList.addAll(newData5)
        messageArrayList.addAll(newData6)
        messageArrayList.addAll(newData7)
        messageArrayList.addAll(newData8)
        messageArrayList.addAll(newData9)
        messageArrayList.addAll(newData10)
        messageArrayList.addAll(newData11)
        messageArrayList.addAll(newData12)
        messageArrayList.addAll(newData13)
        messageArrayList.addAll(newData14)
        messageArrayList.addAll(newData15)
        messageArrayList.addAll(newData16)
        messageArrayList.addAll(newData17)
        messageArrayList.addAll(newData18)
        binding.recyclerView.postDelayed({
            isLoading = false
            adapter.notifyDataSetChanged()
            showProgressBar(false)
        }, 2000)
    }

    private fun scrollToPosition(position: Int) {
        binding.recyclerView.onScrolled(10, 20)
    }

    private fun addSampleDatas() {
        for (i in 1..35) {
            messageArrayList.add("Message $i")
        }
        adapter.notifyDataSetChanged()
    }

    private fun showProgressBar(isVisible: Boolean) {
        binding.progressDialog.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun updateItem(position: Int) {
        // Update the data at the specified position
        messageArrayList[position] = "Updated notifyItemChanged $position"

        // Notify the adapter that the item at the specified position has changed
        adapter.notifyItemChanged(position)
    }

    private fun notifyItemInserted(position: Int) {
        // Update the data at the specified position
        messageArrayList[position] = "Updated notifyItemInserted $position"

        // Notify the adapter that the item at the specified position has changed
        adapter.notifyItemInserted(position)
    }

    private fun updateItemRange(startPosition: Int, itemCount: Int) {
        // Update the data in the specified range
        // matlab 2 range vache data change krvo hoy to aano use thay
        for (i in startPosition until startPosition + itemCount) {
            messageArrayList[i] = "Range Changed notifyItemRangeChanged $i"
        }

        // exitsting data ma changes krse
        // Notify the adapter that the range of items has changed
        adapter.notifyItemRangeChanged(startPosition, itemCount)
    }


    private fun notifyItemRangeChanged(startPosition: Int, itemCount: Int) {
        // Update the data in the specified range
        // matlab 2 range vache data change krvo hoy to aano use thay
        for (i in startPosition until startPosition + itemCount) {
            messageArrayList[i] = "Range Changed notifyItemRangeInserted $i"
        }

        // Notify the adapter that the range of items has changed
        // new  data add thase
        adapter.notifyItemRangeInserted(startPosition, itemCount)
    }

    private fun moveItem(fromPosition: Int, toPosition: Int) {
        // Move the item within the data
        val movedItem = messageArrayList.removeAt(fromPosition)
        messageArrayList.add(toPosition, movedItem)

        // Notify the adapter that the item has been moved
        adapter.notifyItemMoved(fromPosition, toPosition)
    }


    private fun scrollToBottomLikeWhatsapp() {
        val itemCount = adapter.itemCount
        if (itemCount > 0) {
            // Scroll to the last item smoothly
            binding.recyclerView.smoothScrollToPosition(itemCount - 1)
        }
    }


}