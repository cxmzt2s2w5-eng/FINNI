package com.myau.finni

import android.content.Context


object TaskParser {


    fun loadTasks(
        context: Context
    ): List<Task> {

        val text =
            context.assets
                .open("tasks.txt")
                .bufferedReader()
                .use {
                    it.readText()
                }


        return parseTasks(text)
    }



    private fun parseTasks(
        text: String
    ): List<Task> {


        val tasks =
            mutableListOf<Task>()


        val blocks =
            text
                .split("[task]")
                .drop(1)



        blocks.forEach { block ->


            val content =
                block
                    .substringBefore("[/task]")
                    .trim()


            if(content.isEmpty())
                return@forEach



            tasks.add(

                Task(

                    id =
                        getValue(
                            content,
                            "id"
                        )
                            .toIntOrNull()
                            ?: 0,


                    section =
                        getValue(
                            content,
                            "section"
                        ),


                    title =
                        getValue(
                            content,
                            "title"
                        ),


                    type =
                        getValue(
                            content,
                            "type"
                        ),


                    text =
                        getValue(
                            content,
                            "text"
                        ),


                    reward =
                        getValue(
                            content,
                            "reward"
                        )
                            .toIntOrNull()
                            ?: 0,


                    options =
                        getSection(
                            content,
                            "[options]"
                        ),


                    feedback =
                        getSection(
                            content,
                            "[feedback]"
                        )
                )
            )
        }


        return tasks
    }




    private fun getValue(
        text: String,
        key: String
    ): String {


        return text
            .lines()
            .firstOrNull {

                it.trim()
                    .startsWith("$key=")

            }
            ?.substringAfter("=")
            ?.trim()
            ?: ""
    }





    private fun getSection(
        text: String,
        section: String
    ): Map<String,String> {


        val start =
            text.indexOf(section)


        if(start == -1)
            return emptyMap()



        val content =
            text
                .substring(
                    start + section.length
                )
                .substringBefore("[")
                .trim()



        return content
            .lines()
            .filter {
                it.contains("=")
            }
            .associate {


                val parts =
                    it.split(
                        "=",
                        limit = 2
                    )


                parts[0].trim() to
                        parts[1].trim()
            }
    }
}