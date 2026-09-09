package co.solventa.mobile.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import co.solventa.mobile.BuildConfig
import co.solventa.mobile.data.FakeInsuranceRepository
import co.solventa.mobile.domain.LoadState
import co.solventa.mobile.feature.auth.*
import co.solventa.mobile.feature.claims.*
import co.solventa.mobile.feature.debug.ScenarioScreen
import co.solventa.mobile.feature.main.*
import co.solventa.mobile.feature.quote.*
import co.solventa.mobile.feature.settings.*

private object Route {
    const val WELCOME = "welcome"; const val LOGIN = "login"; const val RECOVERY = "recovery"; const val REGISTER = "register"; const val TERMS = "terms"; const val KYC = "kyc"
    const val HOME = "home"; const val POLICIES = "policies"; const val CLAIMS = "claims"; const val NOTIFICATIONS = "notifications"; const val PROFILE = "profile"
    const val QUOTE_PRODUCT = "quote/product"; const val QUOTE_DETAILS = "quote/details"; const val QUOTE_PLANS = "quote/plans"; const val QUOTE_CONSENT = "quote/consent"; const val PAYMENT = "quote/payment"; const val OTP = "quote/otp"; const val ISSUED = "quote/issued"
    const val POLICY = "policy/{id}"; const val CLAIM_DETAIL = "claim/{id}"; const val CLAIM_POLICY = "new-claim/policy"; const val CLAIM_EVENT = "new-claim/event"; const val CLAIM_EVIDENCE = "new-claim/evidence"; const val CLAIM_LOCATION = "new-claim/location"; const val CLAIM_REVIEW = "new-claim/review"; const val CLAIM_RESULT = "new-claim/result"
    const val SCENARIOS = "debug/scenarios"
}

