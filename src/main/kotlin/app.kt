package app

import app.repoMinio.RepoMinio
import io.minio.messages.Item
import org.slf4j.Logger

fun main() {
    val repoMinio = RepoMinio()
    val ctx = MinioContext().also { it.minioRepo = repoMinio }
    val logger: Logger = ctx.logger

    val objectName = "files.tt"
    val fileName = "file1.txt"

    val list = repoMinio.getAllBuckets()?.map { it.name() }
    list?.forEach { logger.info(it.toString()) }


    // upload file
//    val filePath = "${ctx.dir}$fileName"
//    repoMinio.uploadFileTo(bucketName, fileName, filePath)

    // download file
//    val filePath = "${ctx.dirDownload}$fileName"
//    repoMinio.downloadFileFrom(bucketName, objectName, filePath)

    val iterator = repoMinio.getAllObjects(ctx.bucketName)
    if (iterator != null) {
        while (iterator.hasNext()) {
            val item: Item = iterator.next().get()
            logger.info("Объект: {} с версией {}", item.objectName(), item.versionId())
        }
    }

}