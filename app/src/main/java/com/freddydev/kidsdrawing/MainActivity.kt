package com.freddydev.kidsdrawing

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


/**
 * https://ssaurel.medium.com/learn-to-create-a-paint-application-for-android-5b16968063f8
 */
class MainActivity : AppCompatActivity() {

  private lateinit var drawingView: DrawingView
  private lateinit var brushBtn: ImageButton
  private lateinit var smallBtn: ImageButton
  private lateinit var mediumBtn: ImageButton
  private lateinit var largeBtn: ImageButton

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#005F73")))

    drawingView = findViewById(R.id.drawing_view)
    brushBtn = findViewById(R.id.brush)

    drawingView.setSizeForBrush(20.toFloat())
    brushBtn.setOnClickListener {
      showBrushDialog()
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
}