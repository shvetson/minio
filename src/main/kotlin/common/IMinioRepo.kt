package app.common

import io.minio.MinioClient
import io.minio.ObjectWriteResponse
import io.minio.Result
import io.minio.messages.Bucket
import io.minio.messages.Item
import java.io.InputStream

interface IMinioRepo {
    val minioClient: MinioClient
    fun createBucket(bucketName: String)
    fun isBucketExists(bucketName: String): Boolean
    fun deleteBucket(bucketName: String)
    fun getAllBuckets(): List<Bucket>?
    fun getAllObjects(bucketName: String): Iterator<Result<Item>>?
    fun deleteObject(bucketName: String, objectName: String)
    fun copyObject(bucketName: String, source: String, dest: String): ObjectWriteResponse
    fun downloadFileFrom(bucketName: String, objectName: String, fileName: String)
    fun downloadStreamFrom(bucketName: String, objectName: String, fileName: String): InputStream?
    fun uploadFileTo(bucketName: String, objectName: String, fileName: String, contentType: String? = "text/plain")
    fun uploadStreamTo(bucketName: String, objectName: String, fileName: String, contentType: String)

    companion object {
        val NONE = object: IMinioRepo {
            override val minioClient: MinioClient
                get() = TODO("Not yet implemented")

            override fun createBucket(bucketName: String) {
                TODO("Not yet implemented")
            }

            override fun isBucketExists(bucketName: String): Boolean {
                TODO("Not yet implemented")
            }

            override fun deleteBucket(bucketName: String) {
                TODO("Not yet implemented")
            }

            override fun getAllBuckets(): List<Bucket>? {
                TODO("Not yet implemented")
            }

            override fun getAllObjects(bucketName: String): Iterator<Result<Item>>? {
                TODO("Not yet implemented")
            }

            override fun deleteObject(bucketName: String, objectName: String) {
                TODO("Not yet implemented")
            }

            override fun copyObject(bucketName: String, source: String, dest: String): ObjectWriteResponse {
                TODO("Not yet implemented")
            }

            override fun downloadFileFrom(bucketName: String, objectName: String, fileName: String) {
                TODO("Not yet implemented")
            }

            override fun downloadStreamFrom(bucketName: String, objectName: String, fileName: String): InputStream? {
                TODO("Not yet implemented")
            }

            override fun uploadFileTo(bucketName: String, objectName: String, fileName: String, contentType: String?) {
                TODO("Not yet implemented")
            }

            override fun uploadStreamTo(bucketName: String, objectName: String, fileName: String, contentType: String) {
                TODO("Not yet implemented")
            }

        }
    }
}