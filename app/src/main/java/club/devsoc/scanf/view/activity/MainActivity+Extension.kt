package club.devsoc.scanf

import android.app.AlertDialog
import android.content.DialogInterface
import club.devsoc.scanf.view.activity.ImageActivity

fun ImageActivity.showDialogOK(message: String, okListener: DialogInterface.OnClickListener) {
    AlertDialog.Builder(this)
        .setMessage(message)
        .setPositiveButton("OK", okListener)
        .setNegativeButton("Cancel", okListener)
        .create()
        .show()
}
