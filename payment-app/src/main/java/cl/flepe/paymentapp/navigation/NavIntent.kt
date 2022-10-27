package cl.flepe.paymentapp.navigation

import android.content.Context
import android.content.Intent

object NavIntent {

    fun getIntentByPath(context: Context, navDestination: NavDestination) = makeIntent(
            context, navDestination.path
    )

    private fun makeIntent(context: Context, pathToActivity: String): Intent = Intent().setClassName(
            context.packageName, pathToActivity
    )

}