package by.anyatsal.audiomanager.file

import android.util.Log
import java.io.File

class FileInfo {

    companion object {
        const val EXTENSION = "mp3"
    }

    var path = ""
        private set
    var fullName = ""
        private set
    var size = ""
        private set
    var isExist = false
        private set
    var isDirectory = false
        private set
    private var extensionName = ""
    var hasAudio = false
        private set
    private var numberOfAudio = 0

    constructor(path: String) : this(File(path))
    constructor(file: File) {
        this.fullName = file.name
        this.extensionName = file.extension
        this.isExist = file.exists()
        this.isDirectory = file.isDirectory
        this.hasAudio = checkForAudio(file)
        this.numberOfAudio =
            file.listFiles()?.filter { it.isFile && it.extension == EXTENSION }?.size ?: 0
        this.size = when {
            this.numberOfAudio == 0 -> ""
            else -> this.numberOfAudio.toString()
        }
        this.path = checkForAdding(file)
    }

    private fun checkForAdding(file: File): String {
        if (file.isFile) {
            return file.path
        }
        var count = 0
        var tmpFile = file
        file.listFiles()?.forEach {
            if (it.isFile && it.extension == EXTENSION) {
                return file.path
            }
        }
        val tmpList = file.listFiles()?.filter { it.isDirectory }
        run loop@{
            tmpList?.forEach {
                if (checkForAudio(it)) {
                    Log.e("3333 ", it.path)
                    count++
                    tmpFile = it
                    if (count > 1) {
                        return@loop
                    }
                }
            }
        }
        return if (count == 1) {
            Log.e("NAME", tmpFile.path)
            this.fullName = "${file.name}/${tmpFile.name}"
            checkForAdding(tmpFile)
        } else {
            file.path
        }
    }

    private fun checkForAudio(file: File): Boolean {
        if (file.isFile) {
            return file.extension == EXTENSION
        } else {
            file.listFiles()?.forEach {
                if (it.isFile) {
                    if (it.extension == EXTENSION)
                        return true
                    else
                        return@forEach
                }
                if (it.isDirectory) {
                    if (checkForAudio(it))
                        return true
                    else
                        return@forEach
                }
            }
            return false
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is FileInfo) {
            return this.path == other.path
        } else if (other is File) {
            return File(this.path).absolutePath == other.absolutePath
        }
        return false
    }

    override fun hashCode(): Int {
        var result = path.hashCode()
        result = 31 * result + fullName.hashCode()
        result = 31 * result + size.hashCode()
        result = 31 * result + isExist.hashCode()
        result = 31 * result + isDirectory.hashCode()
        result = 31 * result + extensionName.hashCode()
        result = 31 * result + hasAudio.hashCode()
        result = 31 * result + numberOfAudio
        return result
    }
}