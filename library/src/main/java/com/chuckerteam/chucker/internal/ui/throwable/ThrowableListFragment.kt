package com.chuckerteam.chucker.internal.ui.throwable

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.chuckerteam.chucker.R
import com.chuckerteam.chucker.databinding.ChuckerFragmentThrowableListBinding
import com.chuckerteam.chucker.internal.ui.MainViewModel

internal class ThrowableListFragment : Fragment(), ThrowableAdapter.ThrowableClickListListener {

    private lateinit var viewModel: MainViewModel
    private lateinit var errorsBinding: ChuckerFragmentThrowableListBinding
    private lateinit var errorsAdapter: ThrowableAdapter
    var tag2: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        errorsBinding = ChuckerFragmentThrowableListBinding.inflate(inflater, container, false)
        errorsAdapter = ThrowableAdapter(this)

        with(errorsBinding) {
            tutorialLink.movementMethod = LinkMovementMethod.getInstance()
            errorsRecyclerView.apply {
                setHasFixedSize(true)
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                adapter = errorsAdapter
            }
        }

        return errorsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLiveData(tag2).observe(
            viewLifecycleOwner,
            Observer { throwables ->
                errorsAdapter.setData(throwables)
                errorsBinding.tutorialView.visibility = if (throwables.isNullOrEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.chucker_throwables_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.clear) {
            askForConfirmation()
            true
        } else if (item.itemId == R.id.search) {
           doSearch(tag2)
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun doSearch(tag2: String) {
        val holder = SearchViewHolder(requireContext(), null)
       var dialog =  AlertDialog.Builder(requireContext())
               .setView(holder.rootView)
                .show()
        dialog.setOnShowListener(DialogInterface.OnShowListener {
            dialog.window?.attributes?.width   = context?.resources?.displayMetrics?.widthPixels
        })
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


    }

    private fun askForConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.chucker_clear)
            .setMessage(getString(R.string.chucker_clear_throwable_confirmation)+"-"+tag2)
            .setPositiveButton(R.string.chucker_clear) { _, _ ->
                viewModel.clearThrowables(tag2+"")
            }
            .setNegativeButton(R.string.chucker_cancel, null)
            .show()
    }

    override fun onThrowableClick(throwableId: Long, position: Int) {
        ThrowableActivity.start(requireActivity(), throwableId)
    }

    companion object {
        fun newInstance(tag:String): ThrowableListFragment {
            val f = ThrowableListFragment()
            f.tag2 = tag
            return f
        }
    }
}