@Composable
fun SolventaApp(settingsViewModel: SettingsViewModel) {
    val nav = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val mainViewModel: MainViewModel = hiltViewModel()
    val quoteViewModel: QuoteViewModel = hiltViewModel()
    val claimViewModel: ClaimViewModel = hiltViewModel()
    val mainState by mainViewModel.state.collectAsStateWithLifecycle()

    NavHost(navController = nav, startDestination = Route.WELCOME) {
        composable(Route.WELCOME) { WelcomeScreen({ nav.navigate(Route.LOGIN) }, { nav.navigate(Route.REGISTER) }) }
        composable(Route.LOGIN) { LoginScreen(authViewModel, { enterMain(nav) }, { nav.navigate(Route.RECOVERY) }, { nav.navigate(Route.REGISTER) }, { if (BuildConfig.DEBUG) nav.navigate(Route.SCENARIOS) }) }
        composable(Route.RECOVERY) { RecoveryScreen(nav::popBackStack) }
        composable(Route.REGISTER) { RegistrationScreen(nav::popBackStack) { nav.navigate(Route.TERMS) } }
        composable(Route.TERMS) { TermsScreen(nav::popBackStack) { nav.navigate(Route.KYC) } }
        composable(Route.KYC) { KycScreen(nav::popBackStack) { enterMain(nav) } }

        composable(Route.HOME) { MainScaffold(Route.HOME, { navigateBottom(nav, it) }) { padding -> Box(androidx.compose.ui.Modifier.padding(padding)) { HomeScreen(mainState, { quoteViewModel.reset(); nav.navigate(Route.QUOTE_PRODUCT) }, { claimViewModel.reset(); nav.navigate(Route.CLAIM_POLICY) }, { nav.navigate("policy/$it") }, mainViewModel::refresh) } } }
        composable(Route.POLICIES) { MainScaffold(Route.POLICIES, { navigateBottom(nav, it) }) { padding -> Box(androidx.compose.ui.Modifier.padding(padding)) { PoliciesScreen(mainState.policies, { nav.navigate("policy/$it") }, mainViewModel::refresh) } } }
        composable(Route.CLAIMS) { MainScaffold(Route.CLAIMS, { navigateBottom(nav, it) }) { padding -> Box(androidx.compose.ui.Modifier.padding(padding)) { ClaimsScreen(mainState.claims, { claimViewModel.reset(); nav.navigate(Route.CLAIM_POLICY) }, { nav.navigate("claim/$it") }, mainViewModel::refresh) } } }
        composable(Route.NOTIFICATIONS) { MainScaffold(Route.NOTIFICATIONS, { navigateBottom(nav, it) }) { padding -> Box(androidx.compose.ui.Modifier.padding(padding)) { NotificationsScreen(mainState.notifications, mainViewModel::markNotificationRead, mainViewModel::refresh) } } }
        composable(Route.PROFILE) { MainScaffold(Route.PROFILE, { navigateBottom(nav, it) }) { padding -> Box(androidx.compose.ui.Modifier.padding(padding)) { ProfileScreen(settingsViewModel, { logout(nav) }, { if (BuildConfig.DEBUG) nav.navigate(Route.SCENARIOS) }) } } }

        composable(Route.QUOTE_PRODUCT) { ProductScreen(quoteViewModel, nav::popBackStack) { nav.navigate(Route.QUOTE_DETAILS) } }
        composable(Route.QUOTE_DETAILS) { TravelDetailsScreen(quoteViewModel, nav::popBackStack) { nav.navigate(Route.QUOTE_PLANS) } }
        composable(Route.QUOTE_PLANS) { PlansScreen(quoteViewModel, nav::popBackStack) { nav.navigate(Route.QUOTE_CONSENT) } }
        composable(Route.QUOTE_CONSENT) { QuoteConsentScreen(quoteViewModel, nav::popBackStack) { nav.navigate(Route.PAYMENT) } }
        composable(Route.PAYMENT) { PaymentScreen(quoteViewModel, nav::popBackStack) { nav.navigate(Route.OTP) } }
        composable(Route.OTP) { OtpScreen(quoteViewModel, nav::popBackStack) { nav.navigate(Route.ISSUED) } }
        composable(Route.ISSUED) { IssuedScreen { nav.navigate("policy/SOL-TRV-2026-1842") } }

        composable(Route.POLICY) { entry ->
            val id = entry.arguments?.getString("id").orEmpty()
            val policy = (mainState.policies as? LoadState.Content)?.data?.firstOrNull { it.id == id } ?: FakeInsuranceRepository.samplePolicies.first()
            PolicyDetailScreen(policy, nav::popBackStack) { claimViewModel.reset(); nav.navigate(Route.CLAIM_POLICY) }
        }
        composable(Route.CLAIM_DETAIL) { entry ->
            val id = entry.arguments?.getString("id").orEmpty()
            val claim = (mainState.claims as? LoadState.Content)?.data?.firstOrNull { it.id == id } ?: FakeInsuranceRepository.sampleClaims.first()
            ClaimDetailScreen(claim, nav::popBackStack)
        }
        composable(Route.CLAIM_POLICY) { ClaimPolicyScreen(nav::popBackStack) { nav.navigate(Route.CLAIM_EVENT) } }
        composable(Route.CLAIM_EVENT) { ClaimEventScreen(claimViewModel, nav::popBackStack) { nav.navigate(Route.CLAIM_EVIDENCE) } }
        composable(Route.CLAIM_EVIDENCE) { EvidenceScreen(claimViewModel, nav::popBackStack) { nav.navigate(Route.CLAIM_LOCATION) } }
        composable(Route.CLAIM_LOCATION) { LocationScreen(claimViewModel, nav::popBackStack) { nav.navigate(Route.CLAIM_REVIEW) } }
        composable(Route.CLAIM_REVIEW) { ReviewClaimScreen(claimViewModel, nav::popBackStack) { nav.navigate(Route.CLAIM_RESULT) } }
        composable(Route.CLAIM_RESULT) { ClaimResultScreen(claimViewModel) { mainViewModel.refresh(); nav.navigate(Route.CLAIMS) { popUpTo(Route.HOME); launchSingleTop = true } } }

        if (BuildConfig.DEBUG) composable(Route.SCENARIOS) { ScenarioScreen(nav::popBackStack) }
    }
}

private fun enterMain(nav: NavHostController) = nav.navigate(Route.HOME) { popUpTo(Route.WELCOME) { inclusive = true } }
private fun logout(nav: NavHostController) = nav.navigate(Route.WELCOME) { popUpTo(nav.graph.id) { inclusive = true } }
private fun navigateBottom(nav: NavHostController, route: String) = nav.navigate(route) { popUpTo(Route.HOME) { saveState = true }; launchSingleTop = true; restoreState = true }
