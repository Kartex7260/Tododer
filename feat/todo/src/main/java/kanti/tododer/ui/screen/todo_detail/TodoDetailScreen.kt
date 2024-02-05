package kanti.tododer.ui.screen.todo_detail

import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kanti.tododer.feat.todo.R
import kanti.tododer.ui.components.dialogs.DeleteTodoDialog
import kanti.tododer.ui.components.dialogs.RenameDialog
import kanti.tododer.ui.components.menu.NormalTodoDropdownMenu
import kanti.tododer.ui.components.todo.TodoCard
import kanti.tododer.ui.components.todo.TodoData
import kanti.tododer.ui.components.todo.TodoEditor
import kanti.tododer.ui.components.todo.TodoEditorControllers
import kanti.tododer.ui.components.todo.TodoEditorDefaults
import kanti.tododer.ui.screen.todo_detail.viewmodel.TodoDetailViewModel
import kanti.tododer.ui.screen.todo_detail.viewmodel.TodoDetailViewModelImpl
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDetailTopBar(
    back: () -> Unit,
    title: String,
    scrollBehavior: TopAppBarScrollBehavior,
    editorSize: IntSize,
    state: TodoData,
    onDoneChange: (isDone: Boolean) -> Unit,
//	onArchive: () -> Unit,
    onDelete: () -> Unit
) {
    TopAppBar(
        title = {
            val (offsetYFraction, alphaFraction) = if (scrollBehavior.state.overlappedFraction > 0.01f)
                Pair(0f, 1f) else Pair(1f, 0f)
            val offsetYAnim by animateFloatAsState(
                targetValue = offsetYFraction * 64,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                label = ""
            )
            val alphaAnim by animateFloatAsState(
                targetValue = alphaFraction,
                animationSpec = spring(stiffness = Spring.StiffnessMedium),
                label = ""
            )
            Text(
                modifier = Modifier
                    .offset(y = offsetYAnim.dp)
                    .alpha(alpha = alphaAnim),
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = { back() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            val (offsetYFraction, alphaFraction) = with(LocalDensity.current) {
                if (scrollBehavior.state.contentOffset <= -(editorSize.height - 14.dp.toPx()))
                    Pair(0f, 1f) else Pair(1f, 0f)
            }
            val offsetYAnim by animateFloatAsState(
                targetValue = offsetYFraction * 64,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                label = ""
            )
            val alphaAnim by animateFloatAsState(
                targetValue = alphaFraction,
                animationSpec = spring(stiffness = Spring.StiffnessMedium),
                label = ""
            )
            TodoEditorControllers(
                modifier = Modifier
                    .offset(y = offsetYAnim.dp)
                    .alpha(alpha = alphaAnim),
                state = state,
                onDoneChanged = onDoneChange,
//				onArchive = onArchive,
                onDelete = onDelete
            )
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDetailScreen(
    navController: NavController = rememberNavController(),
    vm: TodoDetailViewModel = hiltViewModel<TodoDetailViewModelImpl>(),
    todoId: Long = 0
) {
    val logTag = "TodoDetailScreen"
    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        Log.d(logTag, "onCreate($todoId): vm.show()")
        vm.show(todoId)
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_RESUME) {
        Log.d(logTag, "onResume($todoId): vm.reshow()")
        vm.reshow()
    }

    LifecycleStartEffect(key1 = vm) {
        onStopOrDispose {
            Log.d(logTag, "onStop($todoId): vm.stop()")
            vm.onStop()
        }
    }

    LaunchedEffect(key1 = vm) {
        vm.onExit.collectLatest {
            navController.popBackStack()
        }
    }

    val todoChildrenRoute = stringResource(id = R.string.nav_destination_todo_detail)
    fun todoDetailRoute(todoId: Long): String {
        return "$todoChildrenRoute/$todoId"
    }
    LaunchedEffect(key1 = vm) {
        vm.toNext.collectLatest { todoId ->
            navController.navigate(
                route = todoDetailRoute(todoId)
            )
        }
    }

    val snackBarHost = remember { SnackbarHostState() }
    val regexName = stringResource(id = R.string.regex_name)
    val regexCount = stringResource(id = R.string.regex_count)
    val todoDeleted = stringResource(id = R.string.todo_deleted)
    val todosDeleted = stringResource(id = R.string.todos_deleted)
    val cancelStringRes = stringResource(id = R.string.cancel)
    LaunchedEffect(key1 = vm) {
        vm.childrenTodosDeleted.collectLatest { todos ->
            if (todos.isEmpty())
                return@collectLatest

            val message = if (todos.size == 1) {
                todoDeleted.replace(regexName, todos[0].title)
            } else {
                todosDeleted.replace(regexCount, todos.size.toString())
            }

            val result = snackBarHost.showSnackbar(
                message = message,
                withDismissAction = true,
                actionLabel = cancelStringRes,
                duration = SnackbarDuration.Short
            )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    vm.cancelDeleteChildren()
                }

                else -> {
                    vm.rejectCancelDelete()
                }
            }
        }
    }

    val blankTodoDeleted = stringResource(id = R.string.deleted_blank_todo)
    LaunchedEffect(key1 = vm) {
        vm.blankTodoDeleted.collectLatest {
            snackBarHost.showSnackbar(
                message = blankTodoDeleted,
                withDismissAction = true
            )
        }
    }

    LaunchedEffect(key1 = vm) {
        vm.currentTodoDeleted.collectLatest {
            val result = snackBarHost.showSnackbar(
                message = todoDeleted,
                withDismissAction = true,
                actionLabel = cancelStringRes,
                duration = SnackbarDuration.Short
            )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    vm.cancelDeleteCurrent()
                }

                else -> {
                    vm.rejectCancelDelete()
                }
            }
        }
    }

    val todoDetail by vm.todoDetail.collectAsState()
    val todoChildren by vm.todoChildren.collectAsState()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var editorSize by remember {
        mutableStateOf(IntSize(0, 0))
    }

    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    var showRenameDialog: TodoData? by rememberSaveable { mutableStateOf(null) }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            TodoDetailTopBar(
                back = { navController.popBackStack() },
                title = todoDetail.title,
                scrollBehavior = scrollBehavior,
                editorSize = editorSize,
                state = todoDetail,
                onDoneChange = {
                    vm.changeDoneCurrent(it)
                },
//				onArchive = {},
                onDelete = {
                    showDeleteDialog = true
                }
            )
        },

        snackbarHost = {
            SnackbarHost(hostState = snackBarHost)
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = { vm.createNewTodo() },
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            item {
                TodoEditor(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onSizeChanged { size ->
                            editorSize = size
                        },
                    initialState = todoDetail,
                    strings = TodoEditorDefaults.strings(
                        title = stringResource(id = R.string.title),
                        remark = stringResource(id = R.string.remark)
                    ),
                    onTitleChanged = { title ->
                        vm.changeTitle(title)
                    },
                    onRemarkChanged = { remark ->
                        vm.changeRemark(remark)
                    },
                    onDoneChanged = { isDone ->
                        vm.changeDoneCurrent(isDone)
                    },
//					onArchive = {},
                    onDelete = {
                        showDeleteDialog = true
                    }
                )

                Divider(
                    modifier = Modifier
                        .padding(
                            top = 8.dp,
                            bottom = 8.dp,
                            start = 16.dp,
                            end = 16.dp
                        )
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )
            }

            items(
                items = todoChildren.todos,
                key = { it.id }
            ) { todoData ->
                TodoCard(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 12.dp
                        ),
                    todoData = todoData,
                    onDoneChange = { isDone ->
                        vm.changeDoneChild(todoData.id, isDone)
                    },
                    onClick = {
                        navController.navigate(
                            route = todoDetailRoute(todoData.id)
                        )
                    }
                ) {
                    var expanded by remember {
                        mutableStateOf(false)
                    }
                    IconButton(
                        onClick = { expanded = !expanded }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null
                        )
                    }
                    NormalTodoDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        onRename = {
                            showRenameDialog = todoData
                        },
                        onDelete = { vm.deleteChildren(listOf(todoData)) }
                    )
                }
            }
        }
    }

    if (showDeleteDialog) {
        DeleteTodoDialog(
            onCloseDialog = { showDeleteDialog = false },
            todoTitle = todoDetail.title,
            delete = { vm.deleteCurrent() }
        )
    }

    if (showRenameDialog != null) {
        val renamedTodo = showRenameDialog!!
        RenameDialog(
            onCloseDialog = { showRenameDialog = null },
            label = { Text(text = stringResource(id = R.string.new_title)) },
            name = renamedTodo.title,
            onRename = { newTitle ->
                vm.renameTodo(renamedTodo.id, newTitle)
            }
        )
    }
}

@Preview
@Composable
private fun PreviewTodoDetailScreen() {
    TodoDetailScreen(
        vm = TodoDetailViewModel
    )
}