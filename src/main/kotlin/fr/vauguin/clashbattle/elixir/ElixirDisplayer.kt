package fr.vauguin.clashbattle.elixir

interface ElixirDisplay {
    fun update(elixirs: Int)
}

class ConsoleElixirDisplay(private val maximumElixirs: Int) :
    ElixirDisplay {

    override fun update(elixirs: Int) {
        val bar = StringBuilder()
        for (i in 0..maximumElixirs) {
            val char = if (i <= elixirs) "=" else " "
            bar.append(char)
        }
        print("🧬 Elixirs : ${elixirs.coerceAtLeast(0).coerceAtMost(maximumElixirs)}/$maximumElixirs [$bar]\r")
    }
}