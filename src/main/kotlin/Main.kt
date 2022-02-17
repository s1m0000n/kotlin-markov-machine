class Rule(val pattern: String, val replacement: String, val final: Boolean = false) {
    fun match(expression: String) =
        if (expression.contains(pattern)) expression.replaceFirst(pattern, replacement)
        else null

    override fun toString(): String =
        when (final) {
            true -> """Rule: "$pattern" => "$replacement""""
            false -> """Rule: "$pattern" -> "$replacement""""
        }
}

fun parseRule(text: String, sep: String = "->", finalSep: String = "=>"): Rule? {
    if (text.isEmpty()) return null
    if (text[0] == ':') return null
    val del = if (text.contains(sep)) sep
    else if (text.contains(finalSep)) finalSep
    else return null
    val split = text.split(del)
    val pattern = split[0].trim()
    val replacement = split[1].trim()
    return Rule(pattern, replacement, del == finalSep)
}

fun showAllRules(rules: MutableList<Rule>) {
    for (i in 0..rules.lastIndex) println("$i. ${rules[i]}")
}

fun showHelp() {
    println("Commands:")
    println("  :help - show this help")
    println("  :quit - quit the interpreter")
    println("  :show - show all rules")
    println("  :drop - drop all rules")
    println("  :clear - clear terminal screen")
    println("  :show i1 i2 i3 ... - print rules at indices i1, i2, i3, ...")
    println("  :drop i1 i2 i3 ... - drop rules i1 i2 i3 ...")
    println("  :swap n m - swap the order of n and m positioned rules")
//    println("  :test e r - test if expression e results to r")
    println("  :insert n r - replace n-th rule with a new rule r")
    println("  :verbose e - apply rules to e with steps printed")
    println("  :timeit e - apply rules to e and print how much it took in ms")
//    println("  :loadr f - load rules from file f (regular syntax of rules)")
//    println("  :loadt f - load tests from file f (pairs <expr> => <result> each on new line)")
}

fun pwv(obj: Any, verbose: Boolean = true): Boolean {
    if (verbose) {
        print(obj)
        print(" ")
        val input = readln()
        if (input.lowercase().trim() in setOf(":stop", ":s", ":q", ":quit", ":a", ":abort")) return true
    }
    return false
}

fun run(rules: MutableList<Rule>, expression: String, verbose: Boolean = false): String {
    if (verbose) println("[verbose mode] Press Enter to exec the next step, :stop to leave")
    pwv(expression, verbose)
    var acc = expression
    var final = false
    var replaced = true
    while (!final && replaced) {
        replaced = false
        for (i in 0..rules.lastIndex) {
            val repl = rules[i].match(acc)
            if (repl != null) {
                acc = repl
                final = rules[i].final
                if (pwv("[$i. ${rules[i]}] $acc", verbose)) return "[Stopped]"
                replaced = true
                break
            }
        }
    }
    pwv("No more rules applicable => Stop", verbose && !replaced)
    return acc
}

fun parseRunExpression(text: String, rules:MutableList<Rule>): MutableList<Rule>? {
    if (text.startsWith(":")){
        val split = text.drop(1).split(" ").map { it.trim() }
        val args = Pair(split[0], split.drop(1))
        val borders = 0..rules.lastIndex
        if (args.second.isEmpty()) when(args.first) {
            "q", "quit" -> return null
            "s", "show" -> showAllRules(rules)
            "d", "drop" -> return mutableListOf()
            "c", "clear" -> println("\n".repeat(100))
            "h", "help" -> showHelp()
            else -> println("Unknown command, use :help")
        } else when(args.first) {
            "s", "show" -> for (i in args.second.map { it.toInt() }.filter { it in borders }) println("$i. ${rules[i]}")
            "d", "drop" -> {
                var acc = rules
                for (i in args.second.map { it.toInt() }.filter { it in borders }) acc.removeAt(i)
                return acc
            }
            "swap" -> {
                val indices = args.second.map { it.toInt() }
                val i = indices[0]
                val j = indices[1]
                if (!(i in borders) || !(j in borders))
                    println("Indices for swap must be in [0, ${rules.lastIndex}]")
                else {
                    var acc = rules
                    val tmp = acc[i]
                    acc[i] = acc[j]
                    acc[j] = tmp
                    return acc
                }
            }
            "i", "insert" -> {
                val i = args.second[0].toInt()
                val rule = parseRule(args.second.drop(1).joinToString(" "))
                if (!(i in borders)) println("Index for insertion must be in $borders")
                else if (rule != null) {
                    val rulesWOIndex = rules.toMutableList()
                    rulesWOIndex.removeAt(i)
                    if (rule.pattern in rulesWOIndex.map { r -> r.pattern })
                        println("""A rule with pattern "${rule.pattern}" already exists, not in target position""")
                    else rules[i] = rule
                } else println("Incorrect rule fmt")
            }
            "v", "verbose", "steps", "debug" -> println(run(rules, args.second[0], verbose = true))
        }
    } else println(run(rules, text, verbose = false))
    return rules
}

fun runREPL() {
    var rules = mutableListOf<Rule>()
    while (true) {
        print(">> ")
        val input = readln()
        val rule = parseRule(input)
        if (rule != null) {
            if (rule.pattern in rules.map { r -> r.pattern })
                println("""A rule with pattern "${rule.pattern}" already exists""")
            else rules.add(rule)
        } else {
            val result = parseRunExpression(input, rules)
            if (result == null) return
            else rules = result
        }
    }
}

fun main(args: Array<String>) {
    println("Welcome to NAM interperter")
    println("- Expressions like pattern -> result are interpreted as non terminating rules")
    println("- Expressions like pattern => result are interpreted as terminating rules")
    println("- There are special commands starting with : like :quit (quit the interpreter), :help (show help) etc")
    println("- Otherwise it's interpreted as an expression to run the program on")
    runREPL()
    println("Quitting...")
}