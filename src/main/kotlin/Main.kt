import generated.model.Test

fun main() {
    // This is the Kotlin type for the `structure Test` in `model.smithy`
    val test = Test.Builder().build()
    println(test)
}