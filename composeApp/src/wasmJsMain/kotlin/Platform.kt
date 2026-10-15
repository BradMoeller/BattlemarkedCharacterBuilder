package battlemarked

private fun jsHash(): String = js("window.location.hash")
private fun jsReplaceHash(hash: String) { js("window.history.replaceState(null, '', '#' + hash)") }
private fun jsHref(): String = js("window.location.href.split('#')[0]")
private fun jsCopy(text: String) { js("navigator.clipboard.writeText(text)") }

actual fun readLocationHash(): String = jsHash()
actual fun replaceLocationHash(hash: String) = jsReplaceHash(hash)
actual fun currentUrl(): String = jsHref() + "#" + jsHash().removePrefix("#")
actual fun copyToClipboard(text: String) = jsCopy(text)
