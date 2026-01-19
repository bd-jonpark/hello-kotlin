/**
 * Hello Kotlin - Coverity Test Project
 * Contains intentional security defects for testing
 */
package com.example

import java.io.File
import java.io.FileInputStream
import java.sql.Connection
import java.sql.DriverManager

// Hardcoded credentials - HARDCODED_CREDENTIALS
const val API_KEY = "sk-1234567890abcdef"
const val DB_PASSWORD = "SuperSecret123!"
const val DB_URL = "jdbc:mysql://localhost:3306/mydb"

// SQL Injection vulnerability
fun getUserByName(connection: Connection, username: String): String? {
    // Unsafe string concatenation in SQL query
    val query = "SELECT * FROM users WHERE username = '$username'"
    val stmt = connection.createStatement()
    val rs = stmt.executeQuery(query)
    return if (rs.next()) rs.getString("email") else null
}

// Command Injection vulnerability
fun runCommand(userInput: String): Int {
    // Unsafe command execution
    val process = Runtime.getRuntime().exec("echo $userInput")
    return process.waitFor()
}

// Path Traversal vulnerability
fun readFile(filename: String): String {
    // Unsafe file path construction
    val filepath = "/data/files/$filename"
    return File(filepath).readText()
}

// Resource Leak - FileInputStream not closed
fun processFile(path: String): Int {
    val fis = FileInputStream(path)
    val data = fis.read()
    // Missing fis.close() - RESOURCE_LEAK
    return data
}

// Null pointer dereference
fun processUser(user: Map<String, String?>): String {
    val email = user["email"]
    // Potential null pointer dereference
    return email!!.uppercase()
}

// Division by zero
fun calculateAverage(numbers: List<Int>): Int {
    val sum = numbers.sum()
    val count = numbers.filter { it > 100 }.size
    // Potential division by zero
    return sum / count
}

// Weak cryptography
fun hashPassword(password: String): ByteArray {
    val md = java.security.MessageDigest.getInstance("MD5")  // Weak hash
    return md.digest(password.toByteArray())
}

// Insecure random
fun generateToken(): Long {
    val random = java.util.Random()  // Insecure random
    return random.nextLong()
}

fun main() {
    println("Hello Kotlin with Coverity!")

    // Test command injection
    runCommand("hello; cat /etc/passwd")

    // Test path traversal
    try {
        val content = readFile("../../../etc/passwd")
        println(content)
    } catch (e: Exception) {
        println("File not found")
    }

    // Test null pointer
    val user = mapOf("name" to "John", "email" to null)
    try {
        val result = processUser(user)
        println(result)
    } catch (e: Exception) {
        println("Null pointer: ${e.message}")
    }

    // Test division by zero
    val avg = calculateAverage(listOf(1, 2, 3))
    println("Average: $avg")
}
