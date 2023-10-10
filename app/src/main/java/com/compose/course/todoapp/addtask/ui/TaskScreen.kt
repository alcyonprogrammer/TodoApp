package com.compose.course.todoapp.addtask.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.compose.course.todoapp.addtask.ui.model.TaskModel

@Composable
fun TaskScreen(taskViewModel:TaskViewModel) {

    val showDialog:Boolean by taskViewModel.showDialog.observeAsState(false)

    Box(modifier = Modifier.fillMaxSize()) {
        FabDialog(
            Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .clip(
                    CircleShape
                ), taskViewModel)
        AddTaskDialog(showDialog,
            onDismiss = {taskViewModel.onDialogClose()},
            onTaskAdded = {taskViewModel.onTaskCreated(it)})
        TaskList(taskViewModel)
    }
}

@Composable
fun ItemTask(taskModel:TaskModel, taskViewModel: TaskViewModel){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .pointerInput(Unit){
                          detectTapGestures(onLongPress = {
                              taskViewModel.onItemRemove(taskModel)
                          })  {

                          }
            },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White, contentColor=Color.Black),


    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = taskModel.task, modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
            )
            Checkbox(checked = taskModel.selected,
                onCheckedChange = {taskViewModel.onCheckBoxSelected(taskModel)})
        }
    }

}

@Composable
fun TaskList(taskViewModel: TaskViewModel)
{
    val myTasks : List<TaskModel> = taskViewModel.tasks
    LazyColumn{
        items(myTasks, key = {it.id}){task->
            ItemTask(taskModel = task, taskViewModel = taskViewModel)
        }
    }
}

@Composable
fun FabDialog(modifier: Modifier, taskViewModel: TaskViewModel) {
    FloatingActionButton(onClick = { taskViewModel.onShowDialogClick()}, modifier = modifier, containerColor = Color.Cyan
        ) {
        Icon(Icons.Filled.Add, contentDescription = "")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(show:Boolean, onDismiss:()->Unit, onTaskAdded:(String)->Unit){
    var myTask by remember {mutableStateOf("")}

    if (show){
        Dialog(onDismissRequest = {onDismiss()}) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)) {
                Text(text = "Añade tu tarea",
                    fontSize = 16.sp, modifier =
                    Modifier.align(Alignment.CenterHorizontally),
                    style = TextStyle(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(value = myTask, onValueChange = {myTask = it}, singleLine = true, maxLines = 1)
                Spacer(modifier = Modifier.size(16.dp))

                Button(onClick = {
                    onTaskAdded(myTask)
                    myTask = ""
                }) {
                    Text(text = "Añadir Tarea")
                }
            }
        }
    }
}