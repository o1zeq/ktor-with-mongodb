package top.dedicado.infrastructure.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.requestvalidation.RequestValidation
import top.dedicado.presentation.validators.AuthValidator
import top.dedicado.presentation.validators.RegisterValidator
import top.dedicado.presentation.validators.component.ActivityValidator
import top.dedicado.presentation.validators.principal.AccountValidator
import top.dedicado.presentation.validators.principal.ContactValidator
import top.dedicado.presentation.validators.principal.MembershipValidator
import top.dedicado.presentation.validators.principal.ProjectValidator
import top.dedicado.presentation.validators.principal.SessionValidator
import top.dedicado.presentation.validators.principal.SubscriptionValidator

fun Application.configureValidations() {
    install(RequestValidation) {
        ActivityValidator().configure(this)

        AccountValidator().configure(this)
        ContactValidator().configure(this)
        MembershipValidator().configure(this)
        ProjectValidator().configure(this)
        SessionValidator().configure(this)
        SubscriptionValidator().configure(this)

        AuthValidator().configure(this)
        RegisterValidator().configure(this)
    }
}