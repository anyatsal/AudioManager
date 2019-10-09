package by.anyatsal.audiomanager.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.anyatsal.audiomanager.R
import by.anyatsal.audiomanager.file.FilePageInfo
import by.anyatsal.audiomanager.view.OnBackPressedListener
import kotlinx.android.synthetic.main.fragment_list.*

class FileListFragment : OnBackPressedListener, Fragment() {

    companion object {
        private const val TYPE_FILE = 0
        private const val TYPE_DIRECTORY = 1
    }

    private val mAdapter = FileAdapter()
    private val mFilePageInfo = FilePageInfo()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCurrentFolderName()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recycler_view_file_list.layoutManager = FileListLayoutManager()
        recycler_view_file_list.adapter = mAdapter
    }

    private fun initCurrentFolderName() {
        current_folder.text = mFilePageInfo.getCurrentDirectoryInfo().directory.path
    }

    override fun onBackPressed(): Boolean {
        return when {
            mFilePageInfo.canPopDirectory() -> {
                mFilePageInfo.popDirectory()
                mAdapter.notifyDataSetChanged()
                initCurrentFolderName()
                true
            }
            else -> {
                false
            }
        }
    }

    private inner class FileListLayoutManager : LinearLayoutManager(context)

    private inner class FileAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun getItemViewType(position: Int): Int {
            val fileInfo = mFilePageInfo.getCurrentPageFile(position)
            return if (fileInfo.isDirectory) {
                TYPE_DIRECTORY
            } else {
                TYPE_FILE
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                TYPE_FILE -> {
                    FileViewHolder(parent)
                }
                TYPE_DIRECTORY -> {
                    DirectoryViewHolder(parent)
                }
                else -> throw RuntimeException("Not file, not directory")
            }
        }

        override fun getItemCount(): Int = mFilePageInfo.getCurrentPageFileCount()

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val fileInfo = mFilePageInfo.getCurrentPageFile(position)
            when (holder) {
                is DirectoryViewHolder -> {
                    holder.setTitle(fileInfo.fullName)
                    holder.setBackground(R.drawable.bg_file_directory)
                    holder.setInfo(fileInfo.size)
                    holder.setOnClickListener(View.OnClickListener {
                        mFilePageInfo.pushDirectory(fileInfo)
                        mAdapter.notifyDataSetChanged()
                        initCurrentFolderName()
                    })
                }
                is FileViewHolder -> {
                    holder.setTitle(fileInfo.fullName)
                    holder.setBackground(R.drawable.bg_file_file)
                    holder.setIcon(R.drawable.ic_file_grey)
                }
            }
        }
    }

    private inner class DirectoryViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        layoutInflater.inflate(
            R.layout.item_file_directory,
            parent,
            false
        )
    ) {
        private val titleTextView = itemView.findViewById<TextView>(R.id.itemFileDirectoryTitle)
        private val infoTextView = itemView.findViewById<TextView>(R.id.itemFileDirectoryInfo)

        fun setTitle(title: String) {
            titleTextView.text = title
        }

        fun setInfo(info: String) {
            infoTextView.text = info
        }

        fun setOnClickListener(onClickListener: View.OnClickListener) {
            itemView.setOnClickListener(onClickListener)
        }

        fun setBackground(resID: Int) {
            itemView.setBackgroundResource(resID)
        }
    }

    private inner class FileViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(layoutInflater.inflate(R.layout.item_file_file, parent, false)) {
        private val iconImageView = itemView.findViewById<ImageView>(R.id.itemFileFileIcon)
        private val titleTextView = itemView.findViewById<TextView>(R.id.itemFileFileFileName)

        fun setIcon(resID: Int) {
            iconImageView.setImageResource(resID)
        }

        fun setTitle(title: String) {
            titleTextView.text = title
        }

        fun setBackground(resID: Int) {
            itemView.setBackgroundResource(resID)
        }
    }
}