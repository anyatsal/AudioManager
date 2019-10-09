package by.anyatsal.audiomanager.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import by.anyatsal.audiomanager.view.OnDirectorySelectedListener
import by.anyatsal.audiomanager.R
import by.anyatsal.audiomanager.file.FileInfo
import by.anyatsal.audiomanager.manager.FileManager
import by.anyatsal.audiomanager.view.fragment.FileListFragment
import by.anyatsal.audiomanager.view.OnBackPressedListener
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(),
    OnDirectorySelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        } else {
            if (savedInstanceState == null)
                loadFragment(FileManager.getExternalStoragePath(), false)
        }
    }

    private fun loadFragment(path: String?, withBackStack: Boolean = true) {
        if (path != null) {
            val tr = supportFragmentManager.beginTransaction()
                .replace(R.id.container, FileListFragment())
            if (withBackStack)
                tr.addToBackStack(null)

            tr.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            tr.commit()
        }
    }

    override fun onBackPressed() {
        val fm = supportFragmentManager
        var backPressedListener: OnBackPressedListener? = null
        for (fragment in fm.fragments) {
            if (fragment is OnBackPressedListener) {
                backPressedListener = fragment
                break
            }
        }

        if (backPressedListener != null) {
            if (!backPressedListener.onBackPressed()) {
                exitProcess(0)
            }
        }
    }

    override fun onDirectorySelected(fileInfo: FileInfo) {
        loadFragment(fileInfo.path)
    }
}
