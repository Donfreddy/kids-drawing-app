package com.freddydev.kidsdrawing

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import yuku.ambilwarna.AmbilWarnaDialog
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener


/**
 * https://ssaurel.medium.com/learn-to-create-a-paint-application-for-android-5b16968063f8
 */
class MainActivity : AppCompatActivity() {

  private lateinit var drawingView: DrawingView
  private lateinit var brushBtn: ImageButton
  private lateinit var smallBtn: ImageButton
  private lateinit var mediumBtn: ImageButton
  private lateinit var llPaintColors: LinearLayout
  private lateinit var largeBtn: ImageButton
  private var mImageButtonCurrentPaint: ImageButton? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#005F73")))

    drawingView = findViewById(R.id.drawing_view)
    brushBtn = findViewById(R.id.brush)
    llPaintColors = findViewById(R.id.ll_paint_colors)
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
}