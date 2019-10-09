package by.anyatsal.audiomanager.view

import by.anyatsal.audiomanager.file.FileInfo

interface OnDirectorySelectedListener {
    fun onDirectorySelected(fileInfo: FileInfo)
}