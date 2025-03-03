package com.ltthuc.feature.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject

class SaveInputStreamAsPdfFileOnDirectoryUseCase @Inject constructor(){

    /**
     * Create and save inputStream as a file in the indicated directory
     * the inputStream to save will be a PDF file with random UUID as name
     */
    suspend operator fun invoke(fileName: String, inputStream: InputStream, directory: File): File? {
        var outputFile: File? = null
        withContext(Dispatchers.IO) {
            try {
                val name = "$fileName.pdf"
                val outputDir = File(directory, "download")
                outputFile = File(outputDir, name)
                makeDirIfShould(outputDir)
                val outputStream = FileOutputStream(outputFile, false)
                inputStream.use { fileOut -> fileOut.copyTo(outputStream) }
                outputStream.close()

            } catch (e: IOException) {
                // Something went wrong
            }
        }
        return outputFile
    }

    private fun makeDirIfShould(outputDir: File) {
        if (outputDir.exists().not()) {
            outputDir.mkdirs()
        }
    }
}