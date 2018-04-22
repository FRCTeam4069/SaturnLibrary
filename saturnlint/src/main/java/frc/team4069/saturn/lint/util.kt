package frc.team4069.saturn.lint

fun String.laxReplace(old: String, new: String): String {

    val index = this.toLowerCase().indexOf(old)
    val target = this.substring(index..index + old.lastIndex)

    val newArr = new.toCharArray()

    for (oldCh in target) {
        new.forEachIndexed { i, newCh ->
            if (oldCh.toLowerCase() == newCh.toLowerCase() && oldCh.isUpperCase() && !newCh.isUpperCase()) {
                newArr[i] = newCh.toUpperCase()
            }

        }

    }

    return replace(target, newArr.joinToString(""))
}
