package by.anyatsal.audiomanager.manager

import android.os.Environment
import by.anyatsal.audiomanager.file.FileInfo
import java.io.File
import java.util.*

object FileManager {

    fun listFileInfo(fileInfo: FileInfo): ArrayList<FileInfo> {
        val resultList = ArrayList<FileInfo>()
        val currentDirectory = File(fileInfo.path)
        if (currentDirectory.exists() && currentDirectory.isDirectory) {
            val listFileArray = currentDirectory.listFiles()
            val fileList = ArrayList<FileInfo>()
            val directoryList = ArrayList<FileInfo>()
            listFileArray?.forEach { it ->
                val tmpFileInfo = FileInfo(it)
                if (tmpFileInfo.hasAudio) {
                    if (tmpFileInfo.isDirectory) {
                        directoryList.add(tmpFileInfo)
                    } else
                        fileList.add(tmpFileInfo)
                }
            }
            val tmpDirectoryList =
                directoryList.sortedBy { sortFile -> sortFile.fullName.toLowerCase(Locale.getDefault()) }
            val tmpFileList =
                fileList.sortedBy { sortFile -> sortFile.fullName.toLowerCase(Locale.getDefault()) }
            resultList.addAll(tmpDirectoryList)
            resultList.addAll(tmpFileList)
            return resultList
        } else {
            return resultList
        }
    }

    fun getExternalStoragePath(): String {
        return Environment.getExternalStorageDirectory().path
    }
}