package com.genaku.applog.strategy

import com.genaku.applog.strategy.names.NamesList
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class NamesListTest : FreeSpec({
    val title = "title"
    val titleStr = "\n$title:"
    val first = "1st"
    val second = "2nd"

    "add 1st name" - {
        val names = NamesList()

        names.add(first)
        names.toString(title) shouldBe "$titleStr\n$first"

        "add 2nd name" - {
            names.add(second)
            names.toString(title) shouldBe "$titleStr\n$first\n$second"

            "remove 2nd name" - {
                names.remove(second)
                names.toString(title) shouldBe "$titleStr\n$first"
            }

            "remove 1st name" - {
                names.remove(first)
                names.toString(title) shouldBe "$titleStr\n$second"

                "remove 1st again" - {
                    shouldThrow<NoSuchElementException> { names.remove(first) }
                    names.toString(title) shouldBe "$titleStr\n$second"
                }
            }

            "remove both" - {
                names.remove(second)
                names.remove(first)
                names.toString(title) shouldBe ""
            }

            "add 1st again" - {
                names.add(first)
                names.toString(title) shouldBe "$titleStr\n$first\n$second\n$first"
            }
        }
    }
}) {
    override fun isolationMode(): IsolationMode? = IsolationMode.InstancePerLeaf
}
