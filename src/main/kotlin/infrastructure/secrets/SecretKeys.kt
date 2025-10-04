package top.dedicado.infrastructure.secrets

data class SecretKeys(
    val componentUri: String,
    val principalUri: String,
    val jwtSecret: String,
    val awsAccessKey: String,
    val awsSecretAccessKey: String,
)
