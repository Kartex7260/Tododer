package kanti.tododer.ui.screen.todo_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kanti.tododer.data.model.plan.Plan
import kanti.tododer.data.model.plan.PlanType
import kanti.tododer.feat.todo.R
import kanti.tododer.ui.UiConst
import kanti.tododer.ui.common.MultiSelectionStyle
import kanti.tododer.ui.components.ContentSwitcher
import kanti.tododer.ui.components.ScreenBottomCaption
import kanti.tododer.ui.components.TodoFloatingActionButton
import kanti.tododer.ui.components.TodoGroupPanel
import kanti.tododer.ui.components.dialogs.CreateDialog
import kanti.tododer.ui.components.dialogs.DeleteDialog
import kanti.tododer.ui.components.dialogs.RenameDialog
import kanti.tododer.ui.components.todo.TodoData
import kanti.tododer.ui.screen.todo_list.viewmodel.TodoListViewModel
import kanti.tododer.ui.screen.todo_list.viewmodel.TodoListViewModelImpl
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TodoListTopBar(
    plan: Plan?,
    multiSelection: Boolean,
    navIconClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    optionMenuItems: (@Composable (closeMenu: () -> Unit) -> Unit)?,
    isEditablePlan: Boolean,
    menuOnMultiSelection: () -> Unit,
    menuOnRename: () -> Unit,
    menuOnDelete: () -> Unit
) {
    val title = when (plan?.type) {
        PlanType.All -> stringResource(id = R.string.plan_all)
        PlanType.Default -> stringResource(id = R.string.plan_default)
        PlanType.Custom -> plan.title
        else -> ""
    }

    CenterAlignedTopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(onClick = navIconClick) {
                ContentSwitcher(
                    state = multiSelection,
                    trueContent = {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null
                        )
                    },
                    falseContent = {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = null
                        )
                    }
                )
            }
        },
        actions = {
            if (optionMenuItems != null) {
                var expandOptionMenu by rememberSaveable { mutableStateOf(false) }
                fun closeMenu() {
                    expandOptionMenu = false
                }
                IconButton(onClick = { expandOptionMenu = !expandOptionMenu }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                    )
                }
                DropdownMenu(
                    offset = DpOffset(12.dp, 0.dp),
                    expanded = expandOptionMenu,
                    onDismissRequest = { closeMenu() }
                ) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(id = R.string.multi_selection)) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.multi_select),
                                contentDescription = null
                            )
                        },
                        onClick = {
                            menuOnMultiSelection()
                            closeMenu()
                        }
                    )
                    if (isEditablePlan) {
                        DropdownMenuItem(
                            text = { Text(text = stringResource(id = R.string.rename_plan)) },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Create, contentDescription = null)
                            },
                            onClick = {
                                menuOnRename()
                                closeMenu()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(text = stringResource(id = R.string.delete_plan)) },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                            },
                            onClick = {
                                menuOnDelete()
                                closeMenu()
                            }
                        )
                    }

                    optionMenuItems {
                        closeMenu()
                    }
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
	navController: NavController,
	selectionStyle: Set<MultiSelectionStyle> = setOf(MultiSelectionStyle.ColorFill),
	optionMenuItems: (@Composable (closeMenu: () -> Unit) -> Unit)? = null,
	vm: TodoListViewModel = hiltViewModel<TodoListViewModelImpl>()
) {
    val todoChildrenRoute = stringResource(id = R.string.nav_destination_todo_detail)
    fun todoDetailRoute(todoId: Long): String {
        return "$todoChildrenRoute/$todoId"
    }

    val planListRoute = stringResource(id = R.string.nav_destination_plans)
    val planListRouteIdParam = stringResource(id = R.string.nav_destination_plans_id_param)
    fun planListRoute(deleted: Long? = null): String {
        return "$planListRoute${if (deleted == null) "" else "?$planListRouteIdParam=$deleted"}"
    }

    var showDeletePlanDialog: Boolean by rememberSaveable { mutableStateOf(false) }
    var showRenamePlanDialog: Plan? by rememberSaveable { mutableStateOf(null) }
    var showRenameTodoDialog: TodoData? by rememberSaveable { mutableStateOf(null) }
    var showCreateDialog: Boolean by rememberSaveable { mutableStateOf(false) }

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val todoListUiState by vm.currentPlan.collectAsState()
    val plan = todoListUiState.plan
    val children = todoListUiState.children

    val context = LocalContext.current
    LaunchedEffect(key1 = vm) {
        vm.todosDeleted.collectLatest { deletedTodos ->
            if (deletedTodos.isEmpty())
                return@collectLatest

            val size = deletedTodos.size
            val isSoloTodo = size == 1
            val message = if (isSoloTodo) {
                val regexName = context.getString(R.string.regex_name)
                val todoDeleted = context.getString(R.string.todo_deleted)
                todoDeleted.replace(regexName, deletedTodos[0].todoData.title)
            } else {
                context.resources.getQuantityString(R.plurals.todos_deleted, size, size)
            }

            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = context.getString(R.string.cancel),
                duration = SnackbarDuration.Short
            )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    vm.cancelDelete()
                    if (isSoloTodo && deletedTodos[0].returnToChild) {
                        navController.navigate(
                            route = todoDetailRoute(deletedTodos[0].todoData.id)
                        )
                    }
                }

                SnackbarResult.Dismissed -> vm.rejectCancelChance()
            }
        }
    }

    val blankTodoDeleted = stringResource(id = R.string.deleted_blank_todo)
    LaunchedEffect(key1 = vm) {
        vm.blankTodoDeleted.collectLatest {
            snackbarHostState.showSnackbar(
                message = blankTodoDeleted
            )
        }
    }

    LifecycleStartEffect(key1 = vm) {
        onStopOrDispose {
            vm.onStop()
        }
    }

    LifecycleResumeEffect(key1 = vm) {
        val deletedTodoId = navController.currentBackStackEntry?.savedStateHandle
            ?.get<Long>(UiConst.BackStackKeys.DELETED)
        vm.updateUiState(deletedTodoId)
        navController.currentBackStackEntry?.savedStateHandle?.set(
            UiConst.BackStackKeys.DELETED,
            null
        )
        onPauseOrDispose { }
    }

    LaunchedEffect(key1 = vm) {
        vm.goToTodo.collect { todoId ->
            navController.navigate(
                route = todoDetailRoute(todoId)
            )
        }
    }

    @Composable
    fun <T : Any> rememberSaveableByPlan(
        saver: Saver<T, out Any> = autoSaver(),
        init: () -> T
    ): T {
        return rememberSaveable(
            inputs = arrayOf(plan),
            saver = saver,
            key = plan.id.toString(),
            init = init
        )
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        state = rememberSaveableByPlan(saver = TopAppBarState.Saver) {
            TopAppBarState(
                initialHeightOffsetLimit = -Float.MAX_VALUE,
                initialHeightOffset = 0f,
                initialContentOffset = 0f
            )
        }
    )
    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            TodoListTopBar(
                plan = plan,
                multiSelection = children.selection,
                scrollBehavior = scrollBehavior,
                navIconClick = {
                    if (children.selection)
                        vm.selectionOff()
                    else
                        navController.navigate(route = planListRoute())
                },
                optionMenuItems = optionMenuItems,
                isEditablePlan = todoListUiState.isEditablePlan,
                menuOnMultiSelection = { vm.switchSelection() },
                menuOnRename = { showRenamePlanDialog = plan },
                menuOnDelete = { showDeletePlanDialog = true }
            )
        },

        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                val dismissState = rememberDismissState(
                    confirmValueChange = {
                        snackbarData.dismiss()
                        true
                    }
                )
                SwipeToDismiss(
                    state = dismissState,
                    background = {},
                    dismissContent = { Snackbar(snackbarData = snackbarData) }
                )
            }
        },

        floatingActionButton = {
            TodoFloatingActionButton(
                selection = children.selection,
                onClick = { showCreateDialog = true },
                onCheck = { vm.changeDoneSelected() },
                onDelete = { vm.deleteSelected() }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            state = rememberSaveableByPlan(saver = LazyListState.Saver) {
                LazyListState(0, 0)
            },
            contentPadding = PaddingValues(
                top = 12.dp,
                bottom = 16.dp
            )
        ) {
            items(
                count = children.groups.size,
                key = { index -> children.groups[index].name ?: "" }
            ) { index ->
                TodoGroupPanel(
                    modifier = Modifier
                        .padding(
                            top = 0.dp,
                            bottom = if (children.groups.size - 1 == index) 0.dp else 16.dp,
                            start = 16.dp,
                            end = 16.dp,
                        ),
                    selection = children.selection,
                    isSingleGroup = children.groups.size == 1,
                    group = children.groups[index],
                    selectionStyle = selectionStyle,
                    itemOnLongClick = { todoData -> vm.selection(todoData.id) },
                    itemOnClick = { todoData ->
                        navController.navigate(
                            route = todoDetailRoute(todoData.id)
                        )
                    },
                    itemOnDoneChange = { todoData, isDone -> vm.changeDone(todoData.id, isDone) },
                    itemOnChangeSelect = { todoData, selected ->
                        vm.setSelect(todoData.id, selected)
                    },
                    itemMenuOnRename = { todoData -> showRenameTodoDialog = todoData },
                    itemMenuOnDelete = { todoData -> vm.deleteTodos(listOf(todoData)) }
                )
            }

            item {
                Box(
                    modifier = Modifier
                        .height(56.dp)
                        .fillMaxWidth()
                ) {
                    val captionStg = stringResource(id = R.string.caption_todo_list)
                    val allTodos = children.groups.asSequence()
                        .flatMap { it.todos }
                    ScreenBottomCaption(
                        modifier = Modifier
                            .align(Alignment.BottomCenter),
                        text = captionStg
                            .replace("{1}", plan.title)
                            .replace("{2}", allTodos.filter { it.visible }.count().toString()
                            )
                            .replace(
                                "{3}",
                                allTodos.filter { it.data.isDone && it.visible }.count().toString()
                            )
                    )
                }
            }
        }
    }

    if (showDeletePlanDialog) {
        DeleteDialog(
            onCloseDialog = { showDeletePlanDialog = false },
            title = { Text(text = stringResource(id = R.string.delete_plan_ask)) },
            delete = {
                navController.navigate(route = planListRoute(deleted = plan.id))
            }
        )
    }

    if (showRenamePlanDialog != null) {
        val curPlan = showRenamePlanDialog!!
        RenameDialog(
            onCloseDialog = { showRenamePlanDialog = null },
            label = { Text(text = stringResource(id = R.string.new_title)) },
            name = curPlan.title,
            onRename = { title ->
                vm.renamePlan(title)
            }
        )
    }

    if (showRenameTodoDialog != null) {
        val renamedTodo = showRenameTodoDialog!!
        RenameDialog(
            onCloseDialog = {
                showRenameTodoDialog = null
            },
            label = { Text(text = stringResource(id = R.string.new_title)) },
            name = renamedTodo.title,
            onRename = { newTitle ->
                vm.renameTodo(renamedTodo.id, newTitle)
            }
        )
    }

    if (showCreateDialog) {
        CreateDialog(
            onCloseDialog = { showCreateDialog = false },
            title = { Text(text = stringResource(id = R.string.create_new_todo)) },
            textFieldLabel = { Text(text = stringResource(id = R.string.todo_name)) },
            add = { title ->
                vm.createNewTodo(title = title, goTo = false)
            },
            create = { title ->
                vm.createNewTodo(title = title, goTo = false)
            }
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
fun PreviewTodoListScreen() {
    TodoListScreen(
        navController = rememberNavController(),
        vm = TodoListViewModel,
        optionMenuItems = { closeMenu ->
            DropdownMenuItem(
                text = { Text(text = "Test menu item") },
                onClick = { closeMenu() }
            )
        }
    )
}