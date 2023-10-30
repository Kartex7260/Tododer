package kanti.tododer.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kanti.tododer.databinding.ActivityTodoBinding
import kanti.tododer.ui.common.fabowner.IFloatingActionButtonOwner

@AndroidEntryPoint
class TodoActivity : AppCompatActivity(), IFloatingActionButtonOwner {

	private lateinit var viewBinding: ActivityTodoBinding
	override val floatingActionButton: FloatingActionButton by lazy {
		viewBinding.floatingActionButtonAddTodo
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewBinding = ActivityTodoBinding.inflate(layoutInflater)
		setContentView(viewBinding.root)
	}
}