package webclient

import core.context.WebClient

expect fun defaultWebClient(
    host: String,
    basePath: String
): WebClient