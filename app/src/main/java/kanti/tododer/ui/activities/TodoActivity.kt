package kanti.tododer.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kanti.tododer.databinding.ActivityTodoBinding
import kanti.tododer.ui.common.fabowner.IFloatingActionButtonOwner
import kanti.tododer.ui.common.toolbarowner.IToolbarOwner

@AndroidEntryPoint
class TodoActivity : AppCompatActivity(), IFloatingActionButtonOwner, IToolbarOwner {

	private lateinit var viewBinding: ActivityTodoBinding
	override val floatingActionButton: FloatingActionButton by lazy {
		viewBinding.floatingActionButtonAddTodo
	}
	override val toolbar: MaterialToolbar by lazy {
		viewBinding.materialToolbar
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewBinding = ActivityTodoBinding.inflate(layoutInflater)
		setContentView(viewBinding.root)
	}
}