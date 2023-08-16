package com.timurkhabibulin.domain.entities

enum class Color(
    val colorName: String,
    val colorValue: Long
) {
    BLACK("black",0xFF000000),
    WHITE("white",0xFFFFFFFF),
    YELLOW("yellow",0xFFFFFF00),
    ORANGE("orange",0xFFFFA500),
    RED("red",0xFFFF0000),
    PURPLE("purple",0xFF800080),
    MAGENTA("magenta",0xFFFF00FF),
    GREEN("green",0xFF008000),
    TEAL("teal",0xFF008080),
    BLUE("blue",0xFF0000FF)
}