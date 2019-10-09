package by.anyatsal.audiomanager.file

import by.anyatsal.audiomanager.manager.FileManager
import java.util.ArrayList

class DirectoryInfo {

    val directory: FileInfo
    val fileList: ArrayList<FileInfo> = ArrayList()

    constructor(path: String) : this(FileInfo(path))
    constructor(directory: FileInfo) {
        this.directory = directory
        if (directory.isExist && directory.isDirectory) {
            refresh()
        }
    }

    private fun refresh() {
        fileList.clear()
        this.fileList.addAll(FileManager.listFileInfo(this.directory))
    }
}