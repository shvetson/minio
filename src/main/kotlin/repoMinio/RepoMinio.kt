package app.repoMinio

import app.common.IMinioRepo
import io.minio.*
import io.minio.messages.Bucket
import io.minio.messages.Item
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

class RepoMinio(
    private val host: String = "http://127.0.0.1:9000",
    private val accessKey: String = "minioadmin",
    private val secretKey: String = "minioadmin",
    override val minioClient: MinioClient = MinioClient.builder()
        .endpoint(host)
        .credentials(accessKey, secretKey)
        .build(),
) : IMinioRepo {

    private val logger: Logger = LoggerFactory.getLogger("MINIO")

    // create bucket
    override fun createBucket(bucketName: String) {
        val mbArgs: MakeBucketArgs = MakeBucketArgs.builder().bucket(bucketName).build()
        minioClient.makeBucket(mbArgs)
    }

    // check exists bucket
    override fun isBucketExists(bucketName: String): Boolean {
        val beArgs: BucketExistsArgs = BucketExistsArgs.builder().bucket(bucketName).build()
        return minioClient.bucketExists(beArgs)
    }

    // delete a bucket
    override fun deleteBucket(bucketName: String) {
        val dArgs = RemoveBucketArgs.builder().bucket(bucketName).build()
        if (isBucketExists(bucketName)) {
            minioClient.removeBucket(dArgs)
        } else {
            logger.info("Bucket {} не существует.", bucketName)
        }
    }

    // list buckets
    override fun getAllBuckets(): List<Bucket>? {
        return try {
            minioClient.listBuckets()
        } catch (e: Throwable) {
            logger.error(e.stackTraceToString())
            null
        }
    }

    // list objects on minio
    override fun getAllObjects(bucketName: String): Iterator<Result<Item>>? {
        val lArgs: ListObjectsArgs = ListObjectsArgs.builder()
            .bucket(bucketName)
            .includeVersions(true)
            .recursive(true)
            .build()

        val response: MutableIterable<Result<Item>> = minioClient.listObjects(lArgs)
        return response.iterator()
    }

//    while (iterator.hasNext()) {
//        val item: Item = iterator.next().get()
//        logger.info("Объект: {} с версией {}", item.objectName(), item.versionId())
//    }

    // delete object
    override fun deleteObject(bucketName: String, objectName: String) {
        val rArgs: RemoveObjectArgs = RemoveObjectArgs.builder()
            .bucket(bucketName)
            .`object`(objectName)
            .build()

        minioClient.removeObject(rArgs)
    }

    // copy object
    override fun copyObject(bucketName: String, source: String, dest: String): ObjectWriteResponse {
        val cs: CopySource = CopySource.builder()
            .bucket(bucketName)
            .`object`(source)
            .build()

        val cArgs: CopyObjectArgs = CopyObjectArgs.builder()
            .bucket(bucketName)
            .`object`(dest)
            .source(cs)
            .build()

        val response: ObjectWriteResponse = minioClient.copyObject(cArgs)
        logger.info("Объект {} скопирован.", response.`object`())
        return response
    }

//    val source = "image.png"
//    val dest = "backup/image.png"

    // upload file to minio
    override fun uploadFileTo(bucketName: String, objectName: String, fileName: String, contentType: String?) {

        val uArgs: UploadObjectArgs = UploadObjectArgs.builder()
            .bucket(bucketName)
            .`object`(objectName)
            .filename(fileName)
            .contentType(contentType)
            .build()

        val response: ObjectWriteResponse = minioClient.uploadObject(uArgs)
        logger.info("Загружен файл {}, {}, {}", response.`object`(), response.etag(), response.versionId())
    }

    //    val objectName = "image.jpeg"
    //    val contentType = "image/jpeg"
    //    val fileName = "/tmp/demo/im.jpeg"

    // download files from minio
    override fun downloadFileFrom(bucketName: String, objectName: String, fileName: String) {
        val dArgs: DownloadObjectArgs = DownloadObjectArgs.builder()
            .bucket(bucketName)
            .`object`(objectName)
            .filename(fileName)
            .build()
        minioClient.downloadObject(dArgs)
    }

    // upload stream to minio
    override fun uploadStreamTo(bucketName: String, objectName: String, fileName: String, contentType: String) {
        val size = try {
            File(fileName).length()
        } catch (e: Throwable) {
            logger.error(e.stackTraceToString())
        }
        val fis: FileInputStream = FileInputStream(fileName)

        val uArgs: PutObjectArgs = PutObjectArgs.builder()
            .bucket(bucketName)
            .`object`(objectName)
            .stream(fis, size as Long, -1)
            .contentType(contentType)
            .build()

        val response: ObjectWriteResponse = minioClient.putObject(uArgs)
        logger.info("Загружен файл {}, {}, {}", response.`object`(), response.etag(), response.versionId())
    }

    //    val objectName2 = "hello-world.txt"
    //    val contentType2 = "text/plain"
    //    val fileName2 = "/tmp/demo/hw.txt"

    // download stream from minio
    override fun downloadStreamFrom(bucketName: String, objectName: String, fileName: String): InputStream {
        val dArgs: GetObjectArgs = GetObjectArgs.builder().bucket(bucketName).`object`(objectName).build()
        return minioClient.getObject(dArgs)
    }


}