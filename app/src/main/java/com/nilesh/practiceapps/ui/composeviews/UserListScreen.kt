package com.nilesh.practiceapps.ui.composeviews

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nilesh.practiceapps.R
import com.nilesh.practiceapps.compose_route.Routes
import com.nilesh.practiceapps.network.ResponseClass
import com.nilesh.practiceapps.viewmodel.UserListViewModel


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    userListViewModel: UserListViewModel = hiltViewModel<UserListViewModel>()
) {

    val context = LocalContext.current

    val focusRequester = remember {
        FocusRequester()
    }

    var requestedPosition by remember {
        mutableStateOf("")
    }

    var uiMessage by remember {
        mutableStateOf("Enter element position")
    }

    val userList by userListViewModel.dataList.observeAsState(emptyList())
    LaunchedEffect(Unit) {
        userListViewModel.fetchUserList()
    }

    if (userList.isEmpty()) {
        TextView(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.loading_list),
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
    } else {
        ConstraintLayout(modifier = modifier.fillMaxSize()) {

            val (clickCounterView, buttonView, lazyColumnView) = createRefs()

            OutlinedTextField(
                modifier = Modifier
                    .height(70.dp)
                    .padding(0.dp, 5.dp, 0.dp, 5.dp)
                    .constrainAs(clickCounterView) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(buttonView.start)
                    }
                    .clickable { focusRequester.requestFocus() },
                value = requestedPosition,
                label = {
                    TextView(text = uiMessage, textAlign = TextAlign.Center)
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = {
                    requestedPosition = it
                }
            )

            Button(
                modifier = Modifier
                    .height(60.dp)
                    .padding(0.dp, 15.dp, 0.dp, 2.dp)
                    .constrainAs(buttonView) {
                        top.linkTo(parent.top)
                        start.linkTo(clickCounterView.end)
                        end.linkTo(parent.end)
                    }, onClick = {
                    if (userList.isEmpty()) {
                        uiMessage = context.getString(R.string.list_is_empty)
                    } else if (requestedPosition.isBlank()) {
                        uiMessage = context.getString(R.string.enter_a_position_value)
                    } else if (userList.size < requestedPosition.toInt()) {
                        uiMessage =
                            "The list has ${userList.size} items and you requested ${requestedPosition.toInt()}th item"
                    } else if (userList.size >= (requestedPosition.toInt() - 1)) {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            "userListItem",
                            userList[requestedPosition.toInt() - 1]
                        )
                        navController.navigate(Routes.USER_LIST_DETAIL_SCREEN)
                    } else {
                        uiMessage = context.getString(R.string.something_went_wrong)
                    }
                    requestedPosition = ""
                }) {
                TextView(text = stringResource(R.string.next_page))
            }

            LazyColumn(
                modifier = Modifier
                    .padding(3.dp, 0.dp, 3.dp, 5.dp)
                    .constrainAs(lazyColumnView) {
                        top.linkTo(clickCounterView.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
            ) {
                items(items = userList) { userListItem ->
                    Card(modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp, 3.dp, 4.dp, 3.dp), onClick = {
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            "userListItem",
                            userListItem
                        )
                        navController.navigate(Routes.USER_LIST_DETAIL_SCREEN)
                    }) {
                        UserItemData(userData = userListItem)
                    }
                }
            }
        }
    }

}

@Composable
fun UserItemData(userData: ResponseClass.UserData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp, 3.dp, 4.dp, 3.dp)
    ) {
        TextView(text = userData.id.toString(), textAlign = TextAlign.Start)
        TextView(text = userData.title.toString(), textAlign = TextAlign.Start)
        TextView(text = userData.body.toString(), textAlign = TextAlign.Start)
    }
}