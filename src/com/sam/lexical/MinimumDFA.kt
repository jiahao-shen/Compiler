package com.sam.lexical

class MinimumDFA(private var newFinalState: BooleanArray, private var allNodes: ArrayList<Edge>, private var dfaNode: Int, private var characterSet: Set<Char>) {
    private var setList = ArrayList<Set<Int>>()

    private fun init() {
        val finalStateSet = HashSet<Int>()
        val noFinalStateSet = HashSet<Int>()
        for (i in 1..dfaNode) {
            if (newFinalState[i])
                finalStateSet.add(i)
            else
                noFinalStateSet.add(i)
        }
        setList.add(finalStateSet)
        setList.add(noFinalStateSet)
    }

    private fun toMinimumDFA() {
        init()
        var flag = true
        while (flag) {
            flag = false
            case@
            for (k in setList.indices) {
                val st = setList[k]
                if (st.size <= 1)
                    continue
                for (ch in characterSet) {
                    val mp = HashMap<Int, Int>()
                    for (i in allNodes.indices) {
                        val edge = allNodes[i]
                        if (edge.key == ch && st.contains(edge.u))
                            mp[edge.u] = edge.v
                    }
                    for (i in st) {
                        if (!mp.containsKey(i))
                            mp[i] = -1
                    }
                    val firstSet = HashSet<Int>()
                    val secondSet = HashSet<Int>()
                    for (j in setList.indices) {
                        firstSet.clear()
                        secondSet.clear()
                        val tempSet = setList[k]
                        for ((key, value) in mp) {
                            if (tempSet.contains(value))
                                firstSet.add(key)
                            else
                                secondSet.add(key)
                        }
                        if (firstSet.size != 0 && secondSet.size != 0) {
                            flag = true
                            for (i in tempSet) {
                                if (!firstSet.contains(i) && !secondSet.contains(i))
                                    firstSet.add(i)
                            }
                            setList.removeAt(k)
                            setList.add(firstSet)
                            setList.add(secondSet)
                            break@case
                        }
                    }
                }
            }
        }
//        for (item in setList) {
//            println(item)
//        }
//        println("-----------------------------------")
        for (k in setList.indices) {
            val st = setList[k]
            if (st.size > 1) {
                val first = st.first()
                val tempList = ArrayList<Edge>()
                var i = 0
                while (i < allNodes.size) {
                    val edge = allNodes[i]
                    if(st.contains(edge.u) && edge.u != first) {
                        allNodes.removeAt(i)
                        i--
                    } else if (st.contains(edge.v) && edge.v != first) {
                        allNodes.removeAt(i)
                        i--
                        tempList.add(Edge(edge.u, first, edge.key))
                    }
                    i++
                }
                allNodes.addAll(tempList)
            }
        }
    }

    fun outputMinimumDFA() {
        toMinimumDFA()
        for (item in allNodes)
            println(item)
    }
}

fun main(args: Array<String>) {
    val regex = "0*(100*)*0*"
    val nfa = NFA(regex)
    val dfa = DFA(nfa.getNFAGraphics(), nfa.characterSet, nfa.finalState)
    dfa.toStateMatrix()
    val minimumDFA = MinimumDFA(dfa.newFinalState, dfa.allNodes, dfa.dfaNode, dfa.characterSet)
    minimumDFA.outputMinimumDFA()
}