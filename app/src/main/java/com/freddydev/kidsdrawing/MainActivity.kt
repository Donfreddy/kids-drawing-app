package com.freddydev.kidsdrawing

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import yuku.ambilwarna.AmbilWarnaDialog
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener


/**
 * https://ssaurel.medium.com/learn-to-create-a-paint-application-for-android-5b16968063f8
 */
@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

  private lateinit var drawingView: DrawingView
  private lateinit var brushBtn: ImageButton
  private lateinit var galleryBtn: ImageButton
  private lateinit var smallBtn: ImageButton
  private lateinit var mediumBtn: ImageButton
  private lateinit var llPaintColors: LinearLayout
  private lateinit var largeBtn: ImageButton
  private lateinit var bgImage: ImageView
  private var mImageButtonCurrentPaint: ImageButton? = null

  private val requestPermission: ActivityResultLauncher<Array<String>> =
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
      permissions.entries.forEach {
        val permissionName = it.key
        val isGranted = it.value

        if (isGranted) {
          chooseImageGallery()
        } else {
          if (permissionName == Manifest.permission.READ_EXTERNAL_STORAGE) {
            showRationaleDialog(
              "Kids Drawing",
              "Kids Drawing App needs to Access Your External Storage"
            )
          }
        }
      }
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#005F73")))

    drawingView = findViewById(R.id.drawing_view)
    brushBtn = findViewById(R.id.ib_brush)
    galleryBtn = findViewById(R.id.ib_gallery)
    llPaintColors = findViewById(R.id.ll_paint_colors)
    bgImage = findViewById(R.id.iv_bg)
    mImageButtonCurrentPaint = llPaintColors[1] as ImageButton

    // set Image drawable to pallet pressed
    mImageButtonCurrentPaint!!.setImageDrawable(
      ContextCompat.getDrawable(
        this,
        R.drawable.pallet_pressed
      )
    )

    drawingView.setSizeForBrush(20.toFloat())
    brushBtn.setOnClickListener {
      showBrushDialog()

      // openColorPickerDialogue();
    }

    galleryBtn.setOnClickListener {

      requestStoragePermission()

    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    val inflater: MenuInflater = menuInflater
    inflater.inflate(R.menu.more_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.item1 -> {
        Toast.makeText(this, "Save is selected", Toast.LENGTH_SHORT).show()
        true
      }
      R.id.item2 -> {
        Toast.makeText(this, "Other is selected", Toast.LENGTH_SHORT).show()
        true
      }
      R.id.item3 -> {
        Toast.makeText(this, "About is selected", Toast.LENGTH_SHORT).show()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  private fun showBrushDialog() {
    val brushDialog = Dialog(this)

    brushDialog.setContentView(R.layout.dialog_brush_size)
    brushDialog.setTitle("Brush size: ")
    brushDialog.show()

    smallBtn = brushDialog.findViewById(R.id.small_brush)
    mediumBtn = brushDialog.findViewById(R.id.medium_brush)
    largeBtn = brushDialog.findViewById(R.id.large_brush)

    smallBtn.setOnClickListener {
      drawingView.setSizeForBrush(10.toFloat())
      brushDialog.dismiss()
    }
    mediumBtn.setOnClickListener {
      drawingView.setSizeForBrush(20.toFloat())
      brushDialog.dismiss()
    }
    largeBtn.setOnClickListener {
      drawingView.setSizeForBrush(30.toFloat())
      brushDialog.dismiss()
    }
  }

  fun paintClicked(view: View) {
    if (view !== mImageButtonCurrentPaint) {
      val imageButton = view as ImageButton
      val colorTag = imageButton.tag.toString()
      drawingView.setColor(colorTag)
      imageButton.setImageDrawable(
        ContextCompat.getDrawable(
          this,
          R.drawable.pallet_pressed
        )
      )
      mImageButtonCurrentPaint!!.setImageDrawable(
        ContextCompat.getDrawable(
          this,
          R.drawable.pallet_normal
        )
      )
      mImageButtonCurrentPaint = view
    }
  }

  private fun chooseImageGallery() {
    val intent = Intent(Intent.ACTION_PICK)
    intent.type = "image/*"
    startActivityForResult(intent, IMAGE_CHOOSE)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_CHOOSE) {
      bgImage.setImageURI(data?.data)
    }
  }

  // https://www.geeksforgeeks.org/how-to-create-a-basic-color-picker-tool-in-android
  private fun openColorPickerDialogue() {
    var defaultColor = Color.parseColor(mImageButtonCurrentPaint?.tag.toString())
    val colorPickerDialogue = AmbilWarnaDialog(this, defaultColor,
      object : OnAmbilWarnaListener {
        override fun onCancel(dialog: AmbilWarnaDialog) {}

        override fun onOk(dialog: AmbilWarnaDialog, color: Int) {
          defaultColor = color
        }
      })
    colorPickerDialogue.show()
  }

  private fun requestStoragePermission() {
    if (ActivityCompat.shouldShowRequestPermissionRationale(
        this,
        Manifest.permission.READ_EXTERNAL_STORAGE
      )
    ) {
      showRationaleDialog("Kids Drawing", "Kids Drawing App needs to Access Your External Storage")
    } else {
      requestPermission.launch(arrayOf((Manifest.permission.READ_EXTERNAL_STORAGE)))
      // Todo: Add writing external permission
    }
  }

  /**
   * Show rational dialog for displaying why the app needs .
   * Only shown if the user has denied the permission request previously
   */
  private fun showRationaleDialog(title: String, message: String) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder.setTitle(title).setMessage(message)
      .setPositiveButton("Cancel") { dialog, _ -> dialog.dismiss() }
    builder.create().show()
  }

  /**
   * Method is used to show the Custom Progress Dialog
   */
  private fun customProgressDialog() {
    val customProgressDialog = Dialog(this)

    customProgressDialog.setContentView(R.layout.dialog_custom_progress)

    customProgressDialog.show()
  }

  companion object {
    private const val IMAGE_CHOOSE = 1000;
    private const val PERMISSION_CODE = 1001;
  }
}