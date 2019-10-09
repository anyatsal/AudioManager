package by.anyatsal.audiomanager.file

import by.anyatsal.audiomanager.manager.FileManager
import java.io.File
import java.util.*

class FilePageInfo {

    private val mDirectoryStack = LinkedList<DirectoryInfo>()

    constructor() : this(FileManager.getExternalStoragePath())
    constructor(directoryPath: String) : this(DirectoryInfo(directoryPath))
    constructor(directoryInfo: DirectoryInfo) {
        mDirectoryStack.clear()
        mDirectoryStack.add(directoryInfo)
    }

    fun pushDirectory(fileInfo: FileInfo) {
        val file = File(fileInfo.path)
        if (file.exists() && file.isDirectory) {
            mDirectoryStack.addLast(DirectoryInfo(fileInfo))
        }
    }

    fun popDirectory() {
        if (mDirectoryStack.isNotEmpty()) {
            mDirectoryStack.removeLast()
        }
    }

    fun getCurrentPageFileCount(): Int {
        if (mDirectoryStack.isNotEmpty()) {
            return mDirectoryStack.last.fileList.size
        }
        return 0
    }

    fun getCurrentPageFile(pos: Int): FileInfo {
        return mDirectoryStack.last.fileList[pos]
    }

    fun getCurrentDirectoryInfo(): DirectoryInfo {
        return mDirectoryStack.last
    }

    fun canPopDirectory(): Boolean {
        return mDirectoryStack.size > 1
    }
}