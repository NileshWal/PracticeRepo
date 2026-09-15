package com.nilesh.practiceapps.ui.composeviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.nilesh.practiceapps.R
import com.nilesh.practiceapps.network.ResponseClass

@Composable
fun UserDetailScreen(navController: NavController, userData: ResponseClass.UserData) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Gray)
    ) {

        val (cardIdTitleView, cardIdTextView, cardUserIdTitleView, cardUserIdTextView,
            cardTitleView, cardTitleTextView, cardBodyView, cardBodyTextView) = createRefs()

        CustomCardView(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(4.dp, 3.dp, 4.dp, 3.dp)
                .constrainAs(cardIdTitleView) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(cardIdTextView.start)
                }, text = stringResource(R.string.id)
        )

        CustomCardView(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(4.dp, 3.dp, 4.dp, 3.dp)
                .constrainAs(cardIdTextView) {
                    top.linkTo(parent.top)
                    start.linkTo(cardIdTitleView.end)
                    end.linkTo(parent.end)
                }, text = userData.id.toString()
        )

        CustomCardView(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(4.dp, 3.dp, 4.dp, 3.dp)
                .constrainAs(cardUserIdTitleView) {
                    top.linkTo(cardIdTitleView.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(cardUserIdTextView.start)
                }, text = stringResource(id = R.string.user_id)
        )

        CustomCardView(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(4.dp, 3.dp, 4.dp, 3.dp)
                .constrainAs(cardUserIdTextView) {
                    top.linkTo(cardIdTextView.bottom)
                    start.linkTo(cardUserIdTitleView.end)
                    end.linkTo(parent.end)
                }, text = userData.userId.toString()
        )

        CustomCardView(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp, 3.dp, 4.dp, 3.dp)
                .constrainAs(cardTitleView) {
                    top.linkTo(cardUserIdTitleView.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, text = stringResource(id = R.string.title)
        )

        CustomCardView(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp, 3.dp, 4.dp, 3.dp)
                .constrainAs(cardTitleTextView) {
                    top.linkTo(cardTitleView.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, text = userData.title.toString()
        )

        CustomCardView(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp, 3.dp, 4.dp, 3.dp)
                .constrainAs(cardBodyView) {
                    top.linkTo(cardTitleTextView.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, text = stringResource(R.string.body)
        )

        CustomCardView(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp, 3.dp, 4.dp, 3.dp)
                .constrainAs(cardBodyTextView) {
                    top.linkTo(cardBodyView.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, text = userData.body.toString()
        )

    }
}
