package cl.flepe.payment.data.remote.retrofit

import android.content.Context
import android.os.SystemClock
import android.util.Log
import okhttp3.*
import okhttp3.Interceptor.Chain
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URI
import java.util.*

class FakeInterceptor(private val mContext: Context) : Interceptor {
    private var mContentType = "application/json;charset=UTF-8"

    @Throws(IOException::class)
    override fun intercept(chain: Chain): Response {
        SystemClock.sleep(1000)
        val suggestionFileName: String
        val method: String = chain.request().method.lowercase(Locale.getDefault())
        logRequestBody(chain)
        var defaultFileName: String?
        var response: Response? = null
        // Get Request URI.
        val uri: URI = chain.request().url.toUri()
        defaultFileName = getFileName(chain)
        Log.d(
            TAG,
            "--> Request url: [" + method.uppercase(Locale.getDefault()) + "]" + uri.toString()
        )
        var responseFileName = ""
        suggestionFileName = defaultFileName!!.lowercase(Locale.getDefault())
        val filesWithName = getFilesWithName(suggestionFileName, uri)
        val randomCodeString: String
        var randomCode = 200
        if (filesWithName.isNotEmpty()) {
            if (filesWithName.size > 1) {
                val filesCode = getFilesCode(filesWithName)
                randomCodeString = getRandomCode(filesCode)
                try {
                    randomCode = randomCodeString.toInt()
                } catch (e: Exception) {
                }
                responseFileName = suggestionFileName + "_" + randomCodeString
            } else {
                responseFileName = filesWithName[0]
            }
        }
        responseFileName += ".json"
        if (responseFileName.isNullOrEmpty().not()) {
            val fileName = getFilePath(uri, responseFileName)
            Log.d(TAG, "Read data from file: $fileName")
            try {
                val `is` = mContext.assets.open(fileName)
                val r = BufferedReader(InputStreamReader(`is`))
                val responseStringBuilder = StringBuilder()
                var line: String?
                while (r.readLine().also { line = it } != null) {
                    responseStringBuilder.append(line).append('\n')
                }
                Log.d(
                    TAG,
                    "Response: $responseStringBuilder"
                )
                val builder: Response.Builder = Response.Builder().code(randomCode)
                    .message(responseStringBuilder.toString())
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_0)
                    .body(
                        responseStringBuilder.toString().toByteArray()
                            .toResponseBody(mContentType.toMediaTypeOrNull())
                    )
                    .addHeader("content-type", mContentType)
                response = builder.build()
            } catch (e: IOException) {
                Log.e(TAG, e.message, e)
            }
        } else {
            Log.e(TAG, "File not exist: " + getFilePath(uri, suggestionFileName))
            response = chain.proceed(chain.request())
        }
        Log.d(TAG, "<-- END [" + method.uppercase(Locale.getDefault()) + "]" + uri.toString())
        return response!!
    }

    private fun logRequestBody(chain: Chain) {
        try {
            Log.d(TAG, "Request Body: " + bodyToString(chain.request()))
        } catch (e: Exception) {
            e.localizedMessage?.let { Log.d(TAG, it) }
        }
    }

    private fun upCaseFirstLetter(str: String): String {
        return str.substring(0, 1).uppercase(Locale.getDefault()) + str.substring(1)
    }

    @Throws(IOException::class)
    private fun getFilesWithName(inputFileName: String, uri: URI): ArrayList<String> {
        var inputFileName: String? = inputFileName
        var mockDataPath = uri.host + uri.path
        mockDataPath = mockDataPath.lowercase(Locale.getDefault())
        mockDataPath = mockDataPath.substring(0, mockDataPath.lastIndexOf('/'))
        Log.d(TAG, "Scan files in: $mockDataPath")
        //List all files in folder
        val files = mContext.assets.list(mockDataPath)
        val filesName = ArrayList<String>()
        if (inputFileName != null) {
            for (file in files!!) {
                val replacedFile = file.replace(".json".toRegex(), "")
                if (file.contains(inputFileName)) {
                    filesName.add(replacedFile)
                }
            }
        }
        return filesName
    }

    fun getFilesCode(filesName: ArrayList<String>): ArrayList<String> {
        val filesCode = ArrayList<String>()
        for (fileName in filesName) {
            val arrayString = fileName.split("_").toTypedArray()
            filesCode.add(arrayString[arrayString.size - 1])
        }
        return filesCode
    }

    fun getRandomCode(filesCode: ArrayList<String>): String {
        return filesCode[Random().nextInt(filesCode.size)]
    }

    private fun getFileName(chain: Chain): String {
        val fileName: String =
            chain.request().url.pathSegments[chain.request().url.pathSegments.size - 1]
        return if (fileName.isEmpty()) "index" + FILE_EXTENSION else fileName + "_" + chain.request().method.lowercase(
            Locale.getDefault()
        ) + FILE_EXTENSION
    }

    private fun getFilePath(uri: URI, fileName: String): String {
        val path: String = if (uri.path.lastIndexOf('/') != uri.path.length - 1) {
            uri.path.substring(0, uri.path.lastIndexOf('/') + 1)
        } else {
            uri.path
        }
        return uri.host + path.lowercase(Locale.getDefault()) + fileName
    }

    companion object {
        private val TAG = FakeInterceptor::class.java.simpleName
        private const val FILE_EXTENSION = ""
        private fun bodyToString(request: Request): String {
            return try {
                val copy = request.newBuilder().build()
                val buffer = Buffer()
                copy.body!!.writeTo(buffer)
                buffer.readUtf8()
            } catch (e: IOException) {
                Log.d(TAG, "bodyToString: " + e.localizedMessage)
                ""
            }
        }
    }
}